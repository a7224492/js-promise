package com.kodgames.battleserver.service.battle.region.neimeng.common.zhuang;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.BattleRoom;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.BattleState;
import com.kodgames.battleserver.service.battle.core.zhuang.ZhuangCalculator;

/**
 * 内蒙庄家计算器
 */
public class ZhuangCalculator_NeiMeng extends ZhuangCalculator
{

	@Override
	public void calculateNextZhuang(BattleRoom battleRoom)
	{
		Macro.AssetTrue(null == battleRoom);
		int gameIndex = battleRoom.getGames().size() - 1;
		BattleBean battleBean = battleRoom.getGames().get(gameIndex);

		// 黄庄, 庄家继续坐庄
		if (battleBean.getBattleState() == BattleState.HUANGZHUANG)
		{
			battleBean.setNextZhuang(battleBean.getZhuang());
			return;
		}

		Macro.AssetFalse(battleBean.getBattleState() == BattleState.FINISH);
		// 判断庄家是否胡牌
		int zhuangId = battleBean.getZhuang();

		// 庄家胡牌，则庄家连庄
		if (battleBean.hasHuInfo(zhuangId))
		{
			battleBean.setNextZhuang(zhuangId);
		}
		// 没有黄庄，且庄家没胡牌，则庄家依座位轮换到下家
		else
		{
			battleBean.setNextZhuang(battleBean.getNextRoleId(zhuangId));
		}
	}

}
