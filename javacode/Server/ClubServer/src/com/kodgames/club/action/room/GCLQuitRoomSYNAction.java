package com.kodgames.club.action.room;

import com.kodgames.corgi.core.net.common.ActionAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.club.service.room.ClubRoomService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.club.ClubProtoBuf.GCLQuitRoomSYN;


@ActionAnnotation(messageClass = GCLQuitRoomSYN.class, actionClass = GCLQuitRoomSYNAction.class, serviceClass = ClubRoomService.class)
public class GCLQuitRoomSYNAction extends ProtobufMessageHandler<ClubRoomService, GCLQuitRoomSYN>
{

	private static final Logger logger = LoggerFactory.getLogger(GCLQuitRoomSYNAction.class);
	
	@Override
	public void handleMessage(Connection connection, ClubRoomService service, GCLQuitRoomSYN message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
		
		service.deletePlayer(message);
	}

	@Override
	public Object getMessageKey(Connection connection, GCLQuitRoomSYN message)
	{
		return  message.getRoomId();
	}
}
