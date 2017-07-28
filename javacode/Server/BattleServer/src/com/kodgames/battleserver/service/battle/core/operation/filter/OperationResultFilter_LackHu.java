package com.kodgames.battleserver.service.battle.core.operation.filter;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardInfo;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.check.CheckHelper;

/** 胡牌缺门检测 */
public class OperationResultFilter_LackHu extends OperationResultFilter
{
	@Override
	public boolean filter(BattleBean context, Step result, byte card, boolean phaseDeal)
	{
		if (result != null)
		{
			PlayerInfo playerInfo = context.getPlayers().get(result.getRoleId());
			CardInfo cardInfo = playerInfo.getCards();
			byte[] cardCountArray = CheckHelper.converToCardCountArray(cardInfo);
			if (!phaseDeal)
				cardCountArray[card]++;

			// 获取玩家选择的缺门牌类型
			CardType lackType = CardType.INVALID;
			for (Step step : cardInfo.getCardHeap())
			{
				if (step.getPlayType() == PlayType.OPERATE_LACK)
				{
					lackType = CardType.getCardType(step.getCards().get(0));
					break;
				}
			}

			// 如果玩家选择了缺门，手牌中含有缺门牌不可以胡牌
			if (lackType != CardType.INVALID)
			{
				for (int i = 0; i < cardCountArray.length; i++)
				{
					if (cardCountArray[i] == 0)
						continue;

					if (lackType.isBelongTo((byte)i))
						return false;
				}
			}

			// 玩家手中牌的类型不可以超过2色
			return CheckHelper.getPlayerCardTypeCount(playerInfo, phaseDeal ? 0 : card) <= 2;
		}
		else
			return true;
	}
}