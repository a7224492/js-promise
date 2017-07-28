/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kodgames.corgi.core.net.handler.message;

import com.kodgames.corgi.core.net.handler.netty.MessageProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.core.threadPool.OrderedThreadPoolExecutor;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.handler.AbstractMessageInitializer;
import com.kodgames.corgi.core.net.message.InternalMessage;
import com.kodgames.corgi.core.net.task.MessageTask;
import com.kodgames.corgi.core.service.AbstractMessageService;

import limax.zdb.exception.ZdbRollbackException;

/**
 * @author mrui
 */
public abstract class BaseMessageHandler<T>
{
	private static final Logger logger = LoggerFactory.getLogger(BaseMessageHandler.class);

	// public enum HandlerAction { CONTINUE, TERMINAL}
	OrderedThreadPoolExecutor executor;
	protected Class<? extends AbstractMessageService> serviceClass;

	void setExecutor(OrderedThreadPoolExecutor executor)
	{
		this.executor = executor;
	}

	public void setServiceClass(Class<? extends AbstractMessageService> serviceClass)
	{
		this.serviceClass = serviceClass;
	}
	
	@SuppressWarnings("unchecked")
	public boolean handleMessage(OrderedThreadPoolExecutor parentExecutor,
		final AbstractMessageInitializer msgInitializer, final InternalMessage message) throws ZdbRollbackException
	{
		if (executor == null || parentExecutor == executor)
		{
			if (message.getProtocolID() != 0)
			{
				if ((message.getMessageType() & MessageProcessor.MESSAGE_TYPE_CLIENT) != 0)
				{
					handleMessage(message.getConnection(),
							message.getProtocolID(),
							message.getCallback(),
							message.getDstPeerID(),
							(T) message.getMessage());
				}
				else if ((message.getMessageType() & MessageProcessor.MESSAGE_TYPE_S2I) != 0)
				{
					handleMessage(message.getConnection(),
							message.getProtocolID(),
							message.getCallback(),
							message.getClientID(),
							message.getClientConnectionID(),
							(T) message.getMessage());
				}
				else
				{
					handleMessage(message.getConnection(),
							message.getProtocolID(),
							message.getCallback(),
							(T) message.getMessage());
				}
			}
			else
			{
				logger.error("handleMessage can't get message protocolID");
			}
			return true;
		}

		if (executor != null)
		{
			executor.execute(new MessageTask(msgInitializer, message, this)
			{
				@Override public void run()
				{
					logger.debug("handleMessage.run: run begin. key is " + this.getKey());
					try
					{
						handleMessage(executor, msgInitializer, message);
					}
					catch (Exception e)
					{
						logger.error("exception " + e);
					}
					logger.debug("handleMessage.run: run end. key is " + this.getKey());
				}
			});
			return false;
		}
		return true;
	}

	public Object getMessageKey(AbstractMessageInitializer msgInitializer, InternalMessage message)
	{
		if ((message.getMessageType() & MessageProcessor.MESSAGE_TYPE_S2I) != 0)
		{
			return message.getClientConnectionID();
		}
		else
		{
			return getMessageKey(message.getConnection(), (T) message.getMessage());
		}
	}

	public abstract void handleMessage(Connection connection, int protocolID, int callback, T buffer) throws ZdbRollbackException;

	public abstract void handleMessage(Connection connection, int protocolID, int callback, int dstPeerID,
		T buffer) throws ZdbRollbackException;

	public abstract void handleMessage(Connection connection, int protocolID, int callback, int clientID, int clientConnectionID,
									   T buffer) throws ZdbRollbackException;

	/**
	 * 如果在messageInitializer里设置了BeforeMessageExecutor或将此Handler和MessageExecutor绑定了 ，在线程池会保证相同MessageKey的消息先后执行顺@param
	 * remoteNode
	 */
	public abstract Object getMessageKey(Connection connection, T buffer);
}
