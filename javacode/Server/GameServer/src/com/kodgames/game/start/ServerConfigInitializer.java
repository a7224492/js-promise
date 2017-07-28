package com.kodgames.game.start;

import java.text.ParseException;
import java.util.Map;

import com.kodgames.corgi.core.config.SimpleServerConfig;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.core.util.config_utils.ServerParser;
import com.kodgames.game.service.activity.ActivityService;
import com.kodgames.game.service.button.ButtonService;
import com.kodgames.game.service.role.RoleService;

/**
 * 初始化服务器配置信息
 */
public class ServerConfigInitializer
{
	private static ServerConfigInitializer instance = new ServerConfigInitializer();
	private static final String AREA_ID_KEY = "area_id";
	private static final String SERVER_TYPE_KEY = "server_type";
	private static final String SERVER_ID_KEY = "id";
	private static final String MANAGER_HOST_KEY = "manager_host";
	private static final String MANAGER_PORT_KEY = "manager_port";
	private static final String ENABLE_SIGN_CHECK = "enable_sign_check";		// 是否检查客户端sign

	private SimpleServerConfig managerConfig = null;
	private Integer areaId = null;
	private Integer serverType = null;
	private Integer serverId = null;
	private int defaultRoomCardNum = 0;
	private boolean enableSignCheck = true;

	private ServerConfigInitializer()
	{
	}

	public static ServerConfigInitializer getInstance()
	{
		return instance;
	}

	public void init(String configPath)
	{
		ServerParser sp = new ServerParser(Object.class.getResourceAsStream(configPath));
		sp.read();
		Map<String, String> config = sp.getConfig();
		areaId = Integer.valueOf(config.get(AREA_ID_KEY));
		serverType = Integer.valueOf(config.get(SERVER_TYPE_KEY));
		serverId = Integer.valueOf(config.get(SERVER_ID_KEY));
		managerConfig = new SimpleServerConfig(config.get(MANAGER_HOST_KEY), Integer.valueOf(config.get(MANAGER_PORT_KEY)));
		if (config.containsKey(ENABLE_SIGN_CHECK))
			enableSignCheck = Boolean.valueOf(config.get(ENABLE_SIGN_CHECK));
	}

	public void initProperties(String propertiesPath) throws ParseException
	{
		ServerParser sp = new ServerParser(Object.class.getResourceAsStream(propertiesPath));
		sp.read();
		Map<String, String> config = sp.getConfig();
		defaultRoomCardNum = Integer.valueOf(config.get("defaultRoomCardNum"));

		ServiceContainer.getInstance().getPublicService(RoleService.class).setDefaultRoomCardNum(defaultRoomCardNum);
		ServiceContainer.getInstance().getPublicService(ActivityService.class).initConfig(config);
		ServiceContainer.getInstance().getPublicService(ActivityService.class).initTurntableActivity(config);;
		ServiceContainer.getInstance().getPublicService(ButtonService.class).initConfig(config);
	}

	/**
	 * ManagerServer的地址
	 * @return
     */
	public SimpleServerConfig getManagerConfig()
	{
		return managerConfig;
	}

	public Integer getAreaId()
	{
		return areaId;
	}

	public Integer getServerType()
	{
		return serverType;
	}

	public Integer getServerId()
	{
		return serverId;
	}

	public int getDefaultRoomCardNum()
	{
		return this.defaultRoomCardNum;
	}

	public boolean getEnableSignCheck() { return this.enableSignCheck; }
}
