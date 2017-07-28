package com.kodgames.battleserver.service.battle.region.yangzhou.operation.filter;

import java.util.List;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationResultFilter;
import com.kodgames.battleserver.service.battle.region.yangzhou.Rules_YangZhou;

/**
 * 已经进园子的玩家不能被吃胡
 * 玩家手牌中有配子不能接炮只能自摸
 */
public class OperationResultFilter_Hu_YangZhou extends OperationResultFilter
{
	@Override
	public boolean filter(BattleBean context, Step result, byte card, boolean phaseDeal)
	{
		Macro.AssetFalse(result.getPlayType() == PlayType.OPERATE_CAN_HU, "在不是可以胡的时候进行了检测");
		
		// 过滤后剩下可以胡牌类型
		if (result.getPlayType() != PlayType.OPERATE_CAN_HU)
		{
			return true;
		}
		
		// 过滤后剩下除了自摸以后的胡牌类型
		if (phaseDeal)
		{
			return true;
		}
		
		List<Integer> rules = context.getGameRules();
		int yuanZi = Rules_YangZhou.getYuanZi(rules);
		Step lastStep = context.getLastRecordStep(0);
		PlayerInfo player = context.getPlayerById(lastStep.getRoleId());
		if(yuanZi != 0 && player.getTotalPoint() <= -yuanZi)
		{
			//玩家已进园子，不能胡他的牌
			return false;
		}
		
		PlayerInfo huPlayerInfo = context.getPlayerById(result.getRoleId());
		for(byte handCard : huPlayerInfo.getCards().getHandCards())
		{
			if(context.getCardPool().getMasterCards().contains(handCard))
			{
				//如果玩家手牌中有配子，不能接炮
				return false;
			}
		}
		
		return true;
	}

}
