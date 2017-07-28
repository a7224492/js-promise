package com.kodgames.battleserver.service.battle.region.guangdong.shantou.score.battle;

import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalcluateContext;
import com.kodgames.battleserver.service.battle.region.guangdong.common.score.battle.BattleScoreCalculator_GuangDong;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultEventPROTO;

/**
 * 汕头分数计算
 * 
 * @author kod
 *
 */
public class BattleScoreCalculator_ShanTou extends BattleScoreCalculator_GuangDong
{
	/**
	 * 计算牌局个人得分
	 * 
	 * 不同玩法算分方式不同, 可以重载这个函数
	 */
	@Override
	protected int calculateScore(ResultEventPROTO.Builder event, ScoreCalcluateContext context, int maxValue)
	{
		int score = context.totalAdd;

		int type = event.getSubScoresOrBuilderList().get(0).getType();

		// 最高番限制
		if (context.isHuScore || buyMaFenType.contains(type))
		{
			// 计算乘分
			score *= context.totalMulti;

			// 是否有分数限制
			if (maxValue != 0)
				score = Math.min(score, maxValue);

			score *= context.totalMulti2nd;
		}

		// 杠分需要乘以奖马分
		if (PlayType.isGangOperator(type))
			score *= context.totalMulti2nd;

		return score;
	}
}
