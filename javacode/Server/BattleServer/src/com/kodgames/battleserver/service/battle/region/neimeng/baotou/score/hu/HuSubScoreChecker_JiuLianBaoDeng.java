package com.kodgames.battleserver.service.battle.region.neimeng.baotou.score.hu;

import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardGroup;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.CardGroupType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 牌型算分：九莲宝灯
 * 
 * 九莲宝灯，35分。（由一种花色序数牌子按1112345678999组成的特定牌型）
 */
public class HuSubScoreChecker_JiuLianBaoDeng extends HuSubScoreChecker
{
	private int[] pattern = {3, 1, 1, 1, 1, 1, 1, 1, 3};

	public HuSubScoreChecker_JiuLianBaoDeng()
	{
		super(PlayType.HU_JIU_LIAN_BAO_DENG);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 只能有将、暗坎、暗刻
		List<CardGroup> groups = inoutHuContext.scoreData.getCardGroups();
		for (CardGroup group : groups)
		{
			int type = group.getGroupType();
			if (type != CardGroupType.JIANG && type != CardGroupType.AN_KAN && type != CardGroupType.AN_KE)
			{
				return false;
			}
		}

		// 单花色序数牌的数量与 pattern 吻合
		for (CardType type : CardType.values())
		{
			if (!CardType.isNumberCardType(type))
				continue;

			int index = 0;
			for (; index < pattern.length; ++index)
			{
				byte card = type.convertToCard(index);
				if (inoutHuContext.cardCountList[card] < pattern[index])
					break;
			}

			if (index == pattern.length)
			{
				addScore(inoutHuContext.scoreData);
				return true;
			}
		}

		return false;
	}
}
