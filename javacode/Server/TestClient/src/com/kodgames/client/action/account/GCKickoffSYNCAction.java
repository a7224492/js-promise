package com.kodgames.client.action.account;

import com.kodgames.client.service.account.RoleService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.game.GameProtoBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liufei on 2017/7/1.
 * 服务器将玩家踢下线: 有人用同样的账号登录
 */
@ActionAnnotation(actionClass = GCKickoffSYNCAction.class, messageClass = GameProtoBuf.GCKickoffSYNC.class, serviceClass = RoleService.class)
public class GCKickoffSYNCAction extends ProtobufMessageHandler<RoleService, GameProtoBuf.GCKickoffSYNC> {
    private static final Logger logger = LoggerFactory.getLogger(GCKickoffSYNCAction.class);

    @Override
    public void handleMessage(Connection connection, RoleService service, GameProtoBuf.GCKickoffSYNC message, int callback) {
        logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
        connection.close();
    }
}
