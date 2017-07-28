package com.kodgames.battleserver.service.battle.region.neimeng.common.score.hu;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 检测牌型：够张
 * 
 * 同一类型的牌（筒子、条子、万子），胡牌时够8张。
 */
public class HuSubScoreChecker_GouZhang extends HuSubScoreChecker
{
	private Set<CardType> checkTypes = new HashSet<>();

	public HuSubScoreChecker_GouZhang()
	{
		super(PlayType.HU_GOU_ZHANG);

		checkTypes.add(CardType.WAN);
		checkTypes.add(CardType.TIAO);
		checkTypes.add(CardType.TONG);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 统计每种花色的牌数量
		Map<CardType, Integer> cardTypeCount = new HashMap<>();
		for (CardType type : checkTypes)
		{
			for (Byte c = type.Value(); c < type.MaxValue(); ++c)
			{
				int addCount = inoutHuContext.allCardCountList[c];
				CardType t = CardType.getCardType(c);
				Integer count = cardTypeCount.get(t);
				cardTypeCount.put(t, (null == count) ? addCount : (count + addCount));
			}
		}

		boolean result = false;
		for (CardType type : checkTypes)
		{
			Integer count = cardTypeCount.get(type);
			if (null != count && count >= 8)
			{
				addScore(inoutHuContext.scoreData);
				result = true;
			}
		}

		return result;
	}

}
