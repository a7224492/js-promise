package com.kodgames.battleserver.action.room;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.battleserver.service.battle.common.xbean.BattleRoom;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerGpsInfo;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.room.RoomService;
import com.kodgames.battleserver.start.CBProtobufMessageHandler;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.message.proto.room.RoomProtoBuf.BCGpsInfoRES;
import com.kodgames.message.proto.room.RoomProtoBuf.CBGpsInfoREQ;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

/**
 * GPS同步请求 
 */
@ActionAnnotation(actionClass = CBGpsInfoREQAction.class, messageClass = CBGpsInfoREQ.class, serviceClass = RoomService.class)
public class CBGpsInfoREQAction extends CBProtobufMessageHandler<RoomService, CBGpsInfoREQ>
{
	final static Logger logger = LoggerFactory.getLogger(CBGpsInfoREQAction.class);
	@Override
	public void handleMessage(Connection connection, RoomService service, CBGpsInfoREQ message, int callback)
	{
		int roleId = connection.getRemotePeerID();
		int roomId = connection.getRoomID();

		// 应答GPS信息上报
		BattleRoom room = service.getRoomInfo(roomId);
		PlayerInfo playerInfo = service.getRoomPlayerInfo(roomId, roleId);
		if (null == room || null == playerInfo)
		{
			logger.warn("CBGpsInfoREQAction room is null");
			connection.write(callback,
				BCGpsInfoRES.newBuilder().setResult(PlatformProtocolsConfig.BC_GPS_INFO_FAILED_NOT_IN_ROOM).build());
			return;
		}
		else
		{
			PlayerGpsInfo playerGpsInfo = playerInfo.getGps();
			playerGpsInfo.setIsOpen(message.getIsOpen());
			playerGpsInfo.setLatitude(message.getLatitude());
			playerGpsInfo.setLongitude(message.getLongitude());

			connection.write(callback,
				BCGpsInfoRES.newBuilder().setResult(PlatformProtocolsConfig.BC_GPS_INFO_SUCCESS).build());
		}

		// 同步告警信息
		service.syncSecureDetectInfoToPlayers(room);
	}
}
