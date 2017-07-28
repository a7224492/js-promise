package com.kodgames.activityserver.common.activity.rank;

/**
 * Created by jiangzhen on 2017/7/27
 */
public class DefaultRankData implements IRankData
{
	/**
	 * 玩家id
	 */
	private int roleId;

	/**
	 * 排行值
	 */
	private int value;

	public DefaultRankData(int roleId, int value)
	{
		this.roleId = roleId;
		this.value = value;
	}

	@Override
	public int getRoleId()
	{
		return roleId;
	}

	@Override
	public <T extends Object> T getValue()
	{
		return (T)((Integer)value);
	}

	@Override
	public int compareTo(IRankData o)
	{
		return Integer.compare(value, (int)o.getValue());
	}
}
