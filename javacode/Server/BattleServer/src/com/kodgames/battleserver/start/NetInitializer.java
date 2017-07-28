package com.kodgames.battleserver.start;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.config.SimpleServerConfig;
import com.kodgames.corgi.core.net.handler.MessageExceptionCatchHandler;
import com.kodgames.corgi.core.net.server.SimpleClient;
import com.kodgames.corgi.core.net.server.SimpleSSNettyInitializer;
import com.kodgames.corgi.core.net.server.SimpleServer;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

public class NetInitializer
{
	private Logger logger = LoggerFactory.getLogger(NetInitializer.class);
	private static NetInitializer instance = new NetInitializer();

	private SimpleClient simpleClient;
	//private SimpleServer serverForClient;
	private SimpleServer simpleServer;
	
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
		actionPackageList.add("com.kodgames.battleserver.action");
		List<Class> protocolClassList = new ArrayList<>();
		protocolClassList.add(PlatformProtocolsConfig.class);
		
		SSMessageInitializer ssMessageInitializer = new SSMessageInitializer(actionPackageList, protocolClassList);
		ssMessageInitializer.initialize();
		SimpleSSNettyInitializer ssNettyInitializer = new SimpleSSNettyInitializer(ssMessageInitializer);
		simpleClient = new SimpleClient();
		simpleClient.initialize(ssNettyInitializer, ssMessageInitializer);

		//GBMessageInitializer gbMsgInitializer = new GBMessageInitializer("com.kodgames.battleserver.action");
		//gbMsgInitializer.initialize(Connection.CONNECTION_TYPE_SERVER);
		simpleServer = new SimpleServer();
		simpleServer.initialize(ssNettyInitializer, ssMessageInitializer);
		
		/*CBMessageInitializer cbMsgInitializer = new CBMessageInitializer("com.kodgames.battleserver.action");
		cbMsgInitializer.initialize(Connection.CONNECTION_TYPE_CLIENT);
		cbMsgInitializer.setChannelHandler(new DispatchChannelHandler());
		serverForClient = new SimpleServer();
		serverForClient.initialize(new DispatchNettyInitializer(cbMsgInitializer), cbMsgInitializer);
*/
		//此处设置MessageInitializer主要用来解析协议类型
		ConnectionManager.getInstance().init(ssMessageInitializer);
		
		//添加异常处理handler
		MessageExceptionCatchHandler.getInstance().init("com.kodgames.battleserver.exception");
		
		connectToManagerServer();
	}
	
	public void connectToManagerServer()
	{
		SimpleServerConfig manager = ServerConfigInitializer.getInstance().getManagerConfig();
		InetSocketAddress address = new InetSocketAddress(manager.getHost(), manager.getPort());
		ConnectionManager.getInstance().setManagerServerAddress(address);
		simpleClient.connectTo(address, 100);
	}

	public void openPortForClient(int port)
	{
		simpleServer.openPort(new InetSocketAddress(port));
		logger.info("Open port:{} for client OK!", port);
	}

	public void openPortForGame(int port)
	{
		simpleServer.openPort(new InetSocketAddress(port));
		logger.info("Open port:{} for game OK!", port);
	}
}
