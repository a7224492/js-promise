package com.kodgames.battleserver.service.battle.region.guangdong.common.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardGroup;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.check.CheckHelper;
import com.kodgames.battleserver.service.battle.core.hu.CardGroupType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 
 * <一句话功能简述> 计算是否为门清 <功能详细描述> 计算是否有明type，是否为4杠
 * 
 * @author 姓名 毛建伟
 * @version [版本号, 2017年1月5日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class HuSubScoreChecker_MenQing_ChaoShan extends HuSubScoreChecker
{

	public HuSubScoreChecker_MenQing_ChaoShan()
	{
		super(PlayType.HU_MEN_QING);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 是否为正常的门清（没有明type）
		if (CheckHelper.check_MenQianQing(inoutHuContext.scoreData.getCardGroups()))
		{
			addScore(inoutHuContext.scoreData);
			return true;
		}

		// 四个杠也是门清
		int gangCount = 0;
		for (CardGroup cardGroup : inoutHuContext.scoreData.getCardGroups())
		{
			if (CardGroupType.isGang(cardGroup.getGroupType()) || CardGroupType.KE == cardGroup.getGroupType())
				++gangCount;
		}

		// 四个杠时也是门清，加分
		if (gangCount == 4)
		{
			addScore(inoutHuContext.scoreData);
			return true;
		}

		return false;
	}

}
