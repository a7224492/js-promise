package com.kodgames.battleserver.service.battle.region.meizhou.heyuan.zhuang;

import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.BattleRoom;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.core.BattleHelper;
import com.kodgames.battleserver.service.battle.core.zhuang.ZhuangCalculator;

/**
 * 创建房间的玩家为初始庄家<br>
 * 第一局后，由胡牌者坐庄<br>
 * 若流局，则由原庄家的下家坐庄
 */
public class ZhuangCalculator_HeYuan extends ZhuangCalculator
{

	@Override
	public void calculateNextZhuang(BattleRoom battleRoom)
	{
		// 得到当前局
		BattleBean battleBean = BattleHelper.getInstance().getBattleBean();

		List<Integer> huIndexs = battleBean.getHuIndex();

		// 流局则由原庄家的下家坐庄
		if (huIndexs.size() == 0)
		{
			battleBean.setNextZhuang(battleBean.getNextRoleId(battleBean.getZhuang()));
		}
		// 胡牌者坐庄
		else if (huIndexs.size() == 1)
		{
			Step huRecord = battleBean.getRecords().get(huIndexs.get(0));
			battleBean.setNextZhuang(huRecord.getRoleId());
		}
	}

}
