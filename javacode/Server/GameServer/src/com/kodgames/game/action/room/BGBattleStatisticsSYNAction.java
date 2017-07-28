package com.kodgames.game.action.room;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.game.service.global.GlobalService;
import com.kodgames.message.proto.game.GameProtoBuf.BGBattleStatisticsSYN;

@ActionAnnotation(messageClass = BGBattleStatisticsSYN.class, actionClass = BGBattleStatisticsSYNAction.class, serviceClass = GlobalService.class)
public class BGBattleStatisticsSYNAction extends ProtobufMessageHandler<GlobalService, BGBattleStatisticsSYN>
{

	@Override
	public void handleMessage(Connection connection, GlobalService service, BGBattleStatisticsSYN message, int callback)
	{
		service.updateBattleInfo(connection.getRemotePeerID(),
			message.getTotalRoomCount(),
			message.getPositiveRoomCount(),
			message.getSilentRoomCount());
	}

	@Override
	public Object getMessageKey(Connection connection, BGBattleStatisticsSYN message)
	{
		return connection.getConnectionID();
	}

}
