package com.kodgames.interfaces.action.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.interfaces.service.server.ServerService;
import com.kodgames.message.proto.auth.AuthProtoBuf.GIMergePlayerInfoRES;
import com.kodgames.message.proto.auth.AuthProtoBuf.IAMergePlayerInfoRES;

@ActionAnnotation(messageClass = GIMergePlayerInfoRES.class, actionClass = GIMergePlayerInfoRESAction.class, serviceClass = ServerService.class)
public class GIMergePlayerInfoRESAction extends ProtobufMessageHandler<ServerService, GIMergePlayerInfoRES>
{

	private Logger logger = LoggerFactory.getLogger(GIMergePlayerInfoRESAction.class);

	/**
	 * 在interface上面不应该转发SS之间的消息，这里是错误的做法。
	 */
	@Override
	public void handleMessage(Connection connection, ServerService service, GIMergePlayerInfoRES message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message); 
		Connection authConnection = service.getAuthConnection();
		IAMergePlayerInfoRES.Builder builder = IAMergePlayerInfoRES.newBuilder();
		builder.setResult(message.getResult());
		builder.setOldAccountid(message.getOldAccountid());
		builder.setNewAccountid(message.getNewAccountid());
		builder.setPlayerInfo(message.getPlayerInfo());
		authConnection.write(callback, builder.build());
	}

}
