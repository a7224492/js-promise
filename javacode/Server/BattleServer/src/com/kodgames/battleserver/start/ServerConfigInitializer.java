package com.kodgames.battleserver.start;

import java.util.Map;

import com.kodgames.corgi.core.config.SimpleServerConfig;
import com.kodgames.corgi.core.util.config_utils.ServerParser;

public class ServerConfigInitializer
{
	private static ServerConfigInitializer instance = new ServerConfigInitializer();
	private static final String AREA_ID_KEY = "area_id";
	private static final String SERVER_TYPE_KEY = "server_type";
	private static final String MANAGER_HOST_KEY = "manager_host";
	private static final String MANAGER_PORT_KEY = "manager_port";

	private SimpleServerConfig managerConfig = null;
	private Integer areaId = null;
	private Integer serverType = null;

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
		managerConfig =
			new SimpleServerConfig(config.get(MANAGER_HOST_KEY), Integer.valueOf(config.get(MANAGER_PORT_KEY)));
	}

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
}
