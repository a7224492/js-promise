package com.kodgames.client.service.task.lobby;

import com.kodgames.client.common.Role;
import com.kodgames.client.common.Room;
import com.kodgames.client.service.account.RoleService;
import com.kodgames.client.service.room.RoomService;
import com.kodgames.client.service.task.Task;
import com.kodgames.corgi.core.service.ServiceContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liufei on 2017/6/27.
 * 加入房间
 */
public class TaskJoinRoom implements Task {
    private Logger logger = LoggerFactory.getLogger(TaskJoinRoom.class);
    private int battleId = -1;
    private int roomId = -1;

    public TaskJoinRoom() {
        battleId = -1;
        roomId = -1;
    }

    public TaskJoinRoom(int battle, int room) {
        battleId = battle;
        roomId = room;
    }

    @Override
    public void process(Role role, Room room) {
        logger.info("join room task for role {} connection id {}", role.getRoleId(), role.getConnectionId());
        RoomService service = ServiceContainer.getInstance().getPublicService(RoomService.class);
        if (battleId == -1 && roomId == -1) {
            room = service.getFreeRoom();
            if (room == null) {
                logger.info("doesn't have free room, join room task canceled");

                // 将玩家加入大厅队列
                RoleService roleService = ServiceContainer.getInstance().getPublicService(RoleService.class);
                roleService.addRoleToLobbyList(role);
                return;
            }
            roomId = room.getId();
            battleId = room.getBattleId();
        }

        logger.info("free room id {} in battle {}", roomId, battleId);
        role.resetBattleConnection(battleId);
        role.requestEnterRoom(roomId);
    }
}
