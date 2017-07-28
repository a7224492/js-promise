package com.kodgames.corgi.core.config;

/**
 * Created by lz on 2016/8/5.
 * 最简单的server config
 */
public class SimpleServerConfig
{
	private String host;
	private int port;

	public SimpleServerConfig(String host, int port)
	{
		this.host = host;
		this.port = port;
	}

	public SimpleServerConfig()
	{
	}

	public String getHost()
	{
		return host;
	}

	public int getPort()
	{
		return port;
	}
}
