package com.kodgames.interfaces.net.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.handler.connection.AbstractConnectionHandler;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.interfaces.service.server.ServerService;

public class ISConnectionHandler extends AbstractConnectionHandler 
{
	static private Logger logger = LoggerFactory.getLogger(ISConnectionHandler.class);
	@Override
    public void handleConnectionActive(Connection connection)
    {
	    logger.info("Connection active. Remote Server Address:{}", connection.getNettyNode().getAddress());
	    
	    ServiceContainer.getInstance().getPublicService(ServerService.class).serverConnected(connection.getConnectionID(), connection);
    }

	@Override
    public void handleConnectionInactive(Connection connection)
    {
		logger.info("Connection inactive. Remote Server Address:{}", connection.getNettyNode().getAddress()); 
    }
}
