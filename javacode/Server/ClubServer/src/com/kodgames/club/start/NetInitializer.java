package com.kodgames.club.start;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.config.SimpleServerConfig;
import com.kodgames.corgi.core.constant.ServerType;
import com.kodgames.corgi.core.net.ServerMessageInitializer;
import com.kodgames.corgi.core.net.server.SimpleClient;
import com.kodgames.corgi.core.net.server.SimpleSSNettyInitializer;
import com.kodgames.corgi.core.net.server.SimpleServer;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.club.net.server.SSConnectionHandler;
import com.kodgames.gmtools.httpserver.GmtoolsHttpServer;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

public class NetInitializer
{
	private static Logger logger = LoggerFactory.getLogger(NetInitializer.class);
	private static NetInitializer instance = new NetInitializer();

	private SimpleServer simpleServer;
	private SimpleClient simpleClient;

	private NetInitializer()
	{
	}

	public static NetInitializer getInstance()
	{
		return instance;
	}

	@SuppressWarnings("rawtypes")
	public void init()
		throws Exception
	{
		List<String> actionPackageList = new ArrayList<>();
		actionPackageList.add("com.kodgames.club.action");
		List<Class> protocolClassList = new ArrayList<>();
		protocolClassList.add(PlatformProtocolsConfig.class);
		
		// interface interface首次连接到game时不会触发到active 只有逻辑的client的connection会触发
		ServerMessageInitializer ssMessageInitializer = new SSMessageInitializer(actionPackageList, protocolClassList, new SSConnectionHandler());
		ssMessageInitializer.initialize();
		SimpleSSNettyInitializer ssNettyInitializer = new SimpleSSNettyInitializer(ssMessageInitializer);
		simpleServer = new SimpleServer();
		simpleServer.initialize(ssNettyInitializer, ssMessageInitializer);

		// manager
		simpleClient = new SimpleClient();
		simpleClient.initialize(ssNettyInitializer, ssMessageInitializer);

		ConnectionManager.getInstance().init(ssMessageInitializer);
		connectToManager();
	}

	public void connectToManager()
	{
		SimpleServerConfig manager = ServerConfigInitializer.getInstance().getManagerConfig();
		SocketAddress address = new InetSocketAddress(manager.getHost(), manager.getPort());
		ConnectionManager.getInstance().setManagerServerAddress(new InetSocketAddress(manager.getHost(), manager.getPort()));
		simpleClient.connectTo(address, 5);
	}

	public void connectToGame(InetSocketAddress address)
	{
		simpleClient.connectTo(address, 2);
	}

	public void openPortForInterface(int port)
	{
		simpleServer.openPort(new InetSocketAddress(port));
		logger.info("openPortForInterface : {}", port);
	}

	public void openPortForGmt(int port, int serverId)
	{
		GmtoolsHttpServer.getInstance().start("com.kodgames.club.service.gmtools.handler",
			port,
			serverId,
			ServerType.CLUB_SERVER,
				true);
		logger.info("openPortForGmt : {}", port);
	}

	public void openPortForGame(int port)
	{
		simpleServer.openPort(new InetSocketAddress(port));
		logger.info("openPortForGame : {}", port);
	}

	public void openPortForClient(int port)
	{
		simpleServer.openPort(new InetSocketAddress(port));
		logger.info("openPortForClient : {}", port);
	}
	
}
