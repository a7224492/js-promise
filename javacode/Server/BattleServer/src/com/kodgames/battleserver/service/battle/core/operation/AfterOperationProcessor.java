package com.kodgames.battleserver.service.battle.core.operation;

import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.service.battle.Controller.ControllerManager;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.creator.ICreateContextHandler;

import net.sf.json.JSONObject;

/**
 * 用于主要操作执行之后, 添加附属的操作
 */
public class AfterOperationProcessor implements ICreateContextHandler
{
	public static final String KEY_PROCESSORS = "processors";
	public static final String KEY_PROCESSORS_2ND = "processors_2nd";

	/**
	 * 具体需要执行的附加操作处理器
	 */
	private List<AfterOperationProcessorBase> processors = new ArrayList<>();
	private List<AfterOperationProcessorBase_2nd> processors_2nd = new ArrayList<>();

	/**
	 * 根据上下文构造内容 重载方法
	 */
	public void createFromContext(JSONObject context)
		throws Exception
	{
		if (CreateContextParser.containsKey(context, KEY_PROCESSORS))
			for (JSONObject subContext : CreateContextParser.getJSONArray(context, KEY_PROCESSORS))
				processors.add(AfterOperationProcessorBase.create(subContext));

		if (CreateContextParser.containsKey(context, KEY_PROCESSORS_2ND))
			for (JSONObject subContext : CreateContextParser.getJSONArray(context, KEY_PROCESSORS_2ND))
				processors_2nd.add(AfterOperationProcessorBase_2nd.create(subContext));
	}

	/**
	 * 处理附加操作
	 */
	public void process(ControllerManager controller, Step prevStep)
	{
		for (AfterOperationProcessorBase processor : processors)
		{
			// 处理指定类型的附加操作
			if (processor.getPlayType() != prevStep.getPlayType())
				continue;

			processResult(controller, processor.process(controller, prevStep));
		}

		for (AfterOperationProcessorBase_2nd processor : processors_2nd)
		{
			// 处理指定类型的附加操作
			if (processor.containsPlayType(prevStep.getPlayType()))
				continue;

			processResult(controller, processor.process(controller, prevStep));
		}
	}

	private void processResult(ControllerManager controller, List<Step> result)
	{
		for (Step afterStep : result)
		{
			// 执行需要附加的操作
			controller.getBattleBean().addCardHeap(afterStep);
			controller.addDisplayOperations(afterStep);
		}
	}
}
