package com.kodgames.battleserver.service.battle.core.score.hu;

import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;

/**
 * 检测牌形:七对和牌
 * 
 * 数组中不能包括吃碰杠的牌
 */
public class HuMainScoreChecker_QiDui extends HuMainScoreChecker
{
	public HuMainScoreChecker_QiDui()
	{
		super(PlayType.HU_QI_DUI);
	}
}