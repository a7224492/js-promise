package com.kodgames.battleserver.service.battle.region.neimeng.baotou.score.hu;

import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 牌型算分：四归一
 * 
 * 四归一，2分。（和的那张牌在手牌中已存在3张）
 */
public class HuSubScoreChecker_SiGuiYi extends HuSubScoreChecker
{
	public HuSubScoreChecker_SiGuiYi()
	{
		super(PlayType.HU_SI_GUI_YI);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 牌堆不应为空
		List<Step> records = context.getRecords();
		if (records.isEmpty())
			return false;

		// 最后一步操作只能有一张牌
		Step lastStep = records.get(records.size() - 1);
		List<Byte> cards = lastStep.getCards();
		if (1 != cards.size())
			return false;

		// 手中应该有三张与最后一步相同的牌（在胡牌牌型中共4张）
		byte card = cards.get(0);
		if (4 != inoutHuContext.allCardCountList[card])
			return false;

		addScore(inoutHuContext.scoreData);
		return true;
	}
}
