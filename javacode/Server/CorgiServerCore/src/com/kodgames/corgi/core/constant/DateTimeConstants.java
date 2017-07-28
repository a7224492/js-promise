package com.kodgames.corgi.core.constant;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * 
 *
 */
public class DateTimeConstants
{
	/**
	 * 
	 */
	private DateTimeConstants()
	{
	}

	/**
	 * 一秒钟的毫秒表示
	 */
	public final static long SECOND = 1000L;
	/**
	 * 一分钟的毫秒表示
	 */
	public final static long MINUTE = 60L * SECOND;
	/**
	 * 一小时的毫秒表示
	 */
	public final static long HOUR = 60L * MINUTE;
	/**
	 * 一天的毫秒表示
	 */
	public final static long DAY = 24L * HOUR;
	/**
	 * 一周的毫秒表示
	 */
	public final static long WEEK = 7L * DAY;

	/**
	 * 一个月的毫秒表示(按30天计算)
	 */
	public final static long MONTH = 30L * DAY;
	/**
	 * 一年的毫秒表示(按365天计算)
	 */
	public final static long YEAR = 365L * DAY;

	/**
	 * 用户友好的日期时间格式:yyyy-MM-dd HH:mm:ss
	 */
	public final static String FRIENDLY_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 获取指定时间的yyyy-MM-dd HH:mm:ss表示格式
	 * 
	 * @param timestamp
	 * @return
	 */
	public static String getTimeString(long timestamp)
	{
		Calendar cal = Calendar.getInstance(Locale.CHINA);
		cal.setTimeInMillis(timestamp);
		return new SimpleDateFormat(FRIENDLY_DATE_TIME_FORMAT).format(cal.getTimeInMillis());
	}

	/**
	 * 获取指定时间的时和分部分
	 * 
	 * @param time
	 * @return
	 */
	public static long getHourAndMinute(long time)
	{
		return (time + Calendar.getInstance().get(Calendar.ZONE_OFFSET)) % DAY;
	}

	/**
	 * 获取指定时间的日期部分
	 * 
	 * @param time
	 * @return
	 */
	public static long getDate(long time)
	{
		return time - getHourAndMinute(time);
	}

	/**
	 * 判断两个时间点是否日期相同
	 */
	public static boolean isDateSame(long first, long second)
	{
		return getDate(first) == getDate(second);
	}
	
}
