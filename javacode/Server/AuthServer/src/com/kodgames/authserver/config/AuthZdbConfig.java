package com.kodgames.authserver.config;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import limax.zdb.Procedure;
import xbean.VersionUpdateBean;

public class AuthZdbConfig
{

	private static final Logger logger = LoggerFactory.getLogger(AuthZdbConfig.class);

	public Map<String, Long> versionUpdateKeys = new ConcurrentHashMap<String, Long>();
	private static AuthZdbConfig instance = new AuthZdbConfig();

	private AuthZdbConfig()
	{
	}

	public static AuthZdbConfig getInstance()
	{
		return instance;
	}

	public void walk()
	{
		//初始化索引
		Procedure.call(() -> {
			table.Channel_version_table.get().walk((key, value) -> {
				long now = System.currentTimeMillis();
				versionUpdateKeys.put(key, new Long(now));
				VersionUpdateBean versionUpdateBean = table.Channel_version_table.select(key);
				return true;
			});
			return true;
		});
	}
}
