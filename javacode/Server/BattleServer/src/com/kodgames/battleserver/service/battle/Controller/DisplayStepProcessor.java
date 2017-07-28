package com.kodgames.battleserver.service.battle.Controller;

import java.util.List;
import java.util.Map;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.core.BaseController;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.creator.ICreateContextHandler;
import com.kodgames.message.proto.battle.BattleProtoBuf.PlayStepPROTO;

import net.sf.json.JSONObject;

public abstract class DisplayStepProcessor extends BaseController implements ICreateContextHandler
{
	protected ControllerManager controller = null;

	public void setController(ControllerManager controller)
	{
		this.controller = controller;
	}
	
	public static DisplayStepProcessor create(JSONObject context)
		throws Exception
	{
		DisplayStepProcessor instance = CreateContextHelper.instantiateClass(context, DisplayStepProcessor.class);
		instance.createFromContext(context);
		return instance;
	}
	
	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
	}

	public abstract Map<Integer, List<PlayStepPROTO>> process(BattleBean context, Step displayStep, boolean rejoin);
}
