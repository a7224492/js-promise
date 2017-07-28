package com.kodgames.corgi.core.util.config_utils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class DefaultParser extends Parser
{
	private static final int SERVER_HIERARCHY = 1;
	private static final int DATABASE_HIERARCHY = 2;

	//当前状态
	protected int hierarchyState;

	private ServerConfig current;

	private Map<Integer, ServerConfig> configs;

	//name->config
	private Map<String, ServerConfig.DbConfig> currentServerDbs;

	private Map<String, ServerConfig.DbConfig> defaultDbs;

	//通用配置
	private ServerConfig.DbConfig generalDbConfig;

	public DefaultParser(String path)
		throws FileNotFoundException
	{
		super(path);
	}

	public DefaultParser(InputStream inputStream)
	{
		super(inputStream);
	}

	@Override protected void init()
	{
		configs = new HashMap<>();
	}

	public Map<Integer, ServerConfig> getConfigs()
	{
		return configs;
	}

	@Override protected void onHierarchy(String hierarchy)
	{
		//终止上一个层级的解析
		if (hierarchyState == SERVER_HIERARCHY)
			finishServerHierarchy();

		//切换当前解析状态
		switch (hierarchy)
		{
			case "server":
				hierarchyState = SERVER_HIERARCHY;
				//开始一个新的层级解析
				current = new ServerConfig();
				currentServerDbs = new HashMap<>();
				break;
			case "database":
				hierarchyState = DATABASE_HIERARCHY;
				current = null;
				break;
		}

	}

	@Override protected void onProperty(String line)
	{
		String[] kv = line.split("=");
		Parser.trim(kv);
		switch (hierarchyState)
		{
			case DATABASE_HIERARCHY:
				if (defaultDbs == null)
					defaultDbs = new HashMap<>();
				if (kv[0].startsWith("db"))
					setDbProp(kv[0], kv[1], defaultDbs);
				else
				{
					if (generalDbConfig == null)
					{
						generalDbConfig = new ServerConfig.DbConfig();
					}
					setProperty(kv[0], kv[1], generalDbConfig);
				}
				break;
			case SERVER_HIERARCHY:
				if (kv[0].startsWith("db"))
					setDbProp(kv[0], kv[1], currentServerDbs);
				else
					setProperty(kv[0], kv[1], current);
				break;
		}
	}

	@Override protected void onEnd()
	{
		finishServerHierarchy();
	}

	private void setDbProp(String key, String value, Map<String, ServerConfig.DbConfig> map)
	{
		//db_propName_for_dbName的格式
		String[] str = key.split("_");
		Parser.trim(str);
		String dbName = str[3];
		String dbPropName = str[1];
		ServerConfig.DbConfig temp = map.get(dbName);
		if (temp == null)
		{
			temp = new ServerConfig.DbConfig();
			map.put(dbName, temp);
		}
		setProperty(dbPropName, value, temp);
	}

	//完成并结束当前server层级
	private void finishServerHierarchy()
	{
		if (current == null)
			return;
		currentServerDbs.entrySet().stream().forEach(entry -> {
			ServerConfig.DbConfig temp = entry.getValue();
			if (defaultDbs.containsKey(entry.getKey()))
			{
				ServerConfig.DbConfig defaultDb = defaultDbs.get(entry.getKey());
				for (Field f : temp.getClass().getDeclaredFields())
				{
					try
					{
						if (f.get(temp) == null)
						{
							String name = f.getName();
							Field defaultField = defaultDb.getClass().getDeclaredField(f.getName());
							if (defaultField.get(defaultDb) == null
								&& generalDbConfig.getClass().getDeclaredField(name).get(generalDbConfig) != null)
							{
								//通用配置
								setProperty(name,
									generalDbConfig.getClass().getDeclaredField(name).get(generalDbConfig),
									temp);
							}
							else
							{
								//特定name的通用配置
								setProperty(name, defaultField.get(defaultDb), temp);
							}
						}
					}
					catch (IllegalAccessException | NoSuchFieldException e)
					{
						e.printStackTrace();
					}
				}

			}

			current.addDbConfig(entry.getKey(), temp);
		});
		//把没有写在server标签下的db写进server中
		defaultDbs.keySet()
			.stream()
			.filter(name -> !currentServerDbs.containsKey(name))
			.forEach(name -> current.addDbConfig(name, defaultDbs.get(name)));
		configs.put(current.getType(), current);
	}
}
