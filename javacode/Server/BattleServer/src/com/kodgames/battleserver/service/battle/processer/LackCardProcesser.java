package com.kodgames.battleserver.service.battle.processer;

import java.util.concurrent.ConcurrentHashMap;

import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;

// 定缺
public class LackCardProcesser extends Processer
{
	private ConcurrentHashMap<Integer, Byte> lacks = new ConcurrentHashMap<>();

	@Override
	public void start()
	{
		notifyLackStart();
	}

	@Override
	public void rejoin(int roleId)
	{
		// 复牌
		controller.addDisplayOperations(new Step(roleId, PlayType.OPERATE_DEAL_FIRST));
		controller.sendDisplayOperations(true);

		// 发送定缺
		Step Step = new Step(roleId, PlayType.OPERATE_LACK_START);
		controller.addDisplayOperations(Step);
		if (lacks.containsKey(roleId))
		{
			Step = new Step(roleId, PlayType.OPERATE_LACK, lacks.get(roleId));
			controller.addDisplayOperations(Step);
		}
		lacks.forEach((id, sel) -> controller.addDisplayOperations(new Step(id, PlayType.OPERATE_LACK_SELECT)));
		controller.sendDisplayOperations(true);
	}

	@Override
	public boolean processStep(int roleId, int playType, byte[] cards)
	{
		if (lacks.containsKey(roleId) || playType != PlayType.OPERATE_LACK || cards == null || cards.length != 1)
			return false;

		// 定缺牌型是否有效
		byte lackCard = cards[0];
		CardType cardType = CardType.getCardType(lackCard);
		if (cardType != CardType.WAN && cardType != CardType.TIAO && cardType != CardType.TONG)
			return false;

		lacks.put(roleId, lackCard);

		// 处理定缺
		if (lacks.size() == context.getPlayerIds().size())
		{
			// 4个玩家选择完成之后 再通知当前玩家自己的选择
			controller.addDisplayOperations(new Step(roleId, playType, lackCard));

			processLackCard();
			finish();
		}
		else
		{
			// 只通知当前玩家自己的选择
			controller.addDisplayOperations(new Step(roleId, playType, lackCard));

			// 通知其他玩家自己选择完成
			controller.addDisplayOperations(new Step(roleId, PlayType.OPERATE_LACK_SELECT));

			controller.sendDisplayOperations();
		}

		return true;
	}

	private void notifyLackStart()
	{
		context.getPlayers().keySet().forEach(roleId -> controller.addDisplayOperations(new Step(roleId, PlayType.OPERATE_LACK_START)));
		controller.sendDisplayOperations();
	}

	private void processLackCard()
	{
		for (PlayerInfo player : context.getPlayers().values())
		{
			int roleId = player.getRoleId();
			controller.addDisplayOperations(new Step(roleId, PlayType.OPERATE_LACK_FINISH, lacks.get(roleId)));

			// 保存定缺信息
			context.addCardHeap(roleId, PlayType.OPERATE_LACK, lacks.get(roleId));
		}
		controller.sendDisplayOperations();
	}
}
