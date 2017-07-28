package com.kodgames.battleserver.service.battle.core.score.filter;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.creator.ICreateContextHandler;

import net.sf.json.JSONObject;

public abstract class ScorePointFilter implements ICreateContextHandler
{
	public static ScorePointFilter create(JSONObject context)
		throws Exception
	{
		// 反射构造实例
		ScorePointFilter instance = CreateContextHelper.instantiateClass(context, ScorePointFilter.class);
		instance.createFromContext(context);
		return instance;
	}

	public abstract boolean filter(BattleBean context, byte card);
}
