package com.kodgames.battleserver.service.battle.region.meizhou.tuidaohu.score.battle;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalcluateContext;
import com.kodgames.battleserver.service.battle.core.score.battle.BattleScoreCalculator;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultEventPROTO;

/**
 * 广东推倒胡算分
 */
public class BattleScoreCalculator_MeiZhou_TuiDaoHu extends BattleScoreCalculator
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

		// 最高番限制
		if (context.isHuScore)
		{
			// 是否有分数限制
			if (maxValue != 0)
				score = Math.min(score, maxValue);

			score *= context.totalMulti2nd;
		}

		// 奖马的分数计算
		if (event.getSubScoresOrBuilderList().get(0).getType() == PlayType.DISPLAY_BETTING_HOUSE)
			score *= context.totalMulti2nd;

		return score;
	}

	/**
	 * 计算完具体得分之后, 可以通过这个函数修改客户端用于显示的分数
	 */
	@Override
	protected void modifyScoreFromClientDisplay(ResultEventPROTO.Builder event)
	{
		if (event.getSubScoresBuilderList().get(0).getType() != PlayType.DISPLAY_BETTING_HOUSE)
			return;

		Macro.AssetTrue(event.getSubScoresBuilderList().size() != 2);

		// 奖马放入了2个point所以肯定可以取到1
		int houseNum = event.getSubScoresBuilderList().get(0).getPoint();
		int houseScore = event.getSubScoresBuilderList().get(1).getPoint();

		// 修改为奖马获得的分
		event.getSubScoresBuilderList().get(0).setPoint(houseScore * houseNum);
		event.removeSubScores(1);
	}
}
