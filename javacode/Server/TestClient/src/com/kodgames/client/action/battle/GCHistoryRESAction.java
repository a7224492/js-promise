package com.kodgames.client.action.battle;

import com.kodgames.client.common.Role;
import com.kodgames.client.service.account.RoleService;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.message.protocol.PlatformProtocolsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.client.service.battle.BattleService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.game.GameProtoBuf.GCHistoryRES;

@ActionAnnotation(actionClass = GCHistoryRESAction.class, messageClass = GCHistoryRES.class, serviceClass = BattleService.class)
public class GCHistoryRESAction extends ProtobufMessageHandler<BattleService, GCHistoryRES> {
    private static final Logger logger = LoggerFactory.getLogger(GCHistoryRESAction.class);

    @Override
    public void handleMessage(Connection connection, BattleService service, GCHistoryRES message, int callback) {
        logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

        RoleService roleService = ServiceContainer.getInstance().getPublicService(RoleService.class);
        Role role = roleService.getRole(connection.getConnectionID());
        if (role == null) {
            logger.error("GCHistoryRESAction can't find role with connection id {}", connection.getConnectionID());
            connection.close();
            return;
        }

        if (message.getResult() != PlatformProtocolsConfig.GC_HISTORY_SUCCESS) {
            logger.error("get history list failed for reason {}, role id {}, connection id {}", message.getResult(), role.getRoleId(), connection.getConnectionID());
            roleService.addRoleToLobbyList(role);
            return;
        }
    }
}
