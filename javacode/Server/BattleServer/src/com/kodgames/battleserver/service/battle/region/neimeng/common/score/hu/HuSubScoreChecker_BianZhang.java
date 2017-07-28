package com.kodgames.battleserver.service.battle.region.neimeng.common.score.hu;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardGroup;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.check.CheckHelper;
import com.kodgames.battleserver.service.battle.core.hu.CardGroupType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 检测牌型：边张
 * 
 * 单胡123的3及789的7或1233胡3、7789胡7都为边张。
 */
public class HuSubScoreChecker_BianZhang extends HuSubScoreChecker
{

	public HuSubScoreChecker_BianZhang()
	{
		super(PlayType.HU_BIAN_ZHANG);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 最后一步操作只能有一张牌
		List<Step> records = context.getRecords();
		Step lastStep = records.get(records.size() - 1);
		List<Byte> lastCards = lastStep.getCards();
		if (1 != lastCards.size())
			return false;

		// 只能胡单张
		List<Byte> handCards = context.getPlayers().get(roleId).getCards().getHandCards();
		boolean isZiMo = roleId == lastStep.getRoleId();
		if (isZiMo)
			handCards.remove(lastCards.get(0));
		List<Byte> tingCards = CheckHelper.getTingCards(context, roleId);
		if (isZiMo)
			handCards.add(lastCards.get(0));
		if (1 != tingCards.size())
			return false;

		byte card = lastCards.get(0);
		List<Set<Byte>> patterns = getPatterns(card);
		List<CardGroup> cardGroups = inoutHuContext.scoreData.getCardGroups();
		for (CardGroup group : cardGroups)
		{
			// 边张只能分组到暗坎
			if (CardGroupType.AN_KAN != group.getGroupType())
				continue;

			for (Set<Byte> pattern : patterns)
			{
				// 判断牌组和模式是否匹配
				boolean isMatched = true;
				for (byte c : group.getCardList())
				{
					if (!pattern.contains(c))
					{
						isMatched = false;
						break;
					}
				}

				// 匹配则加分到上下文
				if (isMatched)
				{
					addScore(inoutHuContext.scoreData);
					return true;
				}
			}
		}

		return false;
	}

	private List<Set<Byte>> getPatterns(byte card)
	{
		List<Set<Byte>> requiredCards = new ArrayList<>();
		CardType type = CardType.getCardType(card);

		// 边张模式
		byte[][] patterns = {{(byte)(card - 2), (byte)(card - 1), card}, {card, (byte)(card + 1), (byte)(card + 2)}};

		// 校验每一个模式的花色
		for (byte[] pattern : patterns)
		{
			Set<Byte> cards = new HashSet<>();
			for (byte c : pattern)
			{
				if (type.equals(CardType.getCardType(c)))
					cards.add(c);
				else
					break;
			}

			if (cards.size() == pattern.length)
				requiredCards.add(cards);
		}

		return requiredCards;
	}

}
