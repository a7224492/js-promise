package com.kodgames.battleserver.service.battle.region.meizhou.heyuan.processor;

import java.util.ArrayList;
import java.util.List;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.processer.Processer;

import net.sf.json.JSONObject;

public class Processor_SetMasterHuaCard extends Processer
{

	public static final String KEY_HAS_FLOWER_CARD = "hasFlowerCard";

	/**
	 * 默认的是4张花牌
	 */
	private boolean hasFlowerCard = false;

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		if (CreateContextParser.containsKey(context, KEY_HAS_FLOWER_CARD))
			hasFlowerCard = CreateContextParser.getBoolean(context, KEY_HAS_FLOWER_CARD);
	}

	@Override
	public void start()
	{
		setHuaCard();

		finish();
	}

	private void setHuaCard()
	{
		List<Byte> huaCards = new ArrayList<>();
		if (hasFlowerCard)
		{
			// 设置4张花牌
			for (byte i = CardType.HUA.Value(); i < CardType.HUA.MaxValue(); i++)
			{
				huaCards.add(i);
			}
		}
		else
		{
			// 设置8张花牌
			for (byte i = CardType.SEASON.Value(); i < CardType.SEASON.MaxValue(); i++)
			{
				huaCards.add(i);
			}
		}

		if (huaCards == null || huaCards.size() == 0)
		{
			return;
		}

		for (PlayerInfo player : context.getPlayers().values())
		{
			// 设置花牌当成万能牌
			context.setPlayerMasterCard(player.getRoleId(), huaCards);

			// 让自己的手牌中的花牌个数全部在自己的桌面前显示出来展示给大家看
			Step step = new Step(player.getRoleId(), PlayType.DISPLAY_HUA_PAI, huaCards);

			// 用来复牌万能花牌
			player.getCards().getCardHeap().add(step);
			controller.addDisplayOperations(step);
			controller.sendDisplayOperations();
		}
	}
}
