package com.kodgames.battleserver.service.battle.region.meizhou.tuidaohu.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 广州推到胡的幺九，可能是清幺九或者幺九胡（混幺九）
 */
public class HuSubScoreChecker_YaoJiu extends HuSubScoreChecker
{

	public HuSubScoreChecker_YaoJiu()
	{
		super(PlayType.HU_YAO_JIU_GDTDH);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 是否为将牌，杠牌，刻牌，因为清幺九只能有1,9，所以不可能有顺子，或者其他的字牌
		for (byte i = 0; i < inoutHuContext.allCardCountList.length; i++)
		{
			if (inoutHuContext.allCardCountList[i] == 0)
				continue;

			// 判断是否为万条筒，幺九可以有风牌，也可以没有风牌（广州推倒胡特殊玩法）
			if (CardType.isNumberCard(i) == false)
				continue;

			// 判断序数是否为幺九
			int index = CardType.convertToCardIndex(i);
			if (index != 0 && index != 8)
				return false;
		}

		addScore(inoutHuContext.scoreData);
		return true;
	}

}
