package com.kodgames.replay.service.server;

import java.util.List;
import com.kodgames.replay.start.NetInitializer;
import com.kodgames.replay.start.ServerConfigInitializer;
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

public class ServerService extends PublicService {
    private static final long serialVersionUID = 7686422362539933510L;
    private Logger logger = LoggerFactory.getLogger(ServerService.class);

    // 自身的启动配置
    private ServerConfig self;

    private Connection managerConnection;
    private Connection gameConnection;

    public void onGameConnect(Connection gameConnection) {
        this.gameConnection = gameConnection;
    }

    public void onGameDisconnect() {
        this.gameConnection = null;
    }

    public void onManagerConnect(Connection connection) {
        managerConnection = connection;

        // 连接上Manager后, 请求自己的启动信息 (由Manager分配监听端口)
        SSGetLaunchInfoREQ.Builder builder = SSGetLaunchInfoREQ.newBuilder();
        ServerConfigPROTO.Builder configBuilder = ServerConfigPROTO.newBuilder();
        configBuilder.setArea(ServerConfigInitializer.getInstance().getAreaId());
        configBuilder.setType(ServerConfigInitializer.getInstance().getServerType());
        configBuilder.setId(ServerConfigInitializer.getInstance().getServerId());
        builder.setServer(configBuilder.build());
        connection.write(GlobalConstants.DEFAULT_CALLBACK, builder.build());
    }

    // 与Manager断开连接后, 尝试重新连接
    public void onManagerDisconnect(Connection connection) {
        managerConnection = null;
        NetInitializer.getInstance().connectToManager();
    }

    public Connection getGameConnection() {
        return gameConnection;
    }

    public int getServerId() {
        return self.getId();
    }

    // 收到服务器列表
    public void onAcquireOnlineServersInfo(List<ServerConfigPROTO> list) {
    }

    // manager通知服务器下线
    public void onManagerRemoveServer(int id) {
    }

    public void startSelf(ServerConfig config) {
        AddressConfig forGame = config.getListen_socket_for_server();
        if (forGame == null) {
            logger.error("listen_socket_for_server {} error, getLanchInfo again!!!!!!!!!!!!!!", forGame);
            this.getLaunchInfo();
            return;
        }

        self = config;
        registerToManagerServer();
    }

    private void getLaunchInfo() {
        SSGetLaunchInfoREQ.Builder launchBuilder = SSGetLaunchInfoREQ.newBuilder();
        launchBuilder.setServer(ServerConfigPROTO.newBuilder().setArea(1).setType(ServerType.CLUB_SERVER).build());
        this.managerConnection.write(GlobalConstants.DEFAULT_CALLBACK, launchBuilder.build());
    }

    // 启动监听服务器
    private void registerToManagerServer() {
        NetInitializer.getInstance().openPortForGame(self.getListen_socket_for_server().getPort());
        NetInitializer.getInstance().openPortForGmt(self.getListen_http_for_gmt().getPort(), self.getId());

        ServerProtoBuf.SSRegisterServerREQ.Builder builder = ServerProtoBuf.SSRegisterServerREQ.newBuilder();
        builder.setServer(self.toProto());
        managerConnection.write(GlobalConstants.DEFAULT_CALLBACK, builder.build());
    }
}