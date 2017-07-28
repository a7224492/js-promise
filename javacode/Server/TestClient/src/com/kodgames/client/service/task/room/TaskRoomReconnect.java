package com.kodgames.client.service.task.room;

import com.kodgames.client.common.Role;
import com.kodgames.client.common.Room;
import com.kodgames.client.service.account.RoleService;
import com.kodgames.client.service.room.RoomService;
import com.kodgames.client.service.task.Task;
import com.kodgames.client.service.task.TaskService;
import com.kodgames.client.start.ServerConfigInitializer;
import com.kodgames.corgi.core.service.ServiceContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by liufei on 2017/7/8.
 * 房间内断线重连
 */
public class TaskRoomReconnect implements Task {
    private static Logger logger = LoggerFactory.getLogger(TaskRoomReconnect.class);

    @Override
    public void process(Role role, Room room) {
        logger.info("reconnect task for room {}", room.getId());

        RoleService roleService = ServiceContainer.getInstance().getPublicService(RoleService.class);
        TaskService taskService = ServiceContainer.getInstance().getPublicService(TaskService.class);
        List<Integer> roleIds = room.getRoles();

        Random r = new Random();
        Float rate = ServerConfigInitializer.getInstance().getTaskRoomReconnect();

        for (Integer id : roleIds) {
            role = roleService.getRoleByRoleId(id);
            if (role != null && r.nextFloat() < rate) {
                String username = role.getUsername();
                role.getConnection().close();

                taskService.addReconnectTask(username);
            }
        }
    }
}
