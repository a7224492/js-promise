package com.kodgames.battleserver.service.battle.core.score.hu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.BattleConst;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.CardGroupType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;

/**
 * 检测牌形:一条龙
 * 
 * 有一种相同花色的123，456，789三副顺子
 */
public class HuSubScoreChecker_YiTiaoLong extends HuSubScoreChecker
{
	public HuSubScoreChecker_YiTiaoLong()
	{
		super(PlayType.HU_YI_TIAO_LONG);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 收集牌型中的各花色的坎牌
		Map<CardType, Set<Byte>> kanCards = new HashMap<>();
		inoutHuContext.scoreData.getCardGroups().forEach(group -> {
			int groupType = group.getGroupType();
			if (groupType != CardGroupType.KAN && groupType != CardGroupType.AN_KAN)
				return;

			// 获取当前花色的坎牌集合
			List<Byte> cardList = group.getCardList();
			Macro.AssetTrue(null == cardList || cardList.size() != 3);
			CardType cardType = CardType.getCardType(cardList.get(0));
			Set<Byte> cards = kanCards.get(cardType);
			if (null == cards)
			{
				cards = new HashSet<>();
				kanCards.put(cardType, cards);
			}

			// 如果当前坎牌最小值为147，则加入到相应花色的坎牌集合中
			List<Byte> addCards = new ArrayList<>(cardList);
			Collections.sort(addCards);
			byte first = addCards.get(0);
			int index = CardType.convertToCardIndex(first);
			if (index == 0 || index == 3 || index == 6)
				cards.addAll(cardList);
		});

		// 如果任一花色的坎牌有9种各不相同的牌，即为一条龙
		for (Map.Entry<CardType, Set<Byte>> cards : kanCards.entrySet())
		{
			if (cards.getValue().size() == BattleConst.NUMBER_CARD_COUNT)
			{
				addScore(inoutHuContext.scoreData);

				return true;
			}
		}

		return false;
	}
}