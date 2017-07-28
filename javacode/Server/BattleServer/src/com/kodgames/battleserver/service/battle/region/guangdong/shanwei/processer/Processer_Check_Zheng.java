package com.kodgames.battleserver.service.battle.region.guangdong.shanwei.processer;

import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.processer.Processer;

/**
 * 潮汕的汕尾麻将检测是否需要显示角标，会服务器先传过去，然后客户端摸到这张牌的时候判断是否加角标
 */
public class Processer_Check_Zheng extends Processer
{
	@Override
	public void start()
	{
		// 检查并发送什么为需要加角标的牌
		check();

		finish();
	}

	/**
	 * 检测需要显示角标的牌，
	 */
	private void check()
	{
		// 循环玩家，判断玩家的需要显示正的牌
		for (PlayerInfo player : context.getPlayers().values())
		{
			// 存储玩家的正牌
			List<Byte> playerZhengCards = new ArrayList<>();
			// 获取玩家的正牌，player.getPosition()是按1,2,3,4来排序的，所以需要减去1
			playerZhengCards.add((byte)(CardType.ZI.Value() + ((player.getPosition() - 1))));
			playerZhengCards.add((byte)(CardType.HUA.Value() + ((player.getPosition() - 1))));
			playerZhengCards.add((byte)(CardType.HUA.Value() + (player.getPosition() + 4 - 1)));
			// 构造step
			Step step = new Step(player.getRoleId(), PlayType.DISPLAY_ZHENG_CARD, playerZhengCards);
			// 添加到玩家的cardHeap中，复牌用
			player.getCards().getCardHeap().add(step);
			// 添加到发送列表中
			controller.addDisplayOperations(step);
		}
		
		// 发送消息
		controller.sendDisplayOperations();
	}
}
