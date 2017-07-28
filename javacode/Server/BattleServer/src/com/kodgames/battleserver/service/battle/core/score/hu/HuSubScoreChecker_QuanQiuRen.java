package com.kodgames.battleserver.service.battle.core.score.hu;

import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;

/**
 * 检测牌形:全求人
 */
public class HuSubScoreChecker_QuanQiuRen extends HuSubScoreChecker
{
	public HuSubScoreChecker_QuanQiuRen()
	{
		super(PlayType.HU_QUAN_QIU_REN);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		int totalCardCount = 0;
		for (byte cardCount : inoutHuContext.cardCountList)
			totalCardCount += cardCount;

		// 手牌中只能有一对将
		if (totalCardCount != 2)
			return false;

		List<Step> records = context.getRecords();
		Step lastStep = records.get(records.size() - 1);
		List<Byte> lastCards = lastStep.getCards();
		if (1 != lastCards.size())
			return false;

		boolean isZiMo = roleId == lastStep.getRoleId();
		if (isZiMo)
			return false;

		// 不能有暗杠，其它吃，碰，明，补杠都是可以的
		List<Step> steps = context.getPlayers().get(roleId).getCards().getCardHeap();
		for (Step step : steps)
		{
			if (step.getPlayType() == PlayType.OPERATE_AN_GANG)
				return false;
		}

		addScore(inoutHuContext.scoreData);
		return true;
	}
}