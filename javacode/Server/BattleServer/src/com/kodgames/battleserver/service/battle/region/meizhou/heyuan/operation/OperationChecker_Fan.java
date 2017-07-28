package com.kodgames.battleserver.service.battle.region.meizhou.heyuan.operation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.check.CheckHelper;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.hu.CardGroupType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuCardGroup;
import com.kodgames.battleserver.service.battle.core.operation.OperationCheckerBase;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationFilter;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationFilter_LastOperator;

import net.sf.json.JSONObject;

/**
 * 打哪一张可以翻
 */
public class OperationChecker_Fan extends OperationCheckerBase
{

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		super.createFromContext(context);

		// 翻的条件是抓牌后或碰牌后进行判断
		OperationFilter operationFilter1 = new OperationFilter_LastOperator();
		OperationFilter operationFilter2 = new OperationFilter_LastOperator();
		operationFilter1.createFromContext(CreateContextHelper.createOperationFilter_LastOP(MahjongConstant.PlayType.OPERATE_DEAL));
		operationFilter2.createFromContext(CreateContextHelper.createOperationFilter_LastOP(MahjongConstant.PlayType.OPERATE_PENG_A_CARD));
		preFilters.add(operationFilter1);
		preFilters.add(operationFilter2);
	}

	@Override
	public List<Step> doCheck(BattleBean context, int roleId, byte card, boolean phaseDeal)
	{
		List<Step> cardHeap = context.getPlayerById(roleId).getCards().getCardHeap();

		// 从最后面的操作开始
		for (int i = cardHeap.size() - 1; i >= 0; i--)
		{
			Step step = cardHeap.get(i);

			// 如果有breakFan标记就不是翻的状态了
			if (step.getPlayType() == PlayType.OPERATE_BREAK_FAN_A_CARD)
			{
				break;
			}

			// 如果是翻的状态就进入下一步
			if (step.getPlayType() == PlayType.OPERATE_FAN_A_CARD)
			{
				return null;
			}

			// 如果最后什么标记都没有就也不是翻的状态
			if (i == 0)
			{
				break;
			}
		}

		// 不是玩家自己摸牌，不可以打翻倍
		if (!phaseDeal)
			return null;

		// 可以翻的step列表
		List<Step> result = new ArrayList<>();

		// 可以打出的牌
		List<Byte> cards = canPlay(context, roleId);

		if (cards != null && cards.size() != 0)
		{
			for (byte canFanCard : cards)
			{
				// 符合翻的所有可打出牌
				result.add(new Step(roleId, PlayType.OPERATE_CAN_FAN_A_CARD, canFanCard));
			}
			// 符合翻的所有可打出牌
			// result.add(new Step(roleId, PlayType.OPERATE_CAN_FAN_A_CARD, cards));
			return result;
		}

		return null;
	}

	/**
	 * 得到满足翻条件可以打出的牌
	 */
	public static List<Byte> canPlay(BattleBean context, int roleId)
	{
		// 可以打出的牌
		List<Byte> cardList = new ArrayList<>();

		List<List<HuCardGroup>> huCardList = CheckHelper.getTingGroups(context, roleId);

		createTingJiang(huCardList, context, roleId);

		for (List<HuCardGroup> huCard : huCardList)
		{
			for (HuCardGroup huCardGroup : huCard)
			{
				// 得到将牌组
				if (huCardGroup.groupType == CardGroupType.JIANG)
				{
					// 第一张将牌是花牌
					boolean masterCard1 = false;

					// 第二张是花牌
					boolean masterCard2 = false;

					// 第一张将牌
					byte jiangCard1 = -1;

					// 第二张将牌
					byte jiangCard2 = -1;
					for (int i = 0; i < huCardGroup.cardList.length; i++)
					{
						if (i == 0)
						{
							jiangCard1 = huCardGroup.cardList[i];
							if (jiangCard1 == 0)
							{
								masterCard1 = true;
							}
						}
						if (i == 1)
						{
							jiangCard2 = huCardGroup.cardList[i];
							if (jiangCard2 == 0)
							{
								masterCard2 = true;
							}
						}
					}

					// 将牌组都为花牌就随便打手牌中的哪一张花牌
					if (masterCard1 && masterCard2)
					{
						byte[] handCards = CheckHelper.converToCardCountArray(context.getPlayerById(roleId).getCards());

						for (byte card = CardType.HUA.Value(); card < handCards.length; card++)
						{
							int huaCardCount = handCards[card];

							if (huaCardCount != 0)
							{
								cardList.add(card);
							}
						}
					}

					// 只有一张花牌就打出非花牌那一张
					else if (masterCard1 && !masterCard2)
					{
						cardList.add(jiangCard2);
					}
					else if (!masterCard1 && masterCard2)
					{
						cardList.add(jiangCard1);
					}
				}
			}
		}

		if (cardList != null && cardList.size() != 0)
		{
			// 去除重复的可打出将牌
			HashSet<Byte> set = new HashSet<Byte>(cardList);
			cardList.clear();
			cardList.addAll(set);

			// 十三幺不能翻
			if (cardList.size() < 13)
			{
				return cardList;
			}

		}

		return null;
	}

	/**
	 * 添加一组可以万能牌当将牌的牌组
	 */
	private static void createTingJiang(List<List<HuCardGroup>> huCardList, BattleBean context, int roleId)
	{
		if (huCardList == null || huCardList.isEmpty())
		{
			return;
		}

		// // 这时已经是14张牌了
		// byte[] handCards = CheckHelper.converToCardCountArray(context.getPlayerById(roleId).getCards());

		// // 花牌的数量
		// int huaCardCount = 0;

		// 要手动添加的将对
		List<Byte> doubleCards = new ArrayList<>();

		// // 得到手牌中花牌的数量
		// for (byte card = CardType.HUA.Value(); card < handCards.length; card++)
		// {
		// huaCardCount += handCards[card];
		// }

		// 就是有相同的牌和一组将牌时才出现问题，七小对不会有问题

		for (List<HuCardGroup> huList : huCardList)
		{
			// 是否可以添加到将牌组
			boolean canAddJiang = false;

			// 将牌组的数量
			int jiangGroupCount = 0;

			// 将牌的值
			byte jiangCard = -1;
			for (HuCardGroup huGroup : huList)
			{
				// 如果有好几组将不用处理
				if (jiangGroupCount > 1)
				{
					return;
				}

				// 拿出将牌
				if (huGroup.groupType == CardGroupType.JIANG)
				{
					jiangCard = huGroup.cardList[0];
					jiangGroupCount++;
					continue;
				}

				// 是否有万能花牌
				boolean hasHuaCard = false;

				// 得到胡的非将组
				HuCardGroup huCardGroup = new HuCardGroup(huGroup);

				if (huCardGroup != null)
				{
					// 把拿出的将牌与其他组中的万能花牌互换
					for (int i = 0; i < huCardGroup.cardList.length; i++)
					{
						if (huCardGroup.cardList[i] == 0)
						{
							huCardGroup.cardList[i] = jiangCard;

							hasHuaCard = true;
						}

					}
				}

				// 判断是否可以互换
				if (hasHuaCard && huCardGroup.groupType == CardGroupType.AN_KAN)
				{
					canAddJiang = isAnKan(huCardGroup.cardList);
				}
				else if (hasHuaCard && huCardGroup.groupType == CardGroupType.AN_KE)
				{
					canAddJiang = isAnKe(huCardGroup.cardList);
				}

			}

			// 可以互换就把那张将牌加进来
			if (canAddJiang)
			{
				doubleCards.add(jiangCard);
			}
		}

		// 手动构造hucardgroup放进huCardList中
		for (byte doubleCard : doubleCards)
		{
			HuCardGroup myCreateJiang = new HuCardGroup(2);
			myCreateJiang.groupType = CardGroupType.JIANG;
			myCreateJiang.cardList[0] = 0;
			myCreateJiang.cardList[1] = doubleCard;
			List<HuCardGroup> huCard = new ArrayList<>();

			huCard.add(myCreateJiang);

			huCardList.add(huCard);
		}

	}

	/**
	 * 判断是否符合暗刻
	 */
	private static boolean isAnKe(byte[] cards)
	{
		if (cards.length != 3 || cards == null)
		{
			return false;
		}

		byte card = cards[0];
		for (int i = 0; i < cards.length; i++)
		{
			if (cards[i] != card)
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * 判断是否符合暗坎
	 */
	private static boolean isAnKan(byte[] cards)
	{
		if (cards.length != 3 || cards == null)
		{
			return false;
		}
		byte card = cards[0];
		for (int i = 0; i < cards.length; i++)
		{
			if (cards[i] != (card + i))
			{
				return false;
			}
		}

		return true;
	}

}
