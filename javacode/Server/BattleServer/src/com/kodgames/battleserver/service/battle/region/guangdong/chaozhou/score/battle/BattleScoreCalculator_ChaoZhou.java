package com.kodgames.battleserver.service.battle.region.guangdong.chaozhou.score.battle;

import net.sf.json.JSONObject;

import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalcluateContext;
import com.kodgames.battleserver.service.battle.region.guangdong.common.score.battle.BattleScoreCalculator_GuangDong;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultEventPROTO;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultScorePROTO;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultScorePROTOOrBuilder;

public class BattleScoreCalculator_ChaoZhou extends BattleScoreCalculator_GuangDong
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
			// 是否超越底分
			if (score < baseScore * maxValue)
			{
				score += baseScore;
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
				int maxScore = (score - otherAdd) * maxValue;
				// 算分
				score = (((score - otherAdd) * context.totalMulti / multi) + otherAdd) * multi;
				// 封顶
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
	 * 修改客户端显示，胡牌分中如果有门清，将门清放到第一位 重载方法
	 * 
	 * @param event
	 */
	@Override
	protected void modifyScoreFromClientDisplay(ResultEventPROTO.Builder event)
	{
		super.modifyScoreFromClientDisplay(event);

		// 获取判断门清的位置
		int index = -1;

		// 找到门清的位置
		for (int i = 0; i < event.getSubScoresOrBuilderList().size(); i++)
		{
			if (!PlayType.isHuType(event.getSubScoresOrBuilderList().get(i).getType()))
				continue;

			if (event.getSubScoresOrBuilderList().get(i).getType() == PlayType.HU_MEN_QING)
				index = i;
		}

		// 没有门清
		if (index == -1)
			return;

		// 复制一个门清的显示，并删除掉门清
		ResultScorePROTO.Builder subScoreBuilder = event.getSubScoresBuilder(index);
		event.removeSubScores(index);

		// 将门清位置放置在第一位
		event.addSubScores(0, subScoreBuilder.build());
	}

	/**
	 * 比较两个HuScoreCheckContext
	 */
	@Override
	public int compairScoreContext(HuScoreCheckContext l, HuScoreCheckContext r)
	{
		// 天胡地胡的情况下没有倍数
		if (l.calculatedScore.totalAdd >= 40)
			return 1;
		else if (r.calculatedScore.totalAdd >= 40)
			return -1;
		return l.calculatedScore.totalMulti - r.calculatedScore.totalMulti;
	}
}
