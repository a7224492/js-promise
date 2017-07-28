package com.kodgames.club.action.room;

import com.kodgames.club.service.room.ClubRoomService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.game.GameProtoBuf.GCLGameConfigSYN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ActionAnnotation(messageClass = GCLGameConfigSYN.class, actionClass = GCLGameConfigSYNAction.class, serviceClass = ClubRoomService.class)
public class GCLGameConfigSYNAction extends ProtobufMessageHandler<ClubRoomService, GCLGameConfigSYN>
{

	private static final Logger logger = LoggerFactory.getLogger(GCLGameConfigSYNAction.class);
	
	@Override
	public void handleMessage(Connection connection, ClubRoomService service, GCLGameConfigSYN message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

		// 更新全局房间类型配置
		service.updateRoomConfig(message.getRoomConfigsList());
	}

}
