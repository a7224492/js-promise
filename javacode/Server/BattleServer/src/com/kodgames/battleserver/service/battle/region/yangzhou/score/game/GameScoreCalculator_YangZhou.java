package com.kodgames.battleserver.service.battle.region.yangzhou.score.game;

import com.kodgames.battleserver.service.battle.common.xbean.GameScore;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.score.game.GameScoreCalculator;
import com.kodgames.message.proto.battle.BattleProtoBuf.PlayerMatchResultPROTO;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultEventPROTO;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultScorePROTO;

public class GameScoreCalculator_YangZhou extends GameScoreCalculator
{
	public void calculate(PlayerInfo player, PlayerMatchResultPROTO.Builder playerMatchResult)
	{
		// 如果玩家得分为0, 初始化得分
		if (player.getCards().getGameScore().size() == 0)
			gameScoreFilters.forEach((filter) -> {
				GameScore gameScore = new GameScore();
				gameScore.setScoreType(filter.scoreType);
				gameScore.setAddOperation(filter.addOperation);
				gameScore.setCalculateScorePoint(filter.calculateScorePoint);
				player.getCards().getGameScore().add(gameScore);
			});

		// 累计计算得分
		for (ResultEventPROTO.Builder event : playerMatchResult.getEventsBuilderList())
		{
			for (GameScore gameScore : player.getCards().getGameScore())
			{
				// 如果操作不相同, 就不用检测了.
				if (gameScore.getAddOperation() != event.getAddOperation())
					continue;

				// 统计主分数
				if(this.isSameType(gameScore.getScoreType(), event.getScore().getType()))
				{
					if (gameScore.getCalculateScorePoint())
						gameScore.setTimes(gameScore.getTimes() + (event.getCombinedTimes() + 1) * event.getScore().getPoint());
					else
						gameScore.setTimes(gameScore.getTimes() + (event.getCombinedTimes() + 1));
				}

				// 统计子分数
				for (ResultScorePROTO.Builder eventSubScore : event.getSubScoresBuilderList())
				{
					// 与主分数相同的不再重复统计
					if (eventSubScore.getType() == event.getScore().getType())
						continue;

					// 检查自分数
					if (this.isSameType(eventSubScore.getType(), event.getScore().getType()))
					{
						if (gameScore.getCalculateScorePoint())
							gameScore.setTimes(gameScore.getTimes() + (event.getCombinedTimes() + 1) * eventSubScore.getPoint());
						else
							gameScore.setTimes(gameScore.getTimes() + (event.getCombinedTimes() + 1));
					}
				}
			}
		}
	}
	
	/**
	 * 判断一个playType是否属于房间的统计项
	 * @param gameType:房间的统计项
	 * @param playType:需要被检测的playType
	 */
	private boolean isSameType(int gameType, int playType)
	{
		boolean result = false;
		
		if(gameType == PlayType.OPERATE_GANG_A_CARD)
		{
			result = (playType == PlayType.OPERATE_GANG_A_CARD) || (playType == PlayType.OPERATE_BU_GANG_A_CARD);
		}
		else
			result = (playType == gameType);
		
		return result;
	}
}
