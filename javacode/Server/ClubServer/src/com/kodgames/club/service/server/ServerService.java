package com.kodgames.club.service.server;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.kodgames.club.start.NetInitializer;
import com.kodgames.club.start.ServerConfigInitializer;
import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.constant.ServerType;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.corgi.core.util.config_utils.AddressConfig;
import com.kodgames.corgi.core.util.config_utils.ServerConfig;
import com.kodgames.message.proto.server.ServerProtoBuf;
import com.kodgames.message.proto.server.ServerProtoBuf.SSGetLaunchInfoREQ;
import com.kodgames.message.proto.server.ServerProtoBuf.ServerConfigPROTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerService extends PublicService
{
	private static final long serialVersionUID = 3183880836623673530L;

	private Logger logger = LoggerFactory.getLogger(ServerService.class);

//	private static final int PHASE_PREPARE = 1;
//	private static final int PHASE_RUNNING = 2;
//	private int serverStatus = PHASE_PREPARE;
	
	//自身的启动配置
	private ServerConfig self;

	private Connection managerConnection;
	private Connection gameConnection;

	public void onGameConnect(Connection gameConnection)
	{
		this.gameConnection = gameConnection;
	}

	public void onGameDisconnect()
	{
		this.gameConnection = null;
	}

	public void onManagerConnect(Connection connection)
	{
		managerConnection = connection;
		SSGetLaunchInfoREQ.Builder builder = SSGetLaunchInfoREQ.newBuilder();
		ServerConfigPROTO.Builder configBuilder = ServerConfigPROTO.newBuilder();
		configBuilder.setArea(ServerConfigInitializer.getInstance().getAreaId());
		configBuilder.setType(ServerConfigInitializer.getInstance().getServerType());
		configBuilder.setId(ServerConfigInitializer.getInstance().getServerId());
		builder.setServer(configBuilder.build());
		connection.write(GlobalConstants.DEFAULT_CALLBACK, builder.build());
	}

	public void onManagerDisconnect(Connection connection)
	{
		NetInitializer.getInstance().connectToManager();
	}

	private List<Connection> clients = Collections.synchronizedList(new ArrayList<>());

	public void onClientConnect(Connection connection)
	{
		clients.add(connection);
	}

	public void onClientDisconnect(Connection connection)
	{
		clients.remove(connection);
	}

	public Connection getGameConnection()
	{
		return gameConnection;
	}
	
	public int getServerId()
	{
		return self.getId();
	}

	public void onAcquireLaunchInfo(ServerConfig config)
	{
		self = config;
	}

	public void onAcquireOnlineServersInfo(List<ServerConfigPROTO> list)
	{
//		list.stream()
//			.map(ServerConfig::fromProto)
//			.filter(proto -> proto.getType() == ServerType.GAME_SERVER)
//			.collect(Collectors.toList())
//			.forEach(this::connectGame);
	}

	/**
	 * 获取了一个 game 的信息
	 * @param game 要连接的battle信息
	 */
	private void connectGame(ServerConfig game)
	{
		InetSocketAddress address = game.getListen_socket_for_server().toSocketAddress();
		NetInitializer.getInstance().connectToGame(address);
	}

	/**
	 * manager主动发送移除下线的服务器
	 * @param id 被移除的id
	 */
	public void onManagerRemoveServer(int id)
	{
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

		self = config;

		NetInitializer.getInstance().openPortForGame(forGame.getPort());
//		NetInitializer.getInstance().openPortForClient(forClient.getPort());

		registerToManagerServer();
	}

	private void getLaunchInfo()
	{
		SSGetLaunchInfoREQ.Builder launchBuilder = SSGetLaunchInfoREQ.newBuilder();
		launchBuilder.setServer(ServerConfigPROTO.newBuilder().setArea(1).setType(ServerType.CLUB_SERVER).build());
		this.managerConnection.write(GlobalConstants.DEFAULT_CALLBACK, launchBuilder.build());
	}

	//启动流程
	private void registerToManagerServer()
	{
		NetInitializer.getInstance().openPortForInterface(self.getListen_socket_for_client().getPort());
		NetInitializer.getInstance().openPortForGmt(self.getListen_http_for_gmt().getPort(), self.getId());
		ServerProtoBuf.SSRegisterServerREQ.Builder builder = ServerProtoBuf.SSRegisterServerREQ.newBuilder();
		builder.setServer(self.toProto());
		managerConnection.write(GlobalConstants.DEFAULT_CALLBACK, builder.build());
	}

	public List<Integer> getOnlineClients()
	{
		return new ArrayList<>();
	}

	public List<Connection> getOnlineClientsConnection()
	{
		return clients;
	}
}