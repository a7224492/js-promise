package com.kodgames.battleserver.service.battle.core.operation;

import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationFilter;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationFilter_LastOperator;

import net.sf.json.JSONObject;

/**
 * 检测是否可执行"碰"操作, 如果返回对应操作
 */
public class OperationChecker_Peng extends OperationCheckerBase
{
	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		super.createFromContext(context);

		// 碰牌的前置操作为OPERATE_PLAY_A_CARD碰的牌
		OperationFilter operationFilter = new OperationFilter_LastOperator();
		operationFilter.createFromContext(CreateContextHelper.createOperationFilter_LastOP(MahjongConstant.PlayType.OPERATE_PLAY_A_CARD));
		preFilters.add(operationFilter);

		// 碰牌的前置操作为OPERATE_TING_CARD碰的牌
		operationFilter = new OperationFilter_LastOperator();
		operationFilter.createFromContext(CreateContextHelper.createOperationFilter_LastOP(MahjongConstant.PlayType.OPERATE_TING_CARD));
		preFilters.add(operationFilter);
	}

	@Override
	public List<Step> doCheck(BattleBean context, int roleId, byte card, boolean phaseDeal)
	{
		if (phaseDeal)
			return null;

		// 获取手牌中要碰的牌的数量
		List<Step> result = new ArrayList<>();
		List<Byte> handCards = context.getPlayers().get(roleId).getCards().getHandCards();
		int count = 0;
		for (byte c : handCards)
		{
			if (c == card)
				count++;
		}

		// 大于一个可以碰
		if (count > 1)
			result.add(new Step(roleId, PlayType.OPERATE_CAN_PENG_A_CARD, card));

		return result;
	}
}
