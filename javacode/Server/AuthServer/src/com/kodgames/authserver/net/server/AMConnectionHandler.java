package com.kodgames.authserver.net.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.authserver.service.server.ServerService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.handler.connection.AbstractConnectionHandler;
import com.kodgames.corgi.core.service.ServiceContainer;

public class AMConnectionHandler extends AbstractConnectionHandler
{
	static private Logger logger = LoggerFactory.getLogger(AMConnectionHandler.class);

	@Override
	public void handleConnectionActive(Connection connection)
	{
		logger.info("Connection active : {}!", connection.getConnectionID());
		connection.setConnectionType(Connection.CONNECTION_TYPE_SERVER);
		ServerService service = ServiceContainer.getInstance().getPublicService(ServerService.class);
		service.getLanchInfo(connection);
	}

	@Override
	public void handleConnectionInactive(Connection connection)
	{
		logger.info("Connection inActive : {}!", connection.getConnectionID());
	}
}
