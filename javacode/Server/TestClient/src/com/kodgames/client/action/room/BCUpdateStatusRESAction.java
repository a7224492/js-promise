package com.kodgames.client.action.room;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.client.service.room.RoomService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.room.RoomProtoBuf.BCUpdateStatusRES;

@ActionAnnotation(actionClass = BCUpdateStatusRESAction.class, messageClass = BCUpdateStatusRES.class, serviceClass = RoomService.class)
public class BCUpdateStatusRESAction extends ProtobufMessageHandler<RoomService, BCUpdateStatusRES> {
    private static final Logger logger = LoggerFactory.getLogger(BCUpdateStatusRESAction.class);

    @Override
    public void handleMessage(Connection connection, RoomService service, BCUpdateStatusRES message, int callback) {
        logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
    }
}
