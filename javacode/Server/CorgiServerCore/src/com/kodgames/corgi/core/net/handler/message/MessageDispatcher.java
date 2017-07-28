package com.kodgames.corgi.core.net.handler.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.core.threadPool.OrderedThreadPoolExecutor;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.handler.AbstractMessageInitializer;
import com.kodgames.corgi.core.net.handler.MessageExceptionCatchHandler;
import com.kodgames.corgi.core.net.handler.connection.AbstractConnectionHandler;
import com.kodgames.corgi.core.net.message.InternalMessage;
import com.kodgames.corgi.core.net.task.ConnectionTask;
import com.kodgames.corgi.core.net.task.MessageTask;

import limax.zdb.Procedure;
import limax.zdb.XDeadlock;
import limax.zdb.exception.ZdbRollbackException;

public class MessageDispatcher
{
	private Logger logger = LoggerFactory.getLogger(MessageDispatcher.class);

	private static MessageDispatcher instance = new MessageDispatcher();
	private boolean useZDBProcedure = true;

	public static MessageDispatcher getInstance()
	{
		return instance;
	}

	public void setOpenZDBProcedure(boolean openZDBProcedure)
	{
		this.useZDBProcedure = openZDBProcedure;
	}

	/**
	 * 只处理消息派发，不处理连接的Active和Inactive
	 * 
	 * @param messageInitializer
	 * @param protocol
	 */
	public void dispatchMessage(AbstractMessageInitializer messageInitializer, final InternalMessage protocol)
	{
		final BaseMessageHandler<?> handler = messageInitializer.getMessageHandler(protocol.getProtocolID());
		final OrderedThreadPoolExecutor beforeExecutor = messageInitializer.getBeforeExecutor();
		if (beforeExecutor != null)
		{
			beforeExecutor.execute(new MessageTask(messageInitializer, protocol, handler)
			{
				@Override
				public void run()
				{
					if (useZDBProcedure)
					{
						Procedure.call(() -> {
							try
							{
								// 注册当前线程，代表该线程进行了handler处理
								handler.handleMessage(beforeExecutor, messageInitializer, protocol);
							}
							catch (ZdbRollbackException ex)
							{
								logger.warn("Procedure call found transaction rollback, " + ex.getMessage());
								return false;
							}
							catch (XDeadlock e) 
							{
								logger.warn("deadlock exception: " + e.getMessage());
								return false;
							}
							catch (Throwable e)
							{
								MessageExceptionCatchHandler.getInstance().handleMessage(protocol.getConnection(), protocol.getProtocolID(), protocol.getCallback(), protocol.getMessage());
								logger.error("Failed to hander message:{} protocolId {}, remote {}, connection {}, exception={}",
									protocol.getMessage().getClass().getSimpleName(),
									protocol.getProtocolID(),
									protocol.getConnection().getNettyNode().getAddress(),
									protocol.getConnection().getConnectionID(),
									e);
							}

							return true;
						});
					}
					else
					{
						try
						{
							// 注册当前线程，代表该线程进行了handler处理
							handler.handleMessage(beforeExecutor, messageInitializer, protocol);
						}
						catch (Throwable e)
						{
							MessageExceptionCatchHandler.getInstance().handleMessage(protocol.getConnection(), protocol.getProtocolID(), protocol.getCallback(), protocol.getMessage());
							logger.error("Failed to hander message:{} protocolId {}, remote {}, connection {}, exception={}",
								protocol.getMessage().getClass().getSimpleName(),
								protocol.getProtocolID(),
								protocol.getConnection().getNettyNode().getAddress(),
								protocol.getConnection().getConnectionID(),
								e);
						}
					}

				}
			});
		}
		else
		{
			if (useZDBProcedure)
			{
				Procedure.call(() -> {
					try
					{
						// 注册当前线程，代表该线程进行了handler处理
						handler.handleMessage(beforeExecutor, messageInitializer, protocol);
					}
					catch (ZdbRollbackException ex)
					{
						logger.warn("Procedure call found transaction rollback" + ex.getMessage());
						return false;
					}
					catch (XDeadlock e) 
					{
						logger.warn("deadlock exception: " + e.getMessage());
						return false;
					}
					catch (Throwable e)
					{
						MessageExceptionCatchHandler.getInstance().handleMessage(protocol.getConnection(), protocol.getProtocolID(), protocol.getCallback(), protocol.getMessage());
						logger.error("Failed to hander message:{}", protocol.getMessage().getClass().getSimpleName(), e);
					}
					return true;
				});
			}
			else
			{
				try
				{
					// 注册当前线程，代表该线程进行了handler处理
					handler.handleMessage(beforeExecutor, messageInitializer, protocol);
				}
				catch (Throwable e)
				{
					MessageExceptionCatchHandler.getInstance().handleMessage(protocol.getConnection(), protocol.getProtocolID(), protocol.getCallback(), protocol.getMessage());
					logger.error("Failed to hander message:{}", protocol.getMessage().getClass().getSimpleName(), e);
				}
			}
		}
	}

	/**
	 * 处理Connection的Active和Inactive
	 */
	public void connectionStatusDispatch(AbstractMessageInitializer messageInitializer, Connection connection, boolean isActive)
	{
		AbstractConnectionHandler handler = messageInitializer.getConnectionHandler();
		final OrderedThreadPoolExecutor beforeExecutor = messageInitializer.getBeforeExecutor();
		if (beforeExecutor != null)
		{
			beforeExecutor.execute(new ConnectionTask(messageInitializer, connection, handler)
			{
				@Override
				public void run()
				{
					if (isActive)
					{
						handler.handleConnectionActive(connection);
					}
					else
					{
						handler.handleConnectionInactive(connection);
					}
				}
			});
		}
		else
		{
			if (isActive)
			{
				handler.handleConnectionActive(connection);
			}
			else
			{
				handler.handleConnectionInactive(connection);
			}
		}
	}
}
