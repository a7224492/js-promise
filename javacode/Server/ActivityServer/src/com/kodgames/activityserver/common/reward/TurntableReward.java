package com.kodgames.activityserver.common.reward;

/**
 * Created by jiangzhen on 2017/7/26
 */
public class TurntableReward extends Reward
{
	/**
	 * 奖品概率
	 */
	private int ratio;

	public int getRatio()
	{
		return ratio;
	}

	public void setRatio(int ratio)
	{
		this.ratio = ratio;
	}
}
