package com.kodgames.client.action.battle;

import com.kodgames.client.common.Role;
import com.kodgames.client.common.Room;
import com.kodgames.client.service.account.RoleService;
import com.kodgames.client.service.task.TaskService;
import com.kodgames.client.start.ServerConfigInitializer;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.message.proto.game.GameProtoBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.client.service.room.RoomService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.battle.BattleProtoBuf.BCFinalMatchResultSYN;

import java.util.List;
import java.util.Random;

@ActionAnnotation(actionClass = BCFinalMatchResultSYNAction.class, messageClass = BCFinalMatchResultSYN.class, serviceClass = RoomService.class)
public class BCFinalMatchResultSYNAction extends ProtobufMessageHandler<RoomService, BCFinalMatchResultSYN> {
    private static final Logger logger = LoggerFactory.getLogger(BCFinalMatchResultSYNAction.class);

    @Override
    public void handleMessage(Connection connection, RoomService service, BCFinalMatchResultSYN message, int callback) {
        logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

        TaskService taskService = ServiceContainer.getInstance().getPublicService(TaskService.class);

        RoleService roleService = ServiceContainer.getInstance().getPublicService(RoleService.class);
        Role role = roleService.getRole(connection.getConnectionID());
        if (role == null) {
            logger.error("can't find role with connection id {}", connection.getConnectionID());
            connection.close();
            return;
        }

        // 设置房间已结束 (会调用多次)
        int roomId = role.getRoomId();
        service.roomFinished(roomId);

        Random r = new Random();

        // 添加尝试进入已结束的房间的任务
        if (r.nextFloat() < ServerConfigInitializer.getInstance().getReenterFinishedRoomRate()) {
            int battleId = role.getBattleId();
            taskService.addJoinRoomTask(battleId, roomId);
        }

        // 加入大厅队列
        roleService.addRoleToLobbyList(role);
    }
}
