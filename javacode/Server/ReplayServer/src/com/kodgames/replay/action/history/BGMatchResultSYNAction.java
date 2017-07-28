package com.kodgames.replay.action.history;

import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.replay.service.history.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.message.proto.game.GameProtoBuf.BGMatchResultSYN;

@ActionAnnotation(messageClass = BGMatchResultSYN.class, actionClass = BGMatchResultSYNAction.class, serviceClass = HistoryService.class)
public class BGMatchResultSYNAction extends ProtobufMessageHandler<HistoryService, BGMatchResultSYN> {
    private static final Logger logger = LoggerFactory.getLogger(BGMatchResultSYNAction.class);

    @Override
    public void handleMessage(Connection connection, HistoryService service, BGMatchResultSYN message, int callback) {
        logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

        // 保存战绩
        service.saveHistory(message);
    }

    @Override
    public Object getMessageKey(Connection connection, BGMatchResultSYN message) {
        return message.getRoomId() == 0 ? connection.getConnectionID() : message.getRoomId();
    }
}
