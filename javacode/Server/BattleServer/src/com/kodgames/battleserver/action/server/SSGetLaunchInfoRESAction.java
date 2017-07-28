package com.kodgames.battleserver.action.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.battleserver.service.server.ServerService;
import com.kodgames.corgi.core.constant.ServerType;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.corgi.core.util.config_utils.ServerConfig;
import com.kodgames.message.proto.server.ServerProtoBuf.SSGetLaunchInfoRES;
import com.kodgames.message.proto.server.ServerProtoBuf.ServerConfigPROTO;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

@ActionAnnotation(actionClass = SSGetLaunchInfoRESAction.class, messageClass = SSGetLaunchInfoRES.class, serviceClass = ServerService.class)
public class SSGetLaunchInfoRESAction extends ProtobufMessageHandler<ServerService, SSGetLaunchInfoRES>
{
	final static Logger logger = LoggerFactory.getLogger(SSGetLaunchInfoRESAction.class);

	@Override
	public void handleMessage(Connection connection, ServerService service, SSGetLaunchInfoRES message, int callback)
	{
		logger.info("GetLaunchInfo : {} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
		if (ServerType.BATTLE_SERVER == message.getServer().getType())
		{
			if (message.getResult() != PlatformProtocolsConfig.SS_GET_LAUNCH_INFO_SUCCESS)
			{
				logger.error("GetLaunchInfo failed : error code {}-0x{}", message.getResult(), Integer.toHexString(message.getResult()));
				return;
			}
			ServerConfigPROTO config = message.getServer();
			ServerConfig sc = ServerConfig.fromProto(config);

			ConnectionManager.getInstance().setLocalPeerID(sc.getId());

			service.startSelf(sc);
		}
		else
		{
			logger.error("GetLaunchInfo failed : serverType is {}", message.getServer().getType());
			System.exit(-1);
		}
	}
}