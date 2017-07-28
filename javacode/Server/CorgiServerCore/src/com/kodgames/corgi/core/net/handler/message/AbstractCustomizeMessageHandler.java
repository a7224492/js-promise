package com.kodgames.corgi.core.net.handler.message;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.message.AbstractCustomizeMessage;

public abstract class AbstractCustomizeMessageHandler<T extends AbstractCustomizeMessage> extends BaseMessageHandler<T>
{
	@Override
	public abstract void handleMessage(Connection connection, int protocolID, int callback, T message);
	
	@Override
	public void handleMessage(Connection connection, int protocolID, int callback, int dstConnection, T message)
	{
		handleMessage(connection, protocolID, callback, message);
	}

	@Override
	public void handleMessage(Connection connection, int protocolID, int callback, int clientID, int clientConnectionID, T message)
	{
		handleMessage(connection, protocolID, callback, message);
	}

	@Override
	final public Object getMessageKey(Connection connection, T message)
	{
		return connection.getConnectionID();
	};
}
