package com.kodgames.corgi.core.net.handler.message;

import com.google.protobuf.GeneratedMessage;
import com.kodgames.corgi.core.net.Connection;

public abstract class AbstractProtobufMessageHandler<T extends GeneratedMessage> extends BaseMessageHandler<T>
{
	@Override
	abstract public void handleMessage(Connection connection, int protocolID, int callback, T message);
	
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
	abstract public Object getMessageKey(Connection connection, T buffer);
}
