package com.kodgames.battleserver.service.battle.core.score.filter;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;

import net.sf.json.JSONObject;

/**
 * 分数过滤器, 用于再次加工已经计算出来的分数
 * 
 * 暂时没有使用
 */
public abstract class ScoreFilter
{
	public static ScorePointFilter create(JSONObject context)
		throws Exception
	{
		// 反射构造实例
		ScorePointFilter instance = CreateContextHelper.instantiateClass(context, ScorePointFilter.class);
		instance.createFromContext(context);
		return instance;
	}

	public abstract boolean filter(BattleBean context, byte card, ScoreData scoreData);
}
