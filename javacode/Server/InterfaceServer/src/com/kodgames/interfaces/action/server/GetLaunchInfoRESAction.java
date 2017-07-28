package com.kodgames.interfaces.action.server;

import com.kodgames.corgi.core.session.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.corgi.core.util.config_utils.ServerConfig;
import com.kodgames.interfaces.service.server.ServerService;
import com.kodgames.message.proto.server.ServerProtoBuf.SSGetLaunchInfoRES;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

@ActionAnnotation(messageClass = SSGetLaunchInfoRES.class,
				  actionClass = GetLaunchInfoRESAction.class,
				  serviceClass = ServerService.class)
public class GetLaunchInfoRESAction extends ProtobufMessageHandler<ServerService, SSGetLaunchInfoRES>
{
	final static Logger logger = LoggerFactory.getLogger(GetLaunchInfoRESAction.class);
	
	@Override
	public void handleMessage(Connection connection, ServerService service, SSGetLaunchInfoRES message, int callback)
	{
		// 首先解析自己的配置
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
		if (message.getResult() != PlatformProtocolsConfig.SS_GET_LAUNCH_INFO_SUCCESS)
		{
			logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!getlaunchInfo failed!!!!!!!!!!!!!!!!!!!!");
			return;
		}
		ServerConfig sc = ServerConfig.fromProto(message.getServer());
		ConnectionManager.getInstance().setLocalPeerID(sc.getId());
		service.recvConfig(sc);
	}
}