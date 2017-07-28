package com.kodgames.corgi.core.net.task;

import com.kodgames.core.threadPool.task.Task;
import com.kodgames.corgi.core.net.handler.AbstractMessageInitializer;
import com.kodgames.corgi.core.net.handler.message.BaseMessageHandler;
import com.kodgames.corgi.core.net.message.InternalMessage;

public abstract class MessageTask extends Task
{
	AbstractMessageInitializer msgInitializer;
	InternalMessage message;
	
	BaseMessageHandler handler;

	public MessageTask(AbstractMessageInitializer msgInitializer, InternalMessage message, BaseMessageHandler<?> handler)
	{
		this.msgInitializer = msgInitializer;
		this.message = message;
		this.handler = handler;
	}

	@Override
    public Object getKey()
	{
		return handler.getMessageKey(msgInitializer, message);
	}

	@Override
    public boolean isUrgency()
	{
		if (message == null)
		{
			return false;
		}
		return message.isUrgency();
	}
}
