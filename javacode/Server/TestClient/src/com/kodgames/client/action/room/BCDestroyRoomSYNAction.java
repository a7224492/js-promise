package com.kodgames.client.action.room;

import com.kodgames.client.common.Role;
import com.kodgames.client.service.account.RoleService;
import com.kodgames.corgi.core.service.ServiceContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.client.service.room.RoomService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.room.RoomProtoBuf.BCDestroyRoomSYN;

@ActionAnnotation(actionClass = BCDestroyRoomSYNAction.class, messageClass = BCDestroyRoomSYN.class, serviceClass = RoomService.class)
public class BCDestroyRoomSYNAction extends ProtobufMessageHandler<RoomService, BCDestroyRoomSYN> {
    private static final Logger logger = LoggerFactory.getLogger(BCDestroyRoomSYNAction.class);

    @Override
    public void handleMessage(Connection connection, RoomService service, BCDestroyRoomSYN message, int callback) {
        logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

        RoleService roleService = ServiceContainer.getInstance().getPublicService(RoleService.class);
        Role role = roleService.getRole(connection.getConnectionID());
        if (role == null) {
            logger.error("BCDestroyRoomSYNAction can't find role with connection id {}", connection.getConnectionID());
            connection.close();
            return;
        }

        int roomId = role.getRoomId();
        if (roomId == 0) {
            logger.warn("role {} name {} not in room", role.getRoleId(), role.getUsername());
        } else {
            RoomService roomService = ServiceContainer.getInstance().getPublicService(RoomService.class);
            roomService.roomFinished(roomId);
            role.setRoom(0);
        }

        // 进入大厅
        roleService.addRoleToLobbyList(role);
    }
}
