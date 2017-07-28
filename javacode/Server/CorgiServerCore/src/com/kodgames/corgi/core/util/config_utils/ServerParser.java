package com.kodgames.corgi.core.util.config_utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class ServerParser extends Parser
{
	private Map<String, String> config;

	public ServerParser(String path)
		throws FileNotFoundException
	{
		super(path);
	}

	public ServerParser(File file)
		throws FileNotFoundException
	{
		super(file);
	}

	public ServerParser(InputStream input)
	{
		super(input);
	}

	@Override protected void init()
	{
		config = new HashMap<>();
	}

	@Override protected void onHierarchy(String hierarchy)
	{

	}

	@Override protected void onProperty(String line)
	{
		String[] kv = line.split("=");
		Parser.trim(kv);
		config.put(kv[0], kv[1]);
	}

	@Override protected void onEnd()
	{

	}

	public Map<String, String> getConfig()
	{
		return config;
	}
}
