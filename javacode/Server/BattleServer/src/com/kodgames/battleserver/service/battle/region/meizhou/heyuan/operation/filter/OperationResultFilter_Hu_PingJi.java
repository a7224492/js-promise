package com.kodgames.battleserver.service.battle.region.meizhou.heyuan.operation.filter;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.BattleHelper;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationResultFilter;

/**
 * 河源平鸡胡牌
 */
public class OperationResultFilter_Hu_PingJi extends OperationResultFilter
{

	@Override
	public boolean filter(BattleBean context, Step result, byte card, boolean phaseDeal)
	{
		// 算分(这个原来临时计算现在牌型的牌型分的，因为只有在点完胡之后，牌型分才会计算出来，但是现在是在点胡之前的可以胡状态，如果是胡之后就可以不通过这个来计算牌型分了，可以在result这个玩家id得到玩家身上的牌型分)
		ScoreData scoreData = BattleHelper.getInstance().getHuScoreProcessor().process(context, result.getRoleId(), phaseDeal ? 0 : card, false);
		if (scoreData == null || scoreData.getPoints().size() == 0)
		{
			return false;
		}

		// 平胡牌型
		boolean hasPingHu = false;

		// 添加平胡
		for (ScorePoint point : scoreData.getPoints())
		{
			if (point.getScoreType() == PlayType.HU_PING_HU)
			{
				hasPingHu = true;
			}
			else if (point.getScoreType() == PlayType.HU_HUN_YI_SE)
			{
				return true;
			}
			else if (point.getScoreType() == PlayType.HU_PENG_PENG_HU)
			{
				return true;
			}
			else if (point.getScoreType() == PlayType.HU_QING_YI_SE)
			{
				return true;
			}
			else if (point.getScoreType() == PlayType.HU_HUN_PENG)
			{
				return true;
			}
			else if (point.getScoreType() == PlayType.HU_DA_GE)
			{
				return true;
			}
			else if (point.getScoreType() == PlayType.HU_HUN_YAO_JIU)
			{
				return true;
			}
			else if (point.getScoreType() == PlayType.HU_QING_YAO_JIU)
			{
				return true;
			}
			else if (point.getScoreType() == PlayType.HU_ZI_YI_SE)
			{
				return true;
			}

		}

		// 不是鸡胡的通过
		if (!hasPingHu)
		{
			return true;
		}

		return false;
	}

}
