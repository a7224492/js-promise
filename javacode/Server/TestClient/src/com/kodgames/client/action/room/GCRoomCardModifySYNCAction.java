package com.kodgames.client.action.room;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.client.service.room.RoomService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.game.GameProtoBuf.GCRoomCardModifySYNC;

@ActionAnnotation(actionClass = GCRoomCardModifySYNCAction.class, messageClass = GCRoomCardModifySYNC.class, serviceClass = RoomService.class)
public class GCRoomCardModifySYNCAction extends ProtobufMessageHandler<RoomService, GCRoomCardModifySYNC> {
    private static final Logger logger = LoggerFactory.getLogger(GCRoomCardModifySYNCAction.class);

    @Override
    public void handleMessage(Connection connection, RoomService service, GCRoomCardModifySYNC message, int callback) {
        logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
    }
}
