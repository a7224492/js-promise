package com.kodgames.battleserver.service.battle.region.neimeng.wuhai.score.hu;

import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

public class HuSubScoreChecker_ChaoHaoHuaQiDui extends HuSubScoreChecker
{
	public HuSubScoreChecker_ChaoHaoHuaQiDui()
	{
		super(PlayType.HU_CHAO_HAO_HUA_QI_DUI);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 检测基本牌形是否有七对胡
		boolean has7DuiHu = false;
		for (ScorePoint scorePoint : inoutHuContext.scoreData.getPoints())
			if (scorePoint.getScoreType() == PlayType.HU_QI_DUI)
			{
				has7DuiHu = true;
				break;
			}

		if (has7DuiHu == false)
			return false;

		// 有4张牌一样就认为是豪华七对
		int count = 0;
		for (byte cardCount : inoutHuContext.allCardCountList)
			if (cardCount == 4)
				count++;

		// 一副牌最多只能有3个4张的
		if (count < 2)
			return false;

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
