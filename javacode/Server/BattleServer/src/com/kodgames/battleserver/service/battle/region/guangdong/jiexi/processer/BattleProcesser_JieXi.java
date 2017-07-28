package com.kodgames.battleserver.service.battle.region.guangdong.jiexi.processer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.common.xbean.Step4Display;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.BattleState;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.processer.BattleProcesser;

/**
 * 揭西玩法，移除暗杠
 * 
 * @author 毛建伟
 */
public class BattleProcesser_JieXi extends BattleProcesser
{
	/**
	 * 处理OPERATE_HU操作, 支持一炮多响
	 * 
	 * @param preStep 前一个操作
	 * @param steps 当前所有的胡操作列表
	 */
	@Override
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
		else if (preStep.getPlayType() == PlayType.OPERATE_AN_GANG)
		{
			// 删除补杠分
			List<ScoreData> scoreInfo = context.getPlayers().get(sourceId).getCards().getScoreDatas();
			scoreInfo.remove(scoreInfo.size() - 1);

			// 删除原有补杠
			context.removeCardHeap(sourceId, preStep.getPlayType(), card);

			List<Byte> handCards = context.getPlayerById(sourceId).getCards().getHandCards();
			// 还原成手牌
			for (int i = 0; i < 3; ++i)
				handCards.add(card);
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
