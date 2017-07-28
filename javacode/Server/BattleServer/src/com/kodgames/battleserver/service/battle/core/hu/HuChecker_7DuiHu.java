package com.kodgames.battleserver.service.battle.core.hu;

import java.util.Arrays;
import java.util.List;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.BattleHelper;
import com.kodgames.battleserver.service.battle.core.check.CheckHelper;
import com.kodgames.battleserver.service.battle.core.hu.data.HuCardGroup;
import com.kodgames.battleserver.service.battle.core.hu.data.HuCheckerMemoryPool;
import com.kodgames.battleserver.service.battle.core.hu.filter.CardGroupsFilter;
import com.kodgames.battleserver.service.battle.core.score.hu.HuScoreChecker;
import com.kodgames.battleserver.service.battle.core.score.hu.HuScoreProcessor;

/**
 * 7对,4对等
 */
public class HuChecker_7DuiHu extends HuCheckerBase
{
	public HuChecker_7DuiHu()
	{
		super(PlayType.HU_QI_DUI, PlayType.HU_SI_DUI);
	}

	@Override
	public int getTotalTileCount(BattleBean context)
	{
		return (context.getCardPool().getPlayCardCount() + 1) / 2;
	}

	/**
	 * 检测胡:七对 数组中不能包括吃碰杠的牌
	 */
	@Override
	public boolean check(BattleBean context, byte[] cardCountArray, int fixedTileCount, int masterCardCount, List<Byte> supportedCardTypes, HuCheckerMemoryPool pool,
		List<List<HuCardGroup>> outHuCardGroupCollector, boolean fillMasterCard, CardGroupsFilter cardGroupsFilter, int finishGroupsCount)
	{
		// 七对不能有吃碰杠
		if (fixedTileCount != 0)
			return false;

		List<List<HuCardGroup>> outHuCardGroups = outHuCardGroupCollector != null ? pool.allocHuCardGroupListList() : null;

		try
		{
			int totalTileCount = getTotalTileCount(context);
			// 执行检测
			if (check7Pairs(totalTileCount, cardCountArray, supportedCardTypes, pool, outHuCardGroups, cardGroupsFilter, finishGroupsCount, masterCardCount) == false)
				return false;

			// 用真实牌替换万能牌, 生成胡牌牌形
			if (outHuCardGroupCollector != null)
			{
				// System.out.println("outHuCardGroups size:" + outHuCardGroups.size());
				// outHuCardGroups.forEach(list -> System.out.println(list));

				// 需要输出牌形
				for (List<HuCardGroup> list : outHuCardGroups)
					if (fillMasterCard)
						// 填充万能牌, 生成胡牌牌形
						fillMasterCard(list, masterCardCount, supportedCardTypes, pool, outHuCardGroupCollector, false);
					else
						// 直接输出可胡牌形
						generateOutputCardGroups(list, pool, outHuCardGroupCollector);

				// System.out.println("outHuCardGroupCollector size:" + outHuCardGroupCollector.size());
				// outHuCardGroupCollector.subList(0, 300).forEach(list -> System.out.println(list));
				//
				// for (int i = 0; i < outHuCardGroupCollector.size(); ++i)
				// {
				// List<HuCardGroup> listI = new ArrayList<HuCardGroup>(outHuCardGroupCollector.get(i));
				// listI.sort(new Comparator<HuCardGroup>()
				// {
				// public int compare(HuCardGroup l, HuCardGroup r)
				// {
				// Macro.AssetFalse(l.cardList.get(0) != 0);
				// return l.cardList.get(0) - r.cardList.get(0);
				// }
				// });
				//
				// Macro.AssetFalse(listI.size() == 7);
				// for (int j = 0; j < i; ++j)
				// {
				// boolean same = true;
				// List<HuCardGroup> listJ = new ArrayList<HuCardGroup>(outHuCardGroupCollector.get(j));
				// listI.sort(new Comparator<HuCardGroup>()
				// {
				// public int compare(HuCardGroup l, HuCardGroup r)
				// {
				// return l.cardList.get(0) - r.cardList.get(0);
				// }
				// });
				//
				// Macro.AssetFalse(listJ.size() == 7);
				// for (int k = 0; k < listJ.size(); ++k)
				// {
				// if (listI.get(k).cardList.get(0) != listJ.get(k).cardList.get(0))
				// {
				// same = false;
				// break;
				// }
				// }
				//
				// Macro.AssetFalse(same == false);
				// }
				// }
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
	 * 执行7队胡牌牌形检测
	 */
	private static boolean check7Pairs(int totalTileCount, byte[] cardCountArray, List<Byte> supportedCardTypes, HuCheckerMemoryPool pool, List<List<HuCardGroup>> outHuCardGroupCollector,
		CardGroupsFilter cardGroupsFilter, int finishGroupsCount, int masterCardCount)
	{
		// 构造用于检测7队的牌组
		List<HuCardGroup> cardGroupList = pool.allocHuCardGroupList();
		for (int i = 0; i < totalTileCount; ++i)
			cardGroupList.add(pool.allocHuCardGroup(CardGroupType.JIANG, (byte)0, (byte)0));

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
	 * 尝试根据胡牌规则填充, 感觉很负责, 先不用了, 现在的算法在鬼越多越卡C(34,鬼/2)
	 */
	protected static void fillMasterToEmptyGroupFro7Pairs(BattleBean context, byte[] cardCountArray, List<Byte> supportedCardType, List<HuCardGroup> cardGroupList)
	{
		// 七对, 豪华七对, 清一色, 混一色, 字一色, 清幺九
		HuScoreProcessor huScoreProcessor = BattleHelper.getInstance().getHuScoreProcessor();

		{
			// 清幺九 : 检测手牌是否都是19
			// 混幺九 : 检测手牌是否都是19和字

			do
			{
				HuScoreChecker checker_QingYaoJiu = huScoreProcessor.getHuScoreChecker(PlayType.HU_QING_YAO_JIU);
				HuScoreChecker checker_HunYaoJiu = huScoreProcessor.getHuScoreChecker(PlayType.HU_HUN_YAO_JIU);

				if (checker_QingYaoJiu == null && checker_QingYaoJiu == null)
					break;

				// 检测分值, 理论上, 清幺九>混幺九
				Macro.AssetFalse(checker_QingYaoJiu == null || checker_QingYaoJiu == null || checker_QingYaoJiu.getModiferScore() > checker_QingYaoJiu.getModiferScore());
				Macro.AssetFalse(checker_HunYaoJiu == null || checker_HunYaoJiu == null || checker_HunYaoJiu.getModiferScore() > checker_HunYaoJiu.getModiferScore());

				// 检测牌形
				boolean hasNot19 = false;
				boolean hasNumberCard = false;
				for (HuCardGroup cardGroup : cardGroupList)
				{
					byte card = cardGroup.cardList[0];
					if (card == 0)
						break;

					if (Arrays.binarySearch(CheckHelper.CARD_YAO, card) < 0)
					{
						hasNot19 = true;
						break;
					}

					if (CardType.isNumberCardType(CardType.getCardType(card)))
						hasNumberCard = true;
				}

				// 不符合幺九牌
				if (hasNot19)
					break;

				if (hasNumberCard == false)
				{
					// 清幺九
				}
				else
				{
					// 混幺九
				}
			} while (false);
		}

		{
			// 清一色 : 检测现有牌是否都是同颜色
			// 混一色 : 检测现有牌是否是同颜色的数字牌+字牌
			do
			{
				HuScoreChecker checker_ZiYiSe = huScoreProcessor.getHuScoreChecker(PlayType.HU_ZI_YI_SE);
				HuScoreChecker checker_QingYiSe = huScoreProcessor.getHuScoreChecker(PlayType.HU_QING_YI_SE);
				HuScoreChecker checker_HunYiSe = huScoreProcessor.getHuScoreChecker(PlayType.HU_HUN_YI_SE);

				if (checker_ZiYiSe == null && checker_QingYiSe == null && checker_HunYiSe != null)
					break;

				// 检测分值, 理论上, 字一色>清一色>混一色
				Macro.AssetFalse(checker_ZiYiSe == null || checker_QingYiSe == null || checker_ZiYiSe.getModiferScore() > checker_QingYiSe.getModiferScore());
				Macro.AssetFalse(checker_QingYiSe == null || checker_HunYiSe == null || checker_QingYiSe.getModiferScore() > checker_HunYiSe.getModiferScore());
				Macro.AssetFalse(checker_ZiYiSe == null || checker_HunYiSe == null || checker_ZiYiSe.getModiferScore() > checker_HunYiSe.getModiferScore());

				// 检测牌形
				boolean numberCardTypeLessThan2 = true;
				CardType numberCardType = CardType.INVALID;
				boolean hasZiCard = false;

				for (HuCardGroup cardGroup : cardGroupList)
				{
					byte card = cardGroup.cardList[0];
					if (card == 0)
						break;

					CardType cardType = CardType.getCardType(card);
					Macro.AssetFalse(cardType != CardType.INVALID);
					Macro.AssetFalse(cardType != CardType.HUA);
					if (CardType.isNumberCardType(cardType))
					{
						if (numberCardType == CardType.INVALID)
							numberCardType = cardType;
						else
						{
							numberCardTypeLessThan2 = false;
							break;
						}
					}
					else if (cardType == CardType.ZI)
					{
						hasZiCard = true;
					}
				}

				// 数字牌类型多余一个, 不符合一色牌形
				if (numberCardTypeLessThan2 == false)
					break;

				// 理论上, 三种一色之间应该是互斥的
				if (hasZiCard && numberCardType == CardType.INVALID && checker_ZiYiSe != null)
				{
					// 满足字一色
					break;
				}

				if (hasZiCard == false && numberCardType != CardType.INVALID && checker_ZiYiSe != null)
				{
					// 满足清一色
					break;
				}

				Macro.AssetFalse(hasZiCard && numberCardType != CardType.INVALID);
				if (hasZiCard && numberCardType != CardType.INVALID && checker_ZiYiSe != null)
				{
					// 满足混一色
					break;
				}

			} while (false);
		}

		// 豪华七对 : 尝试填充与

		// 什么都没有 : 随便填充个
	}
}