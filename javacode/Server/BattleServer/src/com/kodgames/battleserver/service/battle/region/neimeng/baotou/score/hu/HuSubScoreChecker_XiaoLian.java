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
 * 牌型算分：小连
 * 
 * 小连，1分。（胡牌时，组成胡牌牌型的两个同花色顺子以234567或345678的形式存在，如果只是包含这两个顺子，不算小连）。
 */
public class HuSubScoreChecker_XiaoLian extends HuSubScoreChecker
{
	public HuSubScoreChecker_XiaoLian()
	{
		super(PlayType.HU_XIAO_LIAN);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 收集牌型中的各花色的坎牌最小索引
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

		// 如果任一花色的坎牌最小索引中同时有2和5，或3和6，即为小连
		boolean result = false;
		for (Set<Integer> cards : kanCards.values())
		{
			if ((cards.contains(1) && cards.contains(4)) || (cards.contains(2) && cards.contains(5)))
			{
				addScore(inoutHuContext.scoreData);
				result = true;
			}
		}

		return result;
	}
}
