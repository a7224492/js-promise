package com.kodgames.battleserver.service.battle.core.hu;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.BattleConst;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuCardGroup;
import com.kodgames.battleserver.service.battle.core.hu.data.HuCheckerMemoryPool;
import com.kodgames.battleserver.service.battle.core.hu.filter.CardGroupsFilter;

/**
 * 13烂
 */
public class HuChecker_13Lan extends HuCheckerBase
{
	public HuChecker_13Lan()
	{
		super(PlayType.HU_13LAN);
	}

	@Override
	public boolean check(BattleBean context, byte[] cardCountArray, int fixedTileCount, int masterCardCount, List<Byte> supportedCardTypes, HuCheckerMemoryPool pool,
		List<List<HuCardGroup>> outHuCardGroupCollector, boolean fillMasterCard, CardGroupsFilter cardGroupsFilter, int finishGroupsCount)
	{
		// 13烂不能有吃碰杠
		if (fixedTileCount != 0)
			return false;

		List<List<HuCardGroup>> outHuCardGroups = outHuCardGroupCollector != null ? pool.allocHuCardGroupListList() : null;

		try
		{
			// 执行检测
			if (check13Lan(cardCountArray, supportedCardTypes, pool, outHuCardGroups, cardGroupsFilter, finishGroupsCount, masterCardCount) == false)
				return false;

			// 需要输出牌形
			if (outHuCardGroups != null)
			{
				for (List<HuCardGroup> list : outHuCardGroups)
					if (fillMasterCard)
						// 填充万能牌, 生成胡牌牌形
						fillMasterCard13Lan(list, masterCardCount, supportedCardTypes, pool, outHuCardGroupCollector);
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

	private static boolean check(byte[] cardCountArray, byte base)
	{
		byte lastCard = 0;
		boolean start = true;
		for (byte card = 0; card < 9; ++card)
		{
			byte cardReal = (byte)(base + card);
			byte cardCount = cardCountArray[cardReal];
			if (cardCount == 0)
				continue;

			// 牌数量不能大于1
			if (cardCount > 1)
				return false;

			if (start)
			{
				lastCard = cardReal;
				start = false;
			}
			else
			{
				if (cardReal - lastCard <= 2)
					return false;
				else
					lastCard = cardReal;
			}
		}

		return true;
	}

	/**
	 * 检测13烂
	 */
	private static boolean check13Lan(byte[] cardCountArray, List<Byte> supportedCardTypes, HuCheckerMemoryPool pool, List<List<HuCardGroup>> outHuCardGroupCollector,
		CardGroupsFilter cardGroupsFilter, int finishGroupsCount, int masterCardCount)
	{
		// 检测十三烂基本规则
		// 不可以有重复的字牌
		for (byte card = 0; card < BattleConst.ZI_CARD_COUNT; ++card)
		{
			byte cardReal = (byte)(CardType.ZI.Value() + card);
			byte cardCount = cardCountArray[cardReal];
			if (cardCount == 0)
				continue;

			// 牌数量不能大于1
			if (cardCount > 1)
				return false;
		}

		// 检测序数牌：不允许重复，不允许同花色相邻牌距离小于3
		boolean res = check(cardCountArray, CardType.WAN.Value()) && check(cardCountArray, CardType.TIAO.Value()) && check(cardCountArray, CardType.TONG.Value());
		if (!res)
			return false;

		// 构造13烂牌组
		List<HuCardGroup> cardGroupList = pool.allocHuCardGroupList();
		for (int i = 0; i < 14; ++i)
			cardGroupList.add(pool.allocHuCardGroup(CardGroupType.DAN_ZHANG, (byte)0));

		try
		{
			// 检测牌形
			if (doCheckCardGroups(cardCountArray, supportedCardTypes, cardGroupList, pool, outHuCardGroupCollector, cardGroupsFilter, finishGroupsCount, false, masterCardCount) == false)
				return false;

			Macro.AssetFalse(outHuCardGroupCollector == null || outHuCardGroupCollector.size() != 0);
			return true;
		}
		finally
		{
			// 回收内存
			pool.deallocList(cardGroupList);
			cardGroupList = null;
		}
	}

	/**
	 * 填充万能牌
	 */
	private void fillMasterCard13Lan(List<HuCardGroup> cardGroupList, int masterCardCount, List<Byte> supportedCardTypes, HuCheckerMemoryPool pool, List<List<HuCardGroup>> outHuCardGroupCollector)
	{
		// 检查支持的牌值
		List<Byte> checkCardList = new ArrayList<Byte>();
		List<CardType> cardTypes = supportedCardTypes.stream().map(t -> CardType.convertToCardType(t)).collect(Collectors.toList());
		for (CardType t : cardTypes)
		{
			for (byte card = t.Value(); card < t.MaxValue(); ++card)
			{
				checkCardList.add(card);
			}
		}

		// 剔除牌型中已有的字牌，和距离小于3的序数牌
		for (HuCardGroup group : cardGroupList)
		{
			byte card = group.cardList[0];
			if (card != 0)
			{
				checkAddition(checkCardList, card, true);
			}
		}

		// 万能牌填充方式的所有组合
		List<List<Byte>> selections = new ArrayList<>();
		selectCards(checkCardList, masterCardCount, 0, new ArrayList<>(), selections);

		// 保存结果
		selections.forEach(sel -> {
			// 保存不需要替换的牌
			List<HuCardGroup> resultList = pool.allocHuCardGroupList();
			for (HuCardGroup group : cardGroupList)
			{
				if (group.cardList[0] != 0)
				{
					resultList.add(pool.allocHuCardGroup(group));
				}
			}

			// 保存需要替换的牌
			sel.forEach(card -> {
				HuCardGroup group = pool.allocHuCardGroup(CardGroupType.DAN_ZHANG, card);
				resultList.add(group);
			});

			outHuCardGroupCollector.add(resultList);
		});
	}

	/**
	 * 从 all 中选择 count 张符合十三烂牌型的牌
	 * 
	 * @param all 可选的牌的集合
	 * @param count 可选的张数
	 * @param indexInAll 从 all 的该索引开始选择
	 * @param current 当前选择的一组牌
	 * @param outSelections 已选择的合法组合
	 */
	private void selectCards(List<Byte> all, int count, int indexInAll, List<Byte> current, List<List<Byte>> outSelections)
	{
		if (indexInAll < 0)
			return;

		// 已选够 count 张牌，保存当前组合
		Macro.AssetTrue(count < 0);
		if (current.size() == count)
		{
			outSelections.add(new ArrayList<>(current));
			return;
		}

		// 继续选择合法的牌，加入到 current 中
		for (int index = indexInAll; index < all.size(); ++index)
		{
			Byte addition = all.get(index);
			if (checkAddition(current, addition, false) == false)
				continue;

			current.add(addition);
			selectCards(all, count, index + 1, current, outSelections);
			current.remove(current.size() - 1);
		}
	}

	/**
	 * addition 加入到 selection 中，是否会影响十三烂牌型 不影响返回 true，影响返回 false
	 * 
	 * @param selection 要检测的选择集合
	 * @param addition 要加入集合的牌
	 * @param filter 如果加入 addition 不合法，是否过滤掉selection中影响 addition 加入的牌
	 */
	private boolean checkAddition(List<Byte> selection, byte addition, boolean filter)
	{
		// 字牌不重复即合法
		if (CardType.ZI.isBelongTo(addition))
		{
			if (selection.contains((Byte)addition))
			{
				if (filter)
					selection.remove((Byte)addition);

				return false;
			}
			
			return true;
		}

		// 序数牌同花色距离至少为3，即合法
		CardType type = CardType.getCardType(addition);
		int index = CardType.convertToCardIndex(addition);
		int begin = Math.max(0, index - 2);
		int end = Math.min(BattleConst.NUMBER_CARD_COUNT - 1, index + 2) + 1;
		boolean isAdditionValid = true;
		for (byte numCard = (byte)(type.Value() + begin); numCard < type.Value() + end; ++numCard)
		{
			if (selection.contains((Byte)numCard))
			{
				if (filter)
					selection.remove((Byte)numCard);

				isAdditionValid = false;
			}
		}

		return isAdditionValid;
	}
}