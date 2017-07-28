package com.kodgames.battleserver.service.battle.region.neimeng.common.score.battle;

import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;

/**
 * “拉”分数检查器
 */
public class BattleScoreChecker_La extends BattleScoreChecker_DunLaPao
{
	public BattleScoreChecker_La()
	{
		super(PlayType.DISPLAY_LA);
	}
	
	@Override
	protected void saveScore(int sourceId, int targetId, int zhuangId, int score, boolean add, List<ScoreData> targetScoreDatas)
	{
		// 如果加减分双方都不是庄家，不计拉分
		if (sourceId != zhuangId && targetId != zhuangId)
			return;

		super.saveScore(sourceId, targetId, zhuangId, score, add, targetScoreDatas);
	}
}
