package com.kodgames.activityserver.common.activity;

import com.kodgames.activityserver.common.activity.condition.ICondition;
import com.kodgames.activityserver.common.activity.event.IActivityEventHandler;

/**
 * Created by jiangzhen on 2017/7/26.
 */
public interface IActivity
{
	/**
	 * 得到活动的时间条件
	 *
	 * @param <S>
	 * @return
	 */
	public <S extends ICondition> S timeCondition();

	/**
	 * 得到活动id
	 * @return
	 */
	public int getActivityId();
}