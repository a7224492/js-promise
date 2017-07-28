package com.kodgames.authserver.service.server;

import com.kodgames.corgi.core.session.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.authserver.start.NetInitializer;
import com.kodgames.authserver.start.ServerConfigInitializer;
import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.constant.ServerType;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.corgi.core.util.config_utils.AddressConfig;
import com.kodgames.corgi.core.util.config_utils.ServerConfig;
import com.kodgames.message.proto.server.ServerProtoBuf.SSGetLaunchInfoREQ;
import com.kodgames.message.proto.server.ServerProtoBuf.SSGetLaunchInfoRES;
import com.kodgames.message.proto.server.ServerProtoBuf.SSRegisterServerREQ;
import com.kodgames.message.proto.server.ServerProtoBuf.SSServerDisconnectSYNC;
import com.kodgames.message.proto.server.ServerProtoBuf.SSServerListSYNC;
import com.kodgames.message.proto.server.ServerProtoBuf.ServerConfigPROTO;

public class ServerService extends PublicService
{
	private static final long serialVersionUID = 3194496576578484356L;
	private static Logger logger = LoggerFactory.getLogger(ServerService.class);

	private ServerConfig selfConfig = null;

	public void onGetLanchInfo(Connection connection, SSGetLaunchInfoRES message, int callback)
	{
		if (ServerType.AUTH_SERVER == message.getServer().getType())
		{
			this.selfConfig = ServerConfig.fromProto(message.getServer());
			ConnectionManager.getInstance().setLocalPeerID(selfConfig.getId());
			AddressConfig interfaceAddressConfig = this.selfConfig.getAddress_http_for_server();
			logger.info("Port for InterfaceServer : {}", interfaceAddressConfig.getPort());
			NetInitializer.getInstance().openPortForInterface(selfConfig.getListen_socket_for_client().getPort());
			NetInitializer.getInstance().openPortForGmt(selfConfig.getListen_http_for_gmt().getPort(),
				selfConfig.getId());
		}
		else
		{
			logger.error("Mismatched self server type : " + this.selfConfig.getType());
			System.exit(-1);
		}

		registerToManagerServer(connection);
	}

	public void onServerConnect(Connection connection, SSServerListSYNC message, int callback)
	{
	}

	public void onServerDisconnect(Connection connection, SSServerDisconnectSYNC message, int callback)
	{
	}

	public void getLanchInfo(Connection connection)
	{
		SSGetLaunchInfoREQ.Builder launchBuilder = SSGetLaunchInfoREQ.newBuilder();
		launchBuilder.setServer(ServerConfigPROTO.newBuilder()
			.setArea(ServerConfigInitializer.getInstance().getAreaId())
			.setType(ServerConfigInitializer.getInstance().getServerType())
			.setId(ServerConfigInitializer.getInstance().getServerId())
			.setListenSocketForClient(ServerConfigInitializer.getInstance().getListenInterface())
			.setListenHttpForGmt(ServerConfigInitializer.getInstance().getListenGmt()));

		connection.write(GlobalConstants.DEFAULT_CALLBACK, launchBuilder.build());
	}

	public void registerToManagerServer(Connection connection)
	{
		SSRegisterServerREQ.Builder rigisterBuilder = SSRegisterServerREQ.newBuilder();
		rigisterBuilder.setServer(selfConfig.toProto());
		connection.write(GlobalConstants.DEFAULT_CALLBACK, rigisterBuilder.build());
	}
	
}
