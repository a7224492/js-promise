package com.kodgames.battleserver.service.battle.core.operation;

import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.service.battle.Controller.ControllerManager;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.creator.ICreateContextHandler;

import net.sf.json.JSONObject;

/**
 * 多个playType的扩展
 */
public abstract class AfterOperationProcessorBase_2nd implements ICreateContextHandler
{
	public static final String KEY_PLAY_TYPES = "playTypes";
	private List<Integer> playTypes = new ArrayList<Integer>();

	public static AfterOperationProcessorBase_2nd create(JSONObject context)
		throws Exception
	{
		AfterOperationProcessorBase_2nd instance = CreateContextHelper.instantiateClass(context, AfterOperationProcessorBase_2nd.class);
		instance.createFromContext(context);
		return instance;
	}

	public boolean containsPlayType(int playType)
	{
		return playTypes.contains(playType);
	}

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		if (context.containsKey(KEY_PLAY_TYPES))
		{
			playTypes.clear();
			for (int index = 0; index < context.getJSONArray(KEY_PLAY_TYPES).size(); index++)
			{
				playTypes.add(CreateContextParser.getIntInArray(context, KEY_PLAY_TYPES, index));
			}
		}
	}

	/**
	 * 处理操作
	 * 
	 * @param controller 控制类, 可用于获取战斗相关上下文和执行具体操作
	 * @param prevStep 对应已经执行的主操作
	 * @return 返回需要执行的操作列表
	 */
	public abstract List<Step> process(ControllerManager controller, Step prevStep);
}
