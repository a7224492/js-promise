package com.kodgames.battleserver.service.battle.region.meizhou.heyuan.processor;

import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.CardPool;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.BattleState;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.processer.BattleProcesser;

public class BattleProcesser_HeYuan extends BattleProcesser
{
	@Override
	protected void processStep(List<Step> result)
	{
		Step preStep = context.getLastRecordStep(0);
		List<Step> huLists = new ArrayList<>();
		Step passStep = null;

		for (Step step : result)
		{
			// 设置操作的默认来源玩家ID
			step.setSourceRoleId(preStep.getRoleId());

			switch (step.getPlayType())
			{
				case PlayType.OPERATE_PASS:
					// 队列中的pass操作只有最后一个需要处理，之前的操作已经执行过
					passStep = step;
					break;

				case PlayType.OPERATE_PLAY_A_CARD:
					doPlayACardOperation(step);
					break;

				case PlayType.OPERATE_FAN_A_CARD:
					doPlayAFanCardOperation(step);
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

	/**
	 * 执行OPERATE_PLAY_A_CARD操作
	 */
	protected void doPlayAFanCardOperation(Step step)
	{
		int roleId = step.getRoleId();
		byte card = step.getCards().get(0);

		// 删除手牌
		context.removeHandCard(roleId, card);

		// 添加翻牌
		context.addOutCard(roleId, card);

		// 把翻牌存到cardheap中
		context.addCardHeap(step);

		// 通知玩家出牌
		controller.addDisplayOperations(step);

		// 增加翻牌操作
		context.saveRecord(step.copy());

		// 翻牌操作最后的附加处理
		afterOperationProcessor.process(controller, step);

		controller.sendDisplayOperations();

		// 附加操作影响牌局结束，进入结算
		if (context.getBattleState() != BattleState.INBATTLE)
		{
			// 结束牌局
			finish();
			return;
		}

		// 新的操作，检测出牌结果对其他玩家的影响
		if (operationChecker.check(roleId, card, false))
			controller.sendDisplayOperations();
		else
			// 没有影响，发牌给下一家
			dealCard(context.getNextRoleId(roleId));
	}

	/**
	 * 执行OPERATE_CHI_A_CARD操作
	 */
	protected void doChiCardOperation(Step preStep, Step step)
	{
		Macro.AssetFalse(step.getPlayType() == PlayType.OPERATE_CHI_A_CARD);
		Macro.AssetFalse(step.getCards().size() == 3);

		int roleId = step.getRoleId();
		int sourceId = preStep.getRoleId();
		byte card = step.getCards().get(0);

		// 删除手牌, 由于对应的等待操作已经验证了合法性, 可以直接删除
		context.removeHandCard(roleId, step.getCards().get(1));
		context.removeHandCard(roleId, step.getCards().get(2));

		// 删除出牌
		context.removeOutCard(sourceId, card);

		// 增加吃操作
		context.addCardHeap(step);
		context.saveRecord(step.copy());

		// 客户端显示流程需要先删除, 再吃.
		// 通知玩家被吃操作,
		controller.addDisplayOperations(new Step(sourceId, PlayType.DISPLAY_BE_CHI, card));

		// 通知玩家吃牌操作
		controller.addDisplayOperations(step);

		// 吃操作最后的附加处理
		afterOperationProcessor.process(controller, step);

		controller.sendDisplayOperations();

		// 新的操作，检测吃牌结果对其他玩家的影响
		if (operationChecker.check(roleId, card, true))
			controller.sendDisplayOperations();
		else
			// 没有影响，通知出牌
			generatePlayACard(roleId);
	}

	/**
	 * 执行OPERATE_PENG_A_CARD操作
	 */
	protected void doPengCardOperation(Step preStep, Step step)
	{
		Macro.AssetFalse(step.getPlayType() == PlayType.OPERATE_PENG_A_CARD);

		int roleId = step.getRoleId();
		int sourceId = preStep.getRoleId();
		byte card = step.getCards().get(0);

		// 删除手牌
		context.removeHandCard(roleId, card, 2);

		// 删除出牌
		context.removeOutCard(sourceId, card);

		// 补充碰牌： Step中原有1张牌，加2张，共3张
		step.addCard(card, 2);

		// 增加碰操作
		context.addCardHeap(step);
		context.saveRecord(step.copy());

		// 通知玩家被碰操作
		controller.addDisplayOperations(new Step(sourceId, PlayType.DISPLAY_BE_PENG, card));

		// 通知玩家碰牌操作
		controller.addDisplayOperations(step);

		// 碰操作最后的附加处理
		afterOperationProcessor.process(controller, step);

		controller.sendDisplayOperations();

		// 检测碰牌结果对其他玩家的影响。
		if (operationChecker.check(roleId, card, true))
			controller.sendDisplayOperations();
		else
			// 新的操作 通知出牌
			generatePlayACard(roleId);
	}

	/**
	 * 执行各种杠操作
	 */
	protected void doGangCardOperation(Step preStep, Step step)
	{
		int roleId = step.getRoleId();
		int sourceId = preStep.getRoleId();
		byte card = step.getCards().get(0);

		// 计算分数结果
		PlayerInfo playerInfo = context.getPlayers().get(roleId);
		ScoreData scoreData = gangScoreProcessor.process(context, playerInfo, step.getPlayType(), card);
		// 如果杠牌分数有效，添加scoreData到玩家身上
		if (scoreData.getPoints().size() > 0)
			playerInfo.getCards().getScoreDatas().add(scoreData);

		// 删除
		switch (step.getPlayType())
		{
			case PlayType.OPERATE_GANG_A_CARD:
				context.removeHandCard(roleId, card, 3);
				context.removeOutCard(sourceId, card);
				// 添加被杠
				controller.addDisplayOperations(new Step(sourceId, PlayType.DISPLAY_BE_GANG, card));
				break;

			case PlayType.OPERATE_AN_GANG:
				context.removeHandCard(roleId, card, 4);
				break;

			case PlayType.OPERATE_BU_GANG_A_CARD:
				// 查找该杠对应的碰，记录被碰的玩家的ID
				List<Step> cardHeap = context.getPlayerById(roleId).getCards().getCardHeap();
				for (Step opStep : cardHeap)
				{
					if (opStep.getPlayType() == PlayType.OPERATE_PENG_A_CARD && opStep.getCards().get(0) == card)
					{
						step.setSourceRoleId(opStep.getSourceRoleId());
						break;
					}
				}
				Macro.AssetTrue(step.getSourceRoleId() == preStep.getRoleId(), String.format("BuGang : no peng -> roleId=%d, card=%d", roleId, card));

				context.removeHandCard(roleId, card, 1);
				context.removeCardHeap(roleId, PlayType.OPERATE_PENG_A_CARD, card);
				break;
		}

		// 补充杠的牌：Step中原来1张牌，加3张，共4张
		step.addCard(card, 3);

		// 增加杠操作
		context.addCardHeap(step);
		context.saveRecord(step.copy());

		// 设置Step索引
		scoreData.setStepIndex(context.getRecords().size() - 1);

		// 通知玩家杠牌结果
		controller.addDisplayOperations(step);

		// 杠操作最后的附加处理
		afterOperationProcessor.process(controller, step);

		controller.sendDisplayOperations();

		// 检测杠牌结果对其他玩家的影响。
		// 如果可以抢杠，通知玩家
		if (operationChecker.check(roleId, card, false))
			controller.sendDisplayOperations();
		else
			dealCard(roleId); // 新的操作 发牌
	}

	@Override
	protected void dealCard(int roleId)
	{
		for (int i = 0; i < context.getPlayerIds().size(); i++)
		{
			if (playerFinishFilter.isFinish(context, roleId))
			{
				roleId = context.getNextRoleId(roleId);
				continue;
			}

			List<Step> result = new ArrayList<>();
			PlayerInfo playerInfo = context.getPlayers().get(roleId);
			CardPool cardPool = context.getCardPool();
			byte dealCard = Byte.MIN_VALUE;

			while (true)
			{
				// 检测牌局状态
				checkBattleFinishState();
				int battleState = context.getBattleState();

				// 结束牌局，进入结算
				if (battleState != BattleState.INBATTLE)
				{
					// 发送之前处理的操作
					controller.addDisplayOperations(result.toArray(new Step[result.size()]));
					controller.sendDisplayOperations();

					// 结束牌局
					finish();
					return;
				}

				byte card = cardPool.getCards().remove(0);
				{
					// 摸到合法的牌, 执行摸牌操作
					Step deal = new Step(roleId, PlayType.OPERATE_DEAL, card);
					context.saveRecord(deal.copy());
					result.add(deal);
					playerInfo.getCards().getHandCards().add(card);
					dealCard = card;
					break;
				}
			}

			// 摸牌操作最后的附加处理
			result.forEach(step -> {
				if (step.getPlayType() == PlayType.OPERATE_DEAL)
					afterOperationProcessor.process(controller, step);
			});

			// 发送deal显示操作
			controller.addDisplayOperations(result.toArray(new Step[result.size()]));
			controller.sendDisplayOperations();

			// 检测吃碰杠等特殊操作
			if (operationChecker.check(roleId, dealCard, true))
				controller.sendDisplayOperations();
			else
				generatePlayACard(roleId);

			break;
		}
	}

}
