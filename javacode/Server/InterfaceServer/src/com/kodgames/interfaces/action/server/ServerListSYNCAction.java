package com.kodgames.interfaces.action.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.interfaces.service.server.ServerService;
import com.kodgames.message.proto.server.ServerProtoBuf.SSServerListSYNC;

@ActionAnnotation(messageClass = SSServerListSYNC.class,
				  actionClass = ServerListSYNCAction.class,
				  serviceClass = ServerService.class)
public class ServerListSYNCAction extends ProtobufMessageHandler<ServerService, SSServerListSYNC>
{
	final static Logger logger = LoggerFactory.getLogger(ServerListSYNCAction.class);
	
	@Override
	public void handleMessage(Connection connection, ServerService service, SSServerListSYNC message, int callback)
	{
		// 连接其他服务器
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
		service.connectToServers(message.getListList());
	}
}