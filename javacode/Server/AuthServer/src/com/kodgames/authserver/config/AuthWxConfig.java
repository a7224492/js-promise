package com.kodgames.authserver.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;

public class AuthWxConfig
{
	private static final Logger logger = LoggerFactory.getLogger(AuthWxConfig.class);

	public static final String OLD = "old";  			//老微信平台
	public static final String NEW = "new";			//新微信平台
		
	public static final String REG = "reg";  			//允许新用户通过老微信平台注册
	public static final String BIND = "bind";			//允许新微信平台通过设备ID绑定老微信平台用户
	public static final String NONE = "none";			//禁止上述操作
	
	public Map<String, Wxdev> mapWxdev = new HashMap<String, Wxdev>();

	private static AuthWxConfig instance = new AuthWxConfig();

	private AuthWxConfig()
	{
	}

	public static AuthWxConfig getInstance()
	{
		return instance;
	}

	public void parse()
		throws IOException
	{
		String name = AuthWxConfig.class.getClassLoader().getResource("auth.json").getPath();
		File file = new File(name);
		
		ByteSource byteSource = Files.asByteSource(file);
		String json = new String(byteSource.read());
		
		Map<String, Object> jsonMap = JSON.parseObject(json);
		
		for (Entry<String, Object> entry : jsonMap.entrySet())
		{
			Wxdev value = JSON.toJavaObject((JSON)entry.getValue(), Wxdev.class);
			mapWxdev.put(entry.getKey(), value);
			System.out.println(entry);
		}
	}
	
	public Map<String, Wxdev> getConfigMap()
	{
		return mapWxdev;
	}
	
	public boolean isValid(int appCode){
		for (Wxdev dev : mapWxdev.values())
		{
			if (dev.isContains(appCode))
			{
				return true;
			}
		}
		return false;
	}

	public boolean isOld(int appCode)
	{
		for (Wxdev dev : mapWxdev.values())
		{
			if (dev.getType().equals(OLD) && dev.isContains(appCode))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isNew(int appCode)
	{
		for (Wxdev dev : mapWxdev.values())
		{
			if (dev.getType().equals(NEW) && dev.isContains(appCode))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean allowRegister(int appCode)
	{
		for (Wxdev dev : mapWxdev.values())
		{
			if (dev.getType().equals(OLD) && dev.getStatus().equals(REG) && dev.isContains(appCode))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean allowBind(int appCode)
	{
		for (Wxdev dev : mapWxdev.values())
		{
			if (dev.getType().equals(NEW) && dev.getStatus().equals(BIND) && dev.isContains(appCode))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public String getAppid(int appCode)
	{
		for (Wxdev dev : mapWxdev.values())
		{
			for (Wxapp app : dev.wxapps)
			{
				if (app.getAppcode() == appCode)
				{
					return app.getWxappid();
				}
			}
		}
		
		logger.warn("can't find wxappid by appCode, appCode={}", appCode);
		
		return "";
	}
	
	public String getSecret(int appCode)
	{
		for (Wxdev dev : mapWxdev.values())
		{
			for (Wxapp app : dev.wxapps)
			{
				if (app.getAppcode() == appCode)
				{
					return app.getWxsecret();
				}
			}
		}
		
		logger.warn("can't find wxsecret by appCode, appCode={}", appCode);
		
		return "";
	}

}
