package com.kodgames.club.action.room;

import com.kodgames.club.service.room.ClubRoomService;
import com.kodgames.club.utils.ClubUtils;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.message.proto.game.GameProtoBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.club.constant.ClubConstants;
import com.kodgames.club.constant.ClubRights;
import com.kodgames.club.service.common.ClubCommonService;
import com.kodgames.club.service.server.ServerService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.message.proto.club.ClubProtoBuf.CCLCreateRoomREQ;
import com.kodgames.message.proto.club.ClubProtoBuf.CLCCreateRoomRES;
import com.kodgames.message.proto.club.ClubProtoBuf.CLGCreateRoomREQ;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

import xbean.MemberInfo;
import xbean.RoomCost;

@ActionAnnotation(messageClass = CCLCreateRoomREQ.class, actionClass = CCLCreateRoomREQAction.class, serviceClass = ClubCommonService.class)
public class CCLCreateRoomREQAction extends ProtobufMessageHandler<ClubCommonService, CCLCreateRoomREQ>
{

	private static final Logger logger = LoggerFactory.getLogger(CCLCreateRoomREQAction.class);

	@Override
	public void handleMessage(Connection connection, ClubCommonService service, CCLCreateRoomREQ message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

		int creator = message.getCreatorId();
		int clubId = message.getClubId();

		if (!service.checkClubId(clubId))
		{
			CLCCreateRoomRES.Builder builder = CLCCreateRoomRES.newBuilder();
			builder.setResult(PlatformProtocolsConfig.CLC_CREATE_ROOM_FAILED_NO_SUCH_CLUB);
			connection.write(callback, builder.build());

			logger.warn("createRoom: no such club {}", clubId);
			return;
		}

		if (service.checkClubStatus(clubId, ClubConstants.CLUB_STATUS.SAEL_FLAG)) {
			CLCCreateRoomRES.Builder builder = CLCCreateRoomRES.newBuilder();
			builder.setResult(PlatformProtocolsConfig.CLC_CREATE_ROOM_FAILED_CLUB_SEALED);
			connection.write(callback, builder.build());

			logger.warn("createRoom: club {} sealed", clubId);
			return;
		}
		MemberInfo member = service.getMemberInfo(creator, clubId);
		// 检查权限够不够
		if (!ClubRights.hasRight(member, ClubConstants.CLUB_ACTIONS.CreateRoom))
		{
			CLCCreateRoomRES.Builder builder = CLCCreateRoomRES.newBuilder();
			builder.setResult(PlatformProtocolsConfig.CLC_CREATE_ROOM_FAILED_NO_RIGHT);
			connection.write(callback, builder.build());

			logger.warn("createRoom: member {} has no right", creator);
			return;
		}

		ClubRoomService crs = ServiceContainer.getInstance().getPublicService(ClubRoomService.class);
		GameProtoBuf.RoomConfigPROTO roomConfig = crs.getRoomConfig(message.getRoundCount());
		int roomCost = roomConfig.getCardCount();
		RoomCost cost = service.getRoomCost(clubId);
		if (cost.getPayType() == ClubConstants.PAY_TYPE.MANAGER_PAY) {
			int managerId = service.getManagerId(clubId);
			MemberInfo manager = service.getMemberInfo(managerId, clubId);

			// 检查经理的俱乐部房卡够不够！
			if (manager.getCardCount() < cost.getCost() * roomCost)
			{
				CLCCreateRoomRES.Builder builder = CLCCreateRoomRES.newBuilder();
				builder.setResult(PlatformProtocolsConfig.CLC_CREATE_ROOM_FAILED_MANAGER_NOT_ENOUGH_CARDS);
				connection.write(callback, builder.build());

				logger.warn("createRoom: member has cards {}, but room cost too much {}",
						member.getCardCount(),
						cost.getCost());
				return;
			}
		} else {
			// 检查这个人的俱乐部房卡够不够！
			if (member.getCardCount() < cost.getCost() * roomCost)
			{
				CLCCreateRoomRES.Builder builder = CLCCreateRoomRES.newBuilder();
				builder.setResult(PlatformProtocolsConfig.CLC_CREATE_ROOM_FAILED_NOT_ENOUGH_CARDS);
				connection.write(callback, builder.build());

				logger.warn("createRoom: member has cards {}, but room cost too much {}",
						member.getCardCount(),
						cost.getCost());
				return;
			}
		}

		// 都检查过了，通知gameServer 建房间！！
		ServerService ss = ServiceContainer.getInstance().getPublicService(ServerService.class);
		Connection gameConnection = ss.getGameConnection();

		CLGCreateRoomREQ.Builder builder = CLGCreateRoomREQ.newBuilder();
		// 俱乐部信息
		builder.setCreatorId(creator);
		builder.setClubId(clubId);
		builder.setCost(cost.getCost());
		builder.setPayType(cost.getPayType());
		// 转发客户端的
		builder.setRoomType(message.getRoomType());
		builder.setRoundCount(message.getRoundCount());
		builder.addAllGameplays(message.getGameplaysList());
		builder.setVoice(message.getVoice());

		gameConnection.write(callback, builder.build());
	}

}
