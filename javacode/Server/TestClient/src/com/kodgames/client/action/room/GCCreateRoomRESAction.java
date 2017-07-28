package com.kodgames.client.action.room;

import com.kodgames.client.common.Role;
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
import com.kodgames.message.proto.game.GameProtoBuf.GCCreateRoomRES;

@ActionAnnotation(actionClass = GCCreateRoomRESAction.class, messageClass = GCCreateRoomRES.class, serviceClass = RoomService.class)
public class GCCreateRoomRESAction extends ProtobufMessageHandler<RoomService, GCCreateRoomRES> {
    private static final Logger logger = LoggerFactory.getLogger(GCCreateRoomRESAction.class);

    @Override
    public void handleMessage(Connection connection, RoomService service, GCCreateRoomRES message, int callback) {
        logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

        RoleService roleService = ServiceContainer.getInstance().getPublicService(RoleService.class);
        Role role = roleService.getRole(connection.getConnectionID());
        if (role == null) {
            logger.error("can't get role for connection id {}", connection.getConnectionID());
            connection.close();
            return;
        }

        if (message.getResult() != PlatformProtocolsConfig.GC_CREATE_ROOM_SUCCESS) {
            logger.info("create room failed for reason {}, connection id {} role {} name {}", message.getResult(), connection.getConnectionID(), role.getRoleId(), role.getUsername());
            if (message.getResult() == PlatformProtocolsConfig.GC_CREATE_ROOM_FAILED_ALREADY_IN_ROOM) {
                // 已在房间中, 断线重连
                logger.info("role {} name {} already in room, close connection and reconnect", role.getRoleId(), role.getUsername());
                String username = role.getUsername();
                connection.close();

                TaskService taskService = ServiceContainer.getInstance().getPublicService(TaskService.class);
                taskService.addReconnectTask(username);
                return;
            }

            // 重新加入空闲玩家队列
            roleService.addRoleToLobbyList(role);
            return;
        }

        // 请求加入房间
        logger.info("role {} name {} request enter room {} in battle {}", role.getRoleId(), role.getUsername(), message.getRoomId(), message.getBattleId());
        role.resetBattleConnection(message.getBattleId());
        role.requestEnterRoom(message.getRoomId());
    }
}
