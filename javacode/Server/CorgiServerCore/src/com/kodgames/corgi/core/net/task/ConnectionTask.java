package com.kodgames.corgi.core.net.task;

import com.kodgames.core.threadPool.task.Task;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.handler.AbstractMessageInitializer;
import com.kodgames.corgi.core.net.handler.connection.AbstractConnectionHandler;

public abstract class ConnectionTask extends Task
{
	AbstractMessageInitializer msgInitializer;	
	AbstractConnectionHandler handler;
	Connection connection;

	public ConnectionTask(AbstractMessageInitializer msgInitializer, Connection connection, AbstractConnectionHandler handler)
	{
		this.msgInitializer = msgInitializer;
		this.handler = handler;
		this.connection = connection;
	}

	@Override
    public Object getKey()
	{
		return handler.getMessageKey(connection);
	}

	@Override
    public boolean isUrgency()
	{
		return false;
	}
}