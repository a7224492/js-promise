package com.kodgames.battleserver.service.battle.core.score.battle;

import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.creator.ICreateContextHandler;
import com.kodgames.battleserver.service.battle.core.score.battle.filter.BattleScoreFilter;

import net.sf.json.JSONObject;

/**
 * 牌局结束时, 添加基于牌局计算产生的分数
 */
public class BattleScoreProcessor implements ICreateContextHandler
{
	public static final String KEY_POINT_CALCULATORS = "pointCaculators";
	public static final String KEY_SCORE_FILTERS = "scoreFilters";

	private List<BattleScoreChecker> pointCaculators = new ArrayList<>();
	private List<BattleScoreFilter> scoreFilters = new ArrayList<>();

	/**
	 * 构造计算所需的数据
	 */
	public void createFromContext(JSONObject context)
		throws Exception
	{
		if (CreateContextParser.containsKey(context, KEY_POINT_CALCULATORS))
		{
			for (JSONObject subContext : CreateContextParser.getJSONArray(context, KEY_POINT_CALCULATORS))
				pointCaculators.add(BattleScoreChecker.create(subContext));
		}

		if (CreateContextParser.containsKey(context, KEY_SCORE_FILTERS))
		{
			for (JSONObject subContext : CreateContextParser.getJSONArray(context, KEY_SCORE_FILTERS))
				scoreFilters.add(BattleScoreFilter.create(subContext));
		}
	}

	/**
	 * 计算当前胡牌分数, 产生一个ScoreData保存在PlayerInfo中
	 */
	public void process(BattleBean context)
	{
		pointCaculators.forEach(element -> element.calculate(context));
		scoreFilters.forEach(element -> element.filter(context));
	}
}
