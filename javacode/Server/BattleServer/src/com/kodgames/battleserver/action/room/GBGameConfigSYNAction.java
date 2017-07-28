package com.kodgames.battleserver.action.room;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.battleserver.service.room.RoomService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.game.GameProtoBuf.GBGameConfigSYN;

@ActionAnnotation(actionClass = GBGameConfigSYN.class, messageClass = GBGameConfigSYN.class, serviceClass = RoomService.class)
public class GBGameConfigSYNAction extends ProtobufMessageHandler<RoomService, GBGameConfigSYN>
{
	final static Logger logger = LoggerFactory.getLogger(GBGameConfigSYN.class);

	@Override
	public void handleMessage(Connection connection, RoomService service, GBGameConfigSYN message, int callback)
	{
		logger.info("GameConfig : battle creator : {}", message.getCreatorClassName());

		// 设置游戏地区名称（Creator名称）
		service.setRegionName(message.getCreatorClassName());

		// 更新全局房间类型配置
		service.updateRoomConfig(message.getRoomConfigsList());
	}

}
