package com.kodgames.game.action.room;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.game.common.Constant;
import com.kodgames.game.common.rule.RoomTypeConfig;
import com.kodgames.game.common.rule.RuleManager;
import com.kodgames.game.service.room.RoomService;
import com.kodgames.game.start.CGProtobufMessageHandler;
import com.kodgames.message.proto.game.GameProtoBuf.CGQueryBattleIdREQ;
import com.kodgames.message.proto.game.GameProtoBuf.GCQueryBattleIdRES;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

import xbean.RoleInfo;
import xbean.RoomInfo;

@ActionAnnotation(messageClass = CGQueryBattleIdREQ.class, actionClass = CGQueryBattleIdREQAction.class, serviceClass = RoomService.class)
public class CGQueryBattleIdREQAction extends CGProtobufMessageHandler<RoomService, CGQueryBattleIdREQ>
{
	private static final Logger logger = LoggerFactory.getLogger(CGQueryBattleIdREQAction.class);

	@Override
	public void handleMessage(Connection connection, RoomService service, CGQueryBattleIdREQ message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

		int roomId = message.getRoomId();
		int roleId = message.getRoleId();

		RoomInfo roomInfo = service.getRoom(roomId);
		RoleInfo roleInfo = service.getRoleInfoByRoleId(roleId);
		GCQueryBattleIdRES.Builder builder = GCQueryBattleIdRES.newBuilder();

		int battleId = service.getBattleIdByRoomId(roomId);

		if (0 == battleId || roomInfo == null)
		{
			int error = PlatformProtocolsConfig.GC_QUERY_BATTLEID_FAILED_ROOM_NOT_EXIST;
			builder.setResult(error);
			connection.write(callback, builder.build());
			String log = String.format("%s : 0x%x", getClass().getSimpleName(), error);
			logger.info(log);

			return;
		}

		RoomTypeConfig rc = RuleManager.getInstance().getRoomConfig(roomInfo.getRoundCount());
		int needCardCount = (null == rc) ? 0 : rc.getAACardCount();

		if (roomInfo.getIsLca())
		{
			needCardCount = 0;
		}

		if (roomInfo.getPayType() == Constant.PAY_TYPE.AA_PAY && roleInfo.getCardCount() < needCardCount)
		{
			int error = PlatformProtocolsConfig.GC_CREATE_ROOM_FAILED_ROOMCARD_NOT_ENOUGTH;
			builder.setResult(error);
			connection.write(callback, builder.build());
			String log = String.format("%s : 0x%x", getClass().getSimpleName(), error);
			logger.info(log);

			return;
		}

		if (roomInfo != null && roomInfo.getClubId() > 0)
		{
			int error = PlatformProtocolsConfig.GC_QUERY_BATTLEID_FAILED_CLUB_ROOM;
			builder.setResult(error);
			connection.write(callback, builder.build());
			String log = String.format("%s : 0x%x", getClass().getSimpleName(), error);
			logger.info(log);

			return;
		}

		builder.setBattleId(battleId);

		builder.setResult(PlatformProtocolsConfig.GC_QUERY_BATTLEID_SUCCESS);
		connection.write(callback, builder.build());
	}
}
