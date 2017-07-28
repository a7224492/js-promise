package com.kodgames.client.service.task.room;

import com.kodgames.client.common.Role;
import com.kodgames.client.common.Room;
import com.kodgames.client.service.account.RoleService;
import com.kodgames.client.service.room.RoomService;
import com.kodgames.client.service.task.Task;
import com.kodgames.client.service.task.lobby.TaskCreateRoom;
import com.kodgames.client.start.ServerConfigInitializer;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.message.proto.room.RoomProtoBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by liufei on 2017/7/4.
 * 投票解散房间
 */
public class TaskVoteDestroyRoom implements Task {
    private static Logger logger = LoggerFactory.getLogger(TaskVoteDestroyRoom.class);
    private boolean force = false;

    // force: 是否强制解散房间 (所有人都投票同意)
    public TaskVoteDestroyRoom(boolean force) {
        this.force = force;
    }

    @Override
    public void process(Role role, Room room) {
        logger.info("vote destroy room task for room {}", room.getId());
        room.touch();
        RoleService roleService = ServiceContainer.getInstance().getPublicService(RoleService.class);
        RoomService roomService = ServiceContainer.getInstance().getPublicService(RoomService.class);
        List<Integer> roleIds = room.getRoles();
        List<Role> roles = new ArrayList<>();
        for (Integer id : roleIds) {
            Role r = roleService.getRoleByRoleId(id);
            if (r != null)
                roles.add(r);
        }

        if (roles.isEmpty()) {
            logger.error("room {} is empty", room.getId());
            roomService.roomFinished(room.getId());
            return;
        }

        // 第一个玩家发起请求
        startVoteDestroy(roles.get(0));

        // 后面的玩家同意或拒绝
        Random r = new Random();
        for (int i = 1; i < roles.size(); i++) {
            boolean agree = false;
            if (force)
                agree = true;
            else {
                if (r.nextFloat() < ServerConfigInitializer.getInstance().getVoteDestroyRoomRate())
                    agree = true;
            }

            if (agree)
                agreeVoteDestroy(roles.get(i));
            else
                disagreeVoteDestroy(roles.get(i));
        }
    }

    // 开始投票
    private void startVoteDestroy(Role role) {
        RoomProtoBuf.CBStartVoteDestroyREQ.Builder builder = RoomProtoBuf.CBStartVoteDestroyREQ.newBuilder();
        role.sendToBattle(builder.build());
    }

    // 同意
    private void agreeVoteDestroy(Role role) {
        RoomProtoBuf.CBVoteDestroyREQ.Builder builder = RoomProtoBuf.CBVoteDestroyREQ.newBuilder();
        builder.setType(1);
        role.sendToBattle(builder.build());
    }

    // 不同意
    private void disagreeVoteDestroy(Role role) {
        RoomProtoBuf.CBVoteDestroyREQ.Builder builder = RoomProtoBuf.CBVoteDestroyREQ.newBuilder();
        builder.setType(2);
        role.sendToBattle(builder.build());
    }
}
