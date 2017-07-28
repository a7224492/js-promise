package com.kodgames.battleserver.service.battle.core.hu.data;

import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.core.hu.CardGroupType;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalcluateContext;

/**
 * 胡牌之后用于计算牌形的数据
 */
public class HuScoreCheckContext
{
	/**
	 * 牌数量数组,不包括吃,碰,杠(明杠,暗杠)
	 */
	public byte[] cardCountList = new byte[CardType.TOTAL_COUNT.Value()];

	/**
	 * 所有牌数量数组
	 */
	public byte[] allCardCountList = new byte[CardType.TOTAL_COUNT.Value()];

	/**
	 * 胡牌分数
	 */
	public ScoreData scoreData = new ScoreData();

	/**
	 * 计算过分数
	 */
	public ScoreCalcluateContext calculatedScore;

	public void copyFrom(HuScoreCheckContext other)
	{
		for (int i = 0; i < cardCountList.length; ++i)
			cardCountList[i] = other.cardCountList[i];
		for (int i = 0; i < allCardCountList.length; ++i)
			allCardCountList[i] = other.allCardCountList[i];
		scoreData.copyFrom(other.scoreData);
		if (calculatedScore == null)
			calculatedScore = new ScoreCalcluateContext();
		calculatedScore.copyFrom(other.calculatedScore);
	}

	public void createCardCountList()
	{
		scoreData.getCardGroups().forEach(cardGroup -> {
			cardGroup.getCardList().forEach(card -> {
				allCardCountList[card]++;
				if (CardGroupType.isInHand(cardGroup.getGroupType()))
					cardCountList[card]++;
			});
		});
	}
}
