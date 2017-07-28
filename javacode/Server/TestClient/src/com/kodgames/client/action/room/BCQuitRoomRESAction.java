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
import com.kodgames.message.proto.room.RoomProtoBuf.BCQuitRoomRES;

@ActionAnnotation(actionClass = BCQuitRoomRESAction.class, messageClass = BCQuitRoomRES.class, serviceClass = RoomService.class)
public class BCQuitRoomRESAction extends ProtobufMessageHandler<RoomService, BCQuitRoomRES> {
    private static final Logger logger = LoggerFactory.getLogger(BCQuitRoomRESAction.class);

    @Override
    public void handleMessage(Connection connection, RoomService service, BCQuitRoomRES message, int callback) {
        logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

        RoleService roleService = ServiceContainer.getInstance().getPublicService(RoleService.class);
        Role role = roleService.getRole(connection.getConnectionID());
        if (role == null) {
            logger.error("BCQuitRoomRESAction can't find role with connection id {}", connection.getConnectionID());
            connection.close();
            return;
        }

        if (message.getResult() != PlatformProtocolsConfig.BC_QUIT_ROOM_SUCCESS) {
            logger.error("role {} name {} quit room failed", role.getRoleId(), role.getUsername());
            String username = role.getUsername();
            connection.close();

            TaskService taskService = ServiceContainer.getInstance().getPublicService(TaskService.class);
            taskService.addReconnectTask(username);
            return;
        }

        // 退出房间 进入大厅
        logger.info("role {} name {} quit room and enter hall, add to free list. connection id {}", role.getRoleId(), role.getUsername(), connection.getConnectionID());
        int roomId = role.getRoomId();
        if (roomId != 0) {
            Room room = service.getRoom(roomId);
            if (room != null)
                room.quit(role);
            role.setRoom(0);
        }
        roleService.addRoleToLobbyList(role);
    }
}