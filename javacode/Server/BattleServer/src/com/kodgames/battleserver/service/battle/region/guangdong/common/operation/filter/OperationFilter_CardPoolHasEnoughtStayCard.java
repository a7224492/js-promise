package com.kodgames.battleserver.service.battle.region.guangdong.common.operation.filter;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationResultFilter;

import net.sf.json.JSONObject;

/**
 * 当前牌池来留有足够的剩余牌
 */
public class OperationFilter_CardPoolHasEnoughtStayCard extends OperationResultFilter
{
	public void createFromContext(JSONObject context)
		throws Exception
	{
	}

	@Override
	public boolean filter(BattleBean context, Step result, byte card, boolean phaseDeal)
	{
		return context.getCardPool().getCards().size() > context.getCardPool().getStayCount();
	}
}
