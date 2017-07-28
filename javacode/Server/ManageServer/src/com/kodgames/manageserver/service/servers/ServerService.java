package com.kodgames.manageserver.service.servers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.constant.ServerType;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.corgi.core.util.config_utils.AddressConfig;
import com.kodgames.corgi.core.util.config_utils.ServerConfig;
import com.kodgames.gmtools.httpserver.GmtoolsHttpServer;
import com.kodgames.message.proto.server.ServerProtoBuf.SSGetLaunchInfoRES;
import com.kodgames.message.proto.server.ServerProtoBuf.SSRegisterServerREQ;
import com.kodgames.message.proto.server.ServerProtoBuf.SSServerDisconnectSYNC;
import com.kodgames.message.proto.server.ServerProtoBuf.SSServerListSYNC;
import com.kodgames.message.proto.server.ServerProtoBuf.ServerConfigPROTO;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

import xbean.IntValue;
import xbean.PortBase;

/**
 * 服务器管理 game server 玩家信息管理等 battle server 打牌服务器 interface server 管理client连接并转发消息
 * 
 * @author lz
 */
public class ServerService extends PublicService
{

	private static final int PREPARE = 1;
	private static final int RUNNING = 2;

	private static final long serialVersionUID = -8272027997288462700L;

	private static final Logger logger = LoggerFactory.getLogger(ServerService.class);

	// 存放所有存活的服务器节点 id -> remote
	private final Map<Integer, Connection> servers = new ConcurrentHashMap<>();

	// 服务器对应的配置 注册时由注册服务器传入
	private final Map<Integer, ServerConfig> serverConfig = new ConcurrentHashMap<>();

	private final Map<Integer, Integer> statusMap = new ConcurrentHashMap<>();

	private final Map<String, List<Integer>> ipUnavailableMap = new ConcurrentHashMap<>();

	// Game服务器节点
	private Connection gameConnection;
	
	//Agent服务器节点
	private Connection agentConnection;

	// Replay服务器节点
	private Connection replayConnection;
	
	public Connection getGameConnection()
	{
		return gameConnection;
	}
	
	public Connection getAgentConnection()
	{
		return agentConnection;
	}

	public Connection getReplayConnection() {
		return replayConnection;
	}

	public void registerServer(Connection connection, SSRegisterServerREQ req)
	{
		// 设置gameServer的链接
		if(req.getServer().getType() == ServerType.GAME_SERVER)
			gameConnection = connection;
		
		//设置agentServer的连接
		if(req.getServer().getType() == ServerType.AGENT_SERVER)
			agentConnection = connection;

		// 设置replayServer的连接
		if (req.getServer().getType() == ServerType.REPLAY_SERVER)
			replayConnection = connection;
		
		int serverId = req.getServer().getId();
		// connection.setServerId(serverId);

		logger.info("a server register, id is " + serverId + " , type is " + connection.getConnectionType());

		statusMap.put(serverId, RUNNING);
		ServerConfig info = serverConfig.get(serverId);
		// 添加到gmt
		if (info.getAddress_http_for_gmt() != null && info.getAddress_http_for_gmt().getPort() != 0)
			addGmtList(info);
		// 通知其他服务器
		broadcastServerOnline(info);
	}

	// 将新加入服务器添加到gmt转发列表中
	private void addGmtList(ServerConfig info)
	{
		int id = info.getId();
		GmtoolsHttpServer.getInstance().addServerList(id, info.getAddress_http_for_gmt().getAddressString(), info.getType());

		GmtoolsHttpServer.getInstance().addServerList(id,
			info.getAddress_http_for_gmt().getAddressString(),
			info.getType());
	}

	// 处理获取启动信息的入口
	public void getLaunchInfo(Connection connection, ServerConfigPROTO customConfig, int callback)
	{
		int type = customConfig.getType();
		int area = customConfig.getArea();

		ServerHelper helper = ServerHelper.getInstance();
		ServerConfig template =
			ServerType.AUTH_SERVER == type ? helper.getAuthConfigTemplate() : helper.getConfigTemplate(area, type);

		ServerConfig res = buildLaunchInfo(customConfig.getArea(),
			connection.getNettyNode().getAddress().getHostString(),
			ServerConfig.fromProto(customConfig),
			template);

		SSGetLaunchInfoRES.Builder builder = SSGetLaunchInfoRES.newBuilder();
		if (res != null)
		{
			statusMap.put(res.getId(), PREPARE);
			serverConfig.put(res.getId(), res);
			servers.put(res.getId(), connection);

			builder.setResult(PlatformProtocolsConfig.SS_GET_LAUNCH_INFO_SUCCESS);
			builder.setServer(res.toProto());
			connection.setRemotePeerID(res.getId());
		}
		else
		{
			builder.setResult(PlatformProtocolsConfig.SS_GET_LAUNCH_INFO_FAILED);
			builder.setServer(customConfig);
		}

		ConnectionManager.getInstance().addToServerConnections(connection);

		connection.write(callback, builder.build());

		SSServerListSYNC.Builder serverBuilder = SSServerListSYNC.newBuilder();
		buildServerList(serverBuilder);
		connection.write(GlobalConstants.DEFAULT_CALLBACK, serverBuilder.build());
	}

	/**
	 * @param area 区域
	 * @param ip 链接的ip
	 * @param custom 上传的server的自定义配置
	 * @param config 根据area和type获得的模板配置
	 */
	private ServerConfig buildLaunchInfo(int area, String ip, ServerConfig custom, ServerConfig config)
	{
		logger.info("build launch info ip is {} type {}", ip, custom.getType());
		if (!ipUnavailableMap.containsKey(ip))
			ipUnavailableMap.put(ip, new ArrayList<>());

		// =============================id相关======================
		int id = 0;
		if (custom.getId() != 0)
		{
			if (!statusMap.containsKey(custom.getId()) || statusMap.get(custom.getId()) != RUNNING)
			{
				// 使用现成的id
				id = custom.getId();
			}
			else
			{
				// TODO 直接中断流程 回个错误码
			}
		}
		else
		{
			// ===========生成id
			int key = (area << 8) + custom.getType();
			IntValue idInt = table.Server_id.update(key);
			if (null == idInt)
			{
				idInt = table.Server_id.insert(key);
				idInt.setId(custom.getId());
			}

			do
			{
				id = idInt.getId() + 1;
				idInt.setId(id);
			} while (!(statusMap.get((key << 16) + id) == null || statusMap.get((key << 16) + id) != RUNNING));
			id = (key << 16) + id;
			// ==========生成id结束
		}
		// ===============================id相关结束==================

		// =================端口生成
		switch (custom.getType())
		{
			case ServerType.BATTLE_SERVER:
				// 从某个值开始分配端口

				AddressConfig bSocket4Server = checkAndBuildPort(ip,
					area,
					custom.getListen_socket_for_server(),
					config.getListen_socket_for_server(),
					custom.getType(),
					Const.PORT_FOR_SOCKET_SERVER);
				if (bSocket4Server == null)
				{
					return null;
				}

				custom.setListen_socket_for_server(bSocket4Server.getAddressString());

				AddressConfig bSocket4Client = checkAndBuildPort(ip,
					area,
					custom.getAddress_socket_for_client(),
					config.getListen_socket_for_client(),
					custom.getType(),
					Const.PORT_FOR_SOCKET_CLIENT);
				if (bSocket4Client == null)
				{
					return null;
				}

				custom.setListen_socket_for_client(bSocket4Client.getAddressString());
				break;

			case ServerType.GAME_SERVER:
				// 从某个值开始分配端口
				AddressConfig gS4Client = checkAndBuildPort(ip,
					area,
					custom.getListen_socket_for_client(),
					config.getListen_socket_for_client(),
					custom.getType(),
					Const.PORT_FOR_SOCKET_CLIENT);
				if (gS4Client == null)
				{
					return null;
				}

				custom.setListen_socket_for_client(gS4Client.getAddressString());

				AddressConfig gH4Gmt = checkAndBuildPort(ip,
					area,
					custom.getListen_http_for_gmt(),
					config.getAddress_http_for_gmt(),
					custom.getType(),
					Const.PORT_FOR_HTTP_GMT);
				if (gH4Gmt == null)
				{
					return null;
				}
				custom.setListen_http_for_gmt(gH4Gmt.getAddressString());
				break;
			case ServerType.INTERFACE_SERVER:
				// 固定端口 每个物理机上启动一个实例
				AddressConfig is4Client = checkAndBuildPort(ip,
					area,
					custom.getListen_socket_for_client(),
					config.getListen_socket_for_client(),
					custom.getType(),
					Const.PORT_FOR_SOCKET_CLIENT);
				if (is4Client == null)
				{
					return null;
				}
				custom.setListen_socket_for_client(is4Client.getAddressString());

				AddressConfig iws4Client = checkAndBuildPort(ip,
					area,
					custom.getListen_web_socket_for_client(),
					config.getListen_web_socket_for_client(),
					custom.getType(),
					Const.PORT_FOR_WEB_SOCKET_CLIENT);
				if (iws4Client == null)
				{
					return null;
				}
				custom.setListen_web_socket_for_client(iws4Client.getAddressString());
				break;
			case ServerType.AUTH_SERVER:
				// 固定端口 只需要分配id 根据本地配置进行设置TODO 将配置切换至本地配置文件
				AddressConfig ah4Client = checkAndBuildPort(ip,
					area,
					custom.getAddress_http_for_client(),
					config.getListen_http_for_client(),
					custom.getType(),
					Const.PORT_FOR_HTTP_CLIENT);
				if (ah4Client == null)
				{
					return null;
				}
				custom.setListen_http_for_client(ah4Client.getAddressString());

				AddressConfig ah4Server = checkAndBuildPort(ip,
					area,
					custom.getListen_http_for_server(),
					config.getListen_http_for_server(),
					custom.getType(),
					Const.PORT_FOR_HTTP_SERVER);
				if (ah4Server == null)
				{
					return null;
				}
				custom.setListen_http_for_server(ah4Server.getAddressString());
				break;
			case ServerType.CLUB_SERVER:
				//固定端口 只需要分配id 根据本地配置进行设置TODO 将配置切换至本地配置文件
				AddressConfig cl4Client = checkAndBuildPort(ip, 
					area, 
					custom.getListen_socket_for_client(), 
					config.getListen_socket_for_client(), 
					custom.getType(), 
					Const.PORT_FOR_SOCKET_CLIENT);
				if (cl4Client == null)
				{
					return null;
				}
				custom.setListen_socket_for_client(cl4Client.getAddressString());

				AddressConfig cl4Server = checkAndBuildPort(ip, 
					area, 
					custom.getListen_socket_for_server(), 
					config.getListen_socket_for_server(), 
					custom.getType(), 
					Const.PORT_FOR_SOCKET_SERVER);
				if (cl4Server == null)
				{
					return null;
				}
				custom.setListen_socket_for_server(cl4Server.getAddressString());

				AddressConfig cl4Gmt = checkAndBuildPort(ip, 
					area, 
					custom.getListen_http_for_gmt(), 
					config.getListen_http_for_gmt(), 
					custom.getType(),
					 Const.PORT_FOR_HTTP_GMT);
				if (cl4Gmt == null) 
				{
					return null;
				}
				custom.setListen_http_for_gmt(cl4Gmt.getAddressString());					
				break;

			case ServerType.AGENT_SERVER:
				//固定端口 只需要分配id 根据本地配置进行设置TODO 将配置切换至本地配置文件
				AddressConfig ag4Client = checkAndBuildPort(ip,
						area,
						custom.getListen_socket_for_client(),
						config.getListen_socket_for_client(),
						custom.getType(),
						Const.PORT_FOR_SOCKET_CLIENT);
				if (ag4Client == null)
				{
					return null;
				}
				custom.setListen_socket_for_client(ag4Client.getAddressString());

				AddressConfig ag4Server = checkAndBuildPort(ip,
						area,
						custom.getListen_socket_for_server(),
						config.getListen_socket_for_server(),
						custom.getType(),
						Const.PORT_FOR_SOCKET_SERVER);
				if (ag4Server == null)
				{
					return null;
				}
				custom.setListen_socket_for_server(ag4Server.getAddressString());

				AddressConfig ag4Gmt = checkAndBuildPort(ip,
						area,
						custom.getListen_http_for_gmt(),
						config.getListen_http_for_gmt(),
						custom.getType(),
						Const.PORT_FOR_HTTP_GMT);
				if (ag4Gmt == null)
				{
					return null;
				}
				custom.setListen_http_for_gmt(ag4Gmt.getAddressString());
				break;

			case ServerType.REPLAY_SERVER:
				AddressConfig rp4Client = checkAndBuildPort(ip,
						area,
						custom.getListen_socket_for_client(),
						config.getListen_socket_for_client(),
						custom.getType(),
						Const.PORT_FOR_SOCKET_CLIENT);
				if (rp4Client == null)
				{
					return null;
				}
				custom.setListen_socket_for_client(rp4Client.getAddressString());

				AddressConfig rp4Server = checkAndBuildPort(ip,
						area,
						custom.getListen_socket_for_server(),
						config.getListen_socket_for_server(),
						custom.getType(),
						Const.PORT_FOR_SOCKET_SERVER);
				if (rp4Server == null)
				{
					return null;
				}
				custom.setListen_socket_for_server(rp4Server.getAddressString());

				AddressConfig rp4Gmt = checkAndBuildPort(ip,
						area,
						custom.getListen_http_for_gmt(),
						config.getListen_http_for_gmt(),
						custom.getType(),
						Const.PORT_FOR_HTTP_GMT);
				if (rp4Gmt == null)
				{
					return null;
				}
				custom.setListen_http_for_gmt(rp4Gmt.getAddressString());
				break;

			case ServerType.ACTIVITY_SERVER:
				AddressConfig as4Client = checkAndBuildPort(ip,
					area,
					custom.getListen_socket_for_client(),
					config.getListen_socket_for_client(),
					custom.getType(),
					Const.PORT_FOR_SOCKET_CLIENT);
				if (as4Client == null)
				{
					return null;
				}
				custom.setListen_socket_for_client(as4Client.getAddressString());

				AddressConfig as4Server = checkAndBuildPort(ip,
					area,
					custom.getListen_socket_for_server(),
					config.getListen_socket_for_server(),
					custom.getType(),
					Const.PORT_FOR_SOCKET_SERVER);
				if (as4Server == null)
				{
					return null;
				}
				custom.setListen_socket_for_server(as4Server.getAddressString());

				AddressConfig as4Gmt = checkAndBuildPort(ip,
					area,
					custom.getListen_http_for_gmt(),
					config.getListen_http_for_gmt(),
					custom.getType(),
					Const.PORT_FOR_HTTP_GMT);
				if (as4Gmt == null)
				{
					return null;
				}
				custom.setListen_http_for_gmt(as4Gmt.getAddressString());
				break;
		}
		custom.setId(String.valueOf(id));

		return custom;
	}
	
	private AddressConfig buildAddressConfig(String ip, int area, int serverType, int portType, AddressConfig template)
	{
		AddressConfig address = new AddressConfig();
		address.copyFrom(template);
		address.setHost(ip);

		if (!template.isAddress() || ipUnavailableMap.get(ip).contains(template.getPort()))
		{
			// 端口为范围配置, 或者配置端口被占用
			String tempKey = ip + area + serverType + portType;
			// 根据db存储的base值分配port
			PortBase tpd = table.Port_table.update(tempKey);
			if (tpd == null)
			{
				tpd = table.Port_table.insert(tempKey);
				tpd.setIp(ip);
				tpd.setArea(area);
				tpd.setServerType(serverType);
				tpd.setPortType(portType);
				tpd.setPort(template.getPortStart());
			}
			int tempPort;
			do
			{
				tempPort = tpd.getPort();
				if (tempPort > template.getPortEnd())
				{
					logger.error("buildAddressConfig serverType {} portType {} all port is used portRange {}--{}",
						serverType,
						portType,
						template.getPortStart(),
						template.getPortEnd());
					return null;
				}
				tpd.setPort(++tempPort);
			} while (ipUnavailableMap.get(ip).contains(tempPort));

			address.setPort(tempPort);
		}

		return address;
	}

	/**
	 * 检测或者生成一个port
	 * 
	 * @param ip 当前要生成的port所在ip
	 * @param area 分区
	 * @param address 自定义配置同时也是生成的最后结果
	 * @param template 配置模板
	 * @param serverType 服务器类型
	 * @param portType 监听端口的使用类型
	 * @return
	 */
	private AddressConfig checkAndBuildPort(String ip, int area, AddressConfig address, AddressConfig template,
		int serverType, int portType)
	{
		if (address != null && address.isAddress()) // 配置了自己的ip和端口
		{
			if (address.isValidConf())
			{
				return address;
			}
			// 范围检测
			if (address.getPort() < template.getPortStart() || address.getPort() > template.getPortEnd())
			{
				logger.error("Server type {} ip {} port type {} conf port {} is not in range {}----{} ",
					serverType,
					ip,
					portType,
					address.getPort(),
					template.getPortStart(),
					template.getPortEnd());
				return this.buildAddressConfig(ip, area, serverType, portType, template);
			}
			// 占用检测
			if (ipUnavailableMap.get(ip).contains(address.getPort()))
			{
				logger.error("Server type {} ip {} port type {} conf port {} is used ",
					serverType,
					ip,
					portType,
					address.getPort());
				return this.buildAddressConfig(ip, area, serverType, portType, template);
			}
			else
			{
				// 没被占用就添加进去
				ipUnavailableMap.get(ip).add(address.getPort());
			}

			return address;
		}

		return this.buildAddressConfig(ip, area, serverType, portType, template);
	}

	// launch的时候在server本身的配置发出去之后发
	private boolean buildServerList(SSServerListSYNC.Builder builder)
	{
		List<ServerConfigPROTO> temp = serverConfig.entrySet()
			.stream()
			.filter(e -> statusMap.get(e.getValue().getId()) != null)
			.filter(e -> statusMap.get(e.getValue().getId()) == RUNNING)
			.map(Map.Entry::getValue)
			.map(ServerConfig::toProto)
			.collect(Collectors.toList());
		builder.addAllList(temp);
		return temp.size() != 0;
	}

	/**
	 * 服务器断线
	 */
	public void disconnect(Connection node)
	{
		int id = node.getRemotePeerID();
		logger.info("server disconnect, address is {} id", node.getNettyNode().getAddress(), id);

		broadcastServerOffline(serverConfig.get(id));
		GmtoolsHttpServer.getInstance().removeServerList(id);

		servers.remove(id);
		serverConfig.remove(id);
		statusMap.remove(id);
	}

	// 注册服务器时向服务器广播
	private void broadcastServerOnline(ServerConfig info)
	{
		// System.out.println(info);

		SSServerListSYNC.Builder builder = SSServerListSYNC.newBuilder();
		builder.addList(info.toProto());
		ConnectionManager.getInstance().broadcastToAllServers(0, builder.build());
	}

	// 注册服务器时向服务器广播
	private void broadcastServerOffline(ServerConfig info)
	{
		if (null == info)
		{
			return;
		}

		SSServerDisconnectSYNC.Builder builder = SSServerDisconnectSYNC.newBuilder();
		builder.setId(info.getId());
		ConnectionManager.getInstance().broadcastToAllServers(0, builder.build());
	}

	/**
	 * 在传入的map中填入当前server的状态信息 { result:1, info:[ { id:serverId, type:type(int), ip:serverIp, port4SocketClient:1234,
	 * port4WebSocketClient:4567, port4Server:123456, port4Gmt:4578 } ] }
	 */
	public void getServersStatus(Map<String, Object> container)
	{
		// List<Map<String, Object>> info = new ArrayList<>();
		// for (Map.Entry<Integer, ServerConfigPROTO> integerServerConfigPROTOEntry : serverConfig.entrySet())
		// {
		// ServerConfigPROTO scp = integerServerConfigPROTOEntry.getValue();
		// Map<String, Object> temp = new HashMap<>();
		// temp.put("id", scp.getServerID());
		// temp.put("type", scp.getServerType());
		// temp.put("ip", scp.getIpForServer());
		// if (ServerType.INTERFACE_SERVER == scp.getServerType())
		// {
		// temp.put("port4SocketClient", scp.getPortForSocketClient());
		// temp.put("port4WebSocketClient", scp.getPortForWebSocketClient());
		// }
		// temp.put("port4Server", scp.getPortForServer());
		// temp.put("port4Gmt", scp.getIpForGMTools());
		// info.add(temp);
		// }
		// container.put("info", info);
	}

	/**
	 * 本地常量
	 */
	private class Const
	{
		static final int PORT_FOR_SOCKET_SERVER = 0x01;
		static final int PORT_FOR_HTTP_SERVER = 0x02;
		static final int PORT_FOR_SOCKET_CLIENT = 0x21;
		static final int PORT_FOR_HTTP_CLIENT = 0x22;
		static final int PORT_FOR_WEB_SOCKET_CLIENT = 0x23;
		static final int PORT_FOR_HTTP_GMT = 0x31;
	}

}
