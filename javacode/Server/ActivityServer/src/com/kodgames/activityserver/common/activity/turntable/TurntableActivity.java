package com.kodgames.activityserver.common.activity.turntable;

import com.kodgames.activityserver.common.activity.condition.ICondition;
import com.kodgames.activityserver.common.activity.turntable.ITurntableActivity;
import com.kodgames.activityserver.common.reward.TurntableReward;
import com.kodgames.message.proto.game.GameProtoBuf;

import java.util.List;

/**
 * Created by jiangzhen on 2017/7/26
 */
public class TurntableActivity implements ITurntableActivity
{
	@Override
	public <S extends ICondition> S timeCondition()
	{
		return null;
	}

	@Override
	public <S extends ICondition> S getPlayerDrawCondition()
	{
		return null;
	}

	@Override
	public TurntableReward drawReward()
	{
		return null;
	}

	@Override
	public List<TurntableReward> queryPlayerRewardRecord(int roleId)
	{
		return null;
	}

	@Override
	public List<TurntableReward> queryTurntableRewards(long day)
	{
		return null;
	}

	@Override
	public boolean setReward(long day, TurntableReward reward)
	{
		return false;
	}

	@Override
	public void handleEvent(int eventId, GameProtoBuf.ActivityEventParam param)
	{

	}
}
