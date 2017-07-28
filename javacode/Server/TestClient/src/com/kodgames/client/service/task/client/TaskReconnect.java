package com.kodgames.client.service.task.client;

import com.kodgames.client.common.Role;
import com.kodgames.client.common.Room;
import com.kodgames.client.service.account.RoleService;
import com.kodgames.client.service.task.Task;
import com.kodgames.client.service.task.lobby.TaskJoinRoom;
import com.kodgames.client.start.ServerConfigInitializer;
import com.kodgames.message.proto.auth.AuthProtoBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liufei on 2017/7/1.
 * 重连一个客户端
 */
public class TaskReconnect implements Task {
    private Logger logger = LoggerFactory.getLogger(TaskJoinRoom.class);
    private String username;

    public TaskReconnect(String username) {
        this.username = username;
    }

    @Override
    public void process(Role role, Room room) {
        logger.info("TaskReconnect username {}, connection id {}", username, role.getConnectionId());

        AuthProtoBuf.CIAccountAuthREQ.Builder builder = AuthProtoBuf.CIAccountAuthREQ.newBuilder();
        builder.setChannel("test");
        builder.setUsername(username);
        builder.setRefreshToken(" ");
        builder.setCode(" ");
        builder.setProVersion("14097");

        role.getConnection().write(RoleService.callbackSeed.getAndIncrement(), builder.build());
    }
}
