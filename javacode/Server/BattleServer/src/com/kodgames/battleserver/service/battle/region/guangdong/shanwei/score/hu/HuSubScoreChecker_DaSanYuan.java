package com.kodgames.battleserver.service.battle.region.guangdong.shanwei.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 检测牌型：大三元（胡牌牌型中有箭牌（中发白）的三个刻子或杠）
 */
public class HuSubScoreChecker_DaSanYuan extends HuSubScoreChecker
{

	public HuSubScoreChecker_DaSanYuan()
	{
		super(PlayType.HU_DA_SAN_YUAN);
	}

	@Override
	public boolean skipCheckTing()
	{
		return false;
	}

	/**
	 * 大三元的检测方法，由于汕尾地区中中发白不能成顺子，所以可以直接判断中发白是否大于三张
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
		// 判断箭牌（中发白）是否大于三张
		for (byte card = CardType.JIAN.Value(); card < CardType.JIAN.MaxValue(); card++)
		{
			if (inoutHuContext.allCardCountList[card] < 3)
				return false;
		}
		
		addScore(inoutHuContext.scoreData);
		return true;
	}

}
