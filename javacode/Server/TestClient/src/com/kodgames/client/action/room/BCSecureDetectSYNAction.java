package com.kodgames.client.action.room;

import com.kodgames.client.service.room.RoomService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.room.RoomProtoBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liufei on 2017/7/1.
 */
@ActionAnnotation(actionClass = BCSecureDetectSYNAction.class, messageClass = RoomProtoBuf.BCSecureDetectSYN.class, serviceClass = RoomService.class)
public class BCSecureDetectSYNAction extends ProtobufMessageHandler<RoomService, RoomProtoBuf.BCSecureDetectSYN> {
    private static final Logger logger = LoggerFactory.getLogger(BCSecureDetectSYNAction.class);

    @Override
    public void handleMessage(Connection connection, RoomService service, RoomProtoBuf.BCSecureDetectSYN message, int callback) {
        logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
    }
}
