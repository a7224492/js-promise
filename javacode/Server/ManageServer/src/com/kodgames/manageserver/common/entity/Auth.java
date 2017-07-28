package com.kodgames.manageserver.common.entity;

public class Auth
{
	private int port4socketClient;

	private int port4server;

	private int port4webSocketClient;

	public void setPort4socketClient(int port4socketClient)
	{
		this.port4socketClient = port4socketClient;
	}

	public int getPort4socketClient()
	{
		return this.port4socketClient;
	}

	public void setPort4server(int port4server)
	{
		this.port4server = port4server;
	}

	public int getPort4server()
	{
		return this.port4server;
	}

	public void setPort4webSocketClient(int port4webSocketClient)
	{
		this.port4webSocketClient = port4webSocketClient;
	}

	public int getPort4webSocketClient()
	{
		return this.port4webSocketClient;
	}

	@Override public String toString()
	{
		String sb = "Auth{" + "port4socketClient=" + port4socketClient +
			", port4server=" + port4server +
			", port4webSocketClient=" + port4webSocketClient +
			'}';
		return sb;
	}
}