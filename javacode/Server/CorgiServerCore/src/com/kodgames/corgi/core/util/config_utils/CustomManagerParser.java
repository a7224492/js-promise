package com.kodgames.corgi.core.util.config_utils;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class CustomManagerParser extends Parser
{

	private static final int SERVER_HIERARCHY = 1;
	private static final int AREA_HIERARCHY = 2;
	private static final int AREA_DEFAULT_HIERARCHY = 3;
	private static final int AREA_DATABASE = 4;

	//当前状态
	protected int hierarchyState;

	private boolean isDefaultArea = false;

	private int currentArea;

	private int currentServerType;

	private CustomManagerConfig configs;

	public CustomManagerParser(String path)
		throws FileNotFoundException
	{
		super(path);
	}

	public CustomManagerParser(InputStream input)
	{
		super(input);
	}

	@Override protected void init()
	{
		configs = new CustomManagerConfig();
	}

	public CustomManagerConfig getConfigs()
	{
		return configs;
	}

	protected void onHierarchy(String hierarchy)
	{
		log("new hierarchy  content is " + hierarchy);
		//终止上一个层级的解析
		finishServerHierarchy();

		//切换当前解析状态
		switch (hierarchy)
		{
			case "server":
				hierarchyState = SERVER_HIERARCHY;
				break;
			case "area":
				hierarchyState = AREA_HIERARCHY;
				isDefaultArea = false;
				break;
			case "area_default":
				hierarchyState = AREA_DEFAULT_HIERARCHY;
				isDefaultArea = true;
				break;
			case "database":
				hierarchyState = AREA_DATABASE;
				break;
		}

	}

	@Override protected void onProperty(String line)
	{
		log("new property  content is " + line);
		String[] kv = line.split("=");
		Parser.trim(kv);
		switch (hierarchyState)
		{
			case AREA_HIERARCHY:
				if ("area_id".equalsIgnoreCase(kv[0]))
					currentArea = Integer.valueOf(kv[1]);
				break;
			case SERVER_HIERARCHY:
				if ("type".equalsIgnoreCase(kv[0]))
					currentServerType = Integer.valueOf(kv[1]);
				if (isDefaultArea)
					configs.setDefaultAreaProperty(currentServerType, kv[0], kv[1]);
				else
					configs.setServerProperty(currentArea, currentServerType, kv[0], kv[1]);
				break;
			case AREA_DATABASE:
				String[] str = kv[0].split("_");
				Parser.trim(str);
				configs.setDbProperties(str[1], str[3], kv[1]);
				break;
		}
	}

	@Override protected void onEnd()
	{
		finishServerHierarchy();
	}

	//完成并结束当前server层级
	private void finishServerHierarchy()
	{
	}

	private void log(String content)
	{
//		System.out.println(content);
	}
}
