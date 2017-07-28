package com.kodgames.replay.action.history;

import com.kodgames.replay.start.CGProtobufMessageHandler;
import com.kodgames.replay.util.MessageTransferUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.replay.service.history.HistoryService;
import com.kodgames.message.proto.game.GameProtoBuf.CGHistoryREQ;
import com.kodgames.message.proto.game.GameProtoBuf.GCHistoryRES;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

@ActionAnnotation(messageClass = CGHistoryREQ.class, actionClass = CGHistoryREQAction.class, serviceClass = HistoryService.class)
public class CGHistoryREQAction extends CGProtobufMessageHandler<HistoryService, CGHistoryREQ> {
    private static final Logger logger = LoggerFactory.getLogger(CGHistoryREQAction.class);

    @Override
    public void handleMessage(Connection connection, HistoryService service, CGHistoryREQ message, int callback) {
        logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
        Integer roleId = message.getClientId();

        GCHistoryRES.Builder builder = GCHistoryRES.newBuilder();
        builder.setResult(PlatformProtocolsConfig.GC_HISTORY_SUCCESS);

        builder.setVersion(service.getVersion(roleId));
        builder.addAllRoomRecords(service.getHistoryList(roleId, message.getVersion()));

        // 通过Game转发返回消息
        MessageTransferUtil.broadcastMsg2Game(callback, message.getClientId(), builder.build());
    }
}
