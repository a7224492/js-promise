package com.kodgames.game.service.activity;

public class ActivityConfig
{
	
	private long activityStartTime;
	private long activityEndTime;
	private long queryStartTime;
	private long queryEndTime;
	
	public long getActivityStartTime()
	{
		return activityStartTime;
	}
	
	public void setActivityStartTime(long time)
	{
		activityStartTime = time;
	}
	
	public long getActivityEndTime()
	{
		return activityEndTime;
	}
	
	public void setActivityEndTime(long time)
	{
		activityEndTime = time;
	}
	
	public long getQueryStartTime()
	{
		return queryStartTime;
	}
	
	public void setQueryStartTime(long time)
	{
		queryStartTime = time;
	}
	
	public long getQueryEndTime()
	{
		return queryEndTime;
	}
	
	public void setQueryEndTime(long time)
	{
		queryEndTime = time;
	}
	
}
