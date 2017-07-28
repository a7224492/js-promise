package com.kodgames.corgi.core.util.config_utils;

import java.io.InputStream;
import java.util.Map;

/**
 * Created by lz on 2016/8/17.
 * manager使用的config
 */
public class ManagerConfig
{

	public static final int AREA_COMMON = 0;
	//default 模板 type->serverConfig
	private Map<Integer, ServerConfig> defaultServerConfigs;

	//自定义配置 用于覆盖
	private CustomManagerConfig customConfig;

	public ManagerConfig(InputStream custom)
	{
		DefaultParser dp = new DefaultParser(Object.class.getResourceAsStream("/default.conf"));
		dp.read();
		defaultServerConfigs = dp.getConfigs();

		CustomManagerParser cp = new CustomManagerParser(custom);
		cp.read();
		customConfig = cp.getConfigs();
	}

	/**
	 * 根据area和type返回配置
	 * @param type 服务器类型
	 * @param area 所在区域
	 */
	public ServerConfig getConfig(int area, int type)
	{
		return customConfig.coverServerConfig(area, type, defaultServerConfigs.get(type));
	}
}
