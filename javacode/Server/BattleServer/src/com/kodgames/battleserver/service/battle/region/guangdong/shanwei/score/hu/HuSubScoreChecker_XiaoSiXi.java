package com.kodgames.battleserver.service.battle.region.guangdong.shanwei.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 检测牌型：小四喜（胡牌牌型中有风牌的三个刻子或杠+一个对子）
 */
public class HuSubScoreChecker_XiaoSiXi extends HuSubScoreChecker
{

	public HuSubScoreChecker_XiaoSiXi()
	{
		super(PlayType.HU_XIAO_SI_XI);
	}

	@Override
	public boolean skipCheckTing()
	{
		return false;
	}

	/**
	 * 小四喜的检测方法，检测是否有箭牌的将，剩余的箭牌是否为刻（不能成顺，所以大于三张必定为刻）
	 * 
	 * 重载方法
	 * 
	 * @param context
	 * @param roleId
	 * @param inoutHuContext
	 * @return
	 */
	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		boolean hasJiang = false;

		// 判断风牌（东南西北）是否大于三张
		for (byte card = CardType.FENG.Value(); card < CardType.FENG.MaxValue(); card++)
		{
			if (inoutHuContext.allCardCountList[card] < 2)
				return false;
			else if (inoutHuContext.allCardCountList[card] == 2)
				hasJiang = true;
		}

		if (hasJiang)
			addScore(inoutHuContext.scoreData);
		
		return hasJiang;
	}

}
