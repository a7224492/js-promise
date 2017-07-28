package com.kodgames.battleserver.service.battle.region.neimeng.common.score.battle;

import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;

/**
 * “蹲”分数检查器
 */
public class BattleScoreChecker_Dun extends BattleScoreChecker_DunLaPao
{
	public BattleScoreChecker_Dun()
	{
		super(PlayType.DISPLAY_DUN);
	}
}
