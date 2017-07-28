package com.kodgames.client.action.account;

import com.kodgames.client.common.Role;
import com.kodgames.message.protocol.PlatformProtocolsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.client.service.account.RoleService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.message.proto.game.GameProtoBuf.GCLoginRES;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;

/**
 * 登录GameServer成功或失败
 */
@ActionAnnotation(actionClass = GCLoginRESAction.class, messageClass = GCLoginRES.class, serviceClass = RoleService.class)
public class GCLoginRESAction extends ProtobufMessageHandler<RoleService, GCLoginRES> {
    private static final Logger logger = LoggerFactory.getLogger(GCLoginRESAction.class);

    @Override
    public void handleMessage(Connection connection, RoleService service, GCLoginRES message, int callback) {
        logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

        Role role = service.getRole(connection.getConnectionID());
        if (role == null) {
            logger.error("GCLoginRESAction can't find role with connection id {}", connection.getConnectionID());
            connection.close();
            return;
        }

        if (message.getResult() != PlatformProtocolsConfig.GC_LOGIN_SUCCESS) {
            logger.warn("login game failed for reason {}", message.getResult());
            connection.close();
            return;
        }

        // 登录成功, 进房间或者大厅
        if (message.getRoomId() > 0 && message.getBattleId() > 0) {
            logger.info("role {} name {} already in room {} at battle {}, request enter room", role.getRoleId(), role.getUsername(), message.getRoomId(), message.getBattleId());
            role.resetBattleConnection(message.getBattleId());

            role.requestEnterRoom(message.getRoomId());
        } else {
            logger.info("role {} name {} enter lobby, add to free role list", role.getRoleId(), role.getUsername());
            service.addRoleToLobbyList(role);
        }
    }
}
