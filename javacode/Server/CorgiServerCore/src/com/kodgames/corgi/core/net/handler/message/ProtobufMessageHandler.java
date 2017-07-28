package com.kodgames.corgi.core.net.handler.message;

import com.google.protobuf.GeneratedMessage;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.service.AbstractMessageService;
import com.kodgames.corgi.core.service.PlayerService;
import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.corgi.core.service.ServiceContainer;

public abstract class ProtobufMessageHandler<S extends AbstractMessageService, T extends GeneratedMessage> extends AbstractProtobufMessageHandler<T>
{
	@SuppressWarnings("unchecked")
	@Override
    public void handleMessage(Connection connection, int protocolID, int callback, T message)
    {
		assert(serviceClass != null);
		S service = null;
		if (PlayerService.class.isAssignableFrom(serviceClass) )
		{
			service = (S)ServiceContainer.getInstance().getPlayerService(connection.getRemotePeerID(), serviceClass);
		}
		else if (PublicService.class.isAssignableFrom(serviceClass))
		{
			service =(S)ServiceContainer.getInstance().getPublicService(serviceClass);		
		}

		handleMessage(connection, service, message, callback);
    }
	
	abstract public void handleMessage(Connection connection, S service, T message, int callback);

	@Override
    public Object getMessageKey(Connection connection, T message)
    {
	    return connection.getConnectionID();
    }
}
