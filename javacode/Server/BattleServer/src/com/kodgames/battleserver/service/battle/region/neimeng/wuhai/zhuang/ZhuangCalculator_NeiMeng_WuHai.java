package com.kodgames.battleserver.service.battle.region.neimeng.wuhai.zhuang;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.BattleRoom;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.BattleState;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.zhuang.ZhuangCalculator;

public class ZhuangCalculator_NeiMeng_WuHai extends ZhuangCalculator
{
	@Override
	public void calculateNextZhuang(BattleRoom battleRoom)
	{
		Macro.AssetTrue(null == battleRoom);
		BattleBean battleBean = battleRoom.getGames().get(battleRoom.getGames().size() - 1);

		// 黄庄, 庄家继续坐庄
		if (battleBean.getBattleState() == BattleState.HUANGZHUANG)
		{
			battleBean.setNextZhuang(battleBean.getZhuang());
		}
		// 非黄庄
		else
		{
			int huIndex = battleBean.getHuIndex().get(0);
			Step huStep = battleBean.getRecords().get(huIndex);
			Step beHuStep = battleBean.getRecords().get(battleBean.getScoreData(huStep.getRoleId(), huIndex).getSourceRecrodIndex());

			// 抢杠胡，被抢杠玩家坐庄
			if (beHuStep.getPlayType() == PlayType.OPERATE_BU_GANG_A_CARD)
			{
				battleBean.setNextZhuang(beHuStep.getRoleId());
				return;
			}

			// 非一炮多响，胡牌玩家坐庄
			if (battleBean.getHuIndex().size() == 1)
				battleBean.setNextZhuang(huStep.getRoleId());
			// 一炮多响，点炮玩家坐庄
			else
				battleBean.setNextZhuang(beHuStep.getRoleId());
		}
	}
}
