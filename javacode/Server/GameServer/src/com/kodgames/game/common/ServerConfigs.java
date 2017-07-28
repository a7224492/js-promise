package com.kodgames.game.common;

import java.util.List;

public class ServerConfigs
{
	private List<ServerJsonKey> serverConfigs;

	public ServerConfigs()
	{

	}

	public List<ServerJsonKey> getServerConfigs()
	{
		return serverConfigs;
	}

	public void setServerConfigs(List<ServerJsonKey> serverConfigs)
	{
		this.serverConfigs = serverConfigs;
	}
}
