package com.kodgames.battleserver.service.battle.core.score.gang;

import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.creator.ICreateContextHandler;
import com.kodgames.battleserver.service.battle.core.score.filter.ScoreSourceFilter;
import com.kodgames.battleserver.service.battle.core.score.filter.ScoreTargetFilter;

import net.sf.json.JSONObject;

public class GangScoreProcessor implements ICreateContextHandler
{
	public static final String KEY_SOURCE_PLAYER_FILTERS = "sourcePlayerFilters";
	public static final String KEY_SCORE_TARGET_FILTERS = "scoreTargetFilters";
	public static final String KEY_POINT_CACULATORS = "pointCaculators";

	private List<ScoreSourceFilter> sourcePlayerFilters = new ArrayList<>();
	private List<ScoreTargetFilter> scoreTargetFilters = new ArrayList<>();
	private List<GangScoreChecker> pointCaculators = new ArrayList<>();

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		// 解析参数
		if (context.containsKey(KEY_SOURCE_PLAYER_FILTERS))
		{
			for (JSONObject subContext : CreateContextParser.getJSONArray(context, KEY_SOURCE_PLAYER_FILTERS))
				sourcePlayerFilters.add(ScoreSourceFilter.create(subContext));
		}
		if (context.containsKey(KEY_SCORE_TARGET_FILTERS))
		{
			for (JSONObject subContext : CreateContextParser.getJSONArray(context, KEY_SCORE_TARGET_FILTERS))
				scoreTargetFilters.add(ScoreTargetFilter.create(subContext));
		}
		if (context.containsKey(KEY_POINT_CACULATORS))
		{
			for (JSONObject subContext : CreateContextParser.getJSONArray(context, KEY_POINT_CACULATORS))
				pointCaculators.add(GangScoreChecker.create(subContext));
		}
	}

	public ScoreData process(BattleBean context, PlayerInfo playerInfo, int playType, byte card)
	{
		GangScoreCheckContext scoreContext = new GangScoreCheckContext();

		// 获取分数来源者
		PlayerInfo sourcePlayer = null;
		for (ScoreSourceFilter filter : sourcePlayerFilters)
		{
			for (PlayerInfo player : context.getPlayers().values())
			{
				if (filter.filter(context, player) == false)
					continue;

				sourcePlayer = player;
				break;
			}

			if (sourcePlayer == null)
				break;
		}

		scoreContext.scoreData.setSourceId(sourcePlayer.getRoleId());
		scoreContext.scoreData.setAddOperation(true);
		scoreContext.scoreData.setSourceCard(card);
		scoreContext.scoreData.setSourceRecrodIndex(context.getRecords().size() - 1); // sourceId 的操作索引

		/*
		 * 计算收取分数的目标
		 */
		for (PlayerInfo player : context.getPlayers().values())
		{
			// 所有过滤器都通过才满足目标
			boolean meetCondition = true;
			for (ScoreTargetFilter filter : scoreTargetFilters)
			{
				if (filter.filter(context, playerInfo, sourcePlayer, player))
					continue;

				meetCondition = false;
				break;
			}

			if (meetCondition)
				scoreContext.scoreData.getScoreTargetList().add(player.getRoleId());
		}

		/*
		 * 计算分数
		 */
		pointCaculators.forEach((caculator) -> {
			if (caculator.getScoreType() == playType)
				caculator.calculate(context, card, scoreContext);
		});

		return scoreContext.scoreData;
	}
}
