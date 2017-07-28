package com.kodgames.battleserver.service.battle.region.meizhou.tuidaohu.zhuang;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.BattleRoom;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.BattleState;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.BattleHelper;
import com.kodgames.battleserver.service.battle.core.zhuang.ZhuangCalculator;

/**
 * 推倒胡,换庄规则
 */
public class ZhuangCalculator_GD extends ZhuangCalculator
{

	@Override
	public void calculateNextZhuang(BattleRoom battleRoom)
	{
		// 获得当前局
		BattleBean battleBean = BattleHelper.getInstance().getBattleBean();

		// 黄庄, 庄家继续坐庄
		if (battleBean.getBattleState() == BattleState.HUANGZHUANG)
		{
			battleBean.setNextZhuang(battleBean.getZhuang());
			return;
		}

		// 只有一家胡牌,过庄到赢家
		Step huRecord = battleBean.getRecords().get(battleBean.getHuIndex().get(0));
		ScoreData scoreData = battleBean.getScoreData(huRecord.getRoleId(), battleBean.getHuIndex().get(0));
		// 判断抢杠胡, 转庄到被抢杠的人（被抢杠的是庄家，不转庄）（只有在抢杠胡的情况下才能一炮多响,同样转庄到被抢杠（点炮）的人）
		{
			int sourceId = scoreData.getSourceId();
			if (sourceId != huRecord.getRoleId() && battleBean.getPlayerLastRecord(sourceId).getPlayType() == PlayType.OPERATE_BU_GANG_A_CARD)
			{
				battleBean.setNextZhuang(sourceId);
				return;
			}
		}

		// 正常胡牌，设置为胡牌玩家做庄
		battleBean.setNextZhuang(huRecord.getRoleId());
	}

}
