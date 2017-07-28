package com.kodgames.game.action.room;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.game.service.room.RoomService;
import com.kodgames.game.util.KodBiLogHelper;
import com.kodgames.message.proto.game.GameProtoBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Liufei on 2017/5/12.
 * 牌局统计, 由玩法服务器提供统计数据
 */
@ActionAnnotation(messageClass = GameProtoBuf.BGRoomDestroyBiSYN.class, actionClass = BGRoomDestroyBILogAction.class, serviceClass = RoomService.class)
public class BGRoomDestroyBILogAction extends ProtobufMessageHandler<RoomService, GameProtoBuf.BGRoomDestroyBiSYN> {
    private static Logger logger = LoggerFactory.getLogger(BGRoomDestroyBILogAction.class);

    @Override
    public void handleMessage(Connection connection, RoomService service, GameProtoBuf.BGRoomDestroyBiSYN message, int callback) {
        logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

        KodBiLogHelper.roomDestroyWf(message);
    }

    @Override
    public Object getMessageKey(Connection connection, GameProtoBuf.BGRoomDestroyBiSYN message) {
        return message.getRoomId() == 0 ? connection.getConnectionID() : message.getRoomId();
    }
}
