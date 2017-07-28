package com.kodgames.client.action.room;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.client.service.room.RoomService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.room.RoomProtoBuf.BCVoteDestroyRES;

@ActionAnnotation(actionClass = BCVoteDestroyRESAction.class, messageClass = BCVoteDestroyRES.class, serviceClass = RoomService.class)
public class BCVoteDestroyRESAction extends ProtobufMessageHandler<RoomService, BCVoteDestroyRES> {
    private static final Logger logger = LoggerFactory.getLogger(BCVoteDestroyRESAction.class);

    @Override
    public void handleMessage(Connection connection, RoomService service, BCVoteDestroyRES message, int callback) {
        logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
    }
}