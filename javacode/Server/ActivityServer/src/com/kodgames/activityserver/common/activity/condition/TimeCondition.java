package com.kodgames.activityserver.common.activity.condition;

/**
 * Created by jiangzhen on 2017/7/27
 */
public class TimeCondition implements ITimeCondition
{
	private long startTime, endTime;

	@Override
	public void setStartTime(long time)
	{
		this.startTime = time;
	}

	@Override
	public void setEndTime(long time)
	{
		this.endTime = time;
	}

	@Override
	public boolean isSatisfy()
	{
		long now = System.currentTimeMillis();
		if (now > startTime && now < endTime)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
