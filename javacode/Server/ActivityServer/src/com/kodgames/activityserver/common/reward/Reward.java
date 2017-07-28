package com.kodgames.activityserver.common.reward;

/**
 * Created by jiangzhen on 2017/7/26
 */
public class Reward
{
	/**
	 * 奖品id
	 */
	private int id;

	/**
	 * 奖品名称
	 */
	private String name;

	public void setName(String name)
	{
		this.name = name;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}
}
