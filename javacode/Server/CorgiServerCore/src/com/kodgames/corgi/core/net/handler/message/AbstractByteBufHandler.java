package com.kodgames.corgi.core.net.handler.message;

import com.kodgames.corgi.core.net.Connection;

import io.netty.buffer.ByteBuf;

public abstract class AbstractByteBufHandler extends BaseMessageHandler<ByteBuf>
{
	@Override
	public abstract void handleMessage(Connection connection, int protocolID, int callback, ByteBuf message);

	@Override
	public abstract void handleMessage(Connection connection, int protocolID, int callback, int dstConnection, ByteBuf message);

	@Override
	public abstract void handleMessage(Connection connection, int protocolID, int callback, int clientID, int clientConnectionID, ByteBuf message);

	@Override
	final public Object getMessageKey(Connection connection, ByteBuf message)
	{
		return connection.getConnectionID();
	};
}