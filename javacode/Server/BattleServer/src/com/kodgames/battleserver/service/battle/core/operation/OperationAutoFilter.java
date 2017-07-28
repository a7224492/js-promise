package com.kodgames.battleserver.service.battle.core.operation;

import java.util.List;

import com.kodgames.battleserver.service.battle.Controller.ControllerManager;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.creator.ICreateContextHandler;

import net.sf.json.JSONObject;

/**
 * 打牌特殊操作的过滤器，用于对这些特殊操作进行自动处理，比如自动胡牌
 *
 */
public abstract class OperationAutoFilter implements ICreateContextHandler
{
	public static OperationAutoFilter create(JSONObject context)
		throws Exception
	{
		// 反射构造实例
		OperationAutoFilter instance = CreateContextHelper.instantiateClass(context, OperationAutoFilter.class);
		instance.createFromContext(context);
		return instance;
	}

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
	}

	public abstract List<Step> filter(ControllerManager controller, List<Step> result);
}
