package com.kodgames.client.service.task.lobby;

import com.kodgames.client.common.Role;
import com.kodgames.client.common.Room;
import com.kodgames.client.service.task.Task;
import com.kodgames.client.start.GameplaysInitializer;
import com.kodgames.client.start.ServerConfigInitializer;
import com.kodgames.message.proto.game.GameProtoBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liufei on 2017/6/27.
 * 创建房间
 */
public class TaskCreateRoom implements Task {
    private static Logger logger = LoggerFactory.getLogger(TaskCreateRoom.class);

    @Override
    public void process(Role role, Room room) {
        logger.info("create room task for role {} connection id {} name {}", role.getRoleId(), role.getConnectionId(), role.getUsername());

        GameProtoBuf.CGCreateRoomREQ.Builder builder = GameProtoBuf.CGCreateRoomREQ.newBuilder();
        builder.setRoomType(0);
        builder.setFreeActivityId(-1);
        builder.setVoice(false);
        builder.setRoundCount(1);           // 8局或16局
        builder.setPayType(ServerConfigInitializer.getInstance().getPayType());
        builder.addAllGameplays(GameplaysInitializer.getInstance().getGameplay(ServerConfigInitializer.getInstance().getArea()));

        role.sendToGame(builder.build());
    }
}
