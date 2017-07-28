package com.kodgames.game.common;

public class ServerJsonKey
{
	private String serverType ;		//
	private String ip ;		//ip
	private int port;       //端口
	
	public ServerJsonKey()
	{
		
	}
	
	public String getServerType()
	{
		return serverType;
	}
	public void setServerType(String serverType)
	{
		this.serverType = serverType;
	}
	public String getIp()
	{
		return ip;
	}
	public void setIp(String ip)
	{
		this.ip = ip;
	}
	public int getPort()
	{
		return port;
	}
	public void setPort(int port)
	{
		this.port = port;
	}

}
