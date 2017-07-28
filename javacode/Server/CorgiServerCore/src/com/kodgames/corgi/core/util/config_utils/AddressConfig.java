package com.kodgames.corgi.core.util.config_utils;

import java.net.InetSocketAddress;

/**
 * Created by lz on 2016/8/18.
 * 地址配置
 * 包括配置单点端口
 * 配置端口范围
 * 配置host
 */
public class AddressConfig
{
	/**
	 * 是一个地址还是一个配置(范围)
	 */
	private boolean isAddress = false;

	private String host = "";
	private int port = 0;;
	private int portStart = 0;
	private int portEnd = 0;

	public AddressConfig(String addStr)
	{
		if (addStr == null || addStr.isEmpty())
			return;
		isAddress = !(addStr.contains("(") && addStr.contains(")"));
		init(addStr);
	}

	public AddressConfig()
	{
	}

	private void init(String str)
	{
		String[] address = str.split(":");
		if (isAddress)
		{
			host = address[0];
			port = Integer.valueOf(address[1]);
		}
		else
		{
			host = address[0];
			String range = address[1].substring(1, address[1].length() - 1);
			String[] rangeValue = range.split("~");
			portStart = Integer.valueOf(rangeValue[0]);
			portEnd = Integer.valueOf(rangeValue[1]);
		}
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public boolean isAddress()
	{
		return isAddress;
	}

	public String getHost()
	{
		return host;
	}

	public int getPort()
	{
		return port;
	}

	public int getPortStart()
	{
		return portStart;
	}

	public int getPortEnd()
	{
		return portEnd;
	}

	public String getAddressString()
	{
		return host == null && port == 0 ? null : host + ":" + port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public InetSocketAddress toSocketAddress()
	{
		return new InetSocketAddress(host, port);
	}

	public void copyFrom(AddressConfig other)
	{
		if (other != null)
		{
			this.isAddress = other.isAddress;
			this.host = new String(other.host);
			this.port = other.port;
			this.portStart = other.portStart;
			this.portEnd = other.portEnd;
		}
	}
	
	public boolean isValidConf()
	{
		return isAddress && port != 0 && !(host.equals("127.0.0.1") || host.equals("0.0.0.0"));
	}
}
