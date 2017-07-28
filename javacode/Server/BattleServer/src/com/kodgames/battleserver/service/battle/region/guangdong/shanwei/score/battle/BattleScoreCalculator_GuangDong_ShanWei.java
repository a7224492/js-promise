package com.kodgames.battleserver.service.battle.region.guangdong.shanwei.score.battle;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalcluateContext;
import com.kodgames.battleserver.service.battle.region.guangdong.common.score.battle.BattleScoreCalculator_GuangDong;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultEventPROTO;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultScorePROTOOrBuilder;

/**
 * 汕尾玩法计算分数
 */
public class BattleScoreCalculator_GuangDong_ShanWei extends BattleScoreCalculator_GuangDong
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
			int multi = 1;
			int otherAdd = 0;
			// 循环所有分数
			for (ResultScorePROTOOrBuilder subScore : event.getSubScoresOrBuilderList())
			{
				// 其他的乘分类型
				if (otherMultiTypes.contains(subScore.getType()))
					multi *= subScore.getPoint();
				// 其他的加分类型
				if (otherAddTypes.contains(subScore.getType()))
					otherAdd += subScore.getPoint();
			}
			score = (((score - otherAdd) * context.totalMulti / multi) + otherAdd) * multi;
			// 是否有分数限制
			if (maxValue != 0)
				score = Math.min(score, maxValue);

			score *= context.totalMulti2nd;
		}

		// 奖马的分数计算、风刻、箭刻、花牌加番也需要计算奖马
		if (type == PlayType.DISPLAY_BETTING_HOUSE || type == PlayType.DISPLAY_FENG_JIA_FAN || type == PlayType.DISPLAY_HUA_JIA_FAN || type == PlayType.DISPLAY_ZI_JIA_FAN
			|| PlayType.isGangOperator(type))
			score *= context.totalMulti2nd;

		return score;
	}

	/**
	 * 修改客户端显示，主要是奖马和花牌
	 * 
	 * @param event
	 */
	@Override
	protected void modifyScoreFromClientDisplay(ResultEventPROTO.Builder event)
	{
		super.modifyScoreFromClientDisplay(event);
		// 奖马的显示（在奖马加倍的时候奖马不是在列表的第一个位置）
		if (event.getSubScoresOrBuilderList().get(0).getType() == PlayType.DISPLAY_BETTING_HOUSE)
		{
			// 获取判断奖马分值的位置
			int index = -1;

			// 找到奖马分值的位置
			for (int i = 0; i < event.getSubScoresOrBuilderList().size(); i++)
			{
				if (event.getSubScoresOrBuilderList().get(i).getType() == PlayType.DISPLAY_BETTING_HOUSE_VALUE)
					index = i;
			}

			if (index == -1)
				return;

			// 奖马放入了2个point所以肯定可以取到1
			int houseNum = event.getSubScoresBuilderList().get(0).getPoint();
			int houseScore = event.getSubScoresBuilderList().get(1).getPoint();

			// 修改为奖马获得的分
			event.getSubScoresBuilderList().get(0).setPoint(houseScore * houseNum);
			// 删除奖马分值（可能不需要显示）
			event.removeSubScores(index);
		}
	}
}
