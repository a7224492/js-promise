package com.kodgames.battleserver.service.battle.region.meizhou.huizhou.operation.filter;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.BattleHelper;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationResultFilter;

public class OperationResultFilter_Hu_HuiZhou extends OperationResultFilter
{
	@Override
	public boolean filter(BattleBean context, Step result, byte card, boolean phaseDeal)
	{
		// 胡的牌型种类
		int huTypeCount = 0;

		// 是否是平胡
		boolean hasPingHu = false;

		Macro.AssetFalse(result.getPlayType() == PlayType.OPERATE_CAN_HU, "在不是可以胡的时候进行了检测");

		// 算分(这个原来临时计算现在牌型的牌型分的，因为只有在点完胡之后，牌型分才会计算出来，但是现在是在点胡之前的可以胡状态，如果是胡之后就可以不通过这个来计算牌型分了，可以在result这个玩家id得到玩家身上的牌型分)
		ScoreData scoreData = BattleHelper.getInstance().getHuScoreProcessor().process(context, result.getRoleId(), phaseDeal ? 0 : card, false);
		if (scoreData == null || scoreData.getPoints().size() == 0)
		{
			return false;
		}

		// 自摸通过，剩下抢杠胡和吃胡
		if (phaseDeal)
		{
			return true;
		}

		for (ScorePoint point : scoreData.getPoints())
		{
			if (PlayType.isHuType(point.getScoreType()))
			{
				huTypeCount++;
			}
			if (point.getScoreType() == PlayType.HU_PING_HU)
			{
				hasPingHu = true;
			}
		}

		// 平胡吃胡不通过
		if (huTypeCount == 1 && hasPingHu)
		{
			return false;
		}

		return true;
	}
}
