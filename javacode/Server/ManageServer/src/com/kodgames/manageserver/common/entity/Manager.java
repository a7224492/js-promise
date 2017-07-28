package com.kodgames.manageserver.common.entity;

public class Manager
{
	private int serverType;

	private String serverName;

	private int socketPort;

	private int httpPort;

	public void setServerType(int serverType)
	{
		this.serverType = serverType;
	}

	public int getServerType()
	{
		return this.serverType;
	}

	public void setServerName(String serverName)
	{
		this.serverName = serverName;
	}

	public String getServerName()
	{
		return this.serverName;
	}

	public void setSocketPort(int socketPort)
	{
		this.socketPort = socketPort;
	}

	public int getSocketPort()
	{
		return this.socketPort;
	}

	public void setHttpPort(int httpPort)
	{
		this.httpPort = httpPort;
	}

	public int getHttpPort()
	{
		return this.httpPort;
	}

	@Override public String toString()
	{
		String sb = "Manager{" + "serverType=" + serverType +
			", serverName='" + serverName + '\'' +
			", socketPort=" + socketPort +
			", httpPort=" + httpPort +
			'}';
		return sb;
	}
}