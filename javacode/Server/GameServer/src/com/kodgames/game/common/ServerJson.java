package com.kodgames.game.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ServerJson
{
	private static ServerJson instance = new ServerJson();
	private static ServerConfigs serverConfig;

	public static ServerJson getInstance()
	{
		return instance;
	}

	public void parse()
		throws IOException
	{

		String name = ServerJson.class.getClassLoader().getResource("server.json").getPath();
		File file = new File(name);
		InputStream inputStream = new FileInputStream(file);
		ObjectMapper mapper = new ObjectMapper();
		serverConfig = mapper.readValue(inputStream, ServerConfigs.class);
	}

	public ServerConfigs getServerConfig()
	{
		return serverConfig;
	}

}
