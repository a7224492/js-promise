package com.kodgames.battleserver.action.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.battleserver.service.server.ServerService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.server.ServerProtoBuf.SSServerDisconnectSYNC;

@ActionAnnotation(actionClass = SSServerDisconnectSYNCAction.class, messageClass = SSServerDisconnectSYNC.class, serviceClass = ServerService.class)
public class SSServerDisconnectSYNCAction extends ProtobufMessageHandler<ServerService, SSServerDisconnectSYNC>
{
	final static Logger logger = LoggerFactory.getLogger(SSServerDisconnectSYNCAction.class);

	@Override
	public void handleMessage(Connection connection, ServerService service, SSServerDisconnectSYNC message, int callback)
	{
		logger.info("ServerDisconnect : connectionId {} serverId {}.", connection.getConnectionID(), message.getId());
		service.onServerDisconnect(message.getId());
	}
}
