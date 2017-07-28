package com.kodgames.battleserver.service.battle.core.playcard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.creator.ICreateContextHandler;
import com.kodgames.battleserver.service.battle.core.playcard.filter.PlayCardFilter;

import net.sf.json.JSONObject;

public class PlayCardProcessor implements ICreateContextHandler
{
	public static final String KEY_scoreResultFilters = "scoreResultFilters";
	private List<PlayCardFilter> playCardFilters = new ArrayList<>();

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		// 解析参数
		if (context.containsKey(KEY_scoreResultFilters))
		{
			for (JSONObject subContext : CreateContextParser.getJSONArray(context, KEY_scoreResultFilters))
				playCardFilters.add(PlayCardFilter.create(subContext));
		}

	}

	/**
	 * 获取Play_A_CARD操作可以打出的牌
	 * 
	 * 即使所有的牌都可以打出, 也要将可以打的牌返回, 客户端完全用返回的牌来操作
	 */
	public List<Byte> generatePlayCard(BattleBean context, int roleId)
	{
		List<Byte> cards = context.getPlayers().get(roleId).getCards().getHandCards().stream().collect(Collectors.toList());
		for (PlayCardFilter filter : playCardFilters)
			cards = filter.filterCard(context, roleId, cards);

		return cards;
	}
}