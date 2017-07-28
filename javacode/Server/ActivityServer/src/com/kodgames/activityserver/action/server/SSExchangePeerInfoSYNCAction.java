package com.kodgames.activityserver.action.server;

import com.kodgames.activityserver.service.activity.ActivityService;
import com.kodgames.activityserver.service.server.ServerService;
import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.game.GameProtoBuf;
import jdk.nashorn.internal.objects.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.constant.ServerType;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.message.proto.server.ServerProtoBuf;

/**
 * Created by marui on 2016/10/9.
 */
@ActionAnnotation(messageClass = ServerProtoBuf.SSExchangePeerInfoSYNC.class, actionClass = SSExchangePeerInfoSYNCAction.class, serviceClass = ServerService.class)
public class SSExchangePeerInfoSYNCAction
        extends ProtobufMessageHandler<ServerService, ServerProtoBuf.SSExchangePeerInfoSYNC>
{
    final static Logger logger = LoggerFactory.getLogger(SSExchangePeerInfoSYNCAction.class);

    @Override
    public void handleMessage(Connection connection, ServerService service,
                              ServerProtoBuf.SSExchangePeerInfoSYNC message, int callback) {
        logger.debug("Exchange peer Info, remote peer id:{}", message.getServerID());
        connection.setRemotePeerID(message.getServerID());
        ConnectionManager.getInstance().addToServerConnections(connection);

        if (ServerType.getType(message.getServerID()) == ServerType.GAME_SERVER)
        {
            ServiceContainer.getInstance().getPublicService(ServerService.class).onGameConnect(connection);
        } else if (ServerType.getType(message.getServerID()) == ServerType.ACTIVITY_SERVER)
        {
            // 推送活动服务器的内存数据给另一个活动服务器
            // TODO 新的活动服务器不需要向老的活动服务器发送数据
            ActivityService activityService = ServiceContainer.getInstance().getPublicService(ActivityService.class);
            GameProtoBuf.ActivityMemoryDataSYN.Builder builder = GameProtoBuf.ActivityMemoryDataSYN.newBuilder();
            builder.addData(activityService.returnRankActivityMemoryPROTO().toByteString());
            connection.write(GlobalConstants.DEFAULT_CALLBACK, builder.build());
        }
    }
}
