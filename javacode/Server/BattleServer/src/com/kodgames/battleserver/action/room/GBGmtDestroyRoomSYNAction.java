package com.kodgames.battleserver.action.room;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.battleserver.common.Constant.RoomDestroyReason;
import com.kodgames.battleserver.service.room.RoomService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.game.GameProtoBuf.GBGmtDestroyRoomSYN;

@ActionAnnotation(actionClass = GBGmtDestroyRoomSYNAction.class, messageClass = GBGmtDestroyRoomSYN.class, serviceClass = RoomService.class)
public class GBGmtDestroyRoomSYNAction extends ProtobufMessageHandler<RoomService, GBGmtDestroyRoomSYN>
{
	final static Logger logger = LoggerFactory.getLogger(GBCreateRoomREQAction.class);
	
	@Override
	public void handleMessage(Connection connection, RoomService service, GBGmtDestroyRoomSYN message, int callback)
	{
		logger.info("GmtDestoryRoom : roomId {}.", message.getRoomId());
		service.destroyRoom(message.getRoomId(), RoomDestroyReason.GMT);
	}

	@Override
	public Object getMessageKey(Connection connection, GBGmtDestroyRoomSYN message)
	{
		if (message.getRoomId() != 0)
		{
			return message.getRoomId();
		}
		else
		{
			logger.error("{} : message key miss roomId -> connectionId={}, message={}", getClass(), connection.getConnectionID(), message);
			return connection.getConnectionID();
		}
	}
}
