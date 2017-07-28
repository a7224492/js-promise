package com.kodgames.battleserver.service.battle.core.operation.filter;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.BattleHelper;
import com.kodgames.battleserver.service.battle.core.check.CompareType;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalcluateContext;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalculateType;

import net.sf.json.JSONObject;

/**
 * 只能点胡大于等于20积分的牌型（可以是组合起来大于等于20）和抢杠胡
 */
public class OperationResultFilter_ScoreLimit extends OperationResultFilter
{
	public static final String KEY_CALC_TYPE = "calcType";
	// public static final String KEY_compareType = "compareType";
	public static final String KEY_VALUE = "value";

	/** 分数计算类型 */
	private ScoreCalculateType calcType = ScoreCalculateType.NONE;

	/**
	 * 分数比较类型
	 * 
	 * 暂时不配置了
	 */
	private CompareType compareType = CompareType.GreaterEqual;

	/** 分数比较值 */
	private int value;

	public void createFromContext(JSONObject context)
		throws Exception
	{
		calcType = CreateContextParser.getScoreCalculateType(context, KEY_CALC_TYPE);
		// compareType = CreateContextParser.getCompareType(context, KEY_compareType);
		value = CreateContextParser.getInt(context, KEY_VALUE);
	}

	@Override
	public boolean filter(BattleBean context, Step result, byte card, boolean phaseDeal)
	{
		// 自摸不做分数检测
		if (phaseDeal)
			return true;

		// 抢杠胡不做分数检测
		Step lastRecord = context.getRecords().get(context.getRecords().size() - 1);
		if (lastRecord.getPlayType() == PlayType.OPERATE_BU_GANG_A_CARD)
			return true;

		// 计算分数,当是自摸的时候不把胡的那张卡传进process
		ScoreData scoreData = BattleHelper.getInstance().getHuScoreProcessor().process(context, result.getRoleId(), phaseDeal ? 0 : card, false);
		if (scoreData == null)
			return false;

		// 计算总翻数
		ScoreCalcluateContext scoreCacluateContext = ScoreCalcluateContext.sumScore(scoreData);
		switch (calcType)
		{
			case TWO_INDEX:
				return compareType.compare(scoreCacluateContext.towIndex, value);

			case TOTAL_ADD:
				return compareType.compare(scoreCacluateContext.totalAdd, value);
				
			case TOTAL_MULTI:
				return compareType.compare(scoreCacluateContext.totalMulti, value);

			default:
				Macro.AssetFalse(false, "Invalid calcType : " + calcType);
				return false;
		}
	}
}