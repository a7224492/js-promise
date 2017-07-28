package com.kodgames.interfaces.action.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.interfaces.service.server.ServerService;
import com.kodgames.message.proto.auth.AuthProtoBuf.CIAccountAuthREQ;
import com.kodgames.message.proto.auth.AuthProtoBuf.IAAccountAuthREQ;
import com.kodgames.message.proto.auth.AuthProtoBuf.ICAccountAuthRES;
import com.kodgames.message.proto.auth.AuthProtoBuf.LibraryVersionPROTO;
import com.kodgames.message.proto.auth.AuthProtoBuf.ProductVersionPROTO;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

@ActionAnnotation(messageClass = CIAccountAuthREQ.class, actionClass = CIAccountAuthREQAction.class, serviceClass = ServerService.class)
public class CIAccountAuthREQAction extends ProtobufMessageHandler<ServerService, CIAccountAuthREQ>
{

	private Logger logger = LoggerFactory.getLogger(CIAccountAuthREQAction.class);

	@Override
	public void handleMessage(Connection connection, ServerService service, CIAccountAuthREQ message, int callback)
	{
		logger.info("CIAccountAuthREQAction message={}", message);

		ConnectionManager.getInstance().addConnection(connection);
		Connection authConnection = service.getAuthConnection();
		if (null == authConnection)
		{
			connection.writeAndClose(callback,
				ICAccountAuthRES.newBuilder()
					.setResult(PlatformProtocolsConfig.IC_AUTH_FAILED_NO_AUTH_SERVER)
					.setChannel("")
					.setUsername("")
					.setRefreshToken("")
					.setAccountId(0)
					.setNickname("")
					.setSex(0)
					.setHeadImageUrl("")
					.setRoleId(0)
					.setGameServerId(0)
					.setIp("")
					.setProVersion(
						ProductVersionPROTO.newBuilder().setNeedUpdate(false).setVersion("").setDescription(""))
					.setLibVersion(LibraryVersionPROTO.newBuilder()
						.setNeedUpdate(false)
						.setForceUpdate(false)
						.setVersion("")
						.setDescription("")
						.setUrl(""))
					.build());
			return;
		}

		authConnection.write(callback,
			IAAccountAuthREQ.newBuilder()
				.setChannel(message.getChannel())
				.setUsername(message.getUsername())
				.setCode(message.getCode())
				.setRefreshToken(message.getRefreshToken())
				.setPlatform(message.getPlatform())
				.setProVersion(message.getProVersion())
				.setLibVersion(message.getLibVersion())
				.setClientConnectionId(connection.getConnectionID())
				.setAppCode(message.getAppCode())
				.setDeviceId(message.getDeviceId())
				.build());
	}

}
