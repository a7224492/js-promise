package com.kodgames.client.action.account;

import com.kodgames.client.common.Role;
import com.kodgames.message.proto.game.GameProtoBuf;
import com.kodgames.message.protocol.PlatformProtocolsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.client.service.account.RoleService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.auth.AuthProtoBuf.ICAccountAuthRES;

@ActionAnnotation(actionClass = ICAccountAuthRESAction.class, messageClass = ICAccountAuthRES.class, serviceClass = RoleService.class)
public class ICAccountAuthRESAction extends ProtobufMessageHandler<RoleService, ICAccountAuthRES> {
    private static final Logger logger = LoggerFactory.getLogger(ICAccountAuthRESAction.class);

    @Override
    public void handleMessage(Connection connection, RoleService service, ICAccountAuthRES message, int callback) {
        logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
        logger.info("{} <---> {}", connection.getConnectionID(), message.getRoleId());

        if (message.getResult() != PlatformProtocolsConfig.IC_AUTH_SUCCESS) {
            logger.info("auth failed for reason {}, account id {}, role id {}", message.getResult(), message.getAccountId(), message.getRoleId());
            connection.close();
            return;
        }

        int roleId = message.getRoleId();
        int accountId = message.getAccountId();
        int gameServerId = message.getGameServerId();
        String unionId = message.getUnionid();
        connection.setRemotePeerID(roleId);

        Role role = service.getRole(connection.getConnectionID());
        if (role == null) {
            logger.error("can't find role with connection id {}", connection.getConnectionID());
            connection.close();
            return;
        }

        role.setRoleId(roleId, message.getUsername(), message.getNickname(), message.getHeadImageUrl(), message.getSex());
        service.updateRoleId(role, roleId, message.getUsername());
        role.initGameConnection(gameServerId);

        // 登录到GameServer
        logger.info("request login to game {}, role {} name {}", gameServerId, roleId, message.getUsername());
        GameProtoBuf.CGLoginREQ.Builder builder = GameProtoBuf.CGLoginREQ.newBuilder();
        builder.setRoleId(roleId);
        builder.setNickname(String.valueOf(roleId));
        builder.setHeadImageUrl(message.getHeadImageUrl());
        builder.setSex(message.getSex());
        builder.setAccountId(accountId);
        builder.setChannel(message.getChannel());
        builder.setUnionid(unionId);
        builder.setSignature("");
        role.sendToGame(builder.build());
    }
}
