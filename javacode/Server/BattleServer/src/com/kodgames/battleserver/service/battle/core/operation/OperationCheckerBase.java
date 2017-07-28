package com.kodgames.battleserver.service.battle.core.operation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.creator.ICreateContextHandler;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationFilter;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationResultFilter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public abstract class OperationCheckerBase implements ICreateContextHandler
{
	public static final String KEY_PRE_FILTER = "preFilter";
	public static final String KEY_AFTER_FILTER = "afterFilter";

	protected List<OperationFilter> preFilters = new ArrayList<>();
	protected List<OperationResultFilter> afterFiltes = new ArrayList<>();

	public static OperationCheckerBase create(JSONObject context)
		throws Exception
	{
		OperationCheckerBase instance = CreateContextHelper.instantiateClass(context, OperationCheckerBase.class);
		instance.createFromContext(context);
		return instance;
	}

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		if (context.containsKey(KEY_PRE_FILTER))
		{
			JSONArray jsons = context.getJSONArray(KEY_PRE_FILTER);
			for (int i = 0; i < jsons.size(); i++)
				preFilters.add(OperationFilter.create(jsons.getJSONObject(i)));
		}

		if (context.containsKey(KEY_AFTER_FILTER))
		{
			JSONArray jsons = context.getJSONArray(KEY_AFTER_FILTER);
			for (int i = 0; i < jsons.size(); i++)
				afterFiltes.add(OperationResultFilter.create(jsons.getJSONObject(i)));
		}
	}

	public void check(BattleBean context, List<Step> inoutResult, int roleId, byte card, boolean phaseDeal)
	{

		// 前置filter过滤
		if (!filterOperation(context, card, phaseDeal))
			return;

		// 获取检测结果
		List<Step> result = doCheck(context, roleId, card, phaseDeal);

		// 后置filter过滤
		filterOperationResult(context, result, inoutResult, card, phaseDeal);
	}

	/**
	 * 前置过滤器检测，只要有一个通过检测，表示这个检测器可以进行检测
	 * 
	 * @param context
	 * @param card
	 * @param phaseDeal
	 * @return
	 */
	protected boolean filterOperation(BattleBean context, byte card, boolean phaseDeal)
	{
		if (preFilters.size() <= 0)
			return true;

		boolean filterSuccess = false;
		for (OperationFilter opFilter : preFilters)
		{
			if (opFilter.filter(context, phaseDeal ? 0 : card) == false)
				continue;

			filterSuccess = true;
		}

		return filterSuccess;
	}

	/**
	 * 后置过滤器，对checker的结果进行过滤
	 * 
	 * @param context
	 * @param result
	 * @param inoutResult
	 * @param card
	 * @param phaseDeal
	 */
	protected void filterOperationResult(BattleBean context, List<Step> result, List<Step> inoutResult, byte card, boolean phaseDeal)
	{
		if (result != null)
		{
			// 应用CheckerFilter
			result = result.stream().filter(op -> {
				boolean ignore = false;
				for (OperationResultFilter filter : afterFiltes)
				{
					if (filter.filter(context, op, card, phaseDeal) == false)
					{
						ignore = true;
						break;
					}
				}
				return !ignore;
			}).collect(Collectors.toList());

			inoutResult.addAll(result);
		}
	}

	abstract public List<Step> doCheck(BattleBean context, int roleId, byte card, boolean phaseDeal);
}