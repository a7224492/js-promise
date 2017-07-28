package com.kodgames.corgi.core.config;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class BaseServerConfig
{
	private InetSocketAddress manageServerAddress;
	private int serverID;
	private String serverName;
	private int serverType;
	private String hostName4Server;
	private int port4Server;
	private String hostName4Client;
	private String hostName4GMTools;
	private int port4GMTools;

	public int getServerID()
	{
		return serverID;
	}

	public void setServerID(int serverID)
	{
		this.serverID = serverID;
	}

	public String getServerName()
	{
		return serverName;
	}

	public void setServerName(String serverName)
	{
		this.serverName = serverName;
	}

	public int getServerType()
	{
		return serverType;
	}

	public void setServerType(int severType)
	{
		this.serverType = severType;
	}

	public String getHostName4Server()
	{
		return hostName4Server;
	}

	public void setHostName4Server(String hostName4Server)
	{
		this.hostName4Server = hostName4Server;
	}

	public int getPort4Server()
	{
		return port4Server;
	}

	public void setPort4Server(int port4Server)
	{
		this.port4Server = port4Server;
	}

	public String getHostName4Client()
	{
		return hostName4Client;
	}

	public void setHostName4Client(String hostName4Client)
	{
		this.hostName4Client = hostName4Client;
	}

	public String getHostName4GMTools()
	{
		return hostName4GMTools;
	}

	public void setHostName4GMTools(String hostName4GMTools)
	{
		this.hostName4GMTools = hostName4GMTools;
	}

	public int getPort4GMTools()
	{
		return port4GMTools;
	}

	public void setPort4GMTools(int port4gmTools)
	{
		port4GMTools = port4gmTools;
	}

	public SocketAddress getManageServerAddress()
	{
		return manageServerAddress;
	}

	public void setManageServerAddress(InetSocketAddress manageServerAddress)
	{
		this.manageServerAddress = manageServerAddress;
	}
}
