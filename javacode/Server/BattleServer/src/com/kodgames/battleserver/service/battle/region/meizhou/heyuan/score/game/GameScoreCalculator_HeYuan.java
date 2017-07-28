package com.kodgames.battleserver.service.battle.region.meizhou.heyuan.score.game;

import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.GameScore;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.score.game.GameScoreCalculator;
import com.kodgames.battleserver.service.battle.core.score.game.GameScoreFilter;
import com.kodgames.message.proto.battle.BattleProtoBuf.PlayerMatchResultPROTO;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultEventPROTO;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultScorePROTO;

import net.sf.json.JSONObject;

public class GameScoreCalculator_HeYuan extends GameScoreCalculator
{
	public static final String KEY_GAME_SCORE_FILTERS_HE_YUAN = "gameScoreFilter_heyuan";

	private List<GameScoreFilter> gameScoreFilters = new ArrayList<>();

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
		for (JSONObject subContext : CreateContextParser.getJSONArray(context, KEY_GAME_SCORE_FILTERS_HE_YUAN))
			gameScoreFilters.add(GameScoreFilter.create(subContext));
	}

	@Override
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

				// 统计主分数（潮汕，潮州主分数中没有奖马，主分数有奖马的理论上不会用到这个类）
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
						{
							// 奖马只统计胡牌类型中的个数
							if (gameScore.getScoreType() == PlayType.DISPLAY_BETTING_HOUSE && PlayType.isHuType(event.getScore().getType()))
								gameScore.setTimes(gameScore.getTimes() + (event.getCombinedTimes() + 1) * (eventSubScore.getPoint() - 1));
							else if (gameScore.getScoreType() != PlayType.DISPLAY_BETTING_HOUSE)
								gameScore.setTimes(gameScore.getTimes() + (event.getCombinedTimes() + 1) * eventSubScore.getPoint());
						}
						else
							gameScore.setTimes(gameScore.getTimes() + (event.getCombinedTimes() + 1));
					}
				}
			}
		}
	}

}
