package com.kodgames.battleserver.service.battle.core.finish;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.creator.ICreateContextHandler;

import net.sf.json.JSONObject;

/**
 * 检测牌局是否可以结束
 */
public abstract class BattleFinishChecker implements ICreateContextHandler
{
	public static BattleFinishChecker create(JSONObject context)
		throws Exception
	{
		BattleFinishChecker instance = CreateContextHelper.instantiateClass(context, BattleFinishChecker.class);
		instance.createFromContext(context);
		return instance;
	}

	public abstract void check(BattleBean context);
	
	/** 是否支持牌局多次胡牌 */
	public abstract boolean enableMutilHu();
}