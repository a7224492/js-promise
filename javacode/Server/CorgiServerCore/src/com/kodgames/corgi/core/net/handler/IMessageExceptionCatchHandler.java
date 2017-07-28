package com.kodgames.corgi.core.net.handler;

import com.kodgames.corgi.core.net.Connection;

public interface IMessageExceptionCatchHandler
{
	public void handleMessage(Connection connection, int protocolID, int callback, Object buffer);
}
