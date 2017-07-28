package com.kodgames;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by maojianwei on 2017/6/30.
 */
public class Config
{
	private String remoteHost = "";

	private String remoteUser = "";

	private String remotePassword = "";

	private String localHost = "";

	public String getRemoteHost()
	{
		return remoteHost;
	}

	public void setRemoteHost(String remoteHost)
	{
		this.remoteHost = remoteHost;
	}

	public String getRemoteUser()
	{
		return remoteUser;
	}

	public void setRemoteUser(String remoteUser)
	{
		this.remoteUser = remoteUser;
	}

	public String getRemotePassword()
	{
		return remotePassword;
	}

	public void setRemotePassword(String remotePassword)
	{
		this.remotePassword = remotePassword;
	}

	public String getLocalHost()
	{
		return localHost;
	}

	public void setLocalHost(String localHost)
	{
		this.localHost = localHost;
	}

	public String getLocalUser()
	{
		return localUser;
	}

	public void setLocalUser(String localUser)
	{
		this.localUser = localUser;
	}

	public String getLocalPassword()
	{
		return localPassword;
	}

	public void setLocalPassword(String localPassword)
	{
		this.localPassword = localPassword;
	}

	public String getDbname()
	{
		return dbname;
	}

	public void setDbname(String dbname)
	{
		this.dbname = dbname;
	}

	private String localUser = "";

	private String localPassword = "";

	private String dbname = "";

	public Config()
	{
		Properties pro = new Properties();
		try
		{
			System.out.println(Config.class.getClassLoader().getResource("./"));
			FileInputStream in = new FileInputStream(Config.class.getClassLoader().getResource("./db.properties").getPath());
			pro.load(in);
			in.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		/**
		 * remotehost="115.159.126.127"
		 localhost="127.0.0.1"
		 remoteuser="erdosmj_write"
		 remotepassword="4mZ281FsBz"
		 localuser="root"
		 localpassword="root"
		 dbname="replay"
		 */

		this.dbname = pro.getProperty("dbname");
		this.remoteHost = pro.getProperty("remotehost");
		this.remotePassword = pro.getProperty("remotepassword");
		this.remoteUser= pro.getProperty("remoteuser");
		this.localHost = pro.getProperty("localhost");
		this.localPassword = pro.getProperty("localpassword");
		this.localUser = pro.getProperty("localuser");
	}

}
