package com.kodgames.battleserver.service.battle.core.score.game;

import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.GameScore;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.creator.ICreateContextHandler;
import com.kodgames.message.proto.battle.BattleProtoBuf.PlayerMatchResultPROTO;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultEventPROTO;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultScorePROTO;

import net.sf.json.JSONObject;

/**
 * 用于统计房间结束结果中制定项得分次数
 * 
 * 其中包含的GameScoreFilter为需要统计的得分项
 */
public class GameScoreCalculator implements ICreateContextHandler
{
	public static final String KEY_GAME_SCORE_FILTER = "gameScoreFilter";

	protected List<GameScoreFilter> gameScoreFilters = new ArrayList<>();

	public static GameScoreCalculator create(JSONObject context)
		throws Exception
	{
		// 反射构造实例
		GameScoreCalculator instance = CreateContextHelper.instantiateClass(context, GameScoreCalculator.class);
		instance.createFromContext(context);
		return instance;
	}

	public void createFromContext(JSONObject context)
		throws Exception
	{
		// 解析参数
		for (JSONObject subContext : CreateContextParser.getJSONArray(context, KEY_GAME_SCORE_FILTER))
			gameScoreFilters.add(GameScoreFilter.create(subContext));
	}

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
				if (gameScore.getScoreType() == event.getScore().getType())
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
					if (gameScore.getScoreType() == eventSubScore.getType())
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
}
