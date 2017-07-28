package com.kodgames.client.net;

import com.kodgames.client.common.Role;
import com.kodgames.client.service.account.RoleService;
import com.kodgames.client.service.task.Task;
import com.kodgames.client.service.task.TaskService;
import com.kodgames.client.start.ServerConfigInitializer;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.message.proto.auth.AuthProtoBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.handler.connection.AbstractConnectionHandler;

public class InterfaceConnectionHandler extends AbstractConnectionHandler {
    private static final Logger logger = LoggerFactory.getLogger(InterfaceConnectionHandler.class);

    public InterfaceConnectionHandler() {
    }

    @Override
    public void handleConnectionActive(Connection connection) {
        connection.setConnectionType(Connection.CONNECTION_TYPE_CLIENT);
        logger.info("connection active, connection id {} type {}", connection.getConnectionID(), connection.getConnectionType());

        // 保存Role
        RoleService roleService = ServiceContainer.getInstance().getPublicService(RoleService.class);
        Role role = new Role(connection);
        roleService.addRole(role);

        // 如果有重登录任务则执行重登录
        // 否则作为新用户发起登录请求
        TaskService taskService = ServiceContainer.getInstance().getPublicService(TaskService.class);
        Task task = taskService.getLoginTask();
        if (task != null) {
            try {
                task.process(role, null);
            } catch (Exception e) {
                logger.error("exception at reconnect task: {}", e.getMessage());
            }
        } else {
            AuthProtoBuf.CIAccountAuthREQ.Builder builder = AuthProtoBuf.CIAccountAuthREQ.newBuilder();
            builder.setChannel("test");
            builder.setUsername(ServerConfigInitializer.getInstance().getUsernamePrefix() + String.valueOf(connection.getConnectionID()));
            builder.setRefreshToken(" ");
            builder.setCode(" ");
            builder.setProVersion("14097");

            connection.write(RoleService.callbackSeed.getAndIncrement(), builder.build());
        }
    }

    @Override
    public void handleConnectionInactive(Connection connection) {
        logger.info("connection inactive, connection id {} type {}", connection.getConnectionID(), connection.getConnectionType());
        RoleService roleService = ServiceContainer.getInstance().getPublicService(RoleService.class);
        roleService.removeRole(connection.getConnectionID());
    }
}
