package com.kodgames.battleserver.service.battle.region.neimeng.common.playcard.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.check.CheckHelper;
import com.kodgames.battleserver.service.battle.core.playcard.filter.PlayCardFilter;
import com.kodgames.battleserver.service.battle.region.neimeng.common.Rules_NeiMeng;

/**
 * 明杠之后需要进入上听, 只能打出能听的牌
 */
public class PlayCardFilter_GangEnterTing extends PlayCardFilter
{

	@Override
	public List<Byte> filterCard(BattleBean context, int roleId, List<Byte> cards)
	{
		List<Step> records = context.getRecords();
		if (records.size() < 2)
			return cards;

		// 倒数第一步应该是摸牌，否则打什么都可以
		Step lastStep = records.get(records.size() - 1);
		if (lastStep.getRoleId() != roleId || lastStep.getPlayType() != PlayType.OPERATE_DEAL)
			return cards;

		// 倒数第二步应该是明杠，否则打什么都可以
		Step preStep = records.get(records.size() - 2);
		int prePlayType = preStep.getPlayType();
		if (preStep.getRoleId() != roleId || (prePlayType != PlayType.OPERATE_GANG_A_CARD && prePlayType != PlayType.OPERATE_BU_GANG_A_CARD))
			return cards;

		// 如果某张牌不在手中就无法上听，这张牌就不能被打出
		List<Byte> handCards = context.getPlayers().get(roleId).getCards().getHandCards();
		List<Byte> oldCards = new ArrayList<>(cards);
		for (Byte card : oldCards)
		{
			handCards.remove(card);
			boolean ting = CheckHelper.checkTing(context, roleId);
			handCards.add(card);
			if (ting == false)
				cards.remove(card);
		}

		// 如果有摊八张玩法，需要做特殊判定
		if (context.getGameRules().contains(Rules_NeiMeng.TAN_BA_ZHANG))
		{
			Map<CardType, Integer> typeCount = new HashMap<>();
			{
				typeCount.put(CardType.WAN, 0);
				typeCount.put(CardType.TIAO, 0);
				typeCount.put(CardType.TONG, 0);
				typeCount.put(CardType.FENG, 0);
			}
			Set<CardType> cardType = typeCount.keySet();

			// 获取玩家带吃碰杠的全部手牌
			List<Byte> copyHandCards = new ArrayList<>(handCards);
			List<Step> cardHeap = context.getPlayers().get(roleId).getCards().getCardHeap();
			cardHeap.forEach(step -> {
				copyHandCards.addAll(step.getCards());
			});

			// 统计手牌中各种花色的牌的数量
			for (CardType type : cardType)
			{
				for (Byte handCard : copyHandCards)
				{
					if (CardType.getCardType(handCard) == type)
						typeCount.put(type, typeCount.get(type) + 1);
				}
			}

			// 去掉导致上听的吃碰杠的牌
			cardHeap.get(cardHeap.size() - 1).getCards().forEach(card -> {
				copyHandCards.remove(card);
			});

			for (Map.Entry<CardType, Integer> map : typeCount.entrySet())
			{
				// 如果当前判定的花色的手牌数量大于七张，则可以打出任意一张不会破坏牌型的牌
				if (map.getValue() > 7)
					return cards;
				// 如果当前判定的花色的手牌数量等于七张，则不能打出此花色的牌
				else if (map.getValue() == 7)
				{
					cards.forEach(playCard -> {
						if (CardType.getCardType(playCard) == map.getKey())
							cards.remove(playCard);
					});
				}
			}
		}

		return cards;
	}

}
