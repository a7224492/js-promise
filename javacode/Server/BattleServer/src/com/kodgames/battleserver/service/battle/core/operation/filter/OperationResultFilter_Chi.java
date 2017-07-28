package com.kodgames.battleserver.service.battle.core.operation.filter;

import java.util.List;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;

/**
 * 吃牌操作过滤器
 */
public class OperationResultFilter_Chi extends OperationResultFilter
{

	@Override
	public boolean filter(BattleBean context, Step result, byte card, boolean phaseDeal)
	{
		// 跳过发牌
		if (phaseDeal)
			return true;

		// 跳过非can吃操作
		if (result.getPlayType() != PlayType.OPERATE_CAN_CHI_A_CARD)
			return true;

		// 获取操作记录
		List<Step> records = context.getRecords();

		// 只能吃上家牌
		Step step = records.get(records.size() - 1);
		if (step.getRoleId() != context.getPreRoleId(result.getRoleId()))
			return false;

		// 获取操作记录
		PlayerInfo player = context.getPlayers().get(result.getRoleId());
		Macro.AssetTrue(null == player);
		List<Step> cardHeap = player.getCards().getCardHeap();
		Macro.AssetTrue(null == cardHeap);

		// 已上听不能吃牌
		if (cardHeap.stream().filter(s -> s.getPlayType() == PlayType.DISPLAY_TING).count() > 0)
			return false;

		return true;
	}

}
