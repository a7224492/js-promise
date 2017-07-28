package com.kodgames.manageserver.start;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.constant.ServerType;
import com.kodgames.corgi.core.net.server.SimpleSSNettyInitializer;
import com.kodgames.corgi.core.net.server.SimpleServer;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

public class NetInitializer
{
	static private final Logger logger = LoggerFactory.getLogger(NetInitializer.class);
	private SimpleServer simpleServer;
	private final int socketPort;

	public NetInitializer(int socketPort)
	{
		this.socketPort = socketPort;
	}

	@SuppressWarnings("rawtypes")
	public void init()
		throws Exception
	{
		List<String> actionPackageList = new ArrayList<>();
		actionPackageList.add("com.kodgames.manageserver.action");
		List<Class> protocolClassList = new ArrayList<>();
		protocolClassList.add(PlatformProtocolsConfig.class);
		
		SSMessageInitializer ssMsgInitializer = new SSMessageInitializer(actionPackageList, protocolClassList);
		ssMsgInitializer.initialize();
		SimpleSSNettyInitializer ssInitializer = new SimpleSSNettyInitializer(ssMsgInitializer);
		simpleServer = new SimpleServer();
		simpleServer.initialize(ssInitializer, ssMsgInitializer);
		ConnectionManager.getInstance().init(ssMsgInitializer);
		ConnectionManager.getInstance().setLocalPeerID(ServerType.MANAGER_ID);
	}

	public void openPort4Server()
	{
		logger.info("Begin to open port:{} for Server", socketPort);
		simpleServer.openPort(new InetSocketAddress(socketPort));
	}
}
