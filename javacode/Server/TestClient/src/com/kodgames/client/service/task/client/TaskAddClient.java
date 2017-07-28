package com.kodgames.client.service.task.client;

import com.kodgames.client.common.Role;
import com.kodgames.client.common.Room;
import com.kodgames.client.service.task.Task;
import com.kodgames.client.service.task.lobby.TaskJoinRoom;
import com.kodgames.client.start.NetInitializer;
import com.kodgames.client.start.ServerConfigInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Created by liufei on 2017/7/1.
 * 新加一个客户端
 */
public class TaskAddClient implements Task {
    private Logger logger = LoggerFactory.getLogger(TaskJoinRoom.class);

    @Override
    public void process(Role role, Room room) {
        logger.info("TaskAddClient connect to interface");

        ServerConfigInitializer config = ServerConfigInitializer.getInstance();
        SocketAddress address = new InetSocketAddress(config.getInterfaceHost(), config.getInterfacePort());
        NetInitializer.getInstance().connectToInterface(address);
    }
}
