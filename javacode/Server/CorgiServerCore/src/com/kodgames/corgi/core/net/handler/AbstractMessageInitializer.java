package com.kodgames.corgi.core.net.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.kodgames.core.threadPool.OrderedThreadPoolExecutor;
import com.kodgames.corgi.core.net.handler.connection.AbstractConnectionHandler;
import com.kodgames.corgi.core.net.handler.message.BaseMessageHandler;
import com.kodgames.corgi.core.service.AbstractMessageService;

public abstract class AbstractMessageInitializer
{
	final public static int CUSTOMIZEMESSAGE_HANDLER = 1;
	final public static int PROTOBUF_HANDLER = 2;
	final public static int BYTEARRAY_HANDLER = 3;
	final public static int BYTEBUF_HANDLER = 4;

	private Map<Integer, BaseMessageHandler<?>> protocolHandlers = new ConcurrentHashMap<Integer, BaseMessageHandler<?>>();
	private Map<Integer, Class<?>> prototypes = new ConcurrentHashMap<Integer, Class<?>>();
	static private Map<Integer, Boolean> protocolPriviledge = new ConcurrentHashMap<Integer, Boolean>();
	@SuppressWarnings("rawtypes")
	static private Map<Class, Integer> prototype2ID = new ConcurrentHashMap<Class, Integer>();

	protected BaseMessageHandler<?> handlerForAll = null;
	private AbstractConnectionHandler connectionHandler;
	private OrderedThreadPoolExecutor beforeExecutor;
	private int msgHandlerType = PROTOBUF_HANDLER;

	public AbstractMessageInitializer()
	{
	}

	/**
	 *
	 * @throws Exception 
	 */
	final public void initialize() throws Exception
	{
		initMessages();
	}

	protected abstract void initMessages() throws Exception;

	public int getMsgHandlerType()
	{
		return msgHandlerType;
	}

	public void setMsgHandlerType(int msgHandlerType)
	{
		this.msgHandlerType = msgHandlerType;
	}

	public AbstractConnectionHandler getConnectionHandler()
	{
		return connectionHandler;
	}

	public void setConnectionHandler(AbstractConnectionHandler connectionHandler)
	{
		this.connectionHandler = connectionHandler;
	}

	public OrderedThreadPoolExecutor getBeforeExecutor()
	{
		return beforeExecutor;
	}

	public void setBeforeExecutor(OrderedThreadPoolExecutor beforeExecutor)
	{
		this.beforeExecutor = beforeExecutor;
	}

	public void setHandler(Class<?> messageClass, Class<? extends AbstractMessageService> serviceClass, BaseMessageHandler<?> messageHandler)
	{
		messageHandler.setServiceClass(serviceClass);

		if (messageClass == null || messageClass == void.class)
		{
			handlerForAll = messageHandler;
			return;
		}

		Integer protocolID = prototype2ID.get(messageClass);
		if (protocolID == null)
		{
			return;
		}
		protocolHandlers.put(protocolID, messageHandler);
	}

	public void setMessageIDClass(int protocolID, Class<?> messageClass, boolean priviledge)
	{
		prototypes.put(protocolID, messageClass);
		prototype2ID.put(messageClass, protocolID);
		protocolPriviledge.put(protocolID, priviledge);
	}

	public Class<?> getMessageClass(int protocolID)
	{
		return prototypes.get(protocolID);
	}
	
	public static boolean getProtocolPrivilege(int protocolID)
	{
		return protocolPriviledge.get(protocolID);
	}

	public int getProtocolID(Class<?> messageClass)
	{
		Integer protocolID = prototype2ID.get(messageClass);
		return protocolID == null ? 0 : protocolID;
	}
	
	public BaseMessageHandler<?> getMessageHandler(int protocolID)
	{
		BaseMessageHandler<?> handler = protocolHandlers.get(protocolID);
		if (handler != null)
		{
			return handler;
		}
		
		return handlerForAll;
	}
}
