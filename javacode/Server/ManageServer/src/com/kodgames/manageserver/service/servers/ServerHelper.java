package com.kodgames.manageserver.service.servers;

import java.util.Map;

import com.kodgames.corgi.core.constant.ServerType;
import com.kodgames.corgi.core.util.config_utils.ManagerConfig;
import com.kodgames.corgi.core.util.config_utils.ServerConfig;
import com.kodgames.corgi.core.util.config_utils.ServerParser;

/**
 * Created by lz on 2016/8/18. 一些配置解析之类的逻辑处理
 */
public class ServerHelper
{
	private static final String PORT_FOR_PURCHASE = "PurchasePort";
	private static final String PURCHASE_BILLINGKEY = "BillingKey";
	private static final String PORT_FOR_AGENT_PURCHASE = "AgentPurchasePort";
	private static ServerHelper instance;

	private ServerHelper()
	{
	}

	public static ServerHelper getInstance()
	{
		if (instance == null)
			instance = new ServerHelper();

		return instance;
	}

	// 基本配置模板
	private ManagerConfig config;
	private int port4Purchase;
	private String billingKey;
	private int port4AgentPurchase;

	public void init()
	{
		config = new ManagerConfig(Object.class.getResourceAsStream("/manager.conf"));
		initProperties("/manager.properties");
	}

	public int getPort4Purchase()
	{
		return port4Purchase;
	}

	public String getBillingKey()
	{
		return billingKey;
	}

	public int getManagerSocketPort()
	{
		return config.getConfig(ManagerConfig.AREA_COMMON, ServerType.MANAGER_SERVER).getListen_socket_for_server().getPort();
	}

	public int getManagerGmtPort()
	{
		return config.getConfig(ManagerConfig.AREA_COMMON, ServerType.MANAGER_SERVER).getListen_http_for_gmt().getPort();
	}

	public ServerConfig getConfigTemplate(int area, int type)
	{
		return config.getConfig(area, type);
	}

	public ServerConfig getAuthConfigTemplate()
	{
		return config.getConfig(ManagerConfig.AREA_COMMON, ServerType.AUTH_SERVER);
	}

	private void initProperties(String propertiesPath)
	{
		ServerParser sp = new ServerParser(Object.class.getResourceAsStream(propertiesPath));
		sp.read();

		Map<String, String> config = sp.getConfig();
		port4Purchase = Integer.valueOf(config.get(PORT_FOR_PURCHASE));
//		port4AgentPurchase = Integer.valueOf(config.get(PORT_FOR_AGENT_PURCHASE));
		billingKey = config.get(PURCHASE_BILLINGKEY);
	}

	public int getPort4AgentPurchase()
	{
		return port4AgentPurchase;
	}

	public void setPort4AgentPurchase(int port4AgentPurchase)
	{
		this.port4AgentPurchase = port4AgentPurchase;
	}
}
