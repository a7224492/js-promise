package com.kodgames.club.net.server;

import com.kodgames.corgi.core.constant.ServerType;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.message.proto.server.ServerProtoBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.handler.connection.AbstractConnectionHandler;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.club.service.server.ServerService;

public class SSConnectionHandler extends AbstractConnectionHandler
{
	static private Logger logger = LoggerFactory.getLogger(SSConnectionHandler.class);

	@Override public void handleConnectionActive(Connection connection)
	{
		logger.info("Connection active. connection id : {}", connection.getConnectionID());

		connection.setConnectionType(Connection.CONNECTION_TYPE_SERVER);
		if ( ConnectionManager.getInstance().getLocalPeerID() != 0 )
		{
			ServerProtoBuf.SSExchangePeerInfoSYNC.Builder builder = ServerProtoBuf.SSExchangePeerInfoSYNC.newBuilder();
			builder.setServerID(ConnectionManager.getInstance().getLocalPeerID());
			connection.write(0, builder.build());
		}

		if (connection.getRemoteNode().getAddress().equals(ConnectionManager.getInstance().getManagerServerAddress()))
		{
			//判断是否是Manager的连接建立
			ServerService service = ServiceContainer.getInstance().getPublicService(ServerService.class);
			//向Manager请求配置信息
			service.onManagerConnect(connection);
		}

	}

	@Override public void handleConnectionInactive(Connection connection)
	{
		logger.info("Connection inActive. connection id : {}", connection.getConnectionID());

		if (connection.getConnectionType() == Connection.CONNECTION_TYPE_SERVER)
		{
			int serverID = connection.getRemotePeerID();
			if (serverID == 0)
			{
				return;
			}
			switch (ServerType.getType(serverID))
			{
				case ServerType.MANAGER_SERVER:
					ServiceContainer.getInstance().getPublicService(ServerService.class).onManagerDisconnect(connection);
					break;
				case ServerType.GAME_SERVER:
					ServiceContainer.getInstance().getPublicService(ServerService.class).onGameDisconnect();
					break;
			}
		}
	}
}
