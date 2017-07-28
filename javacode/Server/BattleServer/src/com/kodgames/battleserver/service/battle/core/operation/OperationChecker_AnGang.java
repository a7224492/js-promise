package com.kodgames.battleserver.service.battle.core.operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * 检测是否可执行"暗杠"操作, 如果返回对应操作
 */
public class OperationChecker_AnGang extends OperationCheckerBase
{
	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		super.createFromContext(context);

		// 暗杠的前置操作为OPERATE_DEAL杠的牌
		OperationFilter operationFilter = new OperationFilter_LastOperator();
		operationFilter.createFromContext(CreateContextHelper.createOperationFilter_LastOP(MahjongConstant.PlayType.OPERATE_DEAL));
		preFilters.add(operationFilter);
	}

	@Override
	public List<Step> doCheck(BattleBean context, int roleId, byte card, boolean phaseDeal)
	{
		// 不是玩家自己摸牌，不可以暗杠
		if (!phaseDeal)
			return null;

		List<Step> result = new ArrayList<>();
		CardInfo cardInfo = context.getPlayers().get(roleId).getCards();

		// 获取手牌中牌对应的数量
		Map<Byte, Integer> tempCheck = new HashMap<>();
		cardInfo.getHandCards().forEach(c -> {
			if (tempCheck.containsKey(c))
				tempCheck.put(c, tempCheck.get(c) + 1);
			else
				tempCheck.put(c, 1);
		});

		// 如果某一种牌的数量达到4张，可以暗杠
		tempCheck.entrySet().stream().filter(entry -> entry.getValue() == 4).forEach(entry -> result.add(new Step(roleId, PlayType.OPERATE_CAN_AN_GANG, entry.getKey())));

		return result;
	}
}