package com.kodgames.battleserver.service.battle.processer;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.kodgames.battleserver.service.battle.common.xbean.CardInfo;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;

// 换三张
public class ChangeCardProcesser extends Processer
{
	private ConcurrentHashMap<Integer, List<Byte>> exchanges = new ConcurrentHashMap<>();

	@Override
	public void start()
	{
		notifyChangeStart();
	}

	@Override
	public void rejoin(int roleId)
	{
		// 复牌
		controller.addDisplayOperations(new Step(roleId, PlayType.OPERATE_DEAL_FIRST));
		controller.sendDisplayOperations(true);

		// 发送换三张
		Step Step = new Step(roleId, PlayType.OPERATE_CHANGECARD_START);
		controller.addDisplayOperations(Step);
		if (exchanges.containsKey(roleId))
		{
			Step = new Step(roleId, PlayType.OPERATE_CHANGECARD, exchanges.get(roleId));
			controller.addDisplayOperations(Step);
		}
		exchanges.forEach((id, sel) -> controller.addDisplayOperations(new Step(id, PlayType.OPERATE_CHANGECARD_SELECT)));
		controller.sendDisplayOperations(true);
	}

	@Override
	public boolean processStep(int roleId, int playType, byte[] cards)
	{
		if (exchanges.containsKey(roleId) || playType != PlayType.OPERATE_CHANGECARD || cards == null || cards.length != 3)
			return false;

		CardInfo cardInfo = context.getPlayers().get(roleId).getCards();
		CardType cardType = CardType.getCardType(cards[0]);
		for (byte card : cards)
		{
			// 手牌中是否有这张牌
			if (!cardInfo.getHandCards().contains(card))
				return false;

			// 交换牌是否同一花色
			if (CardType.getCardType(card) != cardType)
				return false;
		}

		exchanges.put(roleId, convert2ByteList(cards));
	
	
		// 处理换三张
		if (exchanges.size() == context.getPlayerIds().size())
		{
			// 4个玩家选择完成之后 再通知当前玩家自己的选择
			controller.addDisplayOperations(new Step(roleId, playType, exchanges.get(roleId)));
	
			processChangeCard();
			finish();
		}
		else
		{
			// 先通知当前玩家自己的选择
			controller.addDisplayOperations(new Step(roleId, playType, exchanges.get(roleId)));
			// 通知其他玩家自己选择完成
			controller.addDisplayOperations(new Step(roleId, PlayType.OPERATE_CHANGECARD_SELECT));

			controller.sendDisplayOperations();
		}

		return true;
	}

	private void notifyChangeStart()
	{
		context.getPlayers().keySet().forEach(roleId -> controller.addDisplayOperations(new Step(roleId, PlayType.OPERATE_CHANGECARD_START)));
		controller.sendDisplayOperations();
	}

	private void processChangeCard()
	{
		ConcurrentHashMap<Integer, List<Byte>> afterExchanges = new ConcurrentHashMap<>();
		int bankId = context.getZhuang();
		// 随机交换规则
		Random random = new Random(System.currentTimeMillis());
		int changeRule = random.nextInt(3);
		for (int roleId : context.getPlayerIds())
		{
			int targetId = 0;
			switch (changeRule)
			{
				case 0:// 顺时针
					targetId = context.getPreRoleId(roleId);
					break;
				case 1:// 逆时针
					targetId = context.getNextRoleId(roleId);
					break;
				case 2:// 对家
					targetId = context.getOppositeRoleId(roleId);
					break;
			}

			afterExchanges.put(roleId, exchanges.get(targetId).stream().collect(Collectors.toList()));
		}

		for (PlayerInfo player : context.getPlayers().values())
		{
			CardInfo cardInfo = player.getCards();
			int roleId = player.getRoleId();
			// 删除交换牌
			for (byte card : exchanges.get(roleId))
				cardInfo.getHandCards().remove(Byte.valueOf(card));
			// 添加交换到的牌
			for (byte card : afterExchanges.get(roleId))
				cardInfo.getHandCards().add(card);

			// 换三张替换记录中的摸牌记录
			if (roleId == bankId)
			{
				Step dealCard = context.getRecords().get(context.getRecords().size() - 1);
				dealCard.getCards().clear();
				dealCard.getCards().add(afterExchanges.get(roleId).get(0));
			}

			controller.addDisplayOperations(new Step(roleId, PlayType.OPERATE_CHANGECARD_FINISH, afterExchanges.get(roleId)));
			controller.addDisplayOperations(new Step(roleId, PlayType.OPERATE_CHANGECARD_RULE, (byte)changeRule));
		}

		controller.sendDisplayOperations();
	}
}
