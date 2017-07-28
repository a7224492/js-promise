package com.kodgames.game.action.room;

import java.util.HashMap;
import java.util.Map;

import com.kodgames.message.protocol.PlatformProtocolsConfig;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.game.common.Constant;
import com.kodgames.game.common.rule.RoomTypeConfig;
import com.kodgames.game.common.rule.RuleManager;
import com.kodgames.game.service.activity.ActivityService;
import com.kodgames.game.service.role.RoleService;
import com.kodgames.game.service.server.ServerService;
import com.kodgames.game.util.KodBiLogHelper;
import com.kodgames.message.proto.club.ClubProtoBuf;
import com.kodgames.message.proto.game.GameProtoBuf.BGRoomCardModifySYNC;
import com.kodgames.message.proto.game.GameProtoBuf.GCRoomCardModifySYNC;

import xbean.RoleInfo;
import xbean.RoomInfo;

@ActionAnnotation(messageClass = BGRoomCardModifySYNC.class, actionClass = BGRoomCardModifySYNCAction.class, serviceClass = RoleService.class)
public class BGRoomCardModifySYNCAction extends ProtobufMessageHandler<RoleService, BGRoomCardModifySYNC>
{
	private static final Logger logger = LoggerFactory.getLogger(BGRoomCardModifySYNCAction.class);

	@Override
	public void handleMessage(Connection connection, RoleService service, BGRoomCardModifySYNC message, int callback)
	{
		// 获取要消耗的房卡数量
		RoomTypeConfig rc = RuleManager.getInstance().getRoomConfig(message.getRoundCount());
		int cardConsumer = (null == rc) ? 0 : rc.getCardCount();
		int aaCardConsumer = (null == rc) ? 0 : rc.getAACardCount();
		if (cardConsumer <= 0)
		{
			logger.error("Invalid room card consume count : roleId={}, roomId={}, msgCount={}, configCount={}", message.getCreatorId(), message.getRoomId(), message.getRoundCount(), rc);
			return;
		}

		RoomInfo roomInfo = table.Room_info.select(message.getRoomId());

		// 俱乐部房间，不扣普通卡
		if (roomInfo != null && roomInfo.getClubId() > 0)
		{
			cardConsumer = 0;
			aaCardConsumer = 0;
			// 通知club，可以个roomId可以扣卡了
			ClubProtoBuf.GCLEnableSubCardSYN.Builder clubBuilder = ClubProtoBuf.GCLEnableSubCardSYN.newBuilder();
			clubBuilder.setClubId(roomInfo.getClubId());
			clubBuilder.setRoomId(message.getRoomId());

			Connection clubConnection = ServiceContainer.getInstance().getPublicService(ServerService.class).getClubConnection();
			clubConnection.write(callback, clubBuilder.build());
		}

		// 如果是以限免方式创建房间，则不扣卡
		if (roomInfo != null && roomInfo.getIsLca())
		{
			// 不扣卡
			cardConsumer = 0;
			aaCardConsumer = 0;
		}

		Map<Integer, RoleInfo> allRoleInfo = new HashMap<Integer, RoleInfo>();
		for (int i = 0; i < message.getRoleIdsCount(); i++)
		{
			allRoleInfo.put(message.getRoleIds(i), service.getRoleInfoByRoleIdForWrite(message.getRoleIds(i)));
		}
		RoleInfo info = service.getRoleInfoByRoleIdForWrite(message.getCreatorId());

		if (roomInfo.getPayType() == Constant.PAY_TYPE.CREATOR_PAY)
		{
			// 创建房间时应已检测玩家房卡数是否足够，扣卡时必须足够
			if (info.getCardCount() < cardConsumer)
			{
				logger.error("recv BGRoomCardModifySYNC found roundcount {} cardConsumer {} role {} totalRoomCard {} not enough",
					message.getRoundCount(),
					cardConsumer,
					message.getCreatorId(),
					info.getCardCount());
				info.setCardCount(0);
			}
			else // 执行扣卡
			{
				info.setCardCount(info.getCardCount() - cardConsumer);
				info.setTotalCostCardCount(info.getTotalCostCardCount() + cardConsumer);
				logger.debug("recv BGRoomCardModifySYNC roleId {} consumercard {} rest card {}", message.getCreatorId(), cardConsumer, info.getCardCount());
				// 大转盘记录玩家扣卡，如果大转盘没有开始，方法直接返回
				ServiceContainer.getInstance().getPublicService(ActivityService.class).playerCostCard(message.getCreatorId(), cardConsumer);
				KodBiLogHelper.subPlayerCard(message.getCreatorId(), message.getRoomId(), rc.getType(), rc.getCardCount());
			}
		}
		if (roomInfo.getPayType() == Constant.PAY_TYPE.AA_PAY)
		{
			for (Integer roleId : allRoleInfo.keySet())
			{
				RoleInfo role = allRoleInfo.get(roleId);
				if (role.getCardCount() < aaCardConsumer)
				{
					logger.error("recv BGRoomCardModifySYNC found roundcount {} cardConsumer {} role {} totalRoomCard {} not enough",
						message.getRoundCount(),
						aaCardConsumer,
						message.getCreatorId(),
						role.getCardCount());
					role.setCardCount(0);
				}
				else
				{
					role.setCardCount(role.getCardCount() - aaCardConsumer);
					role.setTotalCostCardCount(role.getTotalCostCardCount() + aaCardConsumer);
					logger.debug("recv BGRoomCardModifySYNC roleId {} consumercard {} rest card {}", roleId, aaCardConsumer, role.getCardCount());
					// 大转盘记录玩家扣卡，如果大转盘没有开始，方法直接返回
					ServiceContainer.getInstance().getPublicService(ActivityService.class).playerCostCard(roleId, aaCardConsumer);
					KodBiLogHelper.subPlayerCard(roleId, message.getRoomId(), rc.getType(), rc.getAACardCount());
				}
			}
		}
		// 通知客户端更新房卡数量
		GCRoomCardModifySYNC.Builder builder = GCRoomCardModifySYNC.newBuilder();
		message.getRoleIdsList().forEach(roleId -> {
			builder.setRoomCardCount(service.getRoleInfoByRoleIdForWrite(roleId).getCardCount());
			Connection clientConnection = ConnectionManager.getInstance().getClientVirtualConnection(roleId);
			if (null != clientConnection)
				clientConnection.write(GlobalConstants.DEFAULT_CALLBACK, builder.build());
		});

		ActivityService activityService = ServiceContainer.getInstance().getPublicService(ActivityService.class);
		if (activityService.isActivityEventActive(PlatformProtocolsConfig.PLAYER_COST_CARD_EVENT))
		{
			//TODO 发送活动事件
		}
	}

	@Override
	public Object getMessageKey(Connection connection, BGRoomCardModifySYNC message)
	{
		return message.getRoomId() == 0 ? connection.getConnectionID() : message.getRoomId();
	}
}
