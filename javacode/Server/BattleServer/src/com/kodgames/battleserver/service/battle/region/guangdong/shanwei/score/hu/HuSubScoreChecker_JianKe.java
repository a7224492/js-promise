package com.kodgames.battleserver.service.battle.region.guangdong.shanwei.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardGroup;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.CardGroupType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalculateType;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 汕尾检测箭牌分
 */
public class HuSubScoreChecker_JianKe extends HuSubScoreChecker
{

	public HuSubScoreChecker_JianKe()
	{
		super(PlayType.DISPLAY_ZI_JIA_FAN);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 箭刻（杠）计数
		int jianKeCount = 0;
		// 判断玩家是否有箭刻，箭杠
		for (CardGroup cardGroup : inoutHuContext.scoreData.getCardGroups())
		{
			// 是否为杠，刻，并且是箭牌
			if ((CardGroupType.isKe(cardGroup.getGroupType()) || CardGroupType.isGang(cardGroup.getGroupType())) && CardType.JIAN == CardType.getFengJianType(cardGroup.getCardList().get(0)))
			{
				// 箭刻+1
				++jianKeCount;
			}
		}

		if (jianKeCount == 0)
			return false;

		ScorePoint point = new ScorePoint();
		point.setCalcType(ScoreCalculateType.TOTAL_ADD.getValue());
		point.setScoreType(PlayType.DISPLAY_ZI_JIA_FAN);
		point.setScoreValue(jianKeCount * 2);

		inoutHuContext.scoreData.getPoints().add(point);
		return true;
	}

}
