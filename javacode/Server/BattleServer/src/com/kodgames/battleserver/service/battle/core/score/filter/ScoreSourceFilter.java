package com.kodgames.battleserver.service.battle.core.score.filter;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.creator.ICreateContextHandler;

import net.sf.json.JSONObject;

/**
 * 玩家过滤器
 */
public abstract class ScoreSourceFilter implements ICreateContextHandler
{
	public static ScoreSourceFilter create(JSONObject context)
		throws Exception
	{
		// 反射构造实例
		ScoreSourceFilter instance = CreateContextHelper.instantiateClass(context, ScoreSourceFilter.class);
		instance.createFromContext(context);
		return instance;
	}

	public void createFromContext(JSONObject context)
		throws Exception
	{
		// 暂时没有什么需要构造的
	}

	public abstract boolean filter(BattleBean context, PlayerInfo player);
}