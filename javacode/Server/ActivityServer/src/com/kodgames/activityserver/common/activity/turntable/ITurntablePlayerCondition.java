package com.kodgames.activityserver.common.activity.turntable;

import com.kodgames.activityserver.common.activity.condition.ICondition;

/**
 * Created by jiangzhen on 2017/7/27.
 */
public interface ITurntablePlayerCondition extends ICondition
{
	/**
	 * 增加某个玩家的抽奖次数
	 * @param roleId 玩家id
	 * @param count 次数
	 * @return true 成功 false 失败
	 */
	public boolean addPlayerItemCount(int roleId, int count);

	/**
	 * 减少某个玩家的抽奖次数
	 * @param roleId 玩家id
	 * @param count 次数
	 * @return true 成功 false 失败
	 */
	public boolean subPlayerItemCount(int roleId, int count);

	/**
	 * 查询某个玩家的剩余抽奖次数
	 * @param roleId 玩家id
	 * @return 抽奖次数
	 */
	public int queryPlayerItemCount(int roleId);
}
