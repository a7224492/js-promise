package com.kodgames.battleserver.service.battle.core.playcard.filter;

import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.creator.ICreateContextHandler;

import net.sf.json.JSONObject;

public abstract class PlayCardFilter implements ICreateContextHandler
{
	public static PlayCardFilter create(JSONObject context)
		throws Exception
	{
		// 反射构造实例
		PlayCardFilter instance = CreateContextHelper.instantiateClass(context, PlayCardFilter.class);
		instance.createFromContext(context);
		return instance;
	}

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
	}

	public abstract List<Byte> filterCard(BattleBean context, int roleId, List<Byte> cards);
}