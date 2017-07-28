package com.kodgames.club.action.room;

import com.kodgames.club.utils.ClubUtils;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.message.proto.club.ClubProtoBuf;
import com.kodgames.message.protocol.PlatformProtocolsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.club.service.room.ClubRoomService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.club.ClubProtoBuf.CLCCreateRoomRES;
import com.kodgames.message.proto.club.ClubProtoBuf.GCLCreateRoomRES;


@ActionAnnotation(messageClass = GCLCreateRoomRES.class, actionClass = GCLCreateRoomRESAction.class, serviceClass = ClubRoomService.class)
public class GCLCreateRoomRESAction extends ProtobufMessageHandler<ClubRoomService, GCLCreateRoomRES>
{

	private static final Logger logger = LoggerFactory.getLogger(GCLCreateRoomRESAction.class);
	
	@Override
	public void handleMessage(Connection connection, ClubRoomService service, GCLCreateRoomRES message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
		
		int battleId = message.getBattleId();
		int roomId = message.getRoomId();
		int clubId = message.getClubId();
		int creatorId = message.getCreatorId();
		// 想client转发消息
		CLCCreateRoomRES.Builder builder = CLCCreateRoomRES.newBuilder();
		builder.setResult(message.getResult());
		if (message.getResult() == PlatformProtocolsConfig.GCL_CREATE_ROOM_SUCCESS)
		{
			service.addNewRoomToClub(message);
			builder.setBattleId(battleId);
			builder.setRoomId(roomId);
			builder.setClubId(clubId);
			builder.setVoice(message.getVoice());
		}

		// game转播
		ClubUtils.broadcastMsg2Game(callback, creatorId, builder.build());
	}

}
