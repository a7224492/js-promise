package com.kodgames.battleserver.service.battle.region.meizhou.common.zhuang;

import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.BattleRoom;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.zhuang.ZhuangCalculator;

public class ZhuangCalculator_MeiZhou extends ZhuangCalculator
{

	@Override
	public void calculateNextZhuang(BattleRoom battleRoom)
	{
		// 得到当前局
		BattleBean battleBean = battleRoom.getGames().get(battleRoom.getGames().size() - 1);

		// 荒庄就连庄
		List<Integer> huIndexs = battleBean.getHuIndex();
		if (huIndexs.size() == 0)
		{
			battleBean.setNextZhuang(battleBean.getZhuang());
			return;
		}
		else if (huIndexs.size() == 1)
		{
			// 抢杠胡是被抢杠的玩家坐庄
			Step huRecord = battleBean.getRecords().get(huIndexs.get(0));
			ScoreData scoreData = battleBean.getScoreData(huRecord.getRoleId(), huIndexs.get(0));
			List<ScorePoint> scorePointList = scoreData.getPoints();
			for (ScorePoint point : scorePointList)
			{
				if (point.getScoreType() == PlayType.HU_QIANG_GANG_HU)
				{
					battleBean.setNextZhuang(scoreData.getSourceId());
					return;
				}
			}

			// 正常胡, 过庄到赢家
			battleBean.setNextZhuang(huRecord.getRoleId());
			return;
		}
		else
		{
			// 地胡多响和一杠多响都是输分玩家做庄
			for (Integer index : huIndexs)
			{
				Step huRecord = battleBean.getRecords().get(index);
				int huSourceId = battleBean.getScoreData(huRecord.getRoleId(), index).getSourceId();
				battleBean.setNextZhuang(huSourceId);
				break;
			}

			return;
		}

	}

}
