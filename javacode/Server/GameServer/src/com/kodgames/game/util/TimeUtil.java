package com.kodgames.game.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtil
{
	/**
	 * 得到当天的零点
	 * 
	 * @param time 需要计算的时间
	 * @return 当天的零点
	 */
	public static long zeroTime(long time)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(time));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime().getTime();
	}
	
	/**
	 * 把now时间和某一零点比较，判断now和零点是否是同一天
	 * 
	 * @param now 当前时间
	 * @param zeroTime 零点
	 * @return true 是同一天 false 不是同一天
	 */
	public static boolean isOneDayToZeroTime(long now, long zeroTime)
	{
		return zeroTime(now) == zeroTime;
	}
	
	public static boolean isOneDay(long t1, long t2)
	{
		return zeroTime(t1)==zeroTime(t2);
	}
	
	/**
	 * 获取指定时间的时间戳
	 * 
	 * @param year
	 * @param month
	 * @param date
	 * @return
	 */
	public static long time(int year, int month, int date)
	{
		return TimeUtil.time(year, month, date, 0, 0, 0);
	}

	public static long time(int year, int month, int date, int hour, int minute, int second)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, date, hour, minute, second);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime().getTime();
	}
	
	/**
	 * 把一个时间戳转为formatStr格式的字符串,默认是yyyy-MM-dd HH:mm:ss格式的字符串
	 * @param time 
	 * @return
	 */
	public static String timeString(long timestamp, String formatStr)
	{
		if (formatStr == null || formatStr.equals(""))
		{
			formatStr = "yyyy-MM-dd HH:mm:ss";
		}
		
		Calendar cal = Calendar.getInstance(Locale.CHINA);
        cal.setTimeInMillis(timestamp);
        return new SimpleDateFormat(formatStr).format(cal.getTimeInMillis());
	}
	
	/**
	 * 测试
	 * @param args
	 */
	public static void main(String[] args)
	{
		long t1 = time(2017, 4, 10, 10, 10, 10);
		long t2 = time(2017, 4, 11, 0, 0, 0);
		System.out.println(isOneDay(t1, t2));
	}
}
