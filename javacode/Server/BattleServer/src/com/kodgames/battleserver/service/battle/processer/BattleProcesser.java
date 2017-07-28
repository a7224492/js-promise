package com.kodgames.battleserver.service.battle.processer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.CardPool;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.common.xbean.Step4Display;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.BattleState;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.finish.PlayerFinishChecker;
import com.kodgames.battleserver.service.battle.core.operation.AfterOperationProcessor;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker;
import com.kodgames.battleserver.service.battle.core.pass.PassRecorder;
import com.kodgames.battleserver.service.battle.core.playcard.PlayCardProcessor;
import com.kodgames.battleserver.service.battle.core.score.gang.GangScoreProcessor;

import net.sf.json.JSONObject;

public class BattleProcesser extends Processer
{
	protected OperationChecker operationChecker;
	protected AfterOperationProcessor afterOperationProcessor = new AfterOperationProcessor();
	protected PassRecorder passRecorder;
	protected PlayCardProcessor playerCardProcessor;
	protected GangScoreProcessor gangScoreProcessor;
	protected PlayerFinishChecker playerFinishFilter;

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		playerFinishFilter = new PlayerFinishChecker();
		playerCardProcessor = new PlayCardProcessor();
		operationChecker = new OperationChecker(playerFinishFilter);
		passRecorder = new PassRecorder();
		gangScoreProcessor = new GangScoreProcessor();

		operationChecker.createFromContext(context.getJSONObject(operationChecker.getClass().getSimpleName()));
		afterOperationProcessor.createFromContext(context.getJSONObject(afterOperationProcessor.getClass().getSimpleName()));
		playerCardProcessor.createFromContext(context.getJSONObject(playerCardProcessor.getClass().getSimpleName()));
		gangScoreProcessor.createFromContext(context.getJSONObject(gangScoreProcessor.getClass().getSimpleName()));
		playerFinishFilter.createFromContext(context.getJSONObject(playerFinishFilter.getClass().getSimpleName()));
	}

	/**
	 * 开始牌局
	 */
	@Override
	public void start()
	{
		// 战斗开始，检测庄家能进行的特殊操作(暗杠,自摸胡等)，否则通知庄家出牌
		int roleId = context.getZhuang();

		// 获取庄家第一次摸到的牌
		List<Step> records = context.getRecords();
		Macro.AssetFalse(records.size() == 1);
		Step firstStep = records.get(0);
		Macro.AssetFalse(firstStep.getPlayType() == PlayType.OPERATE_DEAL && firstStep.getCards().size() == 1);
		byte firstCard = firstStep.getCards().get(0);
		
		// 检测庄家的其他特殊操作，胡，听，杠等
		if (operationChecker.check(roleId, firstCard, true))
			controller.sendDisplayOperations();
		else
			generatePlayACard(roleId); // 尝试打牌
	}

	/**
	 * 玩家中途重新加入牌局
	 */
	@Override
	public void rejoin(int roleId)
	{
		controller.addDisplayOperations(new Step(roleId, PlayType.OPERATE_DEAL_FIRST));
		controller.sendDisplayOperations(true);
	}

	/**
	 * 获取当前Step
	 */
	@Override
	public boolean processStep(int roleId, int playType, byte[] cards)
	{
		// 先获取玩家可以进行操作
		List<Step> canDos = controller.getWaitingOperations(roleId);

		// 这里处理的操作都应该是当前等待队列中的操作, 如果还需要等待result中还会包含等待
		List<Step> result = controller.correspondWaitingOperation(roleId, playType, cards);
		if (result == null)
			return false;

		// 为了处理漏胡, pass操作有额外处理
		if (playType == PlayType.OPERATE_PASS)
			passRecorder.recordPass(context, roleId, canDos);

		// process.
		processStep(result);
		return true;
	}

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
			controller.addWaitingOperations(new Step(roleId, PlayType.OPERATE_CAN_PLAY_A_CARD, playerCardProcessor.generatePlayCard(context, roleId)));
		}

		// 发送消息
		controller.sendDisplayOperations();
	}

	protected void checkBattleFinishState()
	{
		battleHelper.getBattleFinishChecker().check(context);
	}

	/**
	 * 执行OPERATE_PASS操作
	 */
	protected void doPassOperation(Step preStep, Step step)
	{
		// 通知玩家
		controller.addDisplayOperations(step);
		controller.sendDisplayOperations();

		// 之前操作为摸牌，pass了摸牌产生的行为，继续打牌
		if (preStep.getPlayType() == PlayType.OPERATE_DEAL)
			generatePlayACard(preStep.getRoleId());
		else if (preStep.getPlayType() == PlayType.OPERATE_BU_GANG_A_CARD)
			// 如果有抢杠胡，但是可以胡牌玩家选择过，那么给补杠玩家发牌
			dealCard(preStep.getRoleId());
		else
		{
			// 如果不是正常流程，之前的操作属于同一个玩家，那么就是这个玩家进行出牌操作
			if (preStep.getRoleId() == step.getRoleId())
				generatePlayACard(preStep.getRoleId());
			else
				// 继续后续的抓牌
				dealCard(context.getNextRoleId(preStep.getRoleId()));
		}
	}

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
				if (CardType.HUA.isBelongTo(card))
				{
					// 如果是花牌, 直接添加到exCards中, 然后继续摸牌
					Step exOp = new Step(roleId, PlayType.DISPLAY_EX_CARD, card);
					result.add(exOp);

					// 添加花牌
					context.saveRecord(exOp.copy());
					context.addCardHeap(roleId, PlayType.DISPLAY_EX_CARD, card);
					playerInfo.getCards().getExCards().add(card);
				}
				else
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

	/**
	 * 自动打牌
	 */
	protected void autoPlayACard(int roleId, byte card)
	{
		controller.addWaitingOperations(new Step(roleId, PlayType.OPERATE_CAN_AUTO_PLAY_LAST_DEALED_CARD, card));
	}

	/**
	 * 执行OPERATE_PLAY_A_CARD操作
	 */
	protected void doPlayACardOperation(Step step)
	{
		int roleId = step.getRoleId();
		byte card = step.getCards().get(0);

		// 删除手牌
		context.removeHandCard(roleId, card);

		// 添加出牌
		context.addOutCard(roleId, card);

		// 通知玩家出牌
		controller.addDisplayOperations(step);

		// 增加打牌操作
		context.saveRecord(step.copy());

		// 出牌操作最后的附加处理
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
	 * 执行OPERATE_TING操作
	 */
	protected void doTingOperation(Step preStep, Step step)
	{
		int roleId = step.getRoleId();

		// 增加明楼操作
		context.addCardHeap(roleId, step.getPlayType());
		context.saveRecord(step.copy());

		// 明楼操作最后的附加处理
		afterOperationProcessor.process(controller, step);

		controller.sendDisplayOperations();

		// 发牌
		dealCard(roleId);
	}

	protected void doTingCardOperation(Step preStep, Step step)
	{
		int roleId = step.getRoleId();
		byte card = step.getCards().get(0);

		// 删除手牌
		context.removeHandCard(roleId, card);

		// 增加听牌操作
		context.addCardHeap(roleId, step.getPlayType(), card);
		context.saveRecord(step.copy());

		// 通知玩家听牌操作
		controller.addDisplayOperations(new Step(roleId, PlayType.OPERATE_PLAY_A_CARD, card));

		// 听操作最后的附加处理
		afterOperationProcessor.process(controller, step);

		controller.sendDisplayOperations();

		// 发牌给下一家
		if (operationChecker.check(roleId, card, false))
			controller.sendDisplayOperations();
		else
			dealCard(context.getNextRoleId(roleId));

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

	/**
	 * 处理OPERATE_HU操作, 支持一炮多响
	 * 
	 * @param preStep 前一个操作
	 * @param steps 当前所有的胡操作列表
	 */
	protected void doHuOperation(Step preStep, List<Step> steps)
	{
		int sourceId = preStep.getRoleId();
		byte card = preStep.getCards().get(0);

		// 产生计算结果
		HashMap<Integer, ScoreData> scoreDataMap = new HashMap<>();
		HashMap<Integer, Step> scoreSteps = new HashMap<>();
		for (Step step : steps)
		{
			int roleId = step.getRoleId();
			boolean isSelfOp = roleId == preStep.getRoleId();
			ScoreData scoreData = battleHelper.getHuScoreProcessor().process(context, roleId, isSelfOp ? 0 : card, false);
			scoreDataMap.put(roleId, scoreData);
			scoreSteps.put(roleId, step);
			
			scoreData.setSourceCard(card);
			context.removeHandCard(roleId, (byte)0);
		}

		boolean isBeHu = false;
		for (Integer roleId : scoreSteps.keySet())
		{
			PlayerInfo playerInfo = context.getPlayers().get(roleId);
			ScoreData scoreData = scoreDataMap.get(roleId);
			Step step = scoreSteps.get(roleId);
			step.getCards().clear();
			step.getCards().add(card);

			// 依据上次出牌玩家判定删除操作
			if (roleId == sourceId)
				context.removeHandCard(roleId, card); // 删除手牌
			else
				isBeHu = true;

			// 增加胡操作
			context.addCardHeap(roleId, step.getPlayType(), card);
			context.saveRecord(step.copy());

			// 添加到计分数组
			playerInfo.getCards().getScoreDatas().add(scoreData);

			// 设置Step索引
			scoreData.setStepIndex(context.getRecords().size() - 1);

			// 客户端消息
			Step4Display send2Clinet = new Step4Display();
			send2Clinet.copyFrom(step);
			send2Clinet.getScoreData().copyFrom(scoreData);

			// 依据socreData给客户端增加点炮,自摸已经在scoreData中
			if (roleId != scoreData.getSourceId())
			{
				ScorePoint dianPao = new ScorePoint();
				dianPao.setScoreType(PlayType.HU_DIAN_PAO);
				send2Clinet.getScoreData().getPoints().add(dianPao);
			}

			// 添加下发胡牌操作消息
			controller.addDisplayOperations(send2Clinet);

			// 胡操作最后的附加处理
			afterOperationProcessor.process(controller, step);
		}

		// 抢杠, 如果不支持抢杠和, 根本不会产生相应的waiting操作, 所以getPlayType不会为OPERATE_BU_GANG_A_CARD
		if (preStep.getPlayType() == PlayType.OPERATE_BU_GANG_A_CARD)
		{
			// 删除补杠分
			List<ScoreData> scoreInfo = context.getPlayers().get(sourceId).getCards().getScoreDatas();
			scoreInfo.remove(scoreInfo.size() - 1);

			// 删除原有补杠
			context.removeCardHeap(sourceId, preStep.getPlayType(), card);

			// 还原成碰
			context.addCardHeap(sourceId, PlayType.OPERATE_PENG_A_CARD, card);
		}
		else if (isBeHu)
			// 当前玩家为点炮者，需要从打牌区删除被胡的牌
			context.removeOutCard(sourceId, card);

		// 通知玩家
		controller.sendDisplayOperations();

		// 检测牌局状态
		checkBattleFinishState();

		// 结束牌局，进入结算
		if (context.getBattleState() != BattleState.INBATTLE)
			finish();
		else
		{
			// 多人胡牌，发牌给牌桌顺序最后一个胡牌玩家的下家
			// 排序胡牌玩家
			List<Integer> huRoleIds = steps.stream().collect(() -> new ArrayList<Integer>(), (list, tempStep) -> list.add(tempStep.getRoleId()), (list1, list2) -> list1.addAll(list2));
			huRoleIds.sort((r1, r2) -> {
				return context.getPlayerById(r2).getPosition() - context.getPlayerById(r1).getPosition();
			});

			// 还可以继续，下个人摸牌
			dealCard(context.getNextRoleId(huRoleIds.get(0)));
		}
	}
}
