package com.kodgames.battleserver.service.battle.core.score.hu;

import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;

/**
 * 检测牌形:四对和牌
 * 
 * 数组中不能包括吃碰杠的牌
 */
public class HuMainScoreChecker_SiDui extends HuMainScoreChecker
{
	public HuMainScoreChecker_SiDui()
	{
		super(PlayType.HU_SI_DUI);
	}
}
