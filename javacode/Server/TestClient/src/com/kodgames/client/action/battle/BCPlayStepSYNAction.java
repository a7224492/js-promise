package com.kodgames.client.action.battle;

import com.kodgames.client.common.Role;
import com.kodgames.client.service.account.RoleService;
import com.kodgames.client.service.task.TaskService;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.message.proto.battle.BattleProtoBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.client.service.battle.BattleService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.battle.BattleProtoBuf.BCPlayStepSYN;

import java.util.ArrayList;
import java.util.List;

@ActionAnnotation(actionClass = BCPlayStepSYNAction.class, messageClass = BCPlayStepSYN.class, serviceClass = BattleService.class)
public class BCPlayStepSYNAction extends ProtobufMessageHandler<BattleService, BCPlayStepSYN> {
    private static final Logger logger = LoggerFactory.getLogger(BCPlayStepSYNAction.class);

    @Override
    public void handleMessage(Connection connection, BattleService service, BCPlayStepSYN message, int callback) {
        logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

        TaskService taskService = ServiceContainer.getInstance().getPublicService(TaskService.class);

        RoleService roleService = ServiceContainer.getInstance().getPublicService(RoleService.class);
        Role role = roleService.getRole(connection.getConnectionID());
        if (role == null) {
            logger.error("BCPlayStepSYNAction can't find role with connection id {}", connection.getConnectionID());
            connection.close();

            // 新加一个连接任务
            taskService.addNewClientTask();
            return;
        }

        for (BattleProtoBuf.PlayStepPROTO step : message.getStepsList()) {
            // 只处理发送给自己的step命令
            if (step.getRoleId() == role.getRoleId()) {
                List<Integer> cards = new ArrayList<>();
                for (byte b : step.getCards()) {
                    Integer i = Integer.valueOf(b);
                    cards.add(i);
                }
                role.step(step.getPointInGame(), step.getPlayType(), cards, step.getSourceRoleId());
            }
        }
    }
}
