package com.kodgames.battleserver.service.battle.region.guangdong.jiexi.score.battle;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalcluateContext;
import com.kodgames.battleserver.service.battle.region.guangdong.chaozhou.score.battle.BattleScoreCalculator_ChaoZhou;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultEventPROTO;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultScorePROTOOrBuilder;

/**
 * 揭西的算分
 * 
 * @author 毛建伟
 */
public class BattleScoreCalculator_JieXi extends BattleScoreCalculator_ChaoZhou
{
	public static final String KEY_ISJIHU = "key_isJihu";

	private int ziMoBaseScore = 2;

	private int dianPaoBaseScore = 1;

	private boolean isJiHu = false;

	private static List<Integer> dianPaoTypes = new ArrayList<Integer>();

	static
	{
		dianPaoTypes.add(PlayType.HU_DIAN_PAO);
	}

	/**
	 * 构造计算所需的数据
	 */
	public void createFromContext(JSONObject context)
		throws Exception
	{
		super.createFromContext(context);
		isJiHu = CreateContextParser.getBoolean(context, KEY_ISJIHU);
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
			int baseScore = dianPaoTypes.contains(event.getScore().getType()) ? dianPaoBaseScore : ziMoBaseScore;
			// 如果不是鸡胡，需要添加底分，否则设置底分为自摸底分
			if (!isJiHu)
			{
				score += baseScore;
			}
			else
				baseScore = ziMoBaseScore;
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
			int maxScore = baseScore * maxValue;
			// 算分
			score = (((score - otherAdd) * context.totalMulti / multi) + otherAdd) * multi;
			// 封顶
			if (maxScore != 0)
				score = Math.min(score, maxScore);

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
		// 七对的比较
		if (checkHasQiDui(l))
			return 1;
		if (checkHasQiDui(r))
			return -1;
		// 鸡胡比较加分
		if (isJiHu)
		{
			return l.calculatedScore.totalAdd - r.calculatedScore.totalAdd;
		}
		// 比较乘分
		return l.calculatedScore.totalMulti - r.calculatedScore.totalMulti;
	}

	/**
	 * 检测牌型中是否有七对
	 * 
	 * @param score 分数信息
	 * @return
	 */
	private boolean checkHasQiDui(HuScoreCheckContext score)
	{
		for (ScorePoint point : score.scoreData.getPoints())
		{
			if (point.getScoreType() == PlayType.HU_QI_DUI)
				return true;
		}

		return false;
	}
}
