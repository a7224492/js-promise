package com.kodgames.club.action.room;

import com.kodgames.club.utils.ClubUtils;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.message.proto.club.ClubProtoBuf;
import com.kodgames.message.proto.game.GameProtoBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.club.constant.ClubConstants;
import com.kodgames.club.constant.ClubRights;
import com.kodgames.club.service.common.ClubCommonService;
import com.kodgames.club.service.room.ClubRoomService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.message.proto.club.ClubProtoBuf.CCLEnterRoomREQ;
import com.kodgames.message.proto.club.ClubProtoBuf.CLCEnterRoomRES;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

import xbean.ClubRoomInfo;
import xbean.MemberInfo;
import xbean.RoomCost;

@ActionAnnotation(messageClass = CCLEnterRoomREQ.class, actionClass = CCLEnterRoomREQAction.class, serviceClass = ClubRoomService.class)
public class CCLEnterRoomREQAction extends ProtobufMessageHandler<ClubRoomService, CCLEnterRoomREQ>
{

	private static final Logger logger = LoggerFactory.getLogger(CCLEnterRoomREQAction.class);

	@Override
	public void handleMessage(Connection connection, ClubRoomService service, CCLEnterRoomREQ message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

		int clubId = message.getClubId();
		int roomId = message.getRoomId();
		int playerId = connection.getRemotePeerID();

		CLCEnterRoomRES.Builder builder = CLCEnterRoomRES.newBuilder();

		ClubRoomInfo room = service.getRoom(clubId, roomId);  // 先取 room 锁，防止room表和club表死锁

		// 没这个room
		if (room == null)
		{
			builder.setResult(PlatformProtocolsConfig.CLC_ENTER_ROOM_FAILED_NO_SUCH_ROOM);
			connection.write(callback, builder.build());
			logger.warn("enterRoom: no such room {}", roomId);
			return;
		}

		ClubCommonService ccs = ServiceContainer.getInstance().getPublicService(ClubCommonService.class);

		if (ccs.checkClubStatus(clubId, ClubConstants.CLUB_STATUS.SAEL_FLAG)) {
			builder.setResult(PlatformProtocolsConfig.CLC_ENTER_ROOM_FAILED_CLUB_SEALED);
			connection.write(callback, builder.build());

			logger.warn("enterRoom: club {} sealed", clubId);
			return;
		}

		MemberInfo member = ccs.getMemberInfo(playerId, clubId);
		if (member == null) {
			builder.setResult(PlatformProtocolsConfig.CLC_ENTER_ROOM_NOT_IN_CLUB);
			connection.write(callback, builder.build());

			logger.warn("enterRoom: player {} not in club {}", playerId, clubId);
			return;
		}
		// 检查权限够不够
		if (!ClubRights.hasRight(member, ClubConstants.CLUB_ACTIONS.EnterRoom))
		{
			builder.setResult(PlatformProtocolsConfig.CLC_ENTER_ROOM_FAILED_NO_RIGHT);
			connection.write(callback, builder.build());

			logger.warn("enterRoom: member {} has no right", playerId);
			return;
		}

		RoomCost cost = service.getRoomCostSnap(clubId, room.getRoomId());
		if (cost.getPayType() == ClubConstants.PAY_TYPE.WINNER_PAY) {
			GameProtoBuf.RoomConfigPROTO roomConfig = service.getRoomConfig(room.getRoundCount());
			int roomCost = roomConfig.getCardCount();
			// 检查这个人的俱乐部房卡够不够！
			if (member.getCardCount() < cost.getCost() * roomCost)
			{
				builder.setResult(PlatformProtocolsConfig.CLC_ENTER_ROOM_FAILED_NOT_ENOUGH_CARDS);
				connection.write(callback, builder.build());

				logger.warn("createRoom: member has cards {}, but room cost too much {}", member.getCardCount(), cost.getCost());
				return;
			}
		}
		
		builder.setResult(PlatformProtocolsConfig.CLC_ENTER_ROOM_SUCCESS);
		builder.setRoomId(room.getRoomId());
		builder.setClubId(clubId);
		builder.setBattleId(room.getBattleId());
		connection.write(callback, builder.build());
	}

	@Override
	public Object getMessageKey(Connection connection, CCLEnterRoomREQ message)
	{
		return message.getClubId();
	}
}
