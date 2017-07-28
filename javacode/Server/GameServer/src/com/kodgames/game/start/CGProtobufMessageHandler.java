package com.kodgames.game.start;

import com.google.protobuf.GeneratedMessage;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.corgi.core.service.AbstractMessageService;

/**
 * Created by marui on 2016/10/11.
 */
public abstract class CGProtobufMessageHandler<S extends AbstractMessageService, T extends GeneratedMessage> extends ProtobufMessageHandler<S,T> {
    @Override
    public Object getMessageKey(Connection connection, T message)
    {
        if ( connection.getRemotePeerID() != 0 )
        {
            return connection.getRemotePeerID();
        }
        else
        {
            return connection.getConnectionID();
        }
    }
}
