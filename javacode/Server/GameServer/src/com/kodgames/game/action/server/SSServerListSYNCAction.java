package com.kodgames.game.action.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.game.service.server.ServerService;
import com.kodgames.message.proto.server.ServerProtoBuf.SSServerListSYNC;

/**
 * Created by lz on 2016/8/22. 在线服务器列表 第一次连接manager时会受到当前所有在线服务器 之后每有一个服务器上线 受到一次一个服务器的信息
 */
@ActionAnnotation(messageClass = SSServerListSYNC.class, actionClass = SSServerListSYNCAction.class, serviceClass = ServerService.class)
public class SSServerListSYNCAction extends ProtobufMessageHandler<ServerService, SSServerListSYNC>
{
	final static Logger logger = LoggerFactory.getLogger(SSServerListSYNCAction.class);

	@Override
	public void handleMessage(Connection connection, ServerService service, SSServerListSYNC message,
		int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
		service.onAcquireOnlineServersInfo(message.getListList());
	}
}