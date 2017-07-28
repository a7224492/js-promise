package com.kodgames.authserver.net.server;

import com.kodgames.authserver.service.server.ServerService;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.message.proto.server.ServerProtoBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.handler.connection.AbstractConnectionHandler;

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

		//connected with manageserver
		if (connection.getRemoteNode().getAddress().equals(ConnectionManager.getInstance().getManagerServerAddress()))
		{
			ServerService service = ServiceContainer.getInstance().getPublicService(ServerService.class);
			service.getLanchInfo(connection);
		}
	}

	@Override public void handleConnectionInactive(Connection connection)
	{
		logger.info("Connection inActive. connection id : {}", connection.getConnectionID());
	}
}
