package com.kodgames.club.action.room;

import com.kodgames.club.service.room.ClubRoomService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.club.ClubProtoBuf.GCLEnableSubCardSYN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ActionAnnotation(messageClass = GCLEnableSubCardSYN.class, actionClass = GCLEnableSubCardSYNAction.class, serviceClass = ClubRoomService.class)
public class GCLEnableSubCardSYNAction extends ProtobufMessageHandler<ClubRoomService, GCLEnableSubCardSYN>
{

	private static final Logger logger = LoggerFactory.getLogger(GCLEnableSubCardSYNAction.class);
	
	@Override
	public void handleMessage(Connection connection, ClubRoomService service, GCLEnableSubCardSYN message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

		int roomId = message.getRoomId();
		int clubId = message.getClubId();

		service.enableRoomSubCard(clubId, roomId);
	}

}
