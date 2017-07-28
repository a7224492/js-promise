package com.kodgames.battleserver.service.battle.region.guangdong.huilai.score.battle;

import net.sf.json.JSONObject;

import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalcluateContext;
import com.kodgames.battleserver.service.battle.region.guangdong.common.score.battle.BattleScoreCalculator_GuangDong;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultEventPROTO;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultScorePROTOOrBuilder;

/**
 * 惠来玩法计分规则
 */
public class BattleScoreCalculator_GuangDong_HuiLai extends BattleScoreCalculator_GuangDong
{
	/**
	 * 底分配置
	 */
	public static final String KEY_BASE_SCORE = "baseScore";

	/**
	 * 底分数值
	 */
	private int baseScore = 0;

	/**
	 * 构造计算所需的数据
	 */
	public void createFromContext(JSONObject context)
		throws Exception
	{
		super.createFromContext(context);
		baseScore = CreateContextParser.getInt(context, KEY_BASE_SCORE);
	}

	/**
	 * 计算牌局个人得分
	 * 
	 * 不同玩法算分方式不同, 可以重载这个函数
	 */
	@Override
	protected int calculateScore(ResultEventPROTO.Builder event, ScoreCalcluateContext context, int maxValue)
	{
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

			// 平胡的情况下不加底分
			if (score - otherAdd == 0)
				score += baseScore;
			int maxScore = (score - otherAdd) * maxValue;

			// 算分
			score = (((score - otherAdd) * context.totalMulti / multi) + otherAdd) * multi;
			// 是否有底分
			if (maxValue != 0)
			{
				score = Math.min(score, maxScore);
			}

			score *= context.totalMulti2nd;
		}

		// 是否为杠分，杠分需要算奖马的分
		if (PlayType.isGangOperator(type))
			score *= context.totalMulti2nd;

		return score;
	}

	/**
	 * 比较两个HuScoreCheckContext
	 */
	@Override
	public int compairScoreContext(HuScoreCheckContext l, HuScoreCheckContext r)
	{
		return l.calculatedScore.totalMulti - r.calculatedScore.totalMulti;
	}
}
