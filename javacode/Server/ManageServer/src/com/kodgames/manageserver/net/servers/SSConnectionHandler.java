package com.kodgames.manageserver.net.servers;

import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.message.proto.server.ServerProtoBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.handler.connection.AbstractConnectionHandler;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.manageserver.service.servers.ServerService;

/**
 * manager把其他所有服务器的的连接当做客户端
 */
public class SSConnectionHandler extends AbstractConnectionHandler
{
	static private final Logger logger = LoggerFactory.getLogger(SSConnectionHandler.class);

	@Override public void handleConnectionActive(Connection connection)
	{
		connection.setConnectionType(Connection.CONNECTION_TYPE_SERVER);
		if ( ConnectionManager.getInstance().getLocalPeerID() != 0 )
		{
			ServerProtoBuf.SSExchangePeerInfoSYNC.Builder builder = ServerProtoBuf.SSExchangePeerInfoSYNC.newBuilder();
			builder.setServerID(ConnectionManager.getInstance().getLocalPeerID());
			connection.write(0, builder.build());
		}
		logger.info("Connection active. Remote Server Address:{}", connection.getNettyNode().getAddress());
	}

	@Override public void handleConnectionInactive(Connection connection)
	{
		logger.info("Connection inactive. Remote Server Address:{} id {}", connection.getNettyNode().getAddress(), 
			connection.getConnectionID());
		
		ServerService service = ServiceContainer.getInstance().getPublicService(ServerService.class);
		service.disconnect(connection);
	}
}
