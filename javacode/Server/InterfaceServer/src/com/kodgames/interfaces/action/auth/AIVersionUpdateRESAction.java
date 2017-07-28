package com.kodgames.interfaces.action.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.interfaces.service.server.ServerService;
import com.kodgames.message.proto.auth.AuthProtoBuf.AIVersionUpdateRES;
import com.kodgames.message.proto.auth.AuthProtoBuf.ICVersionUpdateRES;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

/**
 * auth回应interface请求更新信息
 * 
 * @author 毛建伟
 */
@ActionAnnotation(messageClass = AIVersionUpdateRES.class, actionClass = AIVersionUpdateRESAction.class, serviceClass = ServerService.class)
public class AIVersionUpdateRESAction extends ProtobufMessageHandler<ServerService, AIVersionUpdateRES>
{
	private Logger logger = LoggerFactory.getLogger(AIVersionUpdateRESAction.class);

	@Override
	public void handleMessage(Connection connection, ServerService service, AIVersionUpdateRES message, int callback)
	{
		logger.info("AIVersionUpdateRESAction message={}", message);

		Connection clientConnection = ConnectionManager.getInstance().getConnection(message.getClientConnectionId());
		if (null == clientConnection)
		{
			logger.warn("AIVersionUpdateRESAction : clientConnection is null : channel={}, subchannel={} connectionId={}",
				message.getChannel(),
				message.getSubchannel(),
				message.getClientConnectionId());
			return;
		}

		ICVersionUpdateRES.Builder result = ICVersionUpdateRES.newBuilder();
		result.setResult(message.getResult())
			.setChannel(message.getChannel())
			.setSubchannel(message.getSubchannel())
			.setLibUrl(message.getLibUrl())
			.setLibVersion(message.getLibVersion())
			.setLastLibVersion(message.getLastLibVersion())
			.setProNeedUpdate(message.getProNeedUpdate())
			.setProUrl(message.getProUrl())
			.setProVersion(message.getProVersion())
			.setReviewUrl(message.getReviewUrl())
			.setReviewVersion(message.getReviewVersion());

		if (message.getResult() != PlatformProtocolsConfig.AI_VERSION_UPDATE_FAIL_NO_CHANNEL_INFO)
			clientConnection.write(callback, result.build());
		else
			clientConnection.writeAndClose(callback, result.build());
	}
}
