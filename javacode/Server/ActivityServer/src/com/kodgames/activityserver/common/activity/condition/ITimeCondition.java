package com.kodgames.activityserver.common.activity.condition;

/**
 * Created by jiangzhen on 2017/7/27.
 */
public interface ITimeCondition extends ICondition
{
	/**
	 * 设置开始时间
	 */
	public void setStartTime(long time);

	/**
	 * 设置结束时间
	 */
	public void setEndTime(long time);
}
