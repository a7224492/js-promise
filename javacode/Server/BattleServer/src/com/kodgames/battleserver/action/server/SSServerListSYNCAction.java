package com.kodgames.battleserver.action.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.battleserver.service.server.ServerService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.corgi.core.util.config_utils.ServerConfig;
import com.kodgames.message.proto.server.ServerProtoBuf.SSServerListSYNC;
import com.kodgames.message.proto.server.ServerProtoBuf.ServerConfigPROTO;

@ActionAnnotation(actionClass = SSServerListSYNCAction.class, messageClass = SSServerListSYNC.class, serviceClass = ServerService.class)
public class SSServerListSYNCAction extends ProtobufMessageHandler<ServerService, SSServerListSYNC>
{
	final static Logger logger = LoggerFactory.getLogger(SSServerListSYNCAction.class);

	@Override
	public void handleMessage(Connection connection, ServerService service, SSServerListSYNC message, int callback)
	{
		logger.info("ServerListSYN : connectionId {} message {}", connection.getConnectionID(), message);
		for (ServerConfigPROTO proto : message.getListList())
		{
			service.onServerConnect(ServerConfig.fromProto(proto));
		}
	}
}
