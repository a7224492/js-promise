package com.kodgames.battleserver.service.battle.core.hu;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.BattleConst;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.creator.ICreateContextHandler;
import com.kodgames.battleserver.service.battle.core.hu.data.HuCardGroup;
import com.kodgames.battleserver.service.battle.core.hu.data.HuCheckerMemoryPool;
import com.kodgames.battleserver.service.battle.core.hu.filter.CardGroupsFilter;

import net.sf.json.JSONObject;

public abstract class HuCheckerBase implements ICreateContextHandler
{
	private static final Logger logger = LoggerFactory.getLogger(HuCheckerBase.class);
	
	public static final String Key_cardGroupsfilters = "cardGroupsfilters";

	/** 胡牌组过滤器 */
	private CardGroupsFilter cardGroupsFilter;
	/** 胡牌检测对应的胡牌类型，7对，4对的检测器都是一个 */
	private List<Integer> scoreTypes;

	public static HuCheckerBase create(JSONObject context)
		throws Exception
	{
		HuCheckerBase huCheckerBase = CreateContextHelper.instantiateClass(context, HuCheckerBase.class);
		huCheckerBase.createFromContext(context);
		return huCheckerBase;
	}

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		if (context.containsKey(Key_cardGroupsfilters))
			cardGroupsFilter = CardGroupsFilter.create(context.getJSONObject(Key_cardGroupsfilters));
	}

	protected HuCheckerBase(int... scoreTypes)
	{
		this.scoreTypes = new ArrayList<>();
		for (int type : scoreTypes)
			this.scoreTypes.add(type);
	}

	public boolean hasScoreType(int scoreType)
	{
		return scoreTypes.contains(scoreType);
	}

	public boolean check(BattleBean context, int roleId, byte[] cardCountArray, int fixedTileCount, int masterCardCount, List<Byte> supportedCardTypes, HuCheckerMemoryPool pool,
		List<List<HuCardGroup>> outHuCardGroupCollector, boolean fillMasterCard, int finishGroupsCount)
	{
		// 设置过滤器需要的数据
		if (cardGroupsFilter != null)
		{
			cardGroupsFilter.setContext(context);
			cardGroupsFilter.setRoleId(roleId);
		}

		boolean result = check(context, cardCountArray, fixedTileCount, masterCardCount, supportedCardTypes, pool, outHuCardGroupCollector, fillMasterCard, cardGroupsFilter, finishGroupsCount);

		// 清除数据
		if (cardGroupsFilter != null)
			cardGroupsFilter.clearData();

		return result;
	}

	public abstract boolean check(BattleBean context, byte[] cardCountArray, int fixedTileCount, int masterCardCount, List<Byte> supportedCardTypes, HuCheckerMemoryPool pool,
		List<List<HuCardGroup>> outHuCardGroupCollector, boolean fillMasterCard, CardGroupsFilter cardGroupCheck, int finishGroupsCount);

	/** 获取需要成台的数量 */
	public int getTotalTileCount(BattleBean context)
	{
		return 0;
	}

	/**
	 * 判断两个CardGroup的内容是否一样
	 */
	protected static boolean isCardGroupSame(HuCardGroup l, HuCardGroup r)
	{
		if (l.groupType != r.groupType)
			return false;

		if (l.cardList.length != r.cardList.length)
			return false;

		for (int i = 0; i < l.cardList.length; ++i)
			if (l.cardList[i] != r.cardList[i])
				return false;

		return true;
	}

	/**
	 * 对一向听检测后，填充组中空位置
	 * 
	 * @param cardGroups
	 * @param cardGroup 需要填充的组
	 * @param emptyIndex 组中空白位置索引
	 * @param tingCards
	 */
	public void fillTingGroups(List<HuCardGroup> cardGroups, HuCardGroup cardGroup, int emptyIndex, List<Byte> tingCards)
	{
		switch (cardGroup.groupType)
		{
			case CardGroupType.JIANG:
				// 填充将
				if (Macro.AssetFalse(emptyIndex == 1))
					tingCards.add(cardGroup.cardList[0]);
				break;

			case CardGroupType.AN_KE:
				// 填充暗刻
				if (Macro.AssetFalse(emptyIndex == 2, "支持一向听"))
					tingCards.add(cardGroup.cardList[0]);
				break;

			case CardGroupType.AN_KAN:
				// 填充暗坎
				if (emptyIndex == 0)
				{
					// 空位置在第一张, 用第二张计算
					byte validCard = cardGroup.cardList[1];
					Macro.AssetFalse(CardType.isNumberCard(validCard));
					if (Macro.AssetFalse(validCard != 0))
						tingCards.add((byte)(cardGroup.cardList[1] - 1));
				}
				else
				{
					// 空位置不在第一张, 用第一张计算
					byte validCard = cardGroup.cardList[0];
					if (Macro.AssetFalse(validCard != 0))
						tingCards.add((byte)(validCard + emptyIndex));
				}
				break;
		}
	}

	/**
	 * 递归检测一个固定的牌组胡牌情况
	 * 
	 * 原理:尝试把cardCountArray中的牌填充到指定的牌组cardGroupList中
	 * 
	 * @param cardCountArray 牌数量数组, 下标为牌编号, 值为该牌拥有的数量
	 * @param lastCheckedCard 上一次检测过的牌编号, 用于递归检测
	 * @param cardGroupList 需要匹配的牌组
	 * @param collectAll 是否收集所有的胡牌牌形, 如果为False, 只要有一次胡牌, 不再继续检测
	 * @param outHuCardGroupCollector 保存所检测过的所有可胡牌形
	 * @return true 表示下一张牌可以放到牌组中
	 */
	protected static boolean doCheckCardGroups(byte[] cardCountArray, List<Byte> supportedCardTypes, List<HuCardGroup> cardGroupList, HuCheckerMemoryPool pool,
		List<List<HuCardGroup>> outHuCardGroupCollector, CardGroupsFilter cardGroupsFilter, int finishGroupsCount, boolean enableFengJianAnKan, int masterCardCount)
	{
		cardGroupList.sort(new Comparator<HuCardGroup>()
		{
			public int compare(HuCardGroup l, HuCardGroup r)
			{
				return l.groupType - r.groupType;
			}
		});

		// 检测是否还剩余手牌
		int leftCardCount = 0;
		for (byte count : cardCountArray)
			leftCardCount += count;

		return fillNoneSequenceCardGroups(cardCountArray,
			supportedCardTypes,
			leftCardCount,
			-1,
			cardGroupList,
			pool,
			outHuCardGroupCollector,
			cardGroupsFilter,
			finishGroupsCount,
			enableFengJianAnKan,
			masterCardCount);

	}

	private static boolean fillNoneSequenceCardGroups(byte[] cardCountArray, List<Byte> supportedCardTypes, int leftCardCount, int lastFilledGroupIndex, List<HuCardGroup> cardGroupList,
		HuCheckerMemoryPool pool, List<List<HuCardGroup>> outHuCardGroupCollector, CardGroupsFilter cardGroupsFilter, int finishGroupsCount, boolean enableFengJianAnKan, int masterCardCount)
	{
		if (leftCardCount == 0)
		{
			// 使用过滤器检测牌型是否有效
			boolean validGroups = cardGroupsFilter == null ? true : cardGroupsFilter.filter(cardGroupList);

			// 填充完毕, 保存最终结果
			if (outHuCardGroupCollector != null && validGroups)
			{
				List<HuCardGroup> cloneList = pool.allocHuCardGroupList();
				for (int i = 0; i < cardGroupList.size(); ++i)
					cloneList.add(pool.allocHuCardGroup(cardGroupList.get(i)));
				outHuCardGroupCollector.add(cloneList);
			}

			return validGroups;
		}

		int groupIndex = lastFilledGroupIndex + 1;
		if (groupIndex == cardGroupList.size())
		{
			// 还有手牌, 但是没有牌组可以放, 填充失败
			return false;
		}

		// 检测当前组是否可以填充
		boolean hasPlaced = false;
		HuCardGroup cardGroup = cardGroupList.get(groupIndex);
		switch (cardGroup.groupType)
		{
			case CardGroupType.DAN_ZHANG:
			case CardGroupType.JIANG:
			case CardGroupType.AN_KE:
			{
				// 非序列牌

				// 这个类型的cardGroup可以空几个
				if(masterCardCount > 0)
				{
					int supportEmptyGroupCount = Math.max(1, masterCardCount / cardGroup.cardList.length);

					// 有可以空组的情况，尝试不填充
					// 获取该组在同类型中的倒序索引
					int emptyGroupCount = 1;
					for (int i = groupIndex + 1; i < cardGroupList.size(); i++)
					{
						if (cardGroupList.get(i).groupType != cardGroup.groupType)
							break;

						emptyGroupCount++;
					}

					// 在同类型组中从后到前开始尝试不填充
					// 例如： 可以空2组AN_Ke, cardGroup的倒序索引为3,不尝试填空。
					if (emptyGroupCount <= supportEmptyGroupCount)
					{
						masterCardCount -= cardGroup.cardList.length * emptyGroupCount;

						// 继续检测
						if (fillNoneSequenceCardGroups(cardCountArray,
							supportedCardTypes,
							leftCardCount,
							groupIndex + emptyGroupCount - 1,
							cardGroupList,
							pool,
							outHuCardGroupCollector,
							cardGroupsFilter,
							finishGroupsCount,
							enableFengJianAnKan,
							masterCardCount))
							hasPlaced = true;

						// 还原万能牌数量
						masterCardCount += cardGroup.cardList.length * emptyGroupCount;

						if (hasPlaced)
						{
							// 如果只是判断是否可以胡, 不用再继续检测了
							if (outHuCardGroupCollector == null)
								break;
							// 如果已检测胡牌牌型数量大于需要检测的数量, 不用再继续检测了
							else if (finishGroupsCount > 0 && outHuCardGroupCollector.size() >= finishGroupsCount)
								break;
						}
					}
				}

				// 如果上次跟本次是同样的组, 获取上次填充卡的信息, 用于后续检测
				HuCardGroup lastFilledCardGroup = lastFilledGroupIndex >= 0 ? cardGroupList.get(lastFilledGroupIndex) : null;
				byte lastFilledCard = 0;
				byte lastFilledCardType = CardType.INVALID.Value();
				if (lastFilledCardGroup != null && lastFilledCardGroup.groupType == cardGroup.groupType)
				{
					lastFilledCard = lastFilledCardGroup.cardList[0];
					lastFilledCardType = CardType.getCardType(lastFilledCard).Value();
					// 添加日志
					if (!Macro.AssetFalse(lastFilledCard != 0, "上一次填充进去了0 + lastFilledGroupIndex is : " + lastFilledGroupIndex))
					{
						for (HuCardGroup caGroup : cardGroupList)
							logger.error("填充错误组相关信息", caGroup.toString());
					}
				}

				// 尝试填充所有的牌
				for (byte supportedCardType : supportedCardTypes)
				{
					if (supportedCardType == CardType.HUA.Value())
						// 花牌什么都不能做
						continue;

					if (lastFilledCardType > supportedCardType)
						// 考虑组合, 忽略已经填充过的类型
						continue;

					// 如果是上次检测与本次是同样类型的牌, 继续检测后续的牌, 这样是以组合的方式检测
					CardType cardType = CardType.convertToCardType(supportedCardType);
					for (byte card = lastFilledCardType == supportedCardType ? lastFilledCard : cardType.Value(); card < cardType.MaxValue(); ++card)
					{
						Macro.AssetFalse(cardCountArray[card] >= 0);
						if (cardCountArray[card] == 0)
							continue;

						// 尝试把所有牌填充到牌组里
						for (int i = 0; i < cardGroup.cardList.length; ++i)
						{
							if (cardCountArray[card] == 0)
								break;

							Macro.AssetFalse(cardGroup.cardList[i] == 0);
							cardGroup.cardList[i] = card;
							cardCountArray[card]--;
							leftCardCount--;
						}

						// 继续检测
						if (fillNoneSequenceCardGroups(cardCountArray,
							supportedCardTypes,
							leftCardCount,
							groupIndex,
							cardGroupList,
							pool,
							outHuCardGroupCollector,
							cardGroupsFilter,
							finishGroupsCount,
							enableFengJianAnKan,
							masterCardCount))
							hasPlaced = true;

						// 恢复填充,
						for (int i = 0; i < cardGroup.cardList.length; ++i)
						{
							if (cardGroup.cardList[i] == 0)
								break;

							cardGroup.cardList[i] = 0;
							cardCountArray[card]++;
							leftCardCount++;
						}

						if (hasPlaced)
						{
							// 如果只是判断是否可以胡, 不用再继续检测了
							if (outHuCardGroupCollector == null)
								break;
							// 如果已检测胡牌牌型数量大于需要检测的数量, 不用再继续检测了
							else if (finishGroupsCount > 0 && outHuCardGroupCollector.size() >= finishGroupsCount)
								break;
						}
					}
				}
				break;
			}

			case CardGroupType.AN_KAN:
			{
				hasPlaced = fillSequenceCardGroups(cardCountArray, groupIndex, (byte)0, cardGroupList, pool, outHuCardGroupCollector, cardGroupsFilter, finishGroupsCount, enableFengJianAnKan);
				break;
			}

			default:
				Macro.AssetFalse(false);
				break;
		}

		return hasPlaced;
	}

	private static boolean fillSequenceCardGroups(byte[] cardCountArray, int kanStartGroupIndex, byte lastCheckedCard, List<HuCardGroup> cardGroupList, HuCheckerMemoryPool pool,
		List<List<HuCardGroup>> outHuCardGroupCollector, CardGroupsFilter cardGroupsFilter, int finishGroupsCount, boolean enableFengJianAnKan)
	{
		// 尝试将每张牌放到各个组中
		for (byte card = lastCheckedCard; card < cardCountArray.length; ++card)
		{
			Macro.AssetFalse(cardCountArray[card] >= 0);
			if (cardCountArray[card] == 0)
				continue;

			int cardIndex = CardType.convertToCardIndex(card);
			// 找到最后一个出现相同card的group, 从这个group开始检测, 之前的group在迭代过程中已经检测过
			int startIndex = kanStartGroupIndex;
			for (int groupIndex = kanStartGroupIndex; groupIndex < cardGroupList.size(); ++groupIndex)
				for (byte c : cardGroupList.get(groupIndex).cardList)
					if (c == card)
						startIndex = groupIndex;

			boolean hasPlaced = false;
			for (int groupIndex = startIndex; groupIndex < cardGroupList.size(); ++groupIndex)
			{
				HuCardGroup cardGroup = cardGroupList.get(groupIndex);

				// 忽略已经检测过的相同牌组
				boolean checked = false;
				for (int prevIndex = 0; prevIndex < groupIndex; ++prevIndex)
				{
					if (isCardGroupSame(cardGroup, cardGroupList.get(prevIndex)))
					{
						checked = true;
						break;
					}
				}

				if (checked)
					continue;

				switch (cardGroup.groupType)
				{
					case CardGroupType.AN_KAN:
					{
						// 不检测花牌
						if (CardType.HUA.isBelongTo(card))
							continue;

						// 找到第一个放置过牌的位置
						int existIndex = 0;
						for (; existIndex < cardGroup.cardList.length; ++existIndex)
							if (cardGroup.cardList[existIndex] != 0)
								break;

						// 数字牌检测
						if (CardType.isNumberCard(card))
						{
							// 由于整个检测已经排序, card需要放到第一个已经放置过的牌后面

							if (existIndex == cardGroup.cardList.length)
							{
								// 坎组的首张牌, 会在接下来的迭代中后移, 所以只用检测起始位置不低于之前具有相同起始牌的牌组
								int endKanIndex = cardGroup.cardList.length - 1;
								for (int prevIndex = groupIndex - 1; prevIndex >= 0; prevIndex--)
								{
									HuCardGroup prevCardGroup = cardGroupList.get(prevIndex);
									if (prevCardGroup.groupType != CardGroupType.AN_KAN)
										break;

									// 找到牌值相同, 占位最靠后的牌,
									boolean found = false;
									for (int kanIndex = 0; kanIndex < prevCardGroup.cardList.length; ++kanIndex)
									{
										byte kanCard = prevCardGroup.cardList[kanIndex];
										if (kanCard == 0)
											continue;

										if (kanCard != card)
											break;

										// 靠前牌组的起始位置, 不可能小于后面的, 只用检测到一个就可以
										endKanIndex = kanIndex;
										found = true;
										break;
									}

									if (found)
										break;
								}

								// 遍历所有可以放置的位置, 继续遍历检测
								for (int i = Math.max(0, cardIndex + 3 - BattleConst.NUMBER_CARD_COUNT); i <= endKanIndex && i <= cardIndex; ++i)
								{
									cardGroup.cardList[i] = card;
									cardCountArray[card]--;
									if (fillSequenceCardGroups(cardCountArray,
										kanStartGroupIndex,
										card,
										cardGroupList,
										pool,
										outHuCardGroupCollector,
										cardGroupsFilter,
										finishGroupsCount,
										enableFengJianAnKan))
										hasPlaced = true;
									cardGroup.cardList[i] = 0;
									cardCountArray[card]++;

									if (hasPlaced && outHuCardGroupCollector == null)
										// 只用于判断是否可胡, 不再继续检测
										break;
								}
							}
							else if (existIndex < cardGroup.cardList.length - 1)
							{
								// 当前是已经放置过牌的牌组
								if (CardType.getCardType(cardGroup.cardList[existIndex]) != CardType.getCardType(card))
									// 忽略不相同类型的牌组
									continue;

								// 当前牌需要大于检测牌
								if (card <= cardGroup.cardList[existIndex])
									continue;

								// 计算需要放置的位置
								int putIndex = existIndex + cardIndex - CardType.convertToCardIndex(cardGroup.cardList[existIndex]);
								if (putIndex >= cardGroup.cardList.length)
									// 超出了原本的放置范围, 不在检测
									continue;

								// 放置位置已经有牌, 不再检测
								if (cardGroup.cardList[putIndex] != 0)
									continue;

								// 放置这张牌, 继续递归检测
								cardGroup.cardList[putIndex] = card;
								cardCountArray[card]--;
								if (fillSequenceCardGroups(cardCountArray,
									kanStartGroupIndex,
									card,
									cardGroupList,
									pool,
									outHuCardGroupCollector,
									cardGroupsFilter,
									finishGroupsCount,
									enableFengJianAnKan))
									hasPlaced = true;
								cardGroup.cardList[putIndex] = 0;
								cardCountArray[card]++;
							}
						}
						else if (enableFengJianAnKan)
						{
							int putIndex = -1;
							for (int index = 0; index < cardGroup.cardList.length; index++)
							{
								if (cardGroup.cardList[index] == 0 && putIndex == -1)
									putIndex = index;

								if (cardGroup.cardList[index] == card)
								{
									putIndex = -1;
									break;
								}
							}

							// 已经填充完成，或者填充过该卡牌
							if (putIndex == -1)
								continue;

							// 如果已经有填充，检测填充牌
							if (existIndex != cardGroup.cardList.length)
							{
								byte existCard = cardGroup.cardList[existIndex];
								// 已经添加的不能是数字牌
								if (CardType.isNumberCard(existCard))
									continue;

								CardType inCardType = CardType.getFengJianType(existCard);
								// 填入牌不是一种类型
								if (inCardType != CardType.INVALID && inCardType != CardType.getFengJianType(card))
									continue;
							}

							// 放置这张牌, 继续递归检测
							cardGroup.cardList[putIndex] = card;
							cardCountArray[card]--;
							if (fillSequenceCardGroups(cardCountArray,
								kanStartGroupIndex,
								card,
								cardGroupList,
								pool,
								outHuCardGroupCollector,
								cardGroupsFilter,
								finishGroupsCount,
								enableFengJianAnKan))
								hasPlaced = true;
							cardGroup.cardList[putIndex] = 0;
							cardCountArray[card]++;
						}

						break;
					}

					// 保证牌组排序过, 先检测非序列牌组, 再检测序列牌组
					case CardGroupType.DAN_ZHANG:
					case CardGroupType.JIANG:
					case CardGroupType.AN_KE:
					default:
						Macro.AssetFalse(false);
						break;
				}
			}

			return hasPlaced;
		}

		// 使用过滤器检测牌型是否有效
		boolean validGroups = cardGroupsFilter == null ? true : cardGroupsFilter.filter(cardGroupList);

		// 所有牌都放到了位置, 保存最终结果
		if (outHuCardGroupCollector != null && validGroups)
		{
			List<HuCardGroup> cloneList = pool.allocHuCardGroupList();
			for (int i = 0; i < cardGroupList.size(); ++i)
				cloneList.add(pool.allocHuCardGroup(cardGroupList.get(i)));
			outHuCardGroupCollector.add(cloneList);
		}

		return validGroups;
	}

	protected static void generateOutputCardGroups(List<HuCardGroup> cardGroupList, HuCheckerMemoryPool pool, List<List<HuCardGroup>> outHuCardGroupCollector)
	{
		// 拷贝一份作为输出
		List<HuCardGroup> resultList = pool.allocHuCardGroupList();
		for (HuCardGroup cardGroup : cardGroupList)
			resultList.add(pool.allocHuCardGroup(cardGroup));

		outHuCardGroupCollector.add(resultList);
	}

	protected static void fillMasterCard(List<HuCardGroup> cardGroupList, int masterCardCount, List<Byte> supportedCardTypes, HuCheckerMemoryPool pool, List<List<HuCardGroup>> outHuCardGroupCollector,
		boolean enableFengJianAnKan)
	{
		// 填充可以确定的位置
		masterCardCount = fillMasterCardToFixedCardGroup(cardGroupList, masterCardCount, enableFengJianAnKan);

		// 填充空牌组
		fillMasterCardToEmptyCardGroup(cardGroupList, masterCardCount, supportedCardTypes, -1, pool, outHuCardGroupCollector, enableFengJianAnKan);
	}

	/**
	 * 将牌组中可以确定牌值的空位置, 补充万能牌
	 */
	private static int fillMasterCardToFixedCardGroup(List<HuCardGroup> cardGroupList, int masterCardCount, boolean enableFengJianAnKan)
	{
		// 尝试将万能牌, 填充到所有可以明确牌值的位置
		for (HuCardGroup cardGroup : cardGroupList)
		{
			// 如果没有鬼牌, 中断填充
			if (masterCardCount <= 0)
				break;

			// 查看当前组是否为空, 并获取第一个非空元素编号用于后续计算
			boolean hasEmptyCard = false;
			byte firstValidCardIndex = -1;
			for (byte i = 0; i < cardGroup.cardList.length; ++i)
			{
				byte card = cardGroup.cardList[i];
				if (card != 0)
				{
					if (firstValidCardIndex == -1)
						firstValidCardIndex = i;
				}
				else
					hasEmptyCard = true;
			}

			if (hasEmptyCard == false)
				// 没有空位置, 继续检测后面的
				continue;

			if (firstValidCardIndex == -1)
				// 这个组所有位置全空, 不能够确定填充万能牌
				continue;

			// 当前组不为空, 万能牌可以明确替代相应的位置.
			switch (cardGroup.groupType)
			{
				case CardGroupType.DAN_ZHANG:
					// 13幺不会用这个函数填充
					Macro.AssetFalse(false);
					break;

				case CardGroupType.JIANG:
				case CardGroupType.AN_KE:
				{
					// 刻组:填充指定的牌
					for (int i = 0; i < cardGroup.cardList.length; ++i)
					{
						// 如果没有鬼牌, 中断填充
						if (masterCardCount <= 0)
							break;

						byte card = cardGroup.cardList[i];
						if (card != 0)
							continue;

						// 空位置, 使用万能牌替代
						Macro.AssetFalse(masterCardCount > 0);
						masterCardCount--;
						cardGroup.cardList[i] = cardGroup.cardList[firstValidCardIndex];
					}
					break;
				}

				case CardGroupType.AN_KAN:
				{
					byte firstFilledCard = cardGroup.cardList[firstValidCardIndex];

					// 刻组:填充指定的牌
					// 获取第一张牌
					if (CardType.isNumberCard(firstFilledCard))
					{
						byte firstCard = (byte)(firstFilledCard - firstValidCardIndex);
						for (byte i = 0; i < cardGroup.cardList.length; ++i)
						{
							byte card = cardGroup.cardList[i];
							if (card != 0)
								continue;

							// 如果没有鬼牌, 中断填充
							if (masterCardCount <= 0)
								break;

							// 空位置, 使用万能牌替代
							Macro.AssetFalse(masterCardCount > 0);
							masterCardCount--;
							cardGroup.cardList[i] = (byte)(firstCard + i);
						}
					}
					else if (enableFengJianAnKan)
					{
						// 填充风箭牌
						CardType fengJianType = CardType.getFengJianType(firstFilledCard);
						for (byte i = 0; i < cardGroup.cardList.length; ++i)
						{
							byte card = cardGroup.cardList[i];
							if (card != 0)
								continue;

							// 如果没有鬼牌, 中断填充
							if (masterCardCount <= 0)
								break;

							// 空位置, 使用万能牌替代
							masterCardCount--;

							for (byte j = fengJianType.Value(); j < fengJianType.MaxValue(); j++)
							{
								boolean contains = false;
								for (byte cardInGroup : cardGroup.cardList)
								{
									if (cardInGroup == j)
									{
										contains = true;
										break;
									}
								}

								if (contains)
									continue;

								cardGroup.cardList[i] = j;
							}
						}
					}

					break;
				}

				default:
					Macro.AssetFalse(false);
					break;
			}
		}

		return masterCardCount;
	}

	/**
	 * 将牌组的空组, 替换为万能牌
	 */
	private static void fillMasterCardToEmptyCardGroup(List<HuCardGroup> cardGroupList, int masterCardCount, List<Byte> supportedCardTypes, int lastFilledGroupIndex, HuCheckerMemoryPool pool,
		List<List<HuCardGroup>> outHuCardGroupCollector, boolean enableFengJianAnKan)
	{
		if (masterCardCount == 0)
		{
			// 如果没有万能牌, 直接作为结果保存
			List<HuCardGroup> resultList = pool.allocHuCardGroupList();
			for (HuCardGroup cardGroup : cardGroupList)
				resultList.add(pool.allocHuCardGroup(cardGroup));

			outHuCardGroupCollector.add(resultList);
			return;
		}

		// 从上次检测开始, 如果是第一次lastFilledGroupIndex为-1
		for (int groupIndex = lastFilledGroupIndex + 1; groupIndex < cardGroupList.size(); ++groupIndex)
		{
			HuCardGroup cardGroup = cardGroupList.get(groupIndex);

			// 忽略非空组
			boolean hasValidCard = true;
			for (byte i = 0; i < cardGroup.cardList.length; ++i)
			{
				byte card = cardGroup.cardList[i];
				if (card != 0)
				{
					hasValidCard = false;
					break;
				}
			}

			if (hasValidCard == false)
				continue;

			// 如果上次跟本次是同样的组, 获取上次填充卡的信息, 用于后续检测
			HuCardGroup lastFilledCardGroup = lastFilledGroupIndex >= 0 ? cardGroupList.get(lastFilledGroupIndex) : null;
			byte lastFilledCard = 0;
			byte lastFilledCardType = CardType.INVALID.Value();
			if (lastFilledCardGroup != null && lastFilledCardGroup.groupType == cardGroup.groupType)
			{
				lastFilledCard = lastFilledCardGroup.cardList[0];
				lastFilledCardType = CardType.getCardType(lastFilledCard).Value();
				Macro.AssetFalse(lastFilledCard != 0);
			}

			for (byte supportedCardType : supportedCardTypes)
			{
				if (supportedCardType == CardType.HUA.Value())
					// 花牌什么都不能做
					continue;

				if (lastFilledCardType > supportedCardType)
					// 考虑组合, 忽略已经填充过的类型
					continue;

				// 如果没有鬼牌，不进行填充检测
				if (masterCardCount <= 0)
					break;

				// 可以填充的鬼牌数量
				int fillCount = Math.min(masterCardCount, cardGroup.cardList.length);
				switch (cardGroup.groupType)
				{
					case CardGroupType.DAN_ZHANG:
					case CardGroupType.JIANG:
					case CardGroupType.AN_KE:
					{
						CardType cardType = CardType.convertToCardType(supportedCardType);
						for (byte card = lastFilledCardType == supportedCardType ? lastFilledCard : cardType.Value(); card < cardType.MaxValue(); ++card)
						{
							Macro.AssetFalse(CardType.getCardType(card) != CardType.INVALID);

							// 填充所有
							for (byte i = 0; i < fillCount; ++i)
								cardGroup.cardList[i] = card;
							masterCardCount -= fillCount;
							Macro.AssetFalse(masterCardCount >= 0);

							// 继续检测
							fillMasterCardToEmptyCardGroup(cardGroupList, masterCardCount, supportedCardTypes, groupIndex, pool, outHuCardGroupCollector, enableFengJianAnKan);

							// 恢复填充, 继续后面的检测
							for (byte i = 0; i < fillCount; ++i)
								cardGroup.cardList[i] = 0;
							masterCardCount += fillCount;
						}
						break;
					}

					case CardGroupType.AN_KAN:
					{
						// 花牌不能作为坎
						if (CardType.HUA.isBelongTo(supportedCardType))
							continue;

						// 刻组:填充指定的牌
						if (CardType.isNumberCardType(supportedCardType))
						{
							CardType cardType = CardType.convertToCardType(supportedCardType);
							for (byte checkCard = lastFilledCardType == supportedCardType ? lastFilledCard : cardType.Value(); checkCard < cardType.MaxValue() - 2; ++checkCard)
							{
								// 填充所有
								for (byte i = 0; i < fillCount; ++i)
									cardGroup.cardList[i] = (byte)(checkCard + i);

								masterCardCount -= fillCount;
								Macro.AssetFalse(masterCardCount >= 0);

								// 继续检测
								fillMasterCardToEmptyCardGroup(cardGroupList, masterCardCount, supportedCardTypes, groupIndex, pool, outHuCardGroupCollector, enableFengJianAnKan);

								// 恢复填充, 继续后面的检测
								for (byte i = 0; i < fillCount; ++i)
									cardGroup.cardList[i] = 0;
								masterCardCount += fillCount;
							}
						}
						else if (enableFengJianAnKan)
						{
							// 风牌
							for (byte checkCard = CardType.FENG.Value(); checkCard < CardType.FENG.MaxValue() - 2; checkCard++)
							{
								// 填充所有
								for (byte i = 0; i < fillCount; ++i)
									cardGroup.cardList[i] = (byte)(checkCard + i);

								masterCardCount -= fillCount;
								Macro.AssetFalse(masterCardCount >= 0);

								// 继续检测
								fillMasterCardToEmptyCardGroup(cardGroupList, masterCardCount, supportedCardTypes, groupIndex, pool, outHuCardGroupCollector, enableFengJianAnKan);

								// 恢复填充, 继续后面的检测
								for (byte i = 0; i < fillCount; ++i)
									cardGroup.cardList[i] = (byte)0;
								masterCardCount += fillCount;
							}

							// 箭牌
							for (byte checkCard = CardType.JIAN.Value(); checkCard < CardType.JIAN.MaxValue() - 2; checkCard++)
							{
								// 填充所有
								for (byte i = 0; i < fillCount; ++i)
									cardGroup.cardList[i] = (byte)(checkCard + i);
								masterCardCount -= fillCount;
								Macro.AssetFalse(masterCardCount >= 0);

								// 继续检测
								fillMasterCardToEmptyCardGroup(cardGroupList, masterCardCount, supportedCardTypes, groupIndex, pool, outHuCardGroupCollector, enableFengJianAnKan);

								// 恢复填充, 继续后面的检测
								for (byte i = 0; i < fillCount; ++i)
									cardGroup.cardList[i] = (byte)0;
								masterCardCount += fillCount;
							}
						}

						break;
					}

					default:
						Macro.AssetFalse(false);
						break;
				}
			}
		}
	}
}