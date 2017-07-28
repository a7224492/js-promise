package com.kodgames.client.action.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.client.service.chat.ChatService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.chat.ChatProtoBuf.BCChatRES;

@ActionAnnotation(actionClass = BCChatRESAction.class, messageClass = BCChatRES.class, serviceClass = ChatService.class)
public class BCChatRESAction extends ProtobufMessageHandler<ChatService, BCChatRES> {
    private static final Logger logger = LoggerFactory.getLogger(BCChatRESAction.class);

    @Override
    public void handleMessage(Connection connection, ChatService service, BCChatRES message, int callback) {
        logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
    }
}