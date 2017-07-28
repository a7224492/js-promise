package com.kodgames.game.action.room;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.game.common.Constant;
import com.kodgames.game.common.rule.RoomTypeConfig;
import com.kodgames.game.common.rule.RuleManager;
import com.kodgames.game.service.activity.ActivityService;
import com.kodgames.game.service.activity.LimitedCostlessActivityService;
import com.kodgames.game.service.role.RoleService;
import com.kodgames.game.service.room.RoomService;
import com.kodgames.game.start.CGProtobufMessageHandler;
import com.kodgames.message.proto.game.GameProtoBuf.CGCreateRoomREQ;
import com.kodgames.message.proto.game.GameProtoBuf.GBCreateRoomREQ;
import com.kodgames.message.proto.game.GameProtoBuf.GCCreateRoomRES;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

import xbean.RoleInfo;

@ActionAnnotation(messageClass = CGCreateRoomREQ.class, actionClass = CGCreateRoomREQAction.class, serviceClass = RoomService.class)
public class CGCreateRoomREQAction extends CGProtobufMessageHandler<RoomService, CGCreateRoomREQ>
{
	private static final Logger logger = LoggerFactory.getLogger(CGCreateRoomREQAction.class);

	/**
	 * 创建房间后，将RoleMemInfo中的信息设置好，通过RoomInfo知道该RoomId正在创建
	 */
	@Override
	public void handleMessage(Connection connection, RoomService service, CGCreateRoomREQ message, int callback)
	{
		int creatorId = connection.getRemotePeerID();

		int roomId = ServiceContainer.getInstance().getPublicService(RoleService.class).getRoomIdByRoleId(creatorId);
		if (roomId != 0)
		{
			// 房间已在创建中
			if (service.isCreatingRoom(roomId))
			{
				GCCreateRoomRES.Builder builder = GCCreateRoomRES.newBuilder();
				builder.setResult(PlatformProtocolsConfig.GC_CREATE_ROOM_FAILED_CREATING);
				connection.write(callback, builder.build());

				logger.error("crateRoom: rold {} is creating room {} return false", creatorId, roomId);
				return;
			}

			// 已在房间中的玩家不能再创建房间
			GCCreateRoomRES.Builder builder = GCCreateRoomRES.newBuilder();
			builder.setResult(PlatformProtocolsConfig.GC_CREATE_ROOM_FAILED_ALREADY_IN_ROOM);
			connection.write(callback, builder.build());

			logger.error("createRoom:role {} createroom found already has roomId {}", creatorId, roomId);
			return;
		}

		// 不支持的房间圈数局数
		RoomTypeConfig rc = RuleManager.getInstance().getRoomConfig(message.getRoundCount());
		int needCardCount = 0;
		if (message.getPayType() == Constant.PAY_TYPE.CREATOR_PAY)
			needCardCount = (null == rc) ? 0 : rc.getCardCount();
		if (message.getPayType() == Constant.PAY_TYPE.AA_PAY)
			needCardCount = (null == rc) ? 0 : rc.getAACardCount();

		if (needCardCount <= 0)
		{
			GCCreateRoomRES.Builder builder = GCCreateRoomRES.newBuilder();
			builder.setResult(PlatformProtocolsConfig.GC_CREATE_ROOM_FAILED_INVALID_ROUND_COUNT);
			connection.write(callback, builder.build());

			logger.error("createRoomReq: invalid roundCount -> roleId={}, roundCount={}", creatorId, message.getRoundCount());
			return;
		}

		// 判断是否以限免方式创建房间
		LimitedCostlessActivityService lcaService = ServiceContainer.getInstance().getPublicService(ActivityService.class).getLca();
		boolean isLCARoom = false;
		if (lcaService.isLCACreateRoom(message.getFreeActivityId()))
		{
			if (!lcaService.isActive(message.getFreeActivityId()))
			{
				GCCreateRoomRES.Builder builder = GCCreateRoomRES.newBuilder();
				builder.setResult(PlatformProtocolsConfig.GC_CREATE_ROOM_NO_LCA);
				connection.write(callback, builder.build());
				return;
			}
			isLCARoom = true;
			needCardCount = 0;
		}

		// 玩家房卡不足
		if (!isLCARoom)
		{
			boolean failed = false;
			RoleInfo roleInfo = service.getRoleInfoByRoleId(creatorId);
			if (roleInfo == null)
			{
				logger.error("createRoom failed for can't find role_info for creatorId {}", creatorId);
				failed = true;
			}
			else if (roleInfo.getCardCount() < needCardCount)
			{
				logger.info("createRoom: roleId={}, roundCount={}, needCardCount={}, roleCardCount={}", creatorId, message.getRoundCount(), needCardCount, roleInfo.getCardCount());
				failed = true;
			}

			if (failed)
			{
				GCCreateRoomRES.Builder builder = GCCreateRoomRES.newBuilder();
				builder.setResult(PlatformProtocolsConfig.GC_CREATE_ROOM_FAILED_ROOMCARD_NOT_ENOUGTH);
				connection.write(callback, builder.build());
				return;
			}
		}

		// 没有可用的BattleServer
		Integer battleId = service.getAvailableBattleId();
		if (null == battleId)
		{
			GCCreateRoomRES.Builder builder = GCCreateRoomRES.newBuilder();
			builder.setResult(PlatformProtocolsConfig.GC_CREATE_ROOM_FAILED_NO_BATTLE);
			connection.write(callback, builder.build());

			logger.error("createRoom: roleId {} without available battleId", creatorId);
			return;
		}

		int newRoomId = service.getNewRoomId();
		if (newRoomId == 0)
		{
			GCCreateRoomRES.Builder builder = GCCreateRoomRES.newBuilder();
			builder.setResult(PlatformProtocolsConfig.GC_CREATE_ROOM_FAILED_CREATING);
			connection.write(callback, builder.build());

			logger.error("crateRoom failed for can't get free roomId, creator: {}", creatorId);
			return;
		}

		// 将房间号置为创建中
		if (!service.creatingRoom(battleId, newRoomId, message.getRoundCount(), isLCARoom, 0, 0, message.getPayType()))
		{
			GCCreateRoomRES.Builder builder = GCCreateRoomRES.newBuilder();
			builder.setResult(PlatformProtocolsConfig.GC_CREATE_ROOM_FAILED_CREATING);
			connection.write(callback, builder.build());

			logger.error("crateRoom failed for roomId already exist in table.Room_info, creator: {} roomId: {}", creatorId, newRoomId);
			return;
		}

		// 向Battle请求创建房间
		GBCreateRoomREQ.Builder builder = GBCreateRoomREQ.newBuilder();
		builder.setRoomId(newRoomId);
		builder.setRoomType(message.getRoomType());
		builder.addAllGameplays(message.getGameplaysList());
		builder.setRoundCount(message.getRoundCount());
		builder.setCreatorId(creatorId);
		builder.setPayType(message.getPayType());
		builder.setVoice(message.getVoice());

		Connection battleConnection = ConnectionManager.getInstance().getServerConnnection(battleId);
		battleConnection.write(callback, builder.build());
	}

}
