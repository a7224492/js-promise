package com.mycode.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by jz on 2017/7/13.
 */
public class TimeUtil {
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
}
