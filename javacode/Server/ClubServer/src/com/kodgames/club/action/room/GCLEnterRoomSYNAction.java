package com.kodgames.club.action.room;

import com.kodgames.corgi.core.net.common.ActionAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.club.service.room.ClubRoomService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.club.ClubProtoBuf.GCLEnterRoomSYN;


@ActionAnnotation(messageClass = GCLEnterRoomSYN.class, actionClass = GCLEnterRoomSYNAction.class, serviceClass = ClubRoomService.class)
public class GCLEnterRoomSYNAction extends ProtobufMessageHandler<ClubRoomService, GCLEnterRoomSYN>
{

	private static final Logger logger = LoggerFactory.getLogger(GCLEnterRoomSYNAction.class);
	
	@Override
	public void handleMessage(Connection connection, ClubRoomService service, GCLEnterRoomSYN message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
		
		service.addPlayer(message);
	}

	@Override
	public Object getMessageKey(Connection connection, GCLEnterRoomSYN message)
	{
		return  message.getRoomId();
	}
}
