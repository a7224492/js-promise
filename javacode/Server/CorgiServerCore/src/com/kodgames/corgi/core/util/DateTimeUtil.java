package com.kodgames.corgi.core.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class DateTimeUtil
{
	private static String timeZone = "GMT+8";
	private static ReentrantReadWriteLock timeLock = new ReentrantReadWriteLock();
	
	// common
	// week day
	public static final int MONDAY = 1;
	public static final int TUESDAY = 2;
	public static final int WEDNESDAY = 3;
	public static final int THURSDAY = 4;
	public static final int FRIDAY = 5;
	public static final int SATURDAY = 6;
	public static final int SUNDAY = 7;
	
	/*
	 * 获取Date的字符串格式，以北京时间为准
	 */
	public static String getGMT8String(Date date)
	{
		try
		{
			timeLock.readLock().lock();
			DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
			return dateFormat.format(date);
		}
		finally
		{
			timeLock.readLock().unlock();
		}
	}
	
	/**
	 * 获取今天是本月的第几天
	 */
	public static int getDayOfMonth()
	{
		return getDayOfMonth(System.currentTimeMillis());
	}

	private static int getDayOfMonth(long time)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone(timeZone));
		cal.setTimeInMillis(time);
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 获取当前时间之前n个月1号的Date 00：00
	 * @return 1号的Date
	 */
	public static Date getMonthFirst(long time, int diff)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone(timeZone));
		cal.setTimeInMillis(time);
		
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.MONTH, diff);
		return cal.getTime();
	}
	
	/**
	 * 获取当前时间之前n个月1号的Date 00 ：00
	 * @return 1号的Date
	 */
	public static Date getMonthFirst(int diff)
	{
		long now = getCurrentTimeMillis();
		return getMonthFirst(now, diff);
	}
	
	/**
	 * 获取某个时间是周几
	 * @return 1 ~ 7
	 */
	public static int getDayOfWeek(long time)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone(timeZone));
		cal.setTimeInMillis(time);
		int temp = cal.get(Calendar.DAY_OF_WEEK) - 1;
		return temp == 0 ? SUNDAY : temp;
	}
	
	/**
	 * 获取当前是周几
	 * @return 1 ~ 7
	 */
	public static int getDayOfWeek()
	{
		return getDayOfWeek(System.currentTimeMillis());
	}
	
	/**获取指定日期之前n周所在周几的00：00时间
	 * @return 1号的Date
	 */
	public static Date getWeekDate(long time, int diff, int weekday)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone(timeZone));
		cal.setTimeInMillis(time);
		cal.set(Calendar.DAY_OF_WEEK, weekday);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.WEEK_OF_YEAR, diff);
		return cal.getTime();
	}
	
	/**获取当前日期之前n周周几的00：00时间
	 * @return 1号的Date
	 */
	public static Date getWeekDate(int diff, int weekday)
	{
		long now = getCurrentTimeMillis();
		return getWeekDate(now, diff, weekday);
	}
	
	public static long getCurrentTimeMillis()
	{
		try
		{
			timeLock.readLock().lock();
			return System.currentTimeMillis();
		}
		finally
		{
			timeLock.readLock().unlock();
		}
	}
	
	public static Date getNowDate()
	{
		return new Date();
	}
	
	public static Date getDate(Long milliseconds)
	{
		return new Date(milliseconds);
	}
	
	public static boolean isInSameDay(Date date1, Date date2)
	{
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

	    Calendar cal2 = Calendar.getInstance();
	    cal2.setTime(date2);
	    if(cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
	    	cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR))
	    {
	    	return true;
	    }
	    else
	    {
	    	return false;
	    }
	}

	public static Long getDayEndtime()
	{
		Calendar todayEnd = Calendar.getInstance();
		todayEnd.set(Calendar.HOUR_OF_DAY, 23);
		todayEnd.set(Calendar.MINUTE, 59);
		todayEnd.set(Calendar.SECOND, 59);
		todayEnd.set(Calendar.MILLISECOND, 0);
		return todayEnd.getTime().getTime();
	} 
	
	public static String getAlertTime(int alertTime)
	{
		StringBuffer alertContent = new StringBuffer();
		if (alertTime > 60)
		{
			return alertContent.append((alertTime + 1) / 60).append("分钟").toString();
		}
		else
		{
			return alertContent.append(alertTime).append("秒").toString();
		}
	}

	/**
	 * 获取时间当前时间-date
	 * 
	 * @param date
	 * @return
	 */
	public static int getTimeForMinute(Date date)
	{
		if (date != null)
		{
			long time = System.currentTimeMillis() - date.getTime();
			return (int)(time / (1000 * 60));
		}
		return 0;
	}

	/**
	 * 获取时间显示单位
	 * 
	 * @param time 秒
	 * @return
	 */
	public static String getTimeUnit(int time)
	{
		if (time > 86400)
		{
			return (time / 86400) + "天";
		}
		else if (time > 3600)
		{
			return (time / 3600) + "小时";
		}
		else if (time > 60)
		{
			return (time / 60) + "分钟";
		}
		else
		{
			return time + "秒";
		}
	}

	public static Date getSpecifiedDay(Date date, int offsetDay)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + offsetDay);
		return c.getTime();
	}

	/**
	 * 将java.util.Date转为YYYY-MM-dd HH:mm:ss格式的字符串
	 */
	public static String getTimeString(Date date)
	{
		return new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(date);
	}

	/**
	 * 将long型时间转为YYYY-MM-dd HH:mm:ss格式的字符串
	 */
	public static String getTimeString(long time)
	{
		return getTimeString(new Date(time));
	}

	public static long getZeroTime(long time)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(time));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime().getTime();
	}
}