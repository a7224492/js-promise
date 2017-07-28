package com.kodgames.battleserver.action.room;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.battleserver.service.room.RoomService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.game.GameProtoBuf.GBInviterRoomInfoREQ;

@ActionAnnotation(actionClass = GBInviterRoomInfoREQAction.class, messageClass = GBInviterRoomInfoREQ.class, serviceClass = RoomService.class)
public class GBInviterRoomInfoREQAction extends ProtobufMessageHandler<RoomService, GBInviterRoomInfoREQ>
{
	private static Logger logger = LoggerFactory.getLogger(GBInviterRoomInfoREQAction.class);

	@Override
	public void handleMessage(Connection connection, RoomService service, GBInviterRoomInfoREQ message, int callback)
	{
		logger.info("InviterRoom : roleId {} roomId {}.", message.getRoleId(), message.getRoomdId());
		service.onGBInviterRoomInfoREQ(connection, message, callback);
	}
	
	@Override
	public Object getMessageKey(Connection connection, GBInviterRoomInfoREQ message)
	{
		return message.getRoomdId();
	}

}
