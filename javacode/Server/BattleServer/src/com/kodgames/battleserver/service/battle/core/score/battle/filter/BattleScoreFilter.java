package com.kodgames.battleserver.service.battle.core.score.battle.filter;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.creator.ICreateContextHandler;

import net.sf.json.JSONObject;

/**
 * 计算分数之前对分数进行最后的处理
 * 
 * 常用于删除分数
 */
public abstract class BattleScoreFilter implements ICreateContextHandler
{
	/**
	 * 构造一个ScorePointCaculator实例
	 */
	public static BattleScoreFilter create(JSONObject context)
		throws Exception
	{
		// 反射构造实例
		BattleScoreFilter instance = CreateContextHelper.instantiateClass(context, BattleScoreFilter.class);
		instance.createFromContext(context);
		return instance;
	}

	public void createFromContext(JSONObject context)
		throws Exception
	{
		// 默认实现
	}

	public abstract void filter(BattleBean context);
}
