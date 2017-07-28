package com.kodgames.activityserver.common.activity.rank;

import com.kodgames.message.proto.activity.ActivityProtoBuf;

/**
 * Created by jiangzhen on 2017/7/27.
 */
public interface IRankData extends Comparable<IRankData>
{
	/**
	 * 得到玩家id
	 * @return
	 */
	public int getRoleId();

	/**
	 * 得到排行的数值
	 * @return
	 */
	public <T extends Object> T getValue();

	/**
	 * 转为ActivityRankPROTO
	 * @return
	 */
	public ActivityProtoBuf.ActivityRankPROTO toActivityRankPROTO();
}
