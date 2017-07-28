package com.kodgames.battleserver.service.battle.core.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;

public abstract class HuSubScoreChecker extends HuScoreChecker
{
	protected HuSubScoreChecker(int scoreType)
	{
		super(scoreType);
	}

	/**
	 * 判断是否可以胡牌
	 * 
	 * 注意, 函数有可能是由于听牌检测引起, 如果使用record来判断前置操作, 不能用于检测听牌 , 需要重载skipCheckTing() 
	 * 
	 * @param context 战斗上下文
	 * @param roleId 要检测的玩家Id
	 * @param inoutHuContext 胡牌上下文
	 * @return 是否可以胡牌
	 * @see [类、类#方法、类#成员]
	 */
	public abstract boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext);
}