package com.kodgames.battleserver.service.battle.core.score.gang;

import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;

/**
 * 杠牌
 */
public class GangScoreChecker_Gang extends GangScoreChecker
{
	public GangScoreChecker_Gang()
	{
		super(PlayType.OPERATE_GANG_A_CARD);
	}

	@Override
	public boolean doCalculate(GangScoreCheckContext inoutHuContext)
	{
		addScore(inoutHuContext.scoreData);
		return true;
	}
}