package com.kodgames.client.action.battle;

import com.kodgames.client.common.Role;
import com.kodgames.client.service.account.RoleService;
import com.kodgames.client.service.task.TaskService;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.message.protocol.PlatformProtocolsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.client.service.battle.BattleService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.battle.BattleProtoBuf.BCPlayCardRES;

@ActionAnnotation(actionClass = BCPlayCardRESAction.class, messageClass = BCPlayCardRES.class, serviceClass = BattleService.class)
public class BCPlayCardRESAction extends ProtobufMessageHandler<BattleService, BCPlayCardRES> {
    private static final Logger logger = LoggerFactory.getLogger(BCPlayCardRESAction.class);

    @Override
    public void handleMessage(Connection connection, BattleService service, BCPlayCardRES message, int callback) {
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

        TaskService taskService = ServiceContainer.getInstance().getPublicService(TaskService.class);

        RoleService roleService = ServiceContainer.getInstance().getPublicService(RoleService.class);
        Role role = roleService.getRole(connection.getConnectionID());
        if (role == null) {
            logger.error("BCPlayCardRESAction can't find role with connection id {}", connection.getConnectionID());
            connection.close();

            // 新加一个连接任务
            taskService.addNewClientTask();
            return;
        }

        if (message.getResult() != PlatformProtocolsConfig.BC_PLAYCARD_SUCCESS) {
            // 有可能是多个操作同时出现
            logger.info("failed to play card, result {}, roleId {}, connection id {}", message.getResult(), role.getRoleId(), connection.getConnectionID());
        }
    }
}
