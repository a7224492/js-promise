package com.kodgames.battleserver.service.battle.core.hu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.check.CheckHelper;
import com.kodgames.battleserver.service.battle.core.hu.data.HuCardGroup;
import com.kodgames.battleserver.service.battle.core.hu.data.HuCheckerMemoryPool;
import com.kodgames.battleserver.service.battle.core.hu.filter.CardGroupsFilter;

/**
 * 13幺
 */
public class HuChecker_13YaoHu extends HuCheckerBase
{
	public HuChecker_13YaoHu()
	{
		super(PlayType.HU_SHI_SAN_YAO);
	}

	@Override
	public boolean check(BattleBean context, byte[] cardCountArray, int fixedTileCount, int masterCardCount, List<Byte> supportedCardTypes, HuCheckerMemoryPool pool,
		List<List<HuCardGroup>> outHuCardGroupCollector, boolean fillMasterCard, CardGroupsFilter cardGroupsFilter, int finishGroupsCount)
	{
		// 13幺不能有吃碰杠
		if (fixedTileCount != 0)
			return false;

		List<List<HuCardGroup>> outHuCardGroups = outHuCardGroupCollector != null ? pool.allocHuCardGroupListList() : null;

		try
		{
			// 执行检测
			if (check13Yao(cardCountArray, supportedCardTypes, pool, outHuCardGroups, cardGroupsFilter, finishGroupsCount, masterCardCount) == false)
				return false;

			// 需要输出牌形
			if (outHuCardGroups != null)
			{
				for (List<HuCardGroup> list : outHuCardGroups)
					if (fillMasterCard)
						// 填充万能牌, 生成胡牌牌形
						fillMasterCard13Yao(list, masterCardCount, supportedCardTypes, pool, outHuCardGroupCollector);
					else
						// 直接输出可胡牌形
						generateOutputCardGroups(list, pool, outHuCardGroupCollector);
			}

			return true;
		}
		finally
		{
			// 回收内存
			pool.deallocListList(outHuCardGroups);
			outHuCardGroups = null;
		}
	}

	/**
	 * 检测13幺
	 */
	private static boolean check13Yao(byte[] cardCountArray, List<Byte> supportedCardTypes, HuCheckerMemoryPool pool, List<List<HuCardGroup>> outHuCardGroupCollector,
		CardGroupsFilter cardGroupsFilter, int finishGroupsCount, int masterCardCount)
	{
		byte jiangCard = 0;
		// 检测十三幺基本规则
		for (byte card = 0; card < cardCountArray.length; ++card)
		{
			byte cardCount = cardCountArray[card];
			Macro.AssetFalse(cardCount >= 0);
			if (cardCount == 0)
				continue;

			// 牌数量不能大于2
			if (cardCount > 2)
				return false;

			// 是否只有一个将
			if (cardCount == 2)
				if (jiangCard > 0)
					return false;
				else
					jiangCard = card;

			// 必须为幺牌
			if (Arrays.binarySearch(CheckHelper.CARD_YAO, (byte)card) < 0)
				return false;
		}

		// 构造13幺牌组
		List<HuCardGroup> cardGroupList = pool.allocHuCardGroupList();
		// 如果含有将牌, 不在进行将牌填充
		if (jiangCard > 0)
			cardCountArray[jiangCard] -= 2;
		else
			cardGroupList.add(pool.allocHuCardGroup(CardGroupType.JIANG, (byte)0, (byte)0));

		for (int i = 1; i < 13; ++i)
			cardGroupList.add(pool.allocHuCardGroup(CardGroupType.DAN_ZHANG, (byte)0));

		try
		{
			// 检测牌形
			if (doCheckCardGroups(cardCountArray, supportedCardTypes, cardGroupList, pool, outHuCardGroupCollector, cardGroupsFilter, finishGroupsCount, false, masterCardCount) == false)
				return false;

			// 填充将牌
			if (outHuCardGroupCollector != null && jiangCard > 0)
			{
				for (List<HuCardGroup> list : outHuCardGroupCollector)
					list.add(pool.allocHuCardGroup(CardGroupType.JIANG, jiangCard, jiangCard));
			}

			Macro.AssetFalse(outHuCardGroupCollector == null || outHuCardGroupCollector.size() != 0);
			return true;
		}
		finally
		{
			// 恢复牌组
			if (jiangCard > 0)
				cardCountArray[jiangCard] += 2;

			// 回收内存
			pool.deallocList(cardGroupList);
			cardGroupList = null;
		}
	}

	@Override
	public void fillTingGroups(List<HuCardGroup> cardGroups, HuCardGroup cardGroup, int emptyIndex, List<Byte> tingCards)
	{
		super.fillTingGroups(cardGroups, cardGroup, emptyIndex, tingCards);

		switch (cardGroup.groupType)
		{
			case CardGroupType.DAN_ZHANG:
				// 十三幺，单张填充
				for (byte yaoCard : CheckHelper.CARD_YAO)
				{
					// yaoCard 在CardGroups中是否有填充
					boolean found = true;
					for (HuCardGroup checkGroup : cardGroups)
					{
						Byte existCard = checkGroup.cardList[0];

						if (existCard == 0)
							continue;

						if (existCard == yaoCard)
						{
							found = false;
							break;
						}
					}

					// 如果这张牌没有在组里填充过，进行填充
					if (found)
					{
						tingCards.add(yaoCard);
						break;
					}
				}

				break;
		}
	}

	private static void fillMasterCard13Yao(List<HuCardGroup> cardGroupList, int masterCardCount, List<Byte> supportedCardTypes, HuCheckerMemoryPool pool,
		List<List<HuCardGroup>> outHuCardGroupCollector)
	{
		// 填充十三幺牌型
		List<Byte> checkCardList = new ArrayList<Byte>();
		for (byte card : CheckHelper.CARD_YAO)
			checkCardList.add(card);

		// 检测每一组
		for (HuCardGroup cardGroup : cardGroupList)
		{
			// 没有鬼牌，直接跳出循环
			if (masterCardCount <= 0)
				break;

			// 剔除已经使用的Card
			for (Byte card : cardGroup.cardList)
				checkCardList.remove(card);

			// 无可使用数量，直接跳出循环
			if (checkCardList.size() <= 0)
				break;

			// 获取填充牌
			Byte existCard = cardGroup.cardList[0];
			if (existCard == 0)
				existCard = checkCardList.remove(0);

			// 强制检查一次
			if (existCard == 0)
				continue;

			// 鬼牌填充
			for (int index = 0; index < cardGroup.cardList.length; index++)
			{
				if (masterCardCount <= 0)
					break;

				if (cardGroup.cardList[index] == 0)
				{
					cardGroup.cardList[index] = existCard;
					--masterCardCount;
				}
			}
		}

		// 保存结果
		List<HuCardGroup> resultList = pool.allocHuCardGroupList();
		for (HuCardGroup cardGroup : cardGroupList)
			resultList.add(pool.allocHuCardGroup(cardGroup));
		outHuCardGroupCollector.add(resultList);

		return;
	}
}