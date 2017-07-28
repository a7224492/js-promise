package com.kodgames.activityserver.common.activity.condition;

/**
 * Created by jiangzhen on 2017/7/27.
 * 活动条件
 */
public interface ICondition
{
	/**
	 * 条件是否满足
	 * @return true 满足 false 不满足
	 */
	public boolean isSatisfy();
}
