package com.kodgames.authserver.start;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.kodgames.authserver.auth.wx.WxParam;
import com.kodgames.authserver.config.AuthWxConfig;
import com.kodgames.corgi.core.config.SimpleServerConfig;
import com.kodgames.corgi.core.util.config_utils.ServerParser;

public class ServerConfigInitializer
{
	private static ServerConfigInitializer instance = new ServerConfigInitializer();
	private static final String AREA_ID_KEY = "area_id";
	private static final String SERVER_TYPE_KEY = "server_type";
	private static final String SERVER_ID_KEY = "id";
	private static final String MANAGER_HOST_KEY = "manager_host";
	private static final String MANAGER_PORT_KEY = "manager_port";
	private static final String LISTEN_INTERFACE_KEY = "listen_socket_for_client";
	private static final String LISTEN_GMT_KEY = "listen_http_for_gmt";
	private static final String WXAPPID = "wxappid";
	private static final String WXSECRET = "wxsecret";
	private static final String WXSPLIT = "_";
	private static final String MERGE_ACCOUNT_KEY = "merge_account";
	private static final String MERGE_ACCOUNT_VALUE_OPEN = "1";

	private SimpleServerConfig managerConfig = null;
	private Integer areaId = null;
	private Integer serverType = null;
	private Integer serverId = null;
	private String listenInterface = null;
	private String listenGmt = null;
	private boolean canMergeAccount = false;

	private Map<Integer, WxParam> wxParamMap = new HashMap<>();

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
		listenInterface = config.get(LISTEN_INTERFACE_KEY);
		listenGmt = config.get(LISTEN_GMT_KEY);
	}

	public void initProperties(String propertiesPath)
	{
		ServerParser sp = new ServerParser(Object.class.getResourceAsStream(propertiesPath));
		sp.read();
		Map<String, String> config = sp.getConfig();

		// 解析微信参数
		for (Map.Entry<String, String> entry : config.entrySet())
		{
			String[] keys = entry.getKey().split(WXSPLIT);
			if (keys == null || keys.length != 2)
				continue;

			String paramType = keys[0];
			String value = entry.getValue();
			if (paramType.equals(WXAPPID))
			{
				int appCode = Integer.valueOf(keys[1]);
				WxParam wxParam = getAndInitWxParam(appCode);
				wxParam.setAppid(value);
			}
			else if (paramType.equals(WXSECRET))
			{
				int appCode = Integer.valueOf(keys[1]);
				WxParam wxParam = getAndInitWxParam(appCode);
				wxParam.setSecret(value);
			}
		}

		// 初始化账号合并开关
		canMergeAccount = config.get(MERGE_ACCOUNT_KEY).equals(MERGE_ACCOUNT_VALUE_OPEN);
	}

	public void initJson(String propertiesPath) throws IOException
	{
		AuthWxConfig authWxConfig = AuthWxConfig.getInstance();
		authWxConfig.parse();
		
	}
	private WxParam getAndInitWxParam(int appCode)
	{
		wxParamMap.putIfAbsent(appCode, new WxParam());

		return wxParamMap.get(appCode);
	}

	public boolean canMergeAccount()
	{
		return canMergeAccount;
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

	public Integer getServerId()
	{
		return serverId;
	}

	public String getListenInterface()
	{
		return listenInterface;
	}

	public String getListenGmt()
	{
		return listenGmt;
	}

	public WxParam getWxParam(int appCode)
	{
		return this.wxParamMap.get(appCode);
	}
}
