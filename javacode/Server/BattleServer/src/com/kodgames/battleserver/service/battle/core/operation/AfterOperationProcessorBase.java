package com.kodgames.battleserver.service.battle.core.operation;

import java.util.List;

import com.kodgames.battleserver.service.battle.Controller.ControllerManager;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.creator.ICreateContextHandler;

import net.sf.json.JSONObject;

/**
 * 用于一个主要操作执行之后, 添加附属的操作
 * 
 * 比如吃牌之后增加听牌操作<br>
 * 主要操作包括:<br>
 * OPERATE_PLAY_A_CARD<br>
 * OPERATE_CHI_A_CARD<br>
 * OPERATE_PENG_A_CARD<br>
 * OPERATE_GANG_A_CARD<br>
 * OPERATE_BU_GANG_A_CARD<br>
 * OPERATE_AN_GANG<br>
 * OPERATE_HU
 */
public abstract class AfterOperationProcessorBase implements ICreateContextHandler
{
	public static final String KEY_PLAY_TYPE = "playType";

	/**
	 * 添加到那个操作执行之后
	 */
	private int playType;

	/**
	 * 创建一个继承于AfterOperationProcessorBase的类实例
	 */
	public static AfterOperationProcessorBase create(JSONObject context)
		throws Exception
	{
		AfterOperationProcessorBase instance = CreateContextHelper.instantiateClass(context, AfterOperationProcessorBase.class);
		instance.createFromContext(context);
		return instance;
	}

	/**
	 * 获取这个操作应该附加于那个主要操作之后
	 */
	public int getPlayType()
	{
		return this.playType;
	}

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		playType = CreateContextParser.getInt(context, KEY_PLAY_TYPE);
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
