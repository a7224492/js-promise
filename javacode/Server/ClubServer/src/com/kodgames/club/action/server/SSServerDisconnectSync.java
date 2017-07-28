package com.kodgames.club.action.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.club.service.server.ServerService;
import com.kodgames.message.proto.server.ServerProtoBuf.SSServerDisconnectSYNC;

/**
 * Created by lz on 2016/8/22. 服务器下线通知 由manager发送
 */
@ActionAnnotation(messageClass = SSServerDisconnectSYNC.class, actionClass = SSServerDisconnectSync.class, serviceClass = ServerService.class)
public class SSServerDisconnectSync extends ProtobufMessageHandler<ServerService, SSServerDisconnectSYNC>
{
	final static Logger logger = LoggerFactory.getLogger(SSServerDisconnectSync.class);

	@Override
	public void handleMessage(Connection connection, ServerService service, SSServerDisconnectSYNC message,
		int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
		service.onManagerRemoveServer(message.getId());
	}
}