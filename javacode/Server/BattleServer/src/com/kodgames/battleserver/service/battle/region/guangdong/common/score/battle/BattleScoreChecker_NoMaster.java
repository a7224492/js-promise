package com.kodgames.battleserver.service.battle.region.guangdong.common.score.battle;

import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.score.battle.BattleScoreChecker;

/**
 * 胡牌的时候如果没有万能牌, 分数加倍
 */
public class BattleScoreChecker_NoMaster extends BattleScoreChecker
{
	public BattleScoreChecker_NoMaster()
	{
		super(PlayType.DISPLAY_NO_MASTER_CARD);
	}

	/**
	 * 计算分数, 结果保存到相应的player身上
	 */
	public void calculate(BattleBean context)
	{
		context.getHuIndex().forEach(huRecordIndex -> {
			Step huStep = context.getRecords().get(huRecordIndex);
			PlayerInfo huPlayer = context.getPlayers().get(huStep.getRoleId());

			List<Byte> masterCards = context.getPlayerMasterCards(huPlayer.getRoleId());
			// 鬼牌不为空，判断玩家手中是否有鬼牌
			if (masterCards != null)
			{
				// 检查玩家不包括吃碰杠的牌中是否有鬼牌
				for (byte card : huPlayer.getCards().getHandCards())
					if (masterCards.contains(card))
						return;

				// 判断胡的那一步中是否有鬼牌，并且是自摸
				for (Step step : huPlayer.getCards().getCardHeap())
					// 胡的那一步并且是自摸并且是鬼牌
					if (step.getRoleId() == huPlayer.getRoleId() && step.getPlayType() == PlayType.OPERATE_HU && masterCards.contains(step.getCards().get(0)))
						return;
			}

			// 没有鬼牌, 给胡牌Record增加倍数
			for (ScoreData scoreData : huPlayer.getCards().getScoreDatas())
			{
				if (PlayType.isHuType(scoreData.getPoints().get(0).getScoreType()))
				{
					addScore(scoreData);
					break;
				}
			}
		});
	}
}
