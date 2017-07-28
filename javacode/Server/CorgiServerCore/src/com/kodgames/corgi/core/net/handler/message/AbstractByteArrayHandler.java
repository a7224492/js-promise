package com.kodgames.corgi.core.net.handler.message;

import com.kodgames.corgi.core.net.Connection;

public abstract class AbstractByteArrayHandler extends BaseMessageHandler<byte[]>
{
	@Override
	public abstract void handleMessage(Connection connection, int protocolID, int callback, byte[] message);
	
	@Override
	public abstract void handleMessage(Connection connection, int protocolID, int callback, int dstConnection, byte[] message);

	@Override
	public abstract void handleMessage(Connection connection, int protocolID, int callback, int clientID, int clientConnectionID, byte[] message);

	@Override
	final public Object getMessageKey(Connection connection, byte[] message)
	{
		return connection.getConnectionID();
	};
}