package com.kodgames.interfaces.action.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.interfaces.service.server.ServerService;
import com.kodgames.message.proto.auth.AuthProtoBuf.AIMergePlayerInfoREQ;
import com.kodgames.message.proto.auth.AuthProtoBuf.IGMergePlayerInfoREQ;

@ActionAnnotation(messageClass = AIMergePlayerInfoREQ.class, actionClass = AIMergePlayerInfoREQAction.class, serviceClass = ServerService.class)
public class AIMergePlayerInfoREQAction extends ProtobufMessageHandler<ServerService, AIMergePlayerInfoREQ>
{

	private Logger logger = LoggerFactory.getLogger(AIMergePlayerInfoREQAction.class);

	/**
	 * 在interface上面不应该转发SS之间的消息，这里是错误的做法。
	 */
	@Override
	public void handleMessage(Connection connection, ServerService service, AIMergePlayerInfoREQ message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
		Connection gameConnection = service.getGameConnection();
		IGMergePlayerInfoREQ.Builder builder = IGMergePlayerInfoREQ.newBuilder();
		builder.setUnionidAccountid(message.getUnionidAccountid());
		builder.setOpenidAccountid(message.getOpenidAccountid());
		builder.setPlayerInfo(message.getPlayerInfo());
		gameConnection.write(callback, builder.build());
	}

}
