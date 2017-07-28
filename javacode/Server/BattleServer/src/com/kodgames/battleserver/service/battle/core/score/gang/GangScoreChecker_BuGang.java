package com.kodgames.battleserver.service.battle.core.score.gang;

import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;

/**
 * 补杠
 */
public class GangScoreChecker_BuGang extends GangScoreChecker
{
	public GangScoreChecker_BuGang()
	{
		super(PlayType.OPERATE_BU_GANG_A_CARD);
	}

	@Override
	public boolean doCalculate(GangScoreCheckContext inoutHuContext)
	{
		addScore(inoutHuContext.scoreData);
		return true;
	}
}