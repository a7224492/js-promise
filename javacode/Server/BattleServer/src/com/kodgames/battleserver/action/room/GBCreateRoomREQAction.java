package com.kodgames.battleserver.action.room;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.battleserver.service.battle.BattleService;
import com.kodgames.battleserver.service.room.RoomService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.message.proto.game.GameProtoBuf.BGCreateRoomRES;
import com.kodgames.message.proto.game.GameProtoBuf.GBCreateRoomREQ;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

@ActionAnnotation(actionClass = GBCreateRoomREQ.class, messageClass = GBCreateRoomREQ.class, serviceClass = RoomService.class)
public class GBCreateRoomREQAction extends ProtobufMessageHandler<RoomService, GBCreateRoomREQ>
{
	final static Logger logger = LoggerFactory.getLogger(GBCreateRoomREQAction.class);

	@Override
	public void handleMessage(Connection connection, RoomService service, GBCreateRoomREQ message, int callback)
	{
		logger.info("CreateRoom : The role {} room {}", message.getCreatorId(), message.getRoomId());

		BGCreateRoomRES.Builder builder = BGCreateRoomRES.newBuilder();
		int result =
			service.createRoom(message.getCreatorId(), message.getRoomId(), message.getRoomType(), message.getGameplaysList(), message.getRoundCount(), message.getPayType(), message.getVoice());
		builder.setResult(result);
		builder.setRoomId(message.getRoomId());
		builder.setCreatorId(message.getCreatorId());
		builder.addAllGameplays(message.getGameplaysList());
		builder.setPayType(message.getPayType());
		builder.setVoice(message.getVoice());

		// 设置房间最大玩家人数
		BattleService battleService = ServiceContainer.getInstance().getPublicService(BattleService.class);
		builder.setPlayerMax(battleService.getBattleCreator().getMaxPlayerSize(message.getGameplaysList()));

		if (PlatformProtocolsConfig.BG_CREATE_ROOM_SUCCESS != result)
		{
			logger.warn("CreateRoom failed : The role {} create room {} failed result {}", message.getCreatorId(), message.getRoomId(), result);
		}

		connection.write(callback, builder.build());
	}

	@Override
	public Object getMessageKey(Connection connection, GBCreateRoomREQ message)
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