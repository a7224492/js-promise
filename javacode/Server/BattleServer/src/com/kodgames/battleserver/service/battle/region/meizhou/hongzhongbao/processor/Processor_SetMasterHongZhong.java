package com.kodgames.battleserver.service.battle.region.meizhou.hongzhongbao.processor;

import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.processer.Processer;

public class Processor_SetMasterHongZhong extends Processer
{
	@Override
	public void start()
	{
		setHongZhong();

		finish();
	}

	private void setHongZhong()
	{
		for (PlayerInfo player : context.getPlayers().values())
		{
			// 设置红中当成万能牌
			context.setPlayerMasterCard(player.getRoleId(), CardType.JIAN.Value());

			// 让自己的手牌中的红中鬼牌都展现出来
			Step step = new Step(player.getRoleId(), PlayType.DISPLAY_MASTER_HONG_ZHONG, CardType.JIAN.Value());
			
			// 添加复牌
			player.getCards().getCardHeap().add(step);
			controller.addDisplayOperations(step);
			controller.sendDisplayOperations();
		}
	}
}
