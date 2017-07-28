package com.kodgames.manageserver.start;

import com.kodgames.manageserver.service.servers.ServerHelper;

/**
 * Created by lz on 2016/8/1. 本地配置 任何时间可以直接进行操作 不需要初始化
 */
public class LocalConfigInitializer
{

	private static LocalConfigInitializer instance;

	private LocalConfigInitializer()
	{
	}

	public static LocalConfigInitializer getInstance()
	{
		if (instance == null)
			instance = new LocalConfigInitializer();
		return instance;
	}

	public void init()
	{
		ServerHelper.getInstance().init();
	}
}
