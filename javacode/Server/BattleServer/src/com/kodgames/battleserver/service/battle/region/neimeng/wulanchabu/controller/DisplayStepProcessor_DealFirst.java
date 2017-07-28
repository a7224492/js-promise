package com.kodgames.battleserver.service.battle.region.neimeng.wulanchabu.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.protobuf.ByteString;
import com.kodgames.battleserver.service.battle.Controller.DisplayStepProcessor;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.MahjongHelper;
import com.kodgames.message.proto.battle.BattleProtoBuf.BCBattlePlayerInfoSYN;
import com.kodgames.message.proto.battle.BattleProtoBuf.BattlePlayerInfoPROTO;
import com.kodgames.message.proto.battle.BattleProtoBuf.PlayStepPROTO;

public class DisplayStepProcessor_DealFirst extends DisplayStepProcessor
{
	@Override
	public Map<Integer, List<PlayStepPROTO>> process(BattleBean context, Step displayStep, boolean rejoin)
	{
		Map<Integer, List<PlayStepPROTO>> roleProtoMap = new HashMap<>();
		context.getPlayerIds().forEach(roleId -> roleProtoMap.put(roleId, new ArrayList<>()));

		// 对于发牌和复牌操作， 单独构造消息发送
		BCBattlePlayerInfoSYN.Builder sendBuilder = BCBattlePlayerInfoSYN.newBuilder();
		sendBuilder.setProtocolSeq(controller.getProtocolSeq(context.getRoomId(), displayStep.getRoleId()));
		sendBuilder.setIsRecover(rejoin);
		sendBuilder.setEnableMutilHu(battleHelper.getBattleFinishChecker().enableMutilHu());
		sendBuilder.setTotalCardsNum(context.getCardPool().getPlayCardCount());

		for (PlayerInfo player : context.getPlayers().values())
		{
			BattlePlayerInfoPROTO.Builder infoBuilder = BattlePlayerInfoPROTO.newBuilder();
			infoBuilder.setRoleId(player.getRoleId()).setStatus(player.getStatus()).setPointInGame(player.getPointInGame()).setTotalPoint(player.getTotalPoint());

			// 设置手牌
			ByteString cards;
			List<Byte> handCards = new ArrayList<>();
			player.getCards().getHandCards().forEach(card -> {
				// 手牌只对自己可见
				if (player.getRoleId() == displayStep.getRoleId())
					handCards.add(card);
				else
					handCards.add((byte)-1);
			});

			// 如果上一个操作是发牌，这张牌已经发到手牌中，需要从
			// 为了实现客户端的手牌效果：最后一张是最后摸到的牌
			Step dealStep = context.getPlayerLastRecord(player.getRoleId());
			if (dealStep != null && dealStep.getPlayType() == PlayType.OPERATE_DEAL)
			{
				// 删除发牌
				byte dealCard = (player.getRoleId() == displayStep.getRoleId()) ? dealStep.getCards().get(0) : -1;
				handCards.remove(Byte.valueOf(dealCard));

				// 断线重连，添加摸牌消息
				if (rejoin)
				{
					PlayStepPROTO.Builder stepBuilder = MahjongHelper.step2Proto(dealStep).toBuilder();
					stepBuilder.setCards(convert2ByteString(dealCard));
					roleProtoMap.get(displayStep.getRoleId()).add(stepBuilder.build());
				}
			}
			cards = convert2ByteString(handCards);
			infoBuilder.setHandCards(cards);

			// 打出的牌对任何人都不可见
			List<Byte> outCards = new ArrayList<>();
			player.getCards().getOutCards().forEach(card -> outCards.add((byte)-1));
			cards = convert2ByteString(outCards);
			infoBuilder.setOutCards(cards);

			// 设置操作牌
			for (Step step : player.getCards().getCardHeap())
			{
				PlayStepPROTO.Builder stepBuilder = MahjongHelper.step2Proto(step).toBuilder();

				// 操作牌只对自己可见
				if (player.getRoleId() != displayStep.getRoleId() && CardType.getCardType(step.getCards().get(0)) != CardType.HUA)
					stepBuilder.setCards(convert2ByteString((byte)-1));
				infoBuilder.addOperateCards(stepBuilder.build());
			}
			sendBuilder.addPlayers(infoBuilder.build());
		}

		controller.sendMessage(displayStep.getRoleId(), sendBuilder.build());

		return roleProtoMap;
	}
}
