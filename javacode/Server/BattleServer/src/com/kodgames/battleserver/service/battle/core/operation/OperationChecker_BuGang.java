package com.kodgames.battleserver.service.battle.core.operation;

import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationFilter;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationFilter_LastOperator;

import net.sf.json.JSONObject;

/**
 * 检测是否可执行"补杠"操作, 如果返回对应操作
 */
public class OperationChecker_BuGang extends OperationCheckerBase
{
	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		super.createFromContext(context);

		// 补杠的前置操作为OPERATE_DEAL杠的牌
		OperationFilter operationFilter = new OperationFilter_LastOperator();
		operationFilter.createFromContext(CreateContextHelper.createOperationFilter_LastOP(MahjongConstant.PlayType.OPERATE_DEAL));
		preFilters.add(operationFilter);
	}

	@Override
	public List<Step> doCheck(BattleBean context, int roleId, byte card, boolean phaseDeal)
	{
		// 不是玩家自己摸牌，不可以补杠
		if (!phaseDeal)
			return null;

		List<Step> result = new ArrayList<>();
		CardInfo cardInfo = context.getPlayers().get(roleId).getCards();

		for (Step step : cardInfo.getCardHeap())
		{
			if (step.getPlayType() != PlayType.OPERATE_PENG_A_CARD)
				continue;

			// 玩家手中已经有碰牌，摸到所碰牌可以补杠
			byte pengCard = step.getCards().get(0);
			if (cardInfo.getHandCards().contains(pengCard))
				result.add(new Step(roleId, PlayType.OPERATE_CAN_BU_GANG_A_CARD, pengCard));
		}

		return result;
	}
}