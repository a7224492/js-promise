package com.kodgames.battleserver.service.battle.core.operation;

import java.util.List;

import com.kodgames.battleserver.service.battle.Controller.ControllerManager;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.creator.ICreateContextHandler;

import net.sf.json.JSONObject;

public abstract class OperationCheckerFilter implements ICreateContextHandler
{
	public static OperationCheckerFilter create(JSONObject context)
		throws Exception
	{
		// 反射构造实例
		OperationCheckerFilter instance = CreateContextHelper.instantiateClass(context, OperationCheckerFilter.class);
		instance.createFromContext(context);
		return instance;
	}

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
	}

	public abstract void filter(ControllerManager controller, int roleId, List<Step> result);
}
