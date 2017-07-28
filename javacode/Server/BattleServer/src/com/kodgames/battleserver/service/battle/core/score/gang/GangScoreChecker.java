package com.kodgames.battleserver.service.battle.core.score.gang;

import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.score.ScoreChecker;
import com.kodgames.battleserver.service.battle.core.score.filter.ScorePointFilter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public abstract class GangScoreChecker extends ScoreChecker
{
	public static final String KEY_pointFilters = "pointFilters";
	private List<ScorePointFilter> scorePointFilters = new ArrayList<>();

	protected GangScoreChecker(int scoreType)
	{
		super(scoreType);
	}

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		super.createFromContext(context);
		if (context.containsKey(KEY_pointFilters))
		{
			JSONArray jsons = context.getJSONArray(KEY_pointFilters);
			for (int i = 0; i < jsons.size(); i++)
				scorePointFilters.add(ScorePointFilter.create(jsons.getJSONObject(i)));
		}
	}

	/**
	 * 构造一个ScorePointCaculator实例
	 */
	public static GangScoreChecker create(JSONObject context)
		throws Exception
	{
		// 反射构造实例
		GangScoreChecker instance = CreateContextHelper.instantiateClass(context, GangScoreChecker.class);
		instance.createFromContext(context);
		return instance;
	}

	public void calculate(BattleBean context, byte card, GangScoreCheckContext inoutHuContext)
	{
		// 分数过滤器，如果某一个过滤器没有通过，不添加分数
		for (ScorePointFilter filter : scorePointFilters)
		{
			if (filter.filter(context, card) == false)
				return;
		}

		doCalculate(inoutHuContext);
	}

	/**
	 * 计算得分
	 * 
	 * @param context 战斗上下文
	 * @param roleId 需要计算的玩家Id
	 * @param inoutHuContext 计算结果存入的结构
	 * @return 如果产生结果, 返回true, 同时将结果存入inoutHuContext
	 * @see [类、类#方法、类#成员]
	 */
	public abstract boolean doCalculate(GangScoreCheckContext inoutHuContext);
}
