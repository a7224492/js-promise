package com.kodgames.interfaces.start;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.constant.ServerType;
import com.kodgames.corgi.core.net.server.SimpleCSNettyInitializer;
import com.kodgames.corgi.core.net.server.SimpleClient;
import com.kodgames.corgi.core.net.server.SimpleSSNettyInitializer;
import com.kodgames.corgi.core.net.server.SimpleServer;
import com.kodgames.corgi.core.net.server.SimpleWSNettyInitializer;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

/**
 * 等待客户端连接
 * 主动连接Battle和Game
 */
public class NetInitializer
{
	private Logger logger = LoggerFactory.getLogger(NetInitializer.class);
	
	//与Server的连接
	private SSMessageInitializer ssMessageInitializer;
	private SimpleSSNettyInitializer ssNettyInitializer;
	private SimpleClient ssClient;

	
	//与Client的连接
	private CIMessageInitializer csMsgInitializer;
	private SimpleServer socketServer;
	private SimpleServer websocketServer;
	
	@SuppressWarnings("rawtypes")
	public void init() throws Exception
	{
		List<String> actionPackageList = new ArrayList<>();
		actionPackageList.add("com.kodgames.interfaces.action");
		List<Class> protocolClassList = new ArrayList<>();
		protocolClassList.add(PlatformProtocolsConfig.class);
		
		ssMessageInitializer = new SSMessageInitializer(actionPackageList, protocolClassList);
		ssMessageInitializer.initialize();
		ssNettyInitializer = new SimpleSSNettyInitializer(ssMessageInitializer);
		ssClient = new SimpleClient();
		ssClient.initialize(ssNettyInitializer, ssMessageInitializer);
		
		csMsgInitializer = new CIMessageInitializer(actionPackageList, protocolClassList);
		csMsgInitializer.initialize();

		SimpleCSNettyInitializer socketInitializer = new SimpleCSNettyInitializer(csMsgInitializer);
		socketServer = new SimpleServer();
		socketServer.initialize(socketInitializer, csMsgInitializer);
		
		SimpleWSNettyInitializer websocketInitializer = new SimpleWSNettyInitializer(csMsgInitializer);
		websocketServer = new SimpleServer();
		websocketServer.initialize(websocketInitializer, csMsgInitializer);

		//battleClient等待收到具体配置后，再初始化

		ConnectionManager.getInstance().init(ssMessageInitializer);
	}
	
	public void connectToManager(SocketAddress manageAddr)
	{
		ssClient.connectTo(manageAddr, 5);
	}
	
	private void connectToAuth(SocketAddress addr)
	{
		ssClient.connectTo(addr, 5);
	}

	private void connectToGame(SocketAddress addr)
	{
		ssClient.connectTo(addr, 5);
	}

	private void connectToClub(SocketAddress addr)
	{
		ssClient.connectTo(addr, 5);
	}
	
	private void connectToBattle(SocketAddress addr)
	{
		//SimpleClient client = new SimpleClient();
		//client.initialize(ssInitializer, ssMsgInitializer);
		ssClient.connectTo(addr, 5);

		//battleClientLists.add(client);
	}
	
	public void openPort4SocketClient(int port)
	{
		socketServer.openPort(new InetSocketAddress(port));
		logger.info("Interface Begin to open port:{} for client", port);
	}
	
	public void openPort4WebsocketClient(int port)
	{
		websocketServer.openPort(new InetSocketAddress(port));
		logger.info("Interface Begin to open port:{} for client", port);
	}
	
	public void close()
	{
/*		gameClient.close();
		
		for (SimpleClient battle : battleClientLists)
		{
			battle.close();
		}
		
		ssClient.close();*/
	}

	public void connectToServer(InetSocketAddress addr, int serverType)
	{
		if (serverType == ServerType.AUTH_SERVER)
		{
			connectToAuth(addr);
		}
		if (serverType == ServerType.BATTLE_SERVER)
		{
			connectToBattle(addr);
		}
		else if (serverType == ServerType.GAME_SERVER)
		{
			connectToGame(addr);
		}
		else if (serverType == ServerType.CLUB_SERVER)
		{
			connectToClub(addr);
		}
	}
}
