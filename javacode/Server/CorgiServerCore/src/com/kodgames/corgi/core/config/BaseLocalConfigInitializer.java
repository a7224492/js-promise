package com.kodgames.corgi.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseLocalConfigInitializer
{
	static private Logger logger = LoggerFactory.getLogger(BaseLocalConfigInitializer.class);

	public void init(BaseServerConfig config)
		throws Exception
	{
		init(new HashMap<String, String>(), config);
	}

	public void init(Map<String, String> retArguments, BaseServerConfig config)
		throws Exception
	{
		// 加载数据库配置
		InputStream propertiesStream = BaseLocalConfigInitializer.class.getResourceAsStream("/baseInfo.properties");

		Properties properties = new Properties();
		try
		{
			properties.load(propertiesStream);
		}
		catch (IOException e)
		{
			logger.error("Failed load base local config from /baseInfo.properties");
			throw e;
		}

		try
		{
			String serverName = properties.getProperty("ServerName").trim();
			String MSHostName = properties.getProperty("MSHostName").trim();
			int MSPort = Integer.parseInt(properties.getProperty("MSPort").trim());
			InetSocketAddress manageServerAddress = new InetSocketAddress(MSHostName, MSPort);
			if (serverName == null)
				throw new Exception("The server name can't be null in the baseInfo.properties");

			config.setServerName(serverName);
			config.setManageServerAddress(manageServerAddress);

			{
				retArguments.put("ServerName", serverName);
				retArguments.put("MSHostName", MSHostName);
				retArguments.put("MSPort", String.valueOf(MSPort));
				retArguments.put("Mode",
					properties.getProperty("Mode") == null ? new String() : properties.getProperty("Mode"));
			}

			logger.info("ServerName:{}  ManageServerAddress:{}  ManageServerPort:{}", serverName, MSHostName, MSPort);
			logger.info("Success to load local config!");
		}
		catch (Exception e)
		{
			logger.error("Illegal content in the baseInfo.properties");
			throw e;
		}
	}

}
