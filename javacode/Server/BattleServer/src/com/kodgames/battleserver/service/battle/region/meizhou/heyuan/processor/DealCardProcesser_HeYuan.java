package com.kodgames.battleserver.service.battle.region.meizhou.heyuan.processor;

import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.processer.Processer;

public class DealCardProcesser_HeYuan extends Processer
{
	@Override
	public void start()
	{
		// 初始玩家手牌
		initPlayerCards();

		// 进入下一个步骤
		finish();
	}

	// 给玩家设置手牌
	private void initPlayerCards()
	{
		ArrayList<Step> displays = new ArrayList<>();
		battleHelper.getBattleFinishChecker().check(context);

		int roleId = context.getZhuang();
		for (int i = 0; i < context.getPlayerIds().size(); i++)
		{
			PlayerInfo playerInfo = context.getPlayers().get(roleId);
			boolean isBanker = roleId == context.getZhuang();
			int needCount = context.getCardPool().getPlayCardCount() + (isBanker ? 1 : 0);
			byte dealCard = 0;
			// 花牌作为奖励牌型,需要多发等数量的普通牌
			while (needCount > 0)
			{
				byte[] cards = initCard(needCount);
				needCount = 0;
				for (byte card : cards)
				{
					dealCard = card;
					playerInfo.getCards().getHandCards().add(card);
				}
			}

			// 庄家多一张牌
			if (isBanker)
			{
				Step deal = new Step(roleId, PlayType.OPERATE_DEAL, dealCard);
				context.saveRecord(deal.copy());
				displays.add(deal);
			}

			// 发送给玩家手牌信息
			displays.add(new Step(roleId, PlayType.OPERATE_DEAL_FIRST));

			// next.
			roleId = context.getNextRoleId(roleId);
		}

		controller.addDisplayOperations(displays.toArray(new Step[displays.size()]));
		controller.sendDisplayOperations();
	}

	/**
	 * 从牌池中获取指定数量张牌
	 */
	private byte[] initCard(int count)
	{
		byte[] cards = new byte[count];
		List<Byte> poolCards = context.getCardPool().getCards();
		for (int i = 0; i < count; i++)
			cards[i] = poolCards.remove(0);
		return cards;
	}
}
