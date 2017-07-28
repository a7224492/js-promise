package com.kodgames.corgi.core.util.config_utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lz on 2016/8/17.
 * 一些manager自定义配置存储
 */
public class CustomManagerConfig
{

	//area缺省配置 server_type->property_name->value
	private Map<Integer, Map<String, String>> defaultArea;
	//area_id->server_type->property_name->value
	private Map<Integer, Map<Integer, Map<String, String>>> areas;
	//area_id->db_type->prop_name->value
	private Map<String, Map<String, String>> dbProperties;

	public CustomManagerConfig()
	{
		this.defaultArea = new HashMap<>();
		this.areas = new HashMap<>();
		this.dbProperties = new HashMap<>();
	}

	public void setDbProperties(String key, String propName, String value)
	{
		Map<String, String> temp = dbProperties.get(key);
		if (temp == null)
		{
			temp = new HashMap<>();
			dbProperties.put(key, temp);
		}
		temp.put(propName, value);
	}

	public void setServerProperty(int area, int serverType, String propertyName, String value)
	{
		Map<Integer, Map<String, String>> temp = areas.get(area);
		if (temp == null)
		{
			temp = new HashMap<>();
			areas.put(area, temp);
		}

		Map<String, String> server = temp.get(serverType);
		if (server == null)
		{
			server = new HashMap<>();
			temp.put(serverType, server);
		}
		server.put(propertyName, value);
	}

	public void setDefaultAreaProperty(int serverType, String propertyName, String value)
	{
		Map<String, String> server = defaultArea.get(serverType);
		if (server == null)
		{
			server = new HashMap<>();
			defaultArea.put(serverType, server);
		}
		server.put(propertyName, value);
	}

	@Override public String toString()
	{
		return "CustomManagerConfig{" + "defaultArea=" + defaultArea +
			", areas=" + areas +
			'}';
	}

	public ServerConfig coverServerConfig(int area, int type, ServerConfig config)
	{
		//

		if (area != ManagerConfig.AREA_COMMON)
		{
			//area属性里面共通的db设置
			dbProperties.entrySet().stream().forEach(entry -> {
				//		temp.get(entry.getKey())
				ServerConfig.DbConfig temp = config.getDbConfigs().get(entry.getKey());
				if (temp == null)
				{
					temp = new ServerConfig.DbConfig();
					config.addDbConfig(entry.getKey(), temp);
				}
				Map<String, String> dbProperties = entry.getValue();
				for (Map.Entry<String, String> e : dbProperties.entrySet())
				{

					Parser.setProperty(e.getKey(), e.getValue(), temp);
				}
			});

			//area里面针对每一种server的设置
			Map<String, String> prop = defaultArea.get(type);
			if (prop != null)
				prop.entrySet().stream().forEach(entry -> {
					if (entry.getKey().startsWith("db"))
					{
						setDb(entry, config);
						return;
					}
					try
					{
						config.getClass().getDeclaredField(entry.getKey()).set(config, entry.getValue());
					}
					catch (IllegalAccessException | NoSuchFieldException e)
					{
						e.printStackTrace();
					}
				});
		}

		else
		{
			Map<Integer, Map<String, String>> typeP = areas.get(area);
			if (typeP == null)
				return config;
			Map<String, String> prop = typeP.get(type);
			if (prop != null)
				prop.entrySet().stream().forEach(entry -> {
					if (entry.getKey().startsWith("db"))
					{
						setDb(entry, config);
						return;
					}
					try
					{
						config.getClass().getDeclaredField(entry.getKey()).set(config, entry.getValue());
					}
					catch (IllegalAccessException | NoSuchFieldException e)
					{
						e.printStackTrace();
					}
				});
		}

		return config;
	}

	private void setDb(Map.Entry<String, String> entry, ServerConfig config)
	{
		String[] str = entry.getKey().split("_");
		String key = str[1];
		String prop = str[3];
		Parser.setProperty(prop, entry.getValue(), config.getDbConfigs().get(key));
	}
}

