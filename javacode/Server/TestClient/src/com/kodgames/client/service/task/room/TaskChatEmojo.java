package com.kodgames.client.service.task.room;

import com.kodgames.client.common.Role;
import com.kodgames.client.common.Room;
import com.kodgames.client.service.account.RoleService;
import com.kodgames.client.service.task.Task;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.message.proto.chat.ChatProtoBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * Created by liufei on 2017/7/4.
 */
public class TaskChatEmojo implements Task {
    private static Logger logger = LoggerFactory.getLogger(TaskChatEmojo.class);

    @Override
    public void process(Role role, Room room) {
        logger.info("emojo chat task for room {}", room.getId());

        RoleService roleService = ServiceContainer.getInstance().getPublicService(RoleService.class);
        List<Integer> roleIds = room.getRoles();
        Collections.shuffle(roleIds);

        for (Integer id : roleIds) {
            Role r = roleService.getRoleByRoleId(id);
            if (r != null) {
                ChatProtoBuf.CBChatREQ.Builder builder = ChatProtoBuf.CBChatREQ.newBuilder();
                builder.setType(2);
                builder.setContent("");
                builder.setCode(0);
                r.sendToBattle(builder.build());

                return;
            }
        }
    }
}
