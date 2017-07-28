package com.kodgames.corgi.core.net.handler.connection;

import com.kodgames.corgi.core.net.Connection;

public abstract class AbstractConnectionHandler
{
	public abstract void handleConnectionActive(Connection connection);

	public abstract void handleConnectionInactive(Connection connection);

	public Object getMessageKey(Connection connection)
	{
		return connection.getConnectionID();
	}
}