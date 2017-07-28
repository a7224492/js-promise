package com.kodgames.interfaces.service.server;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.constant.ServerType;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.corgi.core.util.config_utils.AddressConfig;
import com.kodgames.corgi.core.util.config_utils.ServerConfig;
import com.kodgames.corgi.core.util.config_utils.ServerParser;
import com.kodgames.interfaces.start.NetInitializer;
import com.kodgames.message.proto.server.ServerProtoBuf.SSGetLaunchInfoREQ;
import com.kodgames.message.proto.server.ServerProtoBuf.SSRegisterServerREQ;
import com.kodgames.message.proto.server.ServerProtoBuf.ServerConfigPROTO;

/**
 */
public class ServerService extends PublicService
{
	private static final long serialVersionUID = -8272027997288462700L;
	private Logger logger = LoggerFactory.getLogger(ServerService.class);

	private NetInitializer netInitializer;

	private ServerConfig self;
	private Connection manageConnection;
	private Connection authConnection;
	private Connection gameConnection;
	private boolean isReady = false;

	private boolean authReady = false;
	private boolean battleReady = false;
	private boolean gameReady = false;

	private int gameServerId;

	private int clubServerId;

	public int getGameServerId()
	{
		return gameServerId;
	}

	public void setGameServerId(int gameServerId)
	{
		this.gameServerId = gameServerId;
	}

	public int getClubServerId() {
		return clubServerId;
	}

	public void setClubServerId(int clubServerId) {
		this.clubServerId = clubServerId;
	}

	class AccountInfo
	{
		Connection connection;
		int callback;

		public AccountInfo(Connection connection, int callback)
		{
			this.connection = connection;
			this.callback = callback;
		}
	}

	private ServerParser config;

	public void init(NetInitializer netInitializer, ServerParser config)
	{
		this.netInitializer = netInitializer;
		this.config = config;

	}

	public void getlaunchInfo(Connection connection)
	{
		this.manageConnection = connection;

		SSGetLaunchInfoREQ.Builder builder = SSGetLaunchInfoREQ.newBuilder();
		ServerConfigPROTO.Builder configBuilder = ServerConfigPROTO.newBuilder();
		configBuilder.setType(ServerType.INTERFACE_SERVER);
		configBuilder.setArea(1);
		String wsSocketStr = config.getConfig().get("listen_websocket_for_client");
		String socketStr = config.getConfig().get("listen_socket_for_client");
		if (wsSocketStr != null && !wsSocketStr.isEmpty())
		{
			configBuilder.setListenWebSocketForClient(wsSocketStr);
		}

		if (socketStr != null && !socketStr.isEmpty())
		{
			configBuilder.setListenSocketForClient(socketStr);
		}
		builder.setServer(configBuilder.build());
		connection.write(GlobalConstants.DEFAULT_CALLBACK, builder.build());
	}

	public void recvConfig(ServerConfig self)
	{
		this.self = self;
		logger.debug("Interface getlaunchInfo recv config {}", self.toString());
	}

	public void startSelf()
	{
		if (netInitializer == null)
		{
			logger.error("recv self config from manager, but without init netInitializer == null");
			return;
		}

		// 打开socket和websocket端口
		AddressConfig socketConfig = self.getListen_socket_for_client();
		if (null != socketConfig)
		{
			netInitializer.openPort4SocketClient(socketConfig.getPort());
		}
		AddressConfig websocketConfig = self.getListen_web_socket_for_client();
		if (null != websocketConfig)
		{
			netInitializer.openPort4WebsocketClient(websocketConfig.getPort());
		}

		logger.info("openport for client websocketport {}", self.getListen_web_socket_for_client().getPort());
	}

	private void connectToServer(ServerConfig server)
	{
		if (server.getType() == ServerType.GAME_SERVER)
			setGameServerId(server.getId());
		else if (server.getType() == ServerType.CLUB_SERVER)
			setClubServerId(server.getId());

		InetSocketAddress addr = null;

		switch (server.getType())
		{
			case ServerType.AUTH_SERVER:
				addr = server.getAddress_socket_for_client().toSocketAddress();
				break;
			case ServerType.GAME_SERVER:
				addr = server.getAddress_socket_for_client().toSocketAddress();
				break;
			case ServerType.BATTLE_SERVER:
				addr = server.getAddress_socket_for_client().toSocketAddress();
				break;
			case ServerType.CLUB_SERVER:
				addr = server.getAddress_socket_for_client().toSocketAddress();
				break;
		}

		logger.debug("connecting to server type {} addr {}", server.getType(), addr);

		netInitializer.connectToServer(addr, server.getType());

	}

	public void connectToServers(List<ServerConfigPROTO> serverList)
	{
		for (ServerConfigPROTO proto : serverList)
		{
			ServerConfig server = ServerConfig.fromProto(proto);
			if (server.getType() == ServerType.AUTH_SERVER || server.getType() == ServerType.BATTLE_SERVER
				|| server.getType() == ServerType.GAME_SERVER || server.getType() == ServerType.CLUB_SERVER)
			{
				connectToServer(server);
			}
		}
	}

	public void connectServerFinished()
	{
		// 启动完毕后，收到推送的server信息，依然会走到connectFinshed，所以添加状态控制
		if (!isReady)
		{
			this.isReady = true;
			this.startSelf();
			this.registerToManager();
		}
	}

	public void dealNewServerUp(ServerConfig serverConfig)
	{
		this.connectToServer(serverConfig);
	}

	public void dealServerDown(int serverId)
	{
		// do nothing??
		// 因为interface关心的battle和game，都有连接，如果这俩断开，Interface自己能发现
	}

	public void registerToManager()
	{
		SSRegisterServerREQ.Builder build = SSRegisterServerREQ.newBuilder();
		build.setServer(self.toProto());

		manageConnection.write(GlobalConstants.DEFAULT_CALLBACK, build.build());

		logger.info("*******************************Interface start success!!!!*************************************");
	}

	public void connectToManager(SocketAddress manageServerAddress)
	{
		// ConnectionManager.getInstance().addServerInfo(ServerType.MANAGER_ID, manageServerAddress);
		netInitializer.connectToManager(manageServerAddress);
	}

	public void serverConnected(int serverId, Connection connection)
	{
		Integer serverType = ServerType.getType(serverId);

		if (null == connection)
		{
			logger.error("ServerService serverConnected : connection is null");
			return;
		}

		if (serverType == ServerType.AUTH_SERVER)
		{
			setAuthConnection(connection);
			this.authReady = true;
		}
		else if (serverType == ServerType.BATTLE_SERVER)
		{
			this.battleReady = true;
		}
		else if (serverType == ServerType.GAME_SERVER)
		{
			setGameConnection(connection);
			this.gameServerId = serverId;
			this.gameReady = true;
		}

		if (!this.authReady || !this.battleReady || !this.gameReady)
		{
			// 如果auth或者game或者battle还没有连接好，则没有启动完毕
			return;
		}

		connectServerFinished();
	}

	public Connection getAuthConnection()
	{
		return authConnection;
	}

	private void setAuthConnection(Connection connection)
	{
		this.authConnection = connection;
	}
	
	public void setGameConnection(Connection connection)
	{
		this.gameConnection = connection;
	}
	
	public Connection getGameConnection()
	{
		return gameConnection;
	}

}
