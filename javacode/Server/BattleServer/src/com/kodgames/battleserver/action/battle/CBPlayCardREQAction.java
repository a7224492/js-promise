package com.kodgames.battleserver.action.battle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.battleserver.service.battle.BattleService;
import com.kodgames.battleserver.service.room.RoomService;
import com.kodgames.battleserver.start.CBProtobufMessageHandler;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.message.proto.battle.BattleProtoBuf.BCPlayCardRES;
import com.kodgames.message.proto.battle.BattleProtoBuf.CBPlayCardREQ;

@ActionAnnotation(actionClass = CBPlayCardREQAction.class, messageClass = CBPlayCardREQ.class, serviceClass = BattleService.class)
public class CBPlayCardREQAction extends CBProtobufMessageHandler<BattleService, CBPlayCardREQ>
{
	final static Logger logger = LoggerFactory.getLogger(CBPlayCardREQAction.class);

	@Override
	public void handleMessage(Connection connection, BattleService service, CBPlayCardREQ message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

		// 回复打牌响应
		int roleId = connection.getRemotePeerID();
		int roomId = connection.getRoomID();
		int result = service.step(callback, roleId, roomId, message.getPlayType(), message.getCards());

		RoomService roomService = ServiceContainer.getInstance().getPublicService(RoomService.class);
		BCPlayCardRES.Builder resBuilder = BCPlayCardRES.newBuilder();
		resBuilder.setResult(result);
		resBuilder.setProtocolSeq(roomService.getAndSetNewPlayProtocolSequence(roomId, roleId));
		connection.write(callback, resBuilder.build());
	}
}
