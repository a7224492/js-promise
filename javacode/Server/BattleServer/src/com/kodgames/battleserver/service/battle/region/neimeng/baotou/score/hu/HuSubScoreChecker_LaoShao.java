package com.kodgames.battleserver.service.battle.region.neimeng.baotou.score.hu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.CardGroupType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 牌型算分：老少
 * 
 * 老少，1分。（胡牌时，组成胡牌牌型的两个同花色顺子以123789的形式存在，如果只是包含这两个顺子，不算老少）。
 */
public class HuSubScoreChecker_LaoShao extends HuSubScoreChecker
{
	public HuSubScoreChecker_LaoShao()
	{
		super(PlayType.HU_LAO_SHAO);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 收集牌型中的各花色的坎牌
		Map<CardType, Set<Integer>> kanCards = new HashMap<>();
		inoutHuContext.scoreData.getCardGroups().forEach(group -> {
			int groupType = group.getGroupType();
			if (groupType != CardGroupType.KAN && groupType != CardGroupType.AN_KAN)
				return;

			// 获取当前花色的坎牌最小索引集合
			List<Byte> cardList = group.getCardList();
			Macro.AssetTrue(null == cardList || cardList.size() != 3);
			CardType cardType = CardType.getCardType(cardList.get(0));
			Set<Integer> minIndexSet = kanCards.get(cardType);
			if (null == minIndexSet)
			{
				minIndexSet = new HashSet<>();
				kanCards.put(cardType, minIndexSet);
			}

			// 将当前坎牌最小值加入到相应花色的坎牌最小索引集合中
			List<Byte> addCards = new ArrayList<>(cardList);
			Collections.sort(addCards);
			byte minIndex = addCards.get(0);
			int index = CardType.convertToCardIndex(minIndex);
			minIndexSet.add(index);
		});

		// 如果任一花色的坎牌最小索引中同时有0和6，即为老少
		boolean result = false;
		for (Set<Integer> cards : kanCards.values())
		{
			if (cards.contains(0) && cards.contains(6))
			{
				addScore(inoutHuContext.scoreData);
				result = true;
			}
		}

		return result;
	}
}
