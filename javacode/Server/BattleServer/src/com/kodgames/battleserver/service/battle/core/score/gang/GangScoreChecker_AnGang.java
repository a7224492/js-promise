package com.kodgames.battleserver.service.battle.core.score.gang;

import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;

/**
 * 暗杠
 */
public class GangScoreChecker_AnGang extends GangScoreChecker
{
	public GangScoreChecker_AnGang()
	{
		super(PlayType.OPERATE_AN_GANG);
	}

	@Override
	public boolean doCalculate(GangScoreCheckContext inoutHuContext)
	{
		addScore(inoutHuContext.scoreData);
		return true;
	}
}