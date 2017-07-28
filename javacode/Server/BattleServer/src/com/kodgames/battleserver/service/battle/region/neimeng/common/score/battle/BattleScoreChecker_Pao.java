package com.kodgames.battleserver.service.battle.region.neimeng.common.score.battle;

import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;

/**
 * “跑”分数检查器
 */
public class BattleScoreChecker_Pao extends BattleScoreChecker_DunLaPao
{
	public BattleScoreChecker_Pao()
	{
		super(PlayType.DISPLAY_PAO);
	}
}
