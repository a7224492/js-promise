package com.kodgames.battleserver.service.battle.region.meizhou.heyuan.operation;

import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.service.battle.Controller.ControllerManager;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.operation.AfterOperationProcessorBase;

/**
 * 河源 打牌后置处理器 添加破坏可翻操作的标记
 */
public class AfterOperationProcessor_BreakFan extends AfterOperationProcessorBase
{

	@Override
	public List<Step> process(ControllerManager controller, Step prevStep)
	{
		BattleBean context = controller.getBattleBean();
		List<Step> cardHeap = context.getPlayerById(prevStep.getRoleId()).getCards().getCardHeap();

		List<Step> result = new ArrayList<Step>();

		// 从最后面的操作开始
		for (int i = cardHeap.size() - 1; i >= 0; i--)
		{
			Step step = cardHeap.get(i);

			// 如果有breakFan标记就不是翻的状态了
			if (step.getPlayType() == PlayType.OPERATE_BREAK_FAN_A_CARD)
			{
				return result;
			}

			// 如果是翻的状态就进入下一步
			if (step.getPlayType() == PlayType.OPERATE_FAN_A_CARD)
			{
				break;
			}

			// 如果最后什么标记都没有就也不是翻的状态
			if (i == 0)
			{
				return result;
			}
		}

		// 已经打出的那张牌
		Byte playedCard = prevStep.getCards().get(0);

		// 把打出牌的牌放进手牌中检测一下可翻牌型
		context.getPlayerById(prevStep.getRoleId()).getCards().getHandCards().add(playedCard);
		// context.addHandCard(prevStep.getRoleId(), playedCard);

		// 得到可翻牌型
		List<Byte> canFanCards = OperationChecker_Fan.canPlay(context, prevStep.getRoleId());

		// 算完可翻牌型以后把加进去的牌移除
		context.getPlayerById(prevStep.getRoleId()).getCards().getHandCards().remove(playedCard);

		if (canFanCards != null && !canFanCards.isEmpty())
		{
			// 当打出的牌没有破坏翻的牌型时
			if (canFanCards.contains(playedCard))
			{
				return result;
			}
		}

		result.add(new Step(prevStep.getRoleId(), PlayType.OPERATE_BREAK_FAN_A_CARD, playedCard));
		return result;
	}

}
