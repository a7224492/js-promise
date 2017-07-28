package com.kodgames.battleserver.service.battle.core.score.hu;

import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;

/**
 * 检测平胡牌形
 * 
 * 检测其他牌形之间,必须调用这个checker用于构造HuData中的CardGroup
 */
public class HuMainScoreChecker_13Lan extends HuMainScoreChecker
{
	public HuMainScoreChecker_13Lan()
	{
		super(PlayType.HU_13LAN);
	}
}