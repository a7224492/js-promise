package com.kodgames.corgi.core.util.config_utils;

import java.io.*;
import java.lang.reflect.Field;

/**
 * Created by lz on 2016/8/17.
 * 读取解析
 */
public abstract class Parser
{

	private InputStream configFile;

	public Parser(String path)
		throws FileNotFoundException
	{
		this(new File(path));
	}

	public Parser(File file)
		throws FileNotFoundException
	{
		this(new FileInputStream(file));
	}

	public Parser(InputStream input)
	{
		configFile = input;
		init();
	}

	protected abstract void init();

	public Parser read()
	{
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(configFile));
			String line;
			while ((line = br.readLine()) != null)
				parseLine(line);
			onEnd();

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return this;
	}

	//解析一行
	private void parseLine(String line)
	{
		//去掉注释
		int annotation;
		if ((annotation = line.indexOf("#")) != -1)
		{
			line = line.substring(0, annotation);
		}

		if (line.startsWith("[") && line.endsWith("]"))
		{
			//层级标签
			onHierarchy(line.substring(1, line.length() - 1));
		}

		if (line.contains("="))
		{
			onProperty(line);
		}
	}

	protected abstract void onHierarchy(String hierarchy);

	protected abstract void onProperty(String line);

	protected abstract void onEnd();

	public static void setProperty(String fieldName, Object value, Object srcObj)
	{
		try
		{
			Field f = srcObj.getClass().getDeclaredField(fieldName);
			f.setAccessible(true);
			f.set(srcObj, value);
		}
		catch (NoSuchFieldException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}

	public static void trim(String[] str)
	{
		for (int i = 0; i < str.length; i++)
		{
			if (str[i] != null)
				str[i] = str[i].trim();
		}
	}
}
