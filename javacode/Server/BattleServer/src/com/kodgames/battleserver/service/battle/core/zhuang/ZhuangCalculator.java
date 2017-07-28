package com.kodgames.battleserver.service.battle.core.zhuang;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.BattleRoom;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;

import net.sf.json.JSONObject;

/**
 * 用于计算当前庄家模块
 * 
 * 重载这个类, 实现相应的接口来实现不同的庄家计算逻辑
 */
abstract public class ZhuangCalculator
{
	public static ZhuangCalculator create(JSONObject context)
	{
		return CreateContextHelper.instantiateClass(context, ZhuangCalculator.class);
	}

	/**
	 * 计算并设置当前庄家
	 * 
	 * 默认实现逻辑 : 如果是第一局, 当前创建者为当前庄家, 否则使用前一局的getNextZhuang
	 */
	public void calculateZhuang(BattleRoom battleRoom)
	{
		int gameIndex = battleRoom.getGames().size() - 1;
		BattleBean battleBean = battleRoom.getGames().get(gameIndex);

		if (battleRoom.getGames().size() == 1)
			// 如果是第一局, 创建者为庄家
			battleBean.setZhuang(battleRoom.getCreatorId());
		else
			// 否则使用前一局的getNextZhuang
			battleBean.setZhuang(battleRoom.getGames().get(gameIndex - 1).getNextZhuang());
	}

	/**
	 * 计算并设置当前庄家
	 */
	abstract public void calculateNextZhuang(BattleRoom battleRoom);
}
