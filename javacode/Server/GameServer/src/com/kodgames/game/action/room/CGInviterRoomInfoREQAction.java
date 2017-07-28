package com.kodgames.game.action.room;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.game.service.room.RoomService;
import com.kodgames.message.proto.game.GameProtoBuf.CGInviterRoomInfoREQ;
import com.kodgames.message.proto.game.GameProtoBuf.GBInviterRoomInfoREQ;
import com.kodgames.message.proto.game.GameProtoBuf.GCInviterRoomInfoRES;
import com.kodgames.message.proto.game.GameProtoBuf.InviterRoomInfoPROTO;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

import xbean.RoomInfo;

@ActionAnnotation(messageClass = CGInviterRoomInfoREQ.class, actionClass = CGInviterRoomInfoREQAction.class, serviceClass = RoomService.class)
public class CGInviterRoomInfoREQAction extends ProtobufMessageHandler<RoomService, CGInviterRoomInfoREQ>
{
	private static Logger logger = LoggerFactory.getLogger(CGInviterRoomInfoREQAction.class);

	@Override
	public void handleMessage(Connection connection, RoomService service, CGInviterRoomInfoREQ message, int callback)
	{
		logger.info("{}:{}->{}", this.getClass().getSimpleName(), connection.getConnectionID(), message);
		RoomInfo roomInfo = table.Room_info.select(message.getRoomId());
		if (roomInfo == null)
		{
			GCInviterRoomInfoRES.Builder builder = GCInviterRoomInfoRES.newBuilder();
			builder.setResult(PlatformProtocolsConfig.BG_INVITER_ROOMINFO_FAILED_ROOM_NOT_EXIST);
			builder.setInfo(InviterRoomInfoPROTO.newBuilder().setRoomId(0).setCreatorId(0).setNickname("").setHeadImageUrl("").setRoundType(-1).setPayType(0));
			connection.write(callback, builder.build());
			return;
		}
		int battleId = roomInfo.getBattleId();
		Connection battleConnection = ConnectionManager.getInstance().getServerConnnection(battleId);
		battleConnection.write(callback, GBInviterRoomInfoREQ.newBuilder().setRoomdId(message.getRoomId()).setRoleId(connection.getRemotePeerID()).build());
	}

}
