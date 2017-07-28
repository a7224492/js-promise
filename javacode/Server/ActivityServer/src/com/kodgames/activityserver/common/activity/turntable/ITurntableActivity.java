package com.kodgames.activityserver.common.activity.turntable;

import com.kodgames.activityserver.common.activity.IActivity;
import com.kodgames.activityserver.common.activity.condition.ICondition;
import com.kodgames.activityserver.common.reward.TurntableReward;

import java.util.List;

/**
 * Created by jiangzhen on 2017/7/26.
 */
public interface ITurntableActivity extends IActivity
{
	/**
	 * 得到玩家抽奖条件
	 * @param <S>
	 * @return
	 */
	public <S extends ICondition> S getPlayerDrawCondition();

	/**
	 * 玩家抽奖
	 * @return 奖品id
	 */
	public TurntableReward drawReward();

	/**
	 * 查询玩家抽到的奖品
	 * @param roleId
	 * @return
	 */
	public List<TurntableReward> queryPlayerRewardRecord(int roleId);

	/**
	 * 查询某天的所有奖品
	 * @param day
	 * @return
	 */
	public List<TurntableReward> queryTurntableRewards(long day);

	/**
	 * 设置某天的奖品
	 * @return
	 */
	public boolean setReward(long day, TurntableReward reward);
}