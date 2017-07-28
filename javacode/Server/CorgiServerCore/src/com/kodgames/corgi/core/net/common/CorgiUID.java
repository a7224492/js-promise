/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package com.kodgames.corgi.core.net.common;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author marui
 */
public class CorgiUID
{
	private long roleId;
	private int sequenceId;
	private String clientVersion;
	private String channelUniqueId;
	private String channelUserName;
	private int serverXmlVersion;// GameServer当前版本
	private int channelId;

	/**
	 * 获取 serverXmlVersion
	 * @return 返回 serverXmlVersion
	 */
	public int getServerXmlVersion()
	{
		return serverXmlVersion;
	}

	/**
	 * 设置 serverXmlVersion
	 * @param 对serverXmlVersion进行赋�?
	 */
	public void setServerXmlVersion(int serverXmlVersion)
	{
		this.serverXmlVersion = serverXmlVersion;
	}

	private static AtomicInteger seqId = new AtomicInteger(1);

	public static int getNextSeqId()
	{
		return seqId.incrementAndGet();
	}

	public String getChannelUniqueId()
	{
		return channelUniqueId;
	}

	public void setChannelUniqueId(String channelUniqueId)
	{
		this.channelUniqueId = channelUniqueId;
	}

	public long getRoleId()
	{
		return roleId;
	}

	public void setRoleId(long roleId)
	{
		this.roleId = roleId;
	}

	public String getChannelUserName()
	{
		return channelUserName;
	}

	public void setChannelUserName(String channelUserName)
	{
		this.channelUserName = channelUserName;
	}

	public String getClientVersion()
	{
		return clientVersion;
	}

	public void setClientVersion(String clientVersion)
	{
		this.clientVersion = clientVersion;
	}

	public CorgiUID()
	{

	}

	public int getChannelId()
	{
		return channelId;
	}

	public void setChannelId(int channelId)
	{
		this.channelId = channelId;
	}

	/**
	 * @return the sequenceID
	 */
	public int getSequenceId()
	{
		return sequenceId;
	}

	/**
	 * @param sequenceID the sequenceID to set
	 */
	public void setSequenceID(int sequenceId)
	{
		this.sequenceId = sequenceId;
	}
}
