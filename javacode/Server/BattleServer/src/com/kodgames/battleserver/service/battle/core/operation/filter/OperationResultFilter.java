package com.kodgames.battleserver.service.battle.core.operation.filter;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.creator.ICreateContextHandler;

import net.sf.json.JSONObject;

public abstract class OperationResultFilter implements ICreateContextHandler
{
	public static OperationResultFilter create(JSONObject context)
		throws Exception
	{
		OperationResultFilter instance = CreateContextHelper.instantiateClass(context, OperationResultFilter.class);
		instance.createFromContext(context);
		return instance;
	}

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
	}

	/**
	 * 过滤操作结果

	 * @param context 牌局上下文
	 * @param result 要执行的操作
	 * @param card 引起操作的牌
	 * @param phaseDeal 当前是否是自己摸牌
	 * @return 如果该结果不合法, 返回false
	 */
	public abstract boolean filter(BattleBean context, Step result, byte card, boolean phaseDeal);
}