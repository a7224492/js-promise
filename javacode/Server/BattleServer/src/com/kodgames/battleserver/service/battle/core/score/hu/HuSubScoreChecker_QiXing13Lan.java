package com.kodgames.battleserver.service.battle.core.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;

/**
 * 检测牌形:七星十三烂
 * 
 * 在十三烂的基础上，凑齐东南西北中发白，称为七星。
 */
public class HuSubScoreChecker_QiXing13Lan extends HuSubScoreChecker
{

	public HuSubScoreChecker_QiXing13Lan()
	{
		super(PlayType.HU_QIXING13LAN);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{

		if (checkQiXing13Lan(context, roleId, inoutHuContext))
		{
			addScore(inoutHuContext.scoreData);

			return true;
		}

		return false;
	}

	protected boolean checkQiXing13Lan(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 七星13烂，要7张字牌
		byte[] cardCountArray = inoutHuContext.allCardCountList;
		if (checkZiCard(cardCountArray) == false)
		{
			return false;
		}

		// 检查序数牌
		for (CardType type : CardType.values())
		{
			if (CardType.isNumberCardType(type) && checkNumberCard(cardCountArray, type.Value()) == false)
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查字牌（七星）
	 */
	private boolean checkZiCard(byte[] handCardCountList)
	{
		for (byte card = CardType.ZI.Value(); card < CardType.ZI.MaxValue(); ++card)
		{
			if (handCardCountList[card] != 1)
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查数字牌
	 */
	private boolean checkNumberCard(byte[] cardCountArray, byte base)
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

}
