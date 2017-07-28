package com.kodgames.battleserver.service.battle.core.operation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.BattleConst;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationFilter;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationFilter_LastOperator;

import net.sf.json.JSONObject;

/**
 * 检测是否可执行"吃"操作, 如果返回对应操作
 */
public class OperationChecker_Chi extends OperationCheckerBase
{
	/** 是否检测风箭吃牌 */
	public static final String KEY_ENABLE_FENG_JIAN = "enableFengJian";
	protected boolean enableFengJian = false;

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		super.createFromContext(context);

		// 吃手动打出的牌
		OperationFilter operationFilter = new OperationFilter_LastOperator();
		operationFilter.createFromContext(CreateContextHelper.createOperationFilter_LastOP(MahjongConstant.PlayType.OPERATE_PLAY_A_CARD));
		preFilters.add(operationFilter);

		// 吃的前置操作为OPERATE_TING_CARD碰的牌
		operationFilter = new OperationFilter_LastOperator();
		operationFilter.createFromContext(CreateContextHelper.createOperationFilter_LastOP(MahjongConstant.PlayType.OPERATE_TING_CARD));
		preFilters.add(operationFilter);

		if (context.containsKey(KEY_ENABLE_FENG_JIAN))
			enableFengJian = context.getBoolean(KEY_ENABLE_FENG_JIAN);
	}

	@Override
	public List<Step> doCheck(BattleBean context, int roleId, byte card, boolean phaseDeal)
	{
		// 跳过自己的操作
		if (phaseDeal)
			return null;

		// 跳过花牌
		if (CardType.HUA.isBelongTo(card))
			return null;

		// 如果不支持风牌
		if (CardType.FENG.isBelongTo(card) && enableFengJian == false)
			return null;

		// 如果不支持箭牌
		if (CardType.JIAN.isBelongTo(card) && enableFengJian == false)
			return null;

		List<Step> result = new ArrayList<>();
		// 排序手牌
		List<Byte> handCards = context.getPlayers().get(roleId).getCards().getHandCards();
		Collections.sort(handCards);

		// 检测数字牌吃
		checkNumbericChi(roleId, card, handCards, result);

		// 检测风剑牌吃
		checkFengJianChi(roleId, card, handCards, result);

		return result;
	}

	/** 吃检测:数字牌 */
	protected void checkNumbericChi(int roleId, byte card, List<Byte> handCards, List<Step> result)
	{
		// 不是数字牌，不进行检测
		if (CardType.isNumberCard(card) == false)
			return;

		// 记录要吃的牌的类型和索引
		CardType cardType = CardType.getCardType(card);
		int cardIndex = CardType.convertToCardIndex(card);

		// 遍历可能的吃牌序列
		for (int checkIndex = Math.max(0, cardIndex - 2); checkIndex <= cardIndex && checkIndex + 2 < BattleConst.NUMBER_CARD_COUNT; ++checkIndex)
		{
			List<Byte> cards = new ArrayList<Byte>();
			for (int i = 0; i < 3; ++i)
			{
				byte checkCard = cardType.convertToCard(checkIndex + i);
				if (checkCard == card)
					continue;

				if (Collections.binarySearch(handCards, checkCard) >= 0)
					cards.add(checkCard);
			}

			if (cards.size() == 2)
			{
				// 默认要吃的牌在第一张
				cards.add(0, card);

				// 可以构成吃牌操作
				result.add(new Step(roleId, PlayType.OPERATE_CAN_CHI_A_CARD, cards));
			}
		}
	}

	/** 吃检测:风箭牌 */
	protected void checkFengJianChi(int roleId, byte card, List<Byte> handCards, List<Step> result)
	{
		CardType cardType = CardType.getFengJianType(card);
		// 不是风箭牌，不进行检测
		if (cardType == CardType.INVALID)
			return;

		for (byte i = cardType.Value(); i < cardType.MaxValue(); i++)
		{
			if (card == i || Collections.binarySearch(handCards, i) < 0)
				continue;

			for (byte j = (byte)(i + 1); j < cardType.MaxValue(); j++)
			{
				if (j == card || Collections.binarySearch(handCards, j) < 0)
					continue;

				// 可以构成吃牌操作
				Step op = new Step(roleId, PlayType.OPERATE_CAN_CHI_A_CARD);
				op.addCard(card);
				op.addCard(i);
				op.addCard(j);
				result.add(op);
			}
		}
	}
}
