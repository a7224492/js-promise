package com.kodgames.battleserver.service.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.battleserver.start.NetInitializer;
import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.constant.ServerType;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.corgi.core.util.config_utils.AddressConfig;
import com.kodgames.corgi.core.util.config_utils.ServerConfig;
import com.kodgames.message.proto.server.ServerProtoBuf.SSGetLaunchInfoREQ;
import com.kodgames.message.proto.server.ServerProtoBuf.SSRegisterServerREQ;
import com.kodgames.message.proto.server.ServerProtoBuf.ServerConfigPROTO;

public class ServerService extends PublicService
{
	private static final long serialVersionUID = -8272027997288462700L;
	
	private Logger logger = LoggerFactory.getLogger(ServerService.class);

	private ServerConfig selfConfig;

	private ServerConfig gameConfig = null;
	private Connection gameConnection = null;
	private Connection manageConnection = null;

	public ServerConfig getSelfConfig()
	{
		return selfConfig;
	}

	public void startSelf(ServerConfig config)
	{		
		AddressConfig forClient = config.getListen_socket_for_client();
		AddressConfig forGame = config.getListen_socket_for_server();
		if (forClient == null || forGame == null)
		{
			logger.error("listen_socket_for_client {} listen_socket_for_server {} error, getLanchInfo again!!!!!!!!!!!!!!",
				forClient, forGame);
			this.getLaunchInfo();
			return;
		}
		
		selfConfig = config;
		
		NetInitializer.getInstance().openPortForGame(forGame.getPort());
		NetInitializer.getInstance().openPortForClient(forClient.getPort());
		
		registerToManagerServer();
	}

	public void onServerConnect(ServerConfig server)
	{
		switch (server.getType())
		{
			case ServerType.GAME_SERVER:
				this.gameConfig = server;
				break;
			default:
				break;
		}
	}

	public void onServerDisconnect(int serverID)
	{
		if (this.gameConfig != null && this.gameConfig.getId() == serverID)
		{
			this.gameConfig = null;
		}
	}
	
	private void getLaunchInfo()
	{
		SSGetLaunchInfoREQ.Builder launchBuilder = SSGetLaunchInfoREQ.newBuilder();
		launchBuilder.setServer(ServerConfigPROTO.newBuilder().setArea(1).setType(ServerType.BATTLE_SERVER).build());
		this.manageConnection.write(GlobalConstants.DEFAULT_CALLBACK, launchBuilder.build());
	}

	public void getLaunchInfo(Connection connection)
	{
		this.manageConnection = connection;
		this.getLaunchInfo();
	}

	private void registerToManagerServer()
	{
		SSRegisterServerREQ.Builder rigisterBuilder = SSRegisterServerREQ.newBuilder();
		rigisterBuilder.setServer(selfConfig.toProto());
		this.manageConnection.write(GlobalConstants.DEFAULT_CALLBACK, rigisterBuilder.build());
		
		logger.debug("*************************battleserver start success!!!*******************************");
	}

	public Connection getGameConnection()
	{
		return gameConnection;
	}
	
	public void setGameConnection(Connection connection)
	{
		this.gameConnection = connection;
	}

}
