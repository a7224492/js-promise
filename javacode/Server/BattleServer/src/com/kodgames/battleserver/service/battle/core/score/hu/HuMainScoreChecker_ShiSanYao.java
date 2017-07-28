package com.kodgames.battleserver.service.battle.core.score.hu;

import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;

/**
 * 检测牌形:十三幺和牌
 * 
 * 数组中不能包括吃碰杠的牌
 */
public class HuMainScoreChecker_ShiSanYao extends HuMainScoreChecker
{
	public HuMainScoreChecker_ShiSanYao()
	{
		super(PlayType.HU_SHI_SAN_YAO);
	}
}
