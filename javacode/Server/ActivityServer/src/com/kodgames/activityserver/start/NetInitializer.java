package com.kodgames.activityserver.start;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.List;

import com.kodgames.activityserver.net.server.SSConnectionHandler;
import com.kodgames.corgi.core.net.server.SimpleClient;
import io.netty.channel.ChannelInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.config.SimpleServerConfig;
import com.kodgames.corgi.core.net.ServerMessageInitializer;
import com.kodgames.corgi.core.net.server.SimpleSSNettyInitializer;
import com.kodgames.corgi.core.net.server.SimpleServer;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

public class NetInitializer {
    private static Logger logger = LoggerFactory.getLogger(NetInitializer.class);
    private static NetInitializer instance = new NetInitializer();

    private SimpleClient simpleClient;
    private SimpleServer simpleServer;

    private NetInitializer() {
    }

    public static NetInitializer getInstance() {
        return instance;
    }

    @SuppressWarnings("rawtypes")
    public void init() {
        List<String> actionPackageList = new ArrayList<>();
        actionPackageList.add("com.kodgames.activityserver.action");
        List<Class> protocolClassList = new ArrayList<>();
        protocolClassList.add(PlatformProtocolsConfig.class);

        // 监听服务 接受Interface Game的连接
        ServerMessageInitializer ssMessageInitializer = new SSMessageInitializer(actionPackageList, protocolClassList, new SSConnectionHandler());
        try
        {
            ssMessageInitializer.initialize();
        }
        catch (Exception e)
        {
            logger.warn("server message initialize error exception={}", e);
        }

        SimpleSSNettyInitializer ssNettyInitializer = new SimpleSSNettyInitializer(ssMessageInitializer);
		simpleClient = new SimpleClient();
		simpleClient.initialize(ssNettyInitializer, ssMessageInitializer);
		simpleServer = new SimpleServer();
		simpleServer.initialize(ssNettyInitializer, ssMessageInitializer);

        ConnectionManager.getInstance().init(ssMessageInitializer);
        connectToManager();
    }

    // 连接到Manager
    public void connectToManager() {
        SimpleServerConfig manager = ServerConfigInitializer.getInstance().getManagerConfig();

        SocketAddress address = new InetSocketAddress(manager.getHost(), manager.getPort());
        ConnectionManager.getInstance().setManagerServerAddress(new InetSocketAddress(manager.getHost(), manager.getPort()));
		simpleClient.connectTo(address, 50);
        logger.info("trying connect to manager server with address {} : {}", manager.getHost(), manager.getPort());
    }

    // 开启对Game的监听
    public void openPortForGame(int port) {
		simpleServer.openPort(new InetSocketAddress(port));
        logger.info("openPortForGame : {}", port);
    }

    // 开启对GMT的监听
    public void openPortForGmt(int port, int serverId) {
//        GmtoolsHttpServer.getInstance().start("com.kodgames.replay.service.gmtools.handler",
//                port,
//                serverId,
//                ServerType.CLUB_SERVER,
//                true);
//        logger.info("openPortForGmt : {}", port);
    }
}