package com.kodgames.client.action.room;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.client.service.room.RoomService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.room.RoomProtoBuf.BCRoomPlayerInfoSYN;

@ActionAnnotation(actionClass = BCRoomPlayerInfoSYNAction.class, messageClass = BCRoomPlayerInfoSYN.class, serviceClass = RoomService.class)
public class BCRoomPlayerInfoSYNAction extends ProtobufMessageHandler<RoomService, BCRoomPlayerInfoSYN> {
    private static final Logger logger = LoggerFactory.getLogger(BCRoomPlayerInfoSYNAction.class);

    @Override
    public void handleMessage(Connection connection, RoomService service, BCRoomPlayerInfoSYN message, int callback) {
        logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
    }
}
