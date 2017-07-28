package com.kodgames.client.action.account;

import com.kodgames.client.service.account.RoleService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.game.GameProtoBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liufei on 2017/7/7.
 * 防沉迷通知
 */
@ActionAnnotation(actionClass = GCKickoffSYNCAction.class, messageClass = GameProtoBuf.GCAntiAddictionSYN.class, serviceClass = RoleService.class)
public class GCAntiAddictionSYNAction extends ProtobufMessageHandler<RoleService, GameProtoBuf.GCAntiAddictionSYN> {
    private static final Logger logger = LoggerFactory.getLogger(GCAntiAddictionSYNAction.class);

    @Override
    public void handleMessage(Connection connection, RoleService service, GameProtoBuf.GCAntiAddictionSYN message, int callback) {
        logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
    }
}
