package com.kodgames.battleserver.service.battle.region.meizhou.meixianzhuang.operation.filter;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.BattleHelper;
import com.kodgames.battleserver.service.battle.core.check.CheckHelper;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationResultFilter;

public class OperationResultFilter_Hu_DieJiaChiHu extends OperationResultFilter
{
	@Override
	public boolean filter(BattleBean context, Step result, byte card, boolean phaseDeal)
	{

		Macro.AssetFalse(result.getPlayType() == PlayType.OPERATE_CAN_HU, "在不是可以胡的时候进行了检测");

		// 算分(这个原来临时计算现在牌型的牌型分的，因为只有在点完胡之后，牌型分才会计算出来，但是现在是在点胡之前的可以胡状态，如果是胡之后就可以不通过这个来计算牌型分了，可以在result这个玩家id得到玩家身上的牌型分)
		ScoreData scoreData = BattleHelper.getInstance().getHuScoreProcessor().process(context, result.getRoleId(), phaseDeal ? 0 : card, false);
		if (scoreData == null || scoreData.getPoints().size() == 0)
		{
			return false;
		}

		// 过滤后剩下可以胡牌类型
		if (result.getPlayType() != PlayType.OPERATE_CAN_HU)
		{
			return true;
		}
		// 过滤后剩下除了自摸以后的胡牌类型
		if (phaseDeal)
		{
			return true;
		}

		// 让抢杠胡通过
		{
			// 前一个操作是其他玩家补杠
			Step lastStep = context.getLastRecordStep(0);
			if (lastStep.getPlayType() == PlayType.OPERATE_BU_GANG_A_CARD)
				return true;
		}

		// 让地胡可以点胡通过
		{
			if (context.getRecords().size() == 2)
				return true;
		}

		// 是否可以点胡
		boolean isCanDianHu = false;

		// 总的牌型分
		int totalPoint = 0;

		// 让十三幺，全风头，全幺九，坎坎胡，双豪华，三豪华，十八罗汉牌型通过吃胡
		{
			for (ScorePoint point : scoreData.getPoints())
			{
				if (point.getScoreType() == PlayType.HU_SHI_SAN_YAO)
				{
					return true;
				}
				else if (point.getScoreType() == PlayType.HU_ZI_YI_SE)
				{
					return true;
				}
				else if (point.getScoreType() == PlayType.HU_QING_YAO_JIU)
				{
					return true;
				}
				else if (point.getScoreType() == PlayType.HU_SHUANG_HAO_HUA_QI_DUI)
				{
					return true;
				}
				else if (point.getScoreType() == PlayType.HU_SAN_HAO_HUA_QI_DUI)
				{
					return true;
				}
				else if (point.getScoreType() == PlayType.HU_SI_GANG)
				{
					return true;
				}
				// 坎坎胡只单吊点炮胡
				else if (point.getScoreType() == PlayType.HU_KAN_KAN_HU)
				{
					// 前一个操作是其他玩家打牌
					Step lastStep = context.getLastRecordStep(0);
					if (lastStep.getPlayType() == PlayType.OPERATE_PLAY_A_CARD)
					{
						isCanDianHu = isHuDanDiao(context, result.getRoleId(), lastStep);
						return isCanDianHu;
					}
				}
				else if (point.getScoreType() == PlayType.HU_HAO_HUA_QI_DUI)
				{
					totalPoint += point.getScoreValue();
				}
				else if (point.getScoreType() == PlayType.HU_QING_YI_SE)
				{
					totalPoint += point.getScoreValue();
				}
				else if (point.getScoreType() == PlayType.HU_HUN_YAO_JIU)
				{
					totalPoint += point.getScoreValue();
				}
			}
		}

		// 大于20的可以点胡
		if (totalPoint >= 20)
		{
			return true;
		}

		// 其他吃胡的就不通过了
		return false;
	}

	/**
	 * 判断是否可以点炮单吊
	 */
	private boolean isHuDanDiao(BattleBean context, int roleId, Step lastStep)
	{
		byte[] allCardCountArray = CheckHelper.converToAllCardCountArray(context.getPlayerById(roleId).getCards());
		for (byte i = 0; i < allCardCountArray.length; i++)
		{
			// 为单吊将才能地胡
			if (allCardCountArray[i] == 1)
			{
				if (i == lastStep.getCards().get(0))
				{
					return true;
				}
			}
		}

		return false;
	}

}
