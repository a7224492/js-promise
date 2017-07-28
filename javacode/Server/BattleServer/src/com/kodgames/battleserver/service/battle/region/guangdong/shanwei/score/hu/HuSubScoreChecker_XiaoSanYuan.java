package com.kodgames.battleserver.service.battle.region.guangdong.shanwei.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 检测牌型：小三元（胡牌牌型中有箭牌的两个刻子或杠+一个对子，例如123条+555筒+中中中中+发发+白白白）
 */
public class HuSubScoreChecker_XiaoSanYuan extends HuSubScoreChecker
{

	public HuSubScoreChecker_XiaoSanYuan()
	{
		super(PlayType.HU_XIAO_SAN_YUAN);
	}

	@Override
	public boolean skipCheckTing()
	{
		return false;
	}

	/**
	 * 小三元的检测方法，检测是否有箭牌的将，剩余的箭牌是否为刻（不能成顺，所以大于三张必定为刻）
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

		// 判断箭牌（中发白）是否大于三张
		for (byte card = CardType.JIAN.Value(); card < CardType.JIAN.MaxValue(); card++)
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
