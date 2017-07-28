package com.kodgames.battleserver.service.battle.region.guangdong.shanwei.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 检测牌型：大四喜（胡牌牌型中有风牌（东南西北）的四个刻子或杠，不再计算碰碰胡得分）
 */
public class HuSubScoreChecker_DaSiXi extends HuSubScoreChecker
{

	public HuSubScoreChecker_DaSiXi()
	{
		super(PlayType.HU_DA_SI_XI);
	}

	@Override
	public boolean skipCheckTing()
	{
		return false;
	}

	/**
	 * 大四喜的检测方法，汕尾东南西北也不能成顺。所以直接判断是否为三张以上就行了
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
		// 判断风牌（东南西北）是否大于三张
		for (byte card = CardType.FENG.Value(); card < CardType.FENG.MaxValue(); card++)
		{
			if (inoutHuContext.allCardCountList[card] < 3)
				return false;
		}

		addScore(inoutHuContext.scoreData);
		return true;
	}

}
