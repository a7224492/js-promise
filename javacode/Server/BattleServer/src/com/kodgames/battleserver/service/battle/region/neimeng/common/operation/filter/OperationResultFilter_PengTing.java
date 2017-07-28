package com.kodgames.battleserver.service.battle.region.neimeng.common.operation.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.check.CheckHelper;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationResultFilter;
import com.kodgames.battleserver.service.battle.region.neimeng.common.Rules_NeiMeng;

/**
 * 碰牌上听操作过滤器
 */
public class OperationResultFilter_PengTing extends OperationResultFilter
{

	@Override
	public boolean filter(BattleBean context, Step result, byte card, boolean phaseDeal)
	{
		// 不是碰牌就通过，可能是杠牌
		if (phaseDeal || result.getPlayType() != PlayType.OPERATE_CAN_PENG_A_CARD)
			return true;

		int roleId = result.getRoleId();
		CardInfo cardInfo = context.getPlayers().get(roleId).getCards();
		List<Byte> handCards = cardInfo.getHandCards();
		List<Step> cardHeap = cardInfo.getCardHeap();

		// 去掉要碰牌的手牌，检测剩余手牌（碰牌只有一张，需要删两张）
		List<Byte> pengHandCards = result.copy().getCards();
		pengHandCards.add(pengHandCards.get(0));
		pengHandCards.forEach(pengCard -> handCards.remove(pengCard));

		// 伪造碰牌Step
		Step pengStep = new Step();
		pengStep.setPlayType(PlayType.OPERATE_PENG_A_CARD);
		pengStep.getCards().addAll(pengHandCards);
		pengStep.getCards().add(pengHandCards.get(0)); // 碰牌需要三张
		cardHeap.add(pengStep);

		// 如果可以上听，通过检测
		boolean canTing = false;
		List<Byte> oldHandCards = new ArrayList<>(handCards);
		for (Byte playCard : oldHandCards)
		{
			handCards.remove(playCard);
			boolean ting = CheckHelper.checkTing(context, roleId);
			handCards.add(playCard);
			if (ting)
			{
				canTing = true;
				break;
			}
		}

		// 复原牌型
		handCards.addAll(pengHandCards);
		cardHeap.remove(cardHeap.size() - 1);

		// 如果有摊八张玩法，需要做特殊判定
		if (canTing == true && context.getGameRules().contains(Rules_NeiMeng.TAN_BA_ZHANG))
		{
			// 获取玩家手中所有牌，包括吃碰杠，再加上要碰的牌
			List<Byte> allHandCards = new ArrayList<>(handCards);
			allHandCards.add(card);
			if (cardInfo.getCardHeap().size() > 0)
			{
				cardInfo.getCardHeap().forEach(step -> {
					allHandCards.addAll(step.getCards());
				});
			}

			// 满足摊八张条件的花色
			Map<CardType, Integer> typeCount = new HashMap<>();
			{
				typeCount.put(CardType.WAN, 0);
				typeCount.put(CardType.TIAO, 0);
				typeCount.put(CardType.TONG, 0);
				typeCount.put(CardType.FENG, 0);
			}
			Set<CardType> cardType = typeCount.keySet();

			// 分别获取手中每种花色的牌数量
			for (CardType type : cardType)
			{
				for (Byte handCard : allHandCards)
				{
					if (CardType.getCardType(handCard) == type)
						typeCount.put(type, typeCount.get(type) + 1);
				}
			}

			for (Map.Entry<CardType, Integer> map : typeCount.entrySet())
			{
				// 若有某种花色的牌数量大于七张，可碰牌
				if (map.getValue() > 7)
				{
					canTing = true;
					return canTing;
				}
				// 若有某种花色的牌数量等于七张，则需要判断碰牌后可打出的牌是否包含这种花色，如果包含，则不能打出，否则胡牌时手牌无法满足摊八张的条件
				else if (map.getValue() == 7)
				{
					// 获取可打出的手牌
					List<Byte> canPlayCard = new ArrayList<>();
					List<Byte> copyAllHandCards = new ArrayList<>(allHandCards);
					for (Byte removeCard : copyAllHandCards)
					{
						allHandCards.remove(removeCard);
						boolean ting = CheckHelper.checkTing(context, roleId);
						if (ting)
							canPlayCard.add(removeCard);
						allHandCards.add(removeCard);
					}
					// 判定可打手牌中是当前判断的花色的数量
					List<Byte> copyPlayCard = new ArrayList<>();
					canPlayCard.forEach(playCard -> {
						if (CardType.getCardType(playCard) == map.getKey())
							copyPlayCard.add(playCard);
					});
					// 若可打出的手牌全部为当前判断的花色，则不可碰，否则碰后上听，再打出一张牌后，便无法满足摊八张的条件
					if (canPlayCard.size() == copyPlayCard.size())
					{
						canTing = false;
					}
					else
					{
						canTing = true;
						return canTing;
					}
				}
				// 若此花色的牌数量小于七张，碰牌后无法满足摊八张条件，则无法上听
				else
					canTing = false;
			}
		}

		return canTing;
	}

}
