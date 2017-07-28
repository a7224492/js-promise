package com.kodgames.battleserver.service.battle.core.operation.filter;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;

/**
 * 当前牌池来留有足够的剩余牌, 不包含保留牌
 */
public class OperationResultFilter_CardPoolHasEnoughtStayCard extends OperationResultFilter
{
	@Override
	public boolean filter(BattleBean context, Step result, byte card, boolean phaseDeal)
	{
		return context.getCardPool().getCards().size() > context.getCardPool().getStayCount();
	}
}
