package com.kodgames.battleserver.service.battle.region.meizhou.heyuan.operation;

import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.service.battle.Controller.ControllerManager;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.operation.AfterOperationProcessorBase;

/**
 * 河源冷碰
 */
public class AfterOperationProcessor_LengPeng extends AfterOperationProcessorBase
{

	@Override
	public List<Step> process(ControllerManager controller, Step prevStep)
	{
		BattleBean context = controller.getBattleBean();

		List<Step> result = new ArrayList<Step>();
		
		// 得到碰的牌值
		byte pengCard = prevStep.getCards().get(0);

		List<Byte> handCards = context.getPlayerById(prevStep.getRoleId()).getCards().getHandCards();

		for (Byte card : handCards)
		{
			if (card != pengCard)
			{
				continue;
			}
			
			result.add(new Step(prevStep.getRoleId(), PlayType.OPERATE_LENG_PENG_A_CARD, pengCard));

			// 增加冷碰操作
			//context.addCardHeap(prevStep.getRoleId(), PlayType.OPERATE_LENG_PENG_A_CARD, pengCard);
		}

		return result;
	}

}
