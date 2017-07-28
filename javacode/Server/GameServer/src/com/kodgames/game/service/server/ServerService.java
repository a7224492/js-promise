package com.kodgames.game.service.server;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.global.GlobalService;
import com.kodgames.gmtools.httpserver.GmtoolsHttpServer;
import com.kodgames.message.proto.activity.ActivityProtoBuf;
import limax.zdb.Procedure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.constant.ServerType;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.corgi.core.util.config_utils.ServerConfig;
import com.kodgames.game.start.NetInitializer;
import com.kodgames.game.start.ServerConfigInitializer;
import com.kodgames.message.proto.server.ServerProtoBuf;
import com.kodgames.message.proto.server.ServerProtoBuf.SSGetLaunchInfoREQ;
import com.kodgames.message.proto.server.ServerProtoBuf.ServerConfigPROTO;
import xbean.RuntimeGlobalInfo;

/**
 * 负责服务器间消息处理的服务
 */
public class ServerService extends PublicService
{
	private static final long serialVersionUID = 3183880836623673530L;

	private static final Logger logger = LoggerFactory.getLogger(ServerService.class);

	// 服务器状态
	private static final int PHASE_PREPARE = 1;
	private static final int PHASE_RUNNING = 2;
	private int serverStatus = PHASE_PREPARE;

	//自身的启动配置
	private ServerConfig selfConfig;

	//当前在线battle
	private List<Integer> onlineBattle = Collections.synchronizedList(new ArrayList<>());

	private Connection managerConnection;
	private Connection clubConnection;
	private Connection replayConnection;
	private Connection activityServerConnection;

	/**
	 * 当BattleServer连接上时启动本地监听
	 * @param serverId
     */
	public void onBattleConnect(int serverId)
	{
		if (!onlineBattle.contains(serverId))
			onlineBattle.add(serverId);

		// 当与BattleServer连接成功后才开启本地监听服务
		// FIXME
		// 这里是否存在线程安全问题？当多个BattleServer同时连接上时(可以有一定的时间差), start()中会先启动网络连接(耗时操作)再更改状态
		// 并且serverStatus属性也不是volatile的
		if (serverStatus == PHASE_PREPARE)
			start();
	}

	public void onBattleDisconnect(Connection connection)
	{
		if (onlineBattle.contains(connection.getRemotePeerID()))
			onlineBattle.remove(Integer.valueOf(connection.getRemotePeerID()));
		else
			logger.error("battle server {} not exist in list", connection.getRemotePeerID());
	}
	
	public boolean isBattleActive(int battleId)
	{
		return onlineBattle.contains(battleId);
	}

	/**
	 * 保存与ClubServer的连接
	 * @param clubConnection
     */
	public void onClubConnect(Connection clubConnection)
	{
		this.clubConnection = clubConnection;
	}

	public void onClubDisconnect()
	{
		this.clubConnection = null;
	}
	
	public Connection getClubConnection()
	{
		return this.clubConnection;
	}

	/**
	 * 保存与ReplayServer的连接
	 */
	public void onReplayConnect(Connection replayConnection) {
		this.replayConnection = replayConnection;
	}

	public void onReplayDisconnect() {
		this.replayConnection = null;
	}

	public Connection getReplayConnection() {
		return this.replayConnection;
	}

	public void onActivityServerConnect(Connection connection)
	{
		this.activityServerConnection = connection;
	}

	public void onActivityServerDisconnect()
	{
		this.activityServerConnection = null;
	}

	public Connection getActivityServerConnection()
	{
		return this.activityServerConnection;
	}

	/**
	 * 当连上ManagerServer时请求LaunchInfo (由ManagerServer分配监听端口)
	 * @param connection
     */
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

	public List<Integer> getBattleIds()
	{
		return onlineBattle;
	}
	
	public int getServerId()
	{
		return selfConfig.getId();
	}

	public void onAcquireLaunchInfo(ServerConfig config)
	{
		selfConfig = config;
	}

	/**
	 * 在收到SSServerListSYNC时调用, 用于连接BattleServer和ClubServer
	 * @param list 服务器配置列表
     */
	public void onAcquireOnlineServersInfo(List<ServerConfigPROTO> list)
	{
		list.stream()
				.map(ServerConfig::fromProto)
				.filter(proto -> proto.getType() == ServerType.BATTLE_SERVER)
				.collect(Collectors.toList())
				.forEach(this::connectBattle);
		list.stream()
				.map(ServerConfig::fromProto)
				.filter(proto -> proto.getType() == ServerType.CLUB_SERVER)
				.collect(Collectors.toList())
				.forEach(this::connectClub);
		list.stream()
				.map(ServerConfig::fromProto)
				.filter(proto -> proto.getType() == ServerType.AGENT_SERVER)
				.collect(Collectors.toList())
				.forEach(this::connectAgent);
		list.stream()
				.map(ServerConfig::fromProto)
				.filter(proto -> proto.getType() == ServerType.REPLAY_SERVER)
				.collect(Collectors.toList())
				.forEach(this::connectReplay);
		list.stream()
			.map(ServerConfig::fromProto)
			.filter(proto -> proto.getType() == ServerType.ACTIVITY_SERVER)
			.collect(Collectors.toList())
			.forEach(this::connectActivityServer);
	}

	/**
	 * 获取了一个battle的信息
	 * @param battle 要连接的battle信息
	 */
	private void connectBattle(ServerConfig battle)
	{
		InetSocketAddress address = battle.getListen_socket_for_server().toSocketAddress();
		logger.info("new battle online | battle address is {}", address);
		//ConnectionManager.getInstance().addServerInfo(battle.getId(), address);
		NetInitializer.getInstance().connectToBattle(address);
	}

	/**
	 * 获取了一个club的信息
	 * @param club 要连接的battle信息
	 */
	private void connectClub(ServerConfig club)
	{
		InetSocketAddress address = club.getListen_socket_for_server().toSocketAddress();
		logger.info("new battle online | battle address is {}", address);
		//ConnectionManager.getInstance().addServerInfo(battle.getId(), address);
		NetInitializer.getInstance().connectToClub(address);
	}

	/**
	 * 获取了一个agent的信息
	 * @param agent 要连接的agent信息
	 */
	private void connectAgent(ServerConfig agent)
	{
		InetSocketAddress address = agent.getListen_socket_for_server().toSocketAddress();
		logger.info("new agent online | agent address is {}", address);
		NetInitializer.getInstance().connectToAgent(address);
	}

	/**
	 * 获取了一个replay的信息
	 * @param replay 要连接的replay信息
	 */
	private void connectReplay(ServerConfig replay)
	{
		InetSocketAddress address = replay.getListen_socket_for_server().toSocketAddress();
		logger.info("new replay online | replay address is {}", address);
		NetInitializer.getInstance().connectToReplay(address);
	}

	/**
	 * 连接活动服务器
	 * @param activityServer 活动服务器信息
	 */
	private void connectActivityServer(ServerConfig activityServer)
	{
		InetSocketAddress address = activityServer.getListen_socket_for_server().toSocketAddress();
		logger.info("new activityServer online | activityServer address is {}", address);
		NetInitializer.getInstance().connectToReplay(address);
	}

	/**
	 * manager主动发送移除下线的服务器
	 * @param id 被移除的id
	 */
	public void onManagerRemoveServer(int id)
	{
		if (onlineBattle.contains(id))
		{
			//TODO 添加逻辑处理
		}
	}

	/**
	 * 启动服务器，监听网络连接
	 */
	private void start()
	{
		// 监听InterfaceServer的连接
		NetInitializer.getInstance().openPortForInterface(selfConfig.getListen_socket_for_client().getPort());
		// 监听Gmt的连接
		NetInitializer.getInstance().openPortForGmt(selfConfig.getListen_http_for_gmt().getPort(), selfConfig.getId());
		serverStatus = PHASE_RUNNING;

		// 向ManagerServer注册自己
		ServerProtoBuf.SSRegisterServerREQ.Builder builder = ServerProtoBuf.SSRegisterServerREQ.newBuilder();
		builder.setServer(selfConfig.toProto());
		managerConnection.write(GlobalConstants.DEFAULT_CALLBACK, builder.build());

		Procedure.call(() -> {
			// 记录服务器启动时间
			GlobalService service = ServiceContainer.getInstance().getPublicService(GlobalService.class);
			RuntimeGlobalInfo globalInfo = service.getRuntimeBean();
			globalInfo.setServerStartupTime(System.currentTimeMillis());

			// 清空room_info表
			// room_info表没有采用mem_role_info的方式, 在运行时判断版本号, 原因有几点:
			// 1. room_info表数据量较小, 目前不会超过1万个, 删除该表耗时很少
			// 2. roomId检测时会查room_info表以检查该roomId是否在使用中, 所以不能存在旧数据
			List<Integer> ids = new ArrayList<>();
			// 遍历storage表
			table.Room_info.get().walk((key, value) -> {
				ids.add(key);
				return true;
			});

			for(int id : ids)
				table.Room_info.delete(id);

			return true;
		});
	}

	/**
	 * 重启GMT服务
	 */
	public void restartGmtServer()
	{
		logger.debug("restartGmtServer on port {}", selfConfig.getListen_http_for_gmt().getPort());
		GmtoolsHttpServer.getInstance().stop();
		try
		{
			Thread.sleep(5000);
		}
		catch (Exception e)
		{
			logger.warn("exception in sleep {}", e.getMessage());
		}
		NetInitializer.getInstance().openPortForGmt(selfConfig.getListen_http_for_gmt().getPort(), selfConfig.getId());
	}
}