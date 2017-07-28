package com.kodgames.authserver.action.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.authserver.service.server.ServerService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.server.ServerProtoBuf.SSServerListSYNC;

@ActionAnnotation(actionClass = SSServerListSYNCAction.class, messageClass = SSServerListSYNC.class, serviceClass = ServerService.class)
public class SSServerListSYNCAction extends ProtobufMessageHandler<ServerService, SSServerListSYNC>
{
	final static Logger logger = LoggerFactory.getLogger(SSServerListSYNCAction.class);

	@Override
	public void handleMessage(Connection connection, ServerService service, SSServerListSYNC message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
		service.onServerConnect(connection, message, callback);
	}
}
