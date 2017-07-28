package com.kodgames.corgi.core.session;

import com.kodgames.corgi.core.net.Connection;

public class Session
{
	private int sessionID;
	private Connection connection;

	private long upSequenceId;
	private long downSequenceId;
	
	public int getSessionID()
	{
		return sessionID;
	}
	void setSessionID(int sessionID)
	{
		this.sessionID = sessionID;
	}
	public Connection getConnection()
	{
		return connection;
	}
	public void setConnection(Connection connection)
	{
		this.connection = connection;
	}
	public long getUpSequenceId()
	{
		return upSequenceId;
	}
	public void setUpSequenceId(long upSequenceId)
	{
		this.upSequenceId = upSequenceId;
	}
	public long getDownSequenceId()
	{
		return downSequenceId;
	}
	public void setDownSequenceId(long downSequenceId)
	{
		this.downSequenceId = downSequenceId;
	}
}
