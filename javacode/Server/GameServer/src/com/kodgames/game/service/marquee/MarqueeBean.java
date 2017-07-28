package com.kodgames.game.service.marquee;

import java.util.ArrayList;
import java.util.List;

public class MarqueeBean
{
	private long id;
	private int type;
	private int showType;
	private String msg;
	private int weeklyRepeat;
	private long absoluteDate;
	private List<Long> hourAndMinute;
	private int rollTimes;
	private int intervalTime;
	private String color;

	public MarqueeBean()
	{
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public int getShowType()
	{
		return showType;
	}

	public void setShowType(int showType)
	{
		this.showType = showType;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public int getWeeklyRepeat()
	{
		return weeklyRepeat;
	}

	public void setWeeklyRepeat(int weeklyRepeat)
	{
		this.weeklyRepeat = weeklyRepeat;
	}

	public long getAbsoluteDate()
	{
		return absoluteDate;
	}

	public void setAbsoluteDate(long absoluteDate)
	{
		this.absoluteDate = absoluteDate;
	}

	public List<Long> getHourAndMinute()
	{
		if (hourAndMinute == null)
			hourAndMinute = new ArrayList<>();
		return hourAndMinute;
	}

	public void setHourAndMinute(List<Long> hourAndMinute)
	{
		this.getHourAndMinute().addAll(hourAndMinute);
	}

	public int getRollTimes()
	{
		return rollTimes;
	}

	public void setRollTimes(int rollTimes)
	{
		this.rollTimes = rollTimes;
	}

	public int getIntervalTime()
	{
		return intervalTime;
	}

	public void setIntervalTime(int intervalTime)
	{
		this.intervalTime = intervalTime;
	}

	public String getColor()
	{
		return color;
	}

	public void setColor(String color)
	{
		this.color = color;
	}
}
