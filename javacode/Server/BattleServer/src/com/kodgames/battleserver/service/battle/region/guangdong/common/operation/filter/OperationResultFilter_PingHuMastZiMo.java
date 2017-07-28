package com.kodgames.battleserver.service.battle.region.guangdong.common.operation.filter;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.BattleHelper;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationResultFilter;

/**
 * 鸡胡不能吃胡 在大胡规则中，如果牌型为鸡胡则只能自摸
 */
public class OperationResultFilter_PingHuMastZiMo extends OperationResultFilter
{
	@Override
	public boolean filter(BattleBean context, Step result, byte card, boolean phaseDeal)
	{
		Macro.AssetFalse(result.getPlayType() == PlayType.OPERATE_CAN_HU);

		// 如果是自摸不做后续检测
		if (phaseDeal)
			return true;
		// 抢杠胡不受限制
		// TODO：下次更新上线
		Step lastStep = context.getLastRecordStep(0);
		if (lastStep.getPlayType() == PlayType.OPERATE_BU_GANG_A_CARD)
			return true;
		// 计算分数
		ScoreData scoreData = BattleHelper.getInstance().getHuScoreProcessor().process(context, result.getRoleId(), card, true);

		// 计算分数为空，或者分数没有收分项，或者不是平胡
		if (scoreData == null || scoreData.getPoints().size() == 0 || scoreData.getPoints().get(0).getScoreType() == PlayType.HU_PING_HU)
			return false;

		return true;
	}

}
