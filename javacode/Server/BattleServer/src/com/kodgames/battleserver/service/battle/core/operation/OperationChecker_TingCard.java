package com.kodgames.battleserver.service.battle.core.operation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.check.CheckHelper;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationFilter;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationFilter_LastOperator;

import net.sf.json.JSONObject;

/**
 * 听牌检测，检测玩家时候可以出牌听
 *
 */
public class OperationChecker_TingCard extends OperationCheckerBase
{

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		super.createFromContext(context);

		OperationFilter operationFilter = new OperationFilter_LastOperator();
		operationFilter.createFromContext(CreateContextHelper.createOperationFilter_LastOP(MahjongConstant.PlayType.OPERATE_DEAL));
		preFilters.add(operationFilter);
	}

	@Override
	public List<Step> doCheck(BattleBean context, int roleId, byte card, boolean phaseDeal)
	{
		List<Step> result = new ArrayList<>();
		List<Byte> allTingCards = new ArrayList<>();

		if (context.hasCardHeap(roleId, PlayType.OPERATE_TING_CARD))
			return result;

		// 存储所有可以听的牌
		CardInfo cardInfo = context.getPlayers().get(roleId).getCards();
		cardInfo.getHandCards().stream().collect(Collectors.toList()).forEach(handCard -> {
			cardInfo.getHandCards().remove(Byte.valueOf(handCard));
			if (CheckHelper.checkTing(context, roleId))
				allTingCards.add(handCard);
			cardInfo.getHandCards().add(handCard);
		});

		if (!allTingCards.isEmpty())
			result.add(new Step(roleId, PlayType.OPERATE_CAN_TING_CARD, allTingCards));

		return result;
	}
}
