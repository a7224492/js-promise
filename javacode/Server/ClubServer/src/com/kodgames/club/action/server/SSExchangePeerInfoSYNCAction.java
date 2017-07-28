package com.kodgames.club.action.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.club.service.server.ServerService;
import com.kodgames.corgi.core.constant.ServerType;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.message.proto.server.ServerProtoBuf;

/**
 * Created by marui on 2016/10/9.
 */
@ActionAnnotation(messageClass = ServerProtoBuf.SSExchangePeerInfoSYNC.class, actionClass = SSExchangePeerInfoSYNCAction.class, serviceClass = ServerService.class)
public class SSExchangePeerInfoSYNCAction
	extends ProtobufMessageHandler<ServerService, ServerProtoBuf.SSExchangePeerInfoSYNC>
{
	final static Logger logger = LoggerFactory.getLogger(SSExchangePeerInfoSYNCAction.class);

	@Override
	public void handleMessage(Connection connection, ServerService service,
		ServerProtoBuf.SSExchangePeerInfoSYNC message, int callback)
	{
		logger.debug("Exchange peer Info, remote peer id:{}", message.getServerID());
		connection.setRemotePeerID(message.getServerID());
		ConnectionManager.getInstance().addToServerConnections(connection);
		if (ServerType.getType(message.getServerID()) == ServerType.GAME_SERVER)
		{
			ServiceContainer.getInstance().getPublicService(ServerService.class).onGameConnect(connection);
		}
	}
}
