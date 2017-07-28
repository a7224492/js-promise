package com.kodgames.authserver.start;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.authserver.net.server.SSConnectionHandler;
import com.kodgames.corgi.core.config.SimpleServerConfig;
import com.kodgames.corgi.core.constant.ServerType;
import com.kodgames.corgi.core.net.server.SimpleClient;
import com.kodgames.corgi.core.net.server.SimpleSSNettyInitializer;
import com.kodgames.corgi.core.net.server.SimpleServer;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.gmtools.httpserver.GmtoolsHttpServer;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

public class NetInitializer
{
	private static NetInitializer instance = new NetInitializer();
	private SSMessageInitializer ssMessageInitializer;
	private SimpleClient client;
	private SimpleServer server;

	private static Logger logger = LoggerFactory.getLogger(NetInitializer.class);

	private NetInitializer()
	{
	}

	@SuppressWarnings("rawtypes")
	public void init()
		throws Exception
	{
		List<String> actionPackageList = new ArrayList<>();
		actionPackageList.add("com.kodgames.authserver.action");
		List<Class> protocolClassList = new ArrayList<>();
		protocolClassList.add(PlatformProtocolsConfig.class);
		
		ssMessageInitializer =
			new SSMessageInitializer(actionPackageList, protocolClassList, new SSConnectionHandler());
		ssMessageInitializer.initialize();
		client = new SimpleClient();
		client.initialize(new SimpleSSNettyInitializer(ssMessageInitializer), ssMessageInitializer);

		SimpleSSNettyInitializer ssNettyInitializer = new SimpleSSNettyInitializer(ssMessageInitializer);
		server = new SimpleServer();
		server.initialize(ssNettyInitializer, ssMessageInitializer);

		ConnectionManager.getInstance().init(ssMessageInitializer);

		connectToManagerServer();
	}

	public static NetInitializer getInstance()
	{
		return instance;
	}

	public void connectToManagerServer()
	{
		SimpleServerConfig manager = ServerConfigInitializer.getInstance().getManagerConfig();
		InetSocketAddress address = new InetSocketAddress(manager.getHost(), manager.getPort());
		ConnectionManager.getInstance().setManagerServerAddress(address);
		client.connectTo(address, 1);
	}

	public void openPortForInterface(int port)
	{
		server.openPort(new InetSocketAddress(port));
		logger.info("openPortForInterface : {}", port);
	}

	public void openPortForGmt(int port, int serverId)
	{
		GmtoolsHttpServer.getInstance().start("com.kodgames.authserver.service.gmtools",
			port,
			serverId,
			ServerType.AUTH_SERVER);
		logger.info("openPortForGmt : {}", port);
	}
}
