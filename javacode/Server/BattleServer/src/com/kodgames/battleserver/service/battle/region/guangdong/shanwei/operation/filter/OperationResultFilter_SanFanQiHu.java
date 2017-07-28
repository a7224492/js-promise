package com.kodgames.battleserver.service.battle.region.guangdong.shanwei.operation.filter;

import java.util.List;

import net.sf.json.JSONObject;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.BattleHelper;
import com.kodgames.battleserver.service.battle.core.check.CheckHelper;
import com.kodgames.battleserver.service.battle.core.check.CompareType;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationResultFilter;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalcluateContext;

/**
 * 汕尾计算三番起胡
 */
public class OperationResultFilter_SanFanQiHu extends OperationResultFilter
{

	public static final String KEY_CALC_TYPE = "calcType";

	public static final String KEY_VALUE = "value";

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
		value = CreateContextParser.getInt(context, KEY_VALUE);
	}

	@Override
	public boolean filter(BattleBean context, Step result, byte card, boolean phaseDeal)
	{
		Macro.AssetTrue(result.getPlayType() != PlayType.OPERATE_CAN_HU, "在不是胡的时候进行了三番起胡的检测");

		// 计算牌型分
		ScoreData scoreData = BattleHelper.getInstance().getHuScoreProcessor().process(context, result.getRoleId(), phaseDeal ? 0 : card, false);
		if (scoreData == null)
			return false;

		// 获取正card
		List<Byte> zhengCards = context.getCardHeap(result.getRoleId(), PlayType.DISPLAY_ZHENG_CARD).getCards();

		// 花牌得分
		int huaFen = 0;

		// 字牌得分
		int ziFen = 0;

		// 计算花牌的分，花牌一个一分，正花一个两分
		for (byte huaCard : context.getPlayerById(result.getRoleId()).getCards().getExCards())
		{
			++huaFen;
			if (zhengCards.contains(huaCard))
				++huaFen;
		}

		byte[] allCardCountArray = CheckHelper.converToAllCardCountArray(context.getPlayerById(result.getRoleId()).getCards());
		// 计算风牌的分，风牌刻或杠一个一分，正风刻（杠）一个2分
		for (byte fengCard = CardType.FENG.Value(); fengCard < CardType.FENG.MaxValue(); ++fengCard)
		{
			if (allCardCountArray[fengCard] > 2)
			{
				++ziFen;
				if (zhengCards.contains(fengCard))
					++ziFen;
			}
		}
		// 计算箭牌的分，箭牌刻或杠一个两分
		for (byte jianCard = CardType.JIAN.Value(); jianCard < CardType.JIAN.MaxValue(); ++jianCard)
		{
			if (allCardCountArray[jianCard] > 2)
			{
				ziFen = ziFen + 2;
			}
		}
		
		// 计算总翻数
		ScoreCalcluateContext scoreCacluateContext = ScoreCalcluateContext.sumScore(scoreData);
		// 总的胡牌分
		int huPaiFen = scoreCacluateContext.totalAdd + huaFen + ziFen;
		// 返回比较胡牌分是否大于配置的分数
		return compareType.compare(huPaiFen, value);
	}
}
