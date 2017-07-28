package com.kodgames.client.action.room;

import com.kodgames.client.common.Role;
import com.kodgames.client.common.Room;
import com.kodgames.client.service.account.RoleService;
import com.kodgames.client.service.task.TaskService;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.message.protocol.PlatformProtocolsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.client.service.room.RoomService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.room.RoomProtoBuf.BCEnterRoomRES;

@ActionAnnotation(actionClass = BCEnterRoomRESAction.class, messageClass = BCEnterRoomRES.class, serviceClass = RoomService.class)
public class BCEnterRoomRESAction extends ProtobufMessageHandler<RoomService, BCEnterRoomRES> {
    private static final Logger logger = LoggerFactory.getLogger(BCEnterRoomRESAction.class);

    @Override
    public void handleMessage(Connection connection, RoomService service, BCEnterRoomRES message, int callback) {
        logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
        RoleService roleService = ServiceContainer.getInstance().getPublicService(RoleService.class);
        Role role = roleService.getRole(connection.getConnectionID());
        if (role == null) {
            logger.error("BCEnterRoomRESAction can't find role with connection id {}", connection.getConnectionID());
            connection.close();
            return;
        }

        if (message.getResult() != PlatformProtocolsConfig.BC_ENTER_ROOM_SUCCESS) {
            logger.info("enter room failed for role {} name {} connection {}, add to lobby role list", role.getRoleId(), role.getUsername(), connection.getConnectionID());
            roleService.addRoleToLobbyList(role);

            if (message.getResult() == PlatformProtocolsConfig.BC_ENTER_ROOM_FAILED_ROOM_FULL) {
                // 房间已满
                int roomId = role.getRequestEnterRoomId();
                if (roomId > 0) {
                    service.setRoomFull(roomId);
                }
            }
            return;
        }

        int roomId = role.getRequestEnterRoomId();
        if (roomId == 0) {
            // 是否发了多次加入房间? 因为在setRoom的时候会清空requestEnterRoomId
            // 多次进房间都成功的话会出现这个问题
            logger.error("invalid request room id 0, role name {} send request enter room more than once?", role.getUsername());
            int rid = role.getRoomId();
            if (rid != 0) {
                logger.error("role {} name {} already in room {}, but enter another room success", role.getRoleId(), role.getUsername(), rid);
            }
            String username = role.getUsername();
            connection.close();

            TaskService taskService = ServiceContainer.getInstance().getPublicService(TaskService.class);
            taskService.addReconnectTask(username);
            return;
        }
        role.setRoom(roomId);

        Room room = service.getRoom(roomId);
        if (room == null) {
            int battleId = role.getBattleId();
            if (battleId == 0) {
                logger.warn("can't find battle for role {} name {}", role.getRoleId(), role.getUsername());
                String username = role.getUsername();
                connection.close();

                TaskService taskService = ServiceContainer.getInstance().getPublicService(TaskService.class);
                taskService.addReconnectTask(username);
                return;
            }

            room = service.createRoom(roomId, battleId);
            if (room == null) {
                logger.warn("create room {} with battle {} failed for role {} name {}", roomId, battleId, role.getRoleId(), role.getUsername());
                String username = role.getUsername();
                connection.close();

                TaskService taskService = ServiceContainer.getInstance().getPublicService(TaskService.class);
                taskService.addReconnectTask(username);
                return;
            }
        }

        room.setRoomData(message.getGameplaysList(), message.getIsHaveBeginFirstGame(), message.getMaxPlayerCount());
        if (!room.join(role)) {
            logger.error("role {} name {} enter room {}, but join to local room failed", role.getRoleId(), role.getUsername(), roomId);
            String username = role.getUsername();
            connection.close();

            TaskService taskService = ServiceContainer.getInstance().getPublicService(TaskService.class);
            taskService.addReconnectTask(username);
            return;
        }

        // 加入房间队列 (所有用户进房间都会走这里)
        roleService.addRoleToRoomList(role);
    }
}
