package com.kodgames.client.start;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import com.kodgames.client.net.ClientDispatchNettyInitializer;
import com.kodgames.client.net.InterfaceConnectionHandler;
import com.kodgames.corgi.core.net.ServerMessageInitializer;
import com.kodgames.corgi.core.net.server.SimpleClient;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.message.protocol.PlatformProtocolsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetInitializer {
    private static Logger logger = LoggerFactory.getLogger(NetInitializer.class);
    private static NetInitializer instance = new NetInitializer();

    private SimpleClient interfaceClient;

    private NetInitializer() {
    }

    public static NetInitializer getInstance() {
        return instance;
    }

    public void init() throws Exception {
        List<String> packageNameList = new ArrayList<>();
        List<Class> protocolClassList = new ArrayList<>();

        packageNameList.add("com.kodgames.client.action");
        protocolClassList.add(PlatformProtocolsConfig.class);

        ServerMessageInitializer interfaceMsgInitializer = new ServerMessageInitializer(packageNameList, protocolClassList) {
            @Override
            protected void initMessages() throws Exception {
                super.initMessages();

                InterfaceConnectionHandler cbConnectionHandler = new InterfaceConnectionHandler();
                setConnectionHandler(cbConnectionHandler);
            }
        };

        interfaceMsgInitializer.initialize();
        ConnectionManager.getInstance().init(interfaceMsgInitializer);

        interfaceClient = new SimpleClient();
        interfaceClient.initialize(new ClientDispatchNettyInitializer(interfaceMsgInitializer), interfaceMsgInitializer);

        ConnectionManager.getInstance().init(interfaceMsgInitializer);

        ServerConfigInitializer config = ServerConfigInitializer.getInstance();
        SocketAddress address = new InetSocketAddress(config.getInterfaceHost(), config.getInterfacePort());

        for (int i = 0; i < config.getClientCount(); i++)
            connectToInterface(address);
    }

    public void connectToInterface(SocketAddress address) {
        interfaceClient.connectTo(address, 1);
    }
}
