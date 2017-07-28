package com.kodgames.corgi.core.net.handler;

import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.util.PackageScaner;
import com.kodgames.message.proto.server.ServerProtoBuf.ServerExceptionSYNC;

public class MessageExceptionCatchHandler extends HashSet<IMessageExceptionCatchHandler> implements IMessageExceptionCatchHandler
{
	private static final long serialVersionUID = -8804586383858004697L;

	private Logger logger = LoggerFactory.getLogger(MessageExceptionCatchHandler.class);
	private static MessageExceptionCatchHandler instance = new MessageExceptionCatchHandler();
	private MessageExceptionCatchHandler()
	{
	}

	public static MessageExceptionCatchHandler getInstance()
	{
		return instance;
	}
	
	@SuppressWarnings("rawtypes")
	public void init(String handlerPackage) throws ReflectiveOperationException
	{
        try 
        {
			List<Class> list = PackageScaner.getClasses(handlerPackage, ".class", true);
			for (Class cls : list) 
			{
				if(cls!=null && cls!=IMessageExceptionCatchHandler.class && cls!=MessageExceptionCatchHandler.class
						&& IMessageExceptionCatchHandler.class.isAssignableFrom(cls))
				{
					add((IMessageExceptionCatchHandler) cls.newInstance());
				}
			}
		} catch (ReflectiveOperationException e) {
			logger.error("init failed. ");
			throw e;
		}
	}

	@Override
	public void handleMessage(Connection connection, int protocolID,
			int callback, Object buffer) 
	{
		for (IMessageExceptionCatchHandler handler : this) 
		{
			handler.handleMessage(connection, protocolID, callback, buffer);
		}
		
		//缺省情况下，向对方发送协议表明处理异常
		//如果是应答消息，不发送异常
		if (connection.getConnectionType() != Connection.CONNECTION_TYPE_CLIENT)
		{
			return;
		}
		ServerExceptionSYNC.Builder builder = ServerExceptionSYNC.newBuilder();
		builder.setProtocolId(protocolID);
		connection.write(callback, builder.build());
	}
}
