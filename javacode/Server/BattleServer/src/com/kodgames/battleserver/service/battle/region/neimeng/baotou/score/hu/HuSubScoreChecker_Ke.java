package com.kodgames.battleserver.service.battle.region.neimeng.baotou.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardGroup;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.CardGroupType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 牌型算分：壳
 * 
 * 壳，1分。（胡牌时，组成胡牌牌型的暗刻或明刻为壳，每个1分可累加。
 */
public class HuSubScoreChecker_Ke extends HuSubScoreChecker
{
	public HuSubScoreChecker_Ke()
	{
		super(PlayType.HU_KE);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		int count = 0;
		// 获取牌型中刻的数量
		for (CardGroup group : inoutHuContext.scoreData.getCardGroups())
		{
			int groupType = group.getGroupType();
			if (groupType == CardGroupType.KE || groupType == CardGroupType.AN_KE)
				++count;
		}

		boolean hasKe = count > 0;
		if (hasKe)
		{
			ScorePoint point = new ScorePoint();
			point.setScoreType(super.getScoreType());
			point.setCalcType(super.getModifierType().getValue());
			point.setScoreValue(super.getModiferScore() * count);
			inoutHuContext.scoreData.getPoints().add(point);
		}

		return hasKe;
	}
}
