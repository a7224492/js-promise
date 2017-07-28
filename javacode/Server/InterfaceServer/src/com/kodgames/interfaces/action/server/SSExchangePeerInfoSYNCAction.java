package com.kodgames.interfaces.action.server;

import com.kodgames.corgi.core.constant.ServerType;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.interfaces.service.server.ServerService;
import com.kodgames.message.proto.server.ServerProtoBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by marui on 2016/10/9.
 */
@ActionAnnotation(messageClass = ServerProtoBuf.SSExchangePeerInfoSYNC.class,
        actionClass = SSExchangePeerInfoSYNCAction.class,
        serviceClass = ServerService.class)
public class SSExchangePeerInfoSYNCAction extends ProtobufMessageHandler<ServerService, ServerProtoBuf.SSExchangePeerInfoSYNC> {
    final static Logger logger = LoggerFactory.getLogger(SSExchangePeerInfoSYNCAction.class);

    @Override
    public void handleMessage(Connection connection, ServerService service, ServerProtoBuf.SSExchangePeerInfoSYNC message, int callback)
    {
        logger.debug("Exchange peer Info, remote peer id:{} type:{}", message.getServerID(), ServerType.getType(message.getServerID()) );

        connection.setRemotePeerID(message.getServerID());
        ConnectionManager.getInstance().addToServerConnections(connection);
        ServiceContainer.getInstance().getPublicService(ServerService.class).serverConnected(message.getServerID(), connection);
    }
}
