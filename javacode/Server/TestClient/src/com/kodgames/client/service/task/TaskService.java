package com.kodgames.client.service.task;

import com.kodgames.client.common.Role;
import com.kodgames.client.common.Room;
import com.kodgames.client.service.account.RoleService;
import com.kodgames.client.service.room.RoomService;
import com.kodgames.client.service.task.client.TaskAddClient;
import com.kodgames.client.service.task.client.TaskReconnect;
import com.kodgames.client.service.task.lobby.TaskCreateRoom;
import com.kodgames.client.service.task.lobby.TaskJoinRoom;
import com.kodgames.client.service.task.room.*;
import com.kodgames.client.start.NetInitializer;
import com.kodgames.client.start.ServerConfigInitializer;
import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.corgi.core.service.ServiceContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by liufei on 2017/6/27.
 */
public class TaskService extends PublicService {
    private static Logger logger = LoggerFactory.getLogger(TaskService.class);
    private RoleService roleService = null;
    private RoomService roomService = null;

    private List<Task> loginTasks = Collections.synchronizedList(new ArrayList<>());        // 登录任务
    private List<Task> lobbyTasks = Collections.synchronizedList(new ArrayList<>());        // 大厅任务

    private long startTime = System.currentTimeMillis();

    public TaskService() {
        roleService = ServiceContainer.getInstance().getPublicService(RoleService.class);
        roomService = ServiceContainer.getInstance().getPublicService(RoomService.class);
    }

    // 定时检查并执行任务
    public void update() {
        // 登录任务 --------------------------------------------------------------------
        // 在连接完成后执行, 不在这里处理

        // 大厅任务 --------------------------------------------------------------------
        // 如果有空闲玩家, 取一个玩家出来执行任务
        // 如果找不到任务, 则把玩家重新放回空闲队列
        Role role = roleService.getRoleFromLobbyList();
        if (role != null) {
            logger.info("check task for role {} name {}", role.getRoleId(), role.getUsername());
            Task task = getLobbyTask();
            if (task == null)
                roleService.addRoleToLobbyList(role);
            else {
                try {
                    task.process(role, null);
                } catch (Exception e) {
                    logger.error("exception at lobby task: {}", e);
                }
            }
        }

        // 房间任务 --------------------------------------------------------------------
        Room room = roomService.getRoomForTask();
        if (room != null) {
            logger.info("check task for room {}", room.getId());
            Task task = getRoomTask(room);
            if (task != null) {
                try {
                    task.process(null, room);
                } catch (Exception e) {
                    logger.error("exception at room task: {}", e);
                }
            }
        }

        // 补充玩家
        long curTime = System.currentTimeMillis();
        if (curTime - startTime > 60000) {
            startTime = curTime;
            logger.info("current online role count {}, minimal role count {}", roleService.getRoleCount(), ServerConfigInitializer.getInstance().getMinClientCount());
            if (roleService.getRoleCount() < ServerConfigInitializer.getInstance().getMinClientCount()) {
                int count = ServerConfigInitializer.getInstance().getMinClientCount() - roleService.getRoleCount();
                logger.info("add new client count {}", count);

                // 先尝试将已断线的玩家重连
                int index = 10001;
                while (count > 0) {
                    String name = ServerConfigInitializer.getInstance().getUsernamePrefix() + String.valueOf(index);
                    if (roleService.getRoleByUsername(name) == null) {
                        logger.info("add reconnect task for user {}", name);
                        addReconnectTask(name);
                        count--;
                    }
                    index++;

                    if (index > roleService.getMaxConnId())
                        break;
                }

                // 如果人数不够再补充新玩家
                for (int i = 0; i < count; i++) {
                    logger.info("add new client task");
                    addNewClientTask();
                }
            }
        }
    }

    // 添加一个客户端重登录任务
    public void addReconnectTask(String username) {
        // 新建一个连接用于重登录
        addNewClientTask();

        Task task = new TaskReconnect(username);
        loginTasks.add(task);
    }

    // 添加一个新的客户端任务
    public void addNewClientTask() {
        ServerConfigInitializer config = ServerConfigInitializer.getInstance();
        SocketAddress address = new InetSocketAddress(config.getInterfaceHost(), config.getInterfacePort());
        NetInitializer.getInstance().connectToInterface(address);
    }

    // 添加进入房间的任务
    public void addJoinRoomTask(int battleId, int roomId) {
        Task task = new TaskJoinRoom(battleId, roomId);
        lobbyTasks.add(task);
    }

    // 获取登录任务
    public Task getLoginTask() {
        try {
            if (loginTasks.size() > 0)
                return loginTasks.remove(0);
        } catch (IndexOutOfBoundsException e) {
            // 有可能多个线程同时remove
            return null;
        }
        return null;
    }

    // 获取大厅任务
    private Task getLobbyTask() {
        try {
            if (lobbyTasks.size() > 0)
                return lobbyTasks.remove(0);
        } catch (IndexOutOfBoundsException e) {}

        Random r = new Random();
        float value = r.nextFloat();

        // 任务检查顺序:
        // 1. 进房间
        float joinRoomValue = ServerConfigInitializer.getInstance().getTaskJoinRoom();
        if (roomService.getFreeRoomCount() > 0 && joinRoomValue > 0 && value <= joinRoomValue) {
            return new TaskJoinRoom();
        }

        // 2. 创建房间
        logger.info("current room count {}, free room count {}", roomService.getRoomCount(), roomService.getFreeRoomCount());
        value = r.nextFloat();
        float createRoomValue = ServerConfigInitializer.getInstance().getTaskCreateRoom();
        if (roomService.getFreeRoomCount() < ServerConfigInitializer.getInstance().getFreeRoomCount()
            && createRoomValue > 0 && value <= createRoomValue) {
            return new TaskCreateRoom();
        }

        return null;
    }

    // 获取房间内任务 (在牌桌上的玩家执行的任务)
    private Task getRoomTask(Room room) {
        Random r = new Random();
        float value;

        // 是否超过解散时间
        float idleTime = room.getIdleTime();
        if (idleTime >= ServerConfigInitializer.getInstance().getVoteDestroyRoomTime()) {
            return new TaskVoteDestroyRoom(true);
        }

        // 人数是不是不够
        if (!room.isFull()) {
            int battle = room.getBattleId();
            addJoinRoomTask(battle, room.getId());
        }

        // 任务检查顺序:
        // 1. 解散房间
        value = r.nextFloat();
        float taskRoomDestroy = ServerConfigInitializer.getInstance().getTaskRoomVoteDestroy();
        if (taskRoomDestroy > 0 && value <= taskRoomDestroy)
            return new TaskVoteDestroyRoom(false);

        // 2. 断线重连
        value = r.nextFloat();
        float taskRoomReconnect = ServerConfigInitializer.getInstance().getTaskRoomReconnect();
        if (taskRoomReconnect > 0 && value < taskRoomReconnect) {
            return new TaskRoomReconnect();
        }

        // 3. 聊天
        value = r.nextFloat();
        float taskRate = ServerConfigInitializer.getInstance().getTaskRoomChatText();
        if (taskRate > 0 && value <= taskRate)
            return new TaskChatText();
        value = r.nextFloat();
        taskRate = ServerConfigInitializer.getInstance().getTaskRoomChatEmojo();
        if (taskRate > 0 && value <= taskRate)
            return new TaskChatEmojo();
        value = r.nextFloat();
        taskRate = ServerConfigInitializer.getInstance().getTaskRoomChatCustom();
        if (taskRate > 0 && value <= taskRate)
            return new TaskChatCustom();

        return null;
    }
}
