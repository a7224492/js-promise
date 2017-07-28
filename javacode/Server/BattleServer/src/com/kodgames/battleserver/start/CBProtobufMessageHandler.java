package com.kodgames.battleserver.start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.GeneratedMessage;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.corgi.core.service.AbstractMessageService;

/**
 * Created by marui on 2016/10/11.
 */
public abstract class CBProtobufMessageHandler <S extends AbstractMessageService, T extends GeneratedMessage> extends ProtobufMessageHandler<S,T>
{
	final static Logger logger = LoggerFactory.getLogger(CBProtobufMessageHandler.class);
	
    @Override
    public Object getMessageKey(Connection connection, T message)
    {
        if ( connection.getRoomID() != 0 )
        {
            return connection.getRoomID();
        }
        else if (connection.getRemotePeerID() != 0)
        {
        	logger.warn("{} : message key miss roomId -> remotePeerId={}, message={}", getClass(), connection.getRemotePeerID(), message);
            return connection.getRemotePeerID();
        }
        else
        {
        	logger.warn("{} : message key miss roomId -> connectionId={}, message={}", getClass(), connection.getConnectionID(), message);
            return connection.getConnectionID();
        }
    }
}
