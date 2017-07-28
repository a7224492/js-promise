package com.kodgames.battleserver.service.battle.core.operation.filter;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.creator.ICreateContextHandler;

import net.sf.json.JSONObject;

public abstract class OperationFilter implements ICreateContextHandler
{
	public static OperationFilter create(JSONObject context)
		throws Exception
	{
		// 反射构造实例
		OperationFilter instance = CreateContextHelper.instantiateClass(context, OperationFilter.class);
		instance.createFromContext(context);
		return instance;
	}

	public abstract boolean filter(BattleBean context, byte card);
}
