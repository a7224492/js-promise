package com.kodgames.battleserver.service.battle.region.neimeng.wulanchabu.processor;

import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.processer.BattleProcesser;
import com.kodgames.battleserver.service.battle.region.neimeng.common.Rules_NeiMeng;

public class BattleProcessor_WuLanChaBu extends BattleProcesser
{

	@Override
	protected void processStep(List<Step> result)
	{
		Step preStep = context.getLastRecordStep(0);
		List<Step> huLists = new ArrayList<>();
		Step passStep = null;

		for (Step step : result)
		{
			switch (step.getPlayType())
			{
				case PlayType.OPERATE_PASS:
					// 队列中的pass操作只有最后一个需要处理，之前的操作已经执行过
					passStep = step;
					break;

				case PlayType.OPERATE_PLAY_A_CARD:
				case PlayType.OPERATE_PLAY_A_CARD_HIDE:
					doPlayACardOperation(step);
					break;

				case PlayType.OPERATE_AN_GANG:
				case PlayType.OPERATE_GANG_A_CARD:
				case PlayType.OPERATE_BU_GANG_A_CARD:
					doGangCardOperation(preStep, step);
					break;

				case PlayType.OPERATE_PENG_A_CARD:
					doPengCardOperation(preStep, step);
					break;

				case PlayType.OPERATE_CHI_A_CARD:
					doChiCardOperation(preStep, step);
					break;

				case PlayType.OPERATE_TING:
					doTingOperation(preStep, step);
					break;

				case PlayType.OPERATE_TING_CARD:
					doTingCardOperation(preStep, step);
					break;

				case PlayType.OPERATE_HU:
					// 胡牌会删除手牌, 一炮多响的机制需要最后在处理胡牌
					huLists.add(step);
					break;

				case PlayType.OPERATE_CANCEL:
				case PlayType.OPERATE_WAIT:
					// 通知玩家
					controller.addDisplayOperations(step);
					controller.sendDisplayOperations(step.getRoleId(), false);
					break;
			}
		}

		if (huLists.size() > 0)
			doHuOperation(preStep, huLists);
		else if (passStep != null)
			doPassOperation(preStep, passStep);
	}

	@Override
	protected void generatePlayACard(int roleId)
	{
		// 打牌操作
		if (context.hasCardHeap(roleId, PlayType.DISPLAY_AUTO_PLAY_LAST_DEALED_CARD))
		{
			Step lastRecord = context.getLastRecordStep(0);
			Macro.AssetTrue(lastRecord.getPlayType() != PlayType.OPERATE_DEAL);
			byte dealCard = lastRecord.getCards().get(0);
			// 需要自动打牌, 添加出牌, 并自动打牌
			autoPlayACard(roleId, dealCard);
		}
		else
		{
			// 不需要, 添加出牌等待操作
			List<Integer> rules = controller.getBattleBean().getGameRules();
			int playType = rules.contains(Rules_NeiMeng.HEI_MO) ? PlayType.OPERATE_CAN_PLAY_A_CARD_HIDE : PlayType.OPERATE_CAN_PLAY_A_CARD;
			controller.addWaitingOperations(new Step(roleId, playType, playerCardProcessor.generatePlayCard(context, roleId)));
		}

		// 发送消息
		controller.sendDisplayOperations();
	}

}
