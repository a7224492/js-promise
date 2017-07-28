package com.kodgames.battleserver.service.battle.region.guangdong.puning.score;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalcluateContext;
import com.kodgames.battleserver.service.battle.region.guangdong.common.score.battle.BattleScoreCalculator_GuangDong;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultEventPROTO;

/**
 * 汕头分数计算
 */
public class BattleScoreCalculator_PuNing extends BattleScoreCalculator_GuangDong
{
	/**
	 * 计算牌局个人得分
	 * 
	 * 不同玩法算分方式不同, 可以重载这个函数
	 */
	@Override
	protected int calculateScore(ResultEventPROTO.Builder event, ScoreCalcluateContext context, int maxValue)
	{
		// 广东翻数不是连乘
		Macro.AssetFalse(context.towIndex == 0);

		int score = context.totalAdd;
		// 分数类型判定
		int type = event.getSubScoresOrBuilderList().get(0).getType();

		// 最高番限制
		if (context.isHuScore || buyMaFenType.contains(type))
		{
			score *= context.totalMulti;

			// 是否有分数限制
			if (maxValue != 0)
				score = Math.min(score, maxValue);

			score *= context.totalMulti2nd;
		}

		int scoreType = event.getSubScoresOrBuilderList().get(0).getType();
		// 是否为杠分，杠分需要算奖马的分
		if (PlayType.isGangOperator(scoreType) || scoreType == PlayType.DISPLAY_HU_BUY_HORSE_SCORE || scoreType == PlayType.DISPLAY_HU_PUNISH_HORSE_SCORE)
			score *= context.totalMulti2nd;

		return score;
	}
}
