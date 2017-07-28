package com.kodgames.battleserver.service.battle.core.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;

/**
 * 检测牌形:根(川麻)
 */
public class HuSubScoreChecker_Gen extends HuSubScoreChecker
{
	public HuSubScoreChecker_Gen()
	{
		super(PlayType.HU_GEN);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		boolean hasGen = false;
		for (byte c : inoutHuContext.allCardCountList)
		{
			if (c != 4)
				continue;

			hasGen = true;
			addScore(inoutHuContext.scoreData);
		}

		return hasGen;
	}
}