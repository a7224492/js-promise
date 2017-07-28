package com.kodgames.battleserver.net.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.battleserver.service.server.ServerService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.handler.connection.AbstractConnectionHandler;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.message.proto.server.ServerProtoBuf;

public class SSConnectionHandler extends AbstractConnectionHandler
{
	static private Logger logger = LoggerFactory.getLogger(SSConnectionHandler.class);

	@Override
	public void handleConnectionActive(Connection connection)
	{
		logger.info("Connection active, connectionId : {}!", connection.getConnectionID());
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
			service.getLaunchInfo(connection);
		}
	}

	@Override
	public void handleConnectionInactive(Connection connection)
	{
		logger.info("Connection inActive, connectionId : {}!", connection.getConnectionID());
	}
}
