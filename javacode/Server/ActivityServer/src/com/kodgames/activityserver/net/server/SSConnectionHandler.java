package com.kodgames.activityserver.net.server;

import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.kodgames.activityserver.service.activity.ActivityService;
import com.kodgames.activityserver.service.server.ServerService;
import com.kodgames.corgi.core.constant.ServerType;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.message.proto.game.GameProtoBuf;
import com.kodgames.message.proto.server.ServerProtoBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.handler.connection.AbstractConnectionHandler;
import com.kodgames.corgi.core.service.ServiceContainer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.List;

public class SSConnectionHandler extends AbstractConnectionHandler {
    static private Logger logger = LoggerFactory.getLogger(SSConnectionHandler.class);

    @Override
    public void handleConnectionActive(Connection connection) {
        logger.info("Connection active. connection id : {}", connection.getConnectionID());

        connection.setConnectionType(Connection.CONNECTION_TYPE_SERVER);
        if (ConnectionManager.getInstance().getLocalPeerID() != 0) {
            ServerProtoBuf.SSExchangePeerInfoSYNC.Builder builder = ServerProtoBuf.SSExchangePeerInfoSYNC.newBuilder();
            builder.setServerID(ConnectionManager.getInstance().getLocalPeerID());
            connection.write(0, builder.build());
        }

        // 判断是否是Manager的连接建立
        if (connection.getRemoteNode().getAddress().equals(ConnectionManager.getInstance().getManagerServerAddress())) {
            // 向Manager请求配置信息
            ServerService service = ServiceContainer.getInstance().getPublicService(ServerService.class);
            service.onManagerConnect(connection);
        }
    }

    @Override
    public void handleConnectionInactive(Connection connection) {
        logger.info("Connection inActive. connection id : {}", connection.getConnectionID());

        if (connection.getConnectionType() == Connection.CONNECTION_TYPE_SERVER) {
            int serverID = connection.getRemotePeerID();
            if (serverID == 0) {
                return;
            }

            switch (ServerType.getType(serverID)) {
                case ServerType.MANAGER_SERVER:
                    ServiceContainer.getInstance().getPublicService(ServerService.class).onManagerDisconnect(connection);
                    break;

                case ServerType.GAME_SERVER:
                    ServiceContainer.getInstance().getPublicService(ServerService.class).onGameDisconnect();
                    break;

                default:
                    logger.error("invalid server connection to serverid {} with type {}", serverID, ServerType.getType(serverID));
                    break;
            }
        }
    }
}
