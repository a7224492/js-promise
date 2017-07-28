package com.kodgames.battleserver.service.battle.region.neimeng.common.operation.filter;

import java.util.List;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationResultFilter;

/**
 * 杠牌操作过滤器
 */
public class OperationResultFilter_Gang extends OperationResultFilter
{

	@Override
	public boolean filter(BattleBean context, Step result, byte card, boolean phaseDeal)
	{
		// 跳过非can杠操作
		if (result.getPlayType() != PlayType.OPERATE_CAN_GANG_A_CARD && result.getPlayType() != PlayType.OPERATE_CAN_AN_GANG && result.getPlayType() != PlayType.OPERATE_CAN_BU_GANG_A_CARD)
			return true;

		// 获取操作记录
		PlayerInfo player = context.getPlayers().get(result.getRoleId());
		Macro.AssetTrue(null == player);
		List<Step> records = player.getCards().getCardHeap();
		Macro.AssetTrue(null == records);

		// 已上听不能杠牌
		if (records.stream().filter(s -> s.getPlayType() == PlayType.DISPLAY_TING).count() > 0)
			return false;

		return true;
	}

}
