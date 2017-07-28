package com.kodgames.club.action.room;

import com.kodgames.corgi.core.net.common.ActionAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.club.service.room.ClubRoomService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.club.ClubProtoBuf.GCLDestroyRoomSYN;


@ActionAnnotation(messageClass = GCLDestroyRoomSYN.class, actionClass = GCLDestroyRoomSYNAction.class, serviceClass = ClubRoomService.class)
public class GCLDestroyRoomSYNAction extends ProtobufMessageHandler<ClubRoomService, GCLDestroyRoomSYN>
{

	private static final Logger logger = LoggerFactory.getLogger(GCLDestroyRoomSYNAction.class);
	
	@Override
	public void handleMessage(Connection connection, ClubRoomService service, GCLDestroyRoomSYN message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
		
		service.destroyRoom(message);
	}

	@Override
	public Object getMessageKey(Connection connection, GCLDestroyRoomSYN message)
	{
		return  message.getClubId();
	}

}
