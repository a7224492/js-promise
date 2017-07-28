package com.kodgames.battleserver.service.battle.core.hu.filter;

import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.creator.ICreateContextHandler;
import com.kodgames.battleserver.service.battle.core.hu.data.HuCardGroup;

import net.sf.json.JSONObject;

/**
 * 牌型检测出一组胡牌牌型后，检测这种牌型是否可以胡
 *
 */
public abstract class CardGroupsFilter implements ICreateContextHandler
{
	protected BattleBean context;
	protected int roleId;

	public static CardGroupsFilter create(JSONObject context)
		throws Exception
	{
		CardGroupsFilter instance = CreateContextHelper.instantiateClass(context, CardGroupsFilter.class);
		instance.createFromContext(context);
		return instance;
	}

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
	}

	public void setContext(BattleBean context)
	{
		this.context = context;
	}

	public void setRoleId(int roleId)
	{
		this.roleId = roleId;
	}

	public void clearData()
	{
		this.context = null;
		this.roleId = 0;
	}

	public abstract boolean filter(List<HuCardGroup> cardGroups);
}
