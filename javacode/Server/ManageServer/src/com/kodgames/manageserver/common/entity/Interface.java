package com.kodgames.manageserver.common.entity;

import java.util.Map;
public class Interface implements Cloneable
{

	private Map<String, String> ipMap;

	public Map<String, String> getIpMap()
	{
		return ipMap;
	}

	public void setIpMap(Map<String, String> ipMap)
	{
		this.ipMap = ipMap;
	}

	@Override public String toString()
	{
		String sb = "Interface{" + "ipMap=" + ipMap +
			'}';
		return sb;
	}

}
