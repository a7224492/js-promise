package com.kodgames.battleserver.service.battle.region.guangdong.common.score.battle;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.score.battle.BattleScoreChecker;

/**
 * 跟庄
 * 
 * 开局四个人依次打出同一张牌，那么庄家输给各家1分
 */
public class BattleScoreChecker_FollowBanker extends BattleScoreChecker
{
	public BattleScoreChecker_FollowBanker()
	{
		super(PlayType.DISPLAY_FOLLOW_BANKER);
	}

	/**
	 * 计算分数, 结果保存到相应的player身上
	 */
	public void calculate(BattleBean context)
	{
		// 判断第一圈是否都打跟第一张一样的牌
		int count = 0;
		byte card = 0;

		for (Step step : context.getRecords())
		{
			// 如果是非OPERATE_PLAY_A_CARD的主动行为, 不够成跟庄
			if (PlayType.isGangOperator(step.getPlayType()) || step.getPlayType() == PlayType.OPERATE_CHI_A_CARD || step.getPlayType() == PlayType.OPERATE_PENG_A_CARD
				|| step.getPlayType() == PlayType.OPERATE_HU)
				return;

			// 忽略其他行为
			if (step.getPlayType() != PlayType.OPERATE_PLAY_A_CARD)
				continue;

			// 打的牌应该都一样
			if (count == 0)
				card = step.getCards().get(0);
			else if (card != step.getCards().get(0))
				return;

			count++;

			// 跟的次数跟玩家次数一致, 满足跟庄
			if (count == context.getPlayerIds().size())
			{
				// 添加scoreData
				ScoreData scoreData = new ScoreData();
				scoreData.setAddOperation(true);
				scoreData.setSourceCard((byte)0);
				scoreData.setSourceRecrodIndex(-1);
				scoreData.setSourceId(context.getZhuang());
				for (int roleId : context.getPlayerIds())
				{
					// 给不是庄家的人分
					if (roleId != context.getZhuang())
						scoreData.getScoreTargetList().add(roleId);
				}

				addScore(scoreData);

				// 添加到庄家身上，最后会反向添加到其他人身上
				context.getPlayerById(context.getZhuang()).getCards().getScoreDatas().add(scoreData);

				return;
			}
		}
	}
}
