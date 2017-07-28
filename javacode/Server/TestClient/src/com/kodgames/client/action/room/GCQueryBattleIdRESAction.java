package com.kodgames.client.action.room;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.client.service.account.RoleService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.game.GameProtoBuf.GCQueryBattleIdRES;

@ActionAnnotation(actionClass = GCCreateRoomRESAction.class, messageClass = GCQueryBattleIdRES.class, serviceClass = RoleService.class)
public class GCQueryBattleIdRESAction extends ProtobufMessageHandler<RoleService, GCQueryBattleIdRES> {
    private static final Logger logger = LoggerFactory.getLogger(GCQueryBattleIdRESAction.class);

    @Override
    public void handleMessage(Connection connection, RoleService service, GCQueryBattleIdRES message, int callback) {
        logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
    }
}