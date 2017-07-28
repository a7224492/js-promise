package com.kodgames.game.action.history;

import com.kodgames.game.service.server.ServerService;
import com.kodgames.game.start.CGProtobufMessageHandler;
import com.kodgames.message.proto.game.GameProtoBuf;
import com.kodgames.message.protocol.PlatformProtocolsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.message.proto.game.GameProtoBuf.CGHistoryREQ;

import java.util.ArrayList;

@ActionAnnotation(messageClass = CGHistoryREQ.class, actionClass = CGHistoryREQAction.class, serviceClass = ServerService.class)
public class CGHistoryREQAction extends CGProtobufMessageHandler<ServerService, CGHistoryREQ>
{
	private static final Logger logger = LoggerFactory.getLogger(CGHistoryREQAction.class);

	@Override
	public void handleMessage(Connection connection, ServerService service, CGHistoryREQ message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

		// 将请求转发给ReplayServer
		CGHistoryREQ.Builder builder = CGHistoryREQ.newBuilder();
		builder.mergeFrom(message).setClientId(connection.getRemotePeerID());

		Connection replay = service.getReplayConnection();
		if (replay == null)
		{
			GameProtoBuf.GCHistoryRES.Builder result = GameProtoBuf.GCHistoryRES.newBuilder();
			result.setResult(PlatformProtocolsConfig.GC_HISTORY_SUCCESS);

			result.setVersion(0);

			// 向客户端回复消息
			connection.write(callback, result.build());

			logger.error("replay server connection is null, drop CGHistoryREQ packet. roleId {}", connection.getRemotePeerID());
		}
		else
			replay.write(callback, builder.build());
	}
}
