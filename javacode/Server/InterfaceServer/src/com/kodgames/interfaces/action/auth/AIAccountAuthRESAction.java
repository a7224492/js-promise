package com.kodgames.interfaces.action.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.interfaces.service.server.ServerService;
import com.kodgames.message.proto.auth.AuthProtoBuf.AIAccountAuthRES;
import com.kodgames.message.proto.auth.AuthProtoBuf.ICAccountAuthRES;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

@ActionAnnotation(messageClass = AIAccountAuthRES.class, actionClass = AIAccountAuthRESAction.class, serviceClass = ServerService.class)
public class AIAccountAuthRESAction extends ProtobufMessageHandler<ServerService, AIAccountAuthRES>
{

	private Logger logger = LoggerFactory.getLogger(AIAccountAuthRESAction.class);

	@Override
	public void handleMessage(Connection connection, ServerService service, AIAccountAuthRES message, int callback)
	{
		logger.info("AIAccountAuthRESAction message={}", message);

		Connection accountConnection = ConnectionManager.getInstance().getConnection(message.getClientConnectionId());
		if (null == accountConnection)
		{
			logger.warn("AIAccountAuthRESAction : accountConnection is null : channel={}, username={} connectionId={}",
				message.getChannel(),
				message.getUsername(),
				message.getClientConnectionId());
			return;
		}

		ICAccountAuthRES.Builder authRESBuilder = ICAccountAuthRES.newBuilder();

		int result = message.getResult();
		if (PlatformProtocolsConfig.AI_AUTH_SUCCESS == result)
			result = PlatformProtocolsConfig.IC_AUTH_SUCCESS;

		authRESBuilder.setResult(result)
			.setChannel(message.getChannel())
			.setUsername(message.getUsername())
			.setRefreshToken(message.getRefreshToken())
			.setAccountId(message.getAccountId())
			.setNickname(message.getNickname())
			.setSex(message.getSex())
			.setHeadImageUrl(message.getHeadImageUrl())
			.setGameServerId(service.getGameServerId())
			.setClubServerId(service.getClubServerId())
			.setRoleId(message.getAccountId())
			.setIp(accountConnection.getRemoteNode().getAddress().getHostString())
			.setProVersion(message.getProVersion())
			.setLibVersion(message.getLibVersion())
			.setTimestamp(message.getTimestamp())
			.setUnionid(message.getUnionid())
			.setSignature(message.getSignature());

		if (PlatformProtocolsConfig.AI_AUTH_SUCCESS == message.getResult())
		{
			accountConnection.setRemotePeerID(message.getAccountId());
			accountConnection.write(callback, authRESBuilder.build());
		}
		else
		{
			accountConnection.writeAndClose(callback, authRESBuilder.build());
		}

	}

}
