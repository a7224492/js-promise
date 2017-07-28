package com.kodgames.battleserver.service.battle.core.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;

/**
 * 检测牌形:抢杠和
 */
public class HuSubScoreChecker_QiangGangHu extends HuSubScoreChecker
{
	
	public HuSubScoreChecker_QiangGangHu()
	{
		super(PlayType.HU_QIANG_GANG_HU);
	}

	@Override
	public boolean skipCheckTing()
	{
		return false;
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 前一个操作是其他玩家补杠
		Step lastStep = context.getLastRecordStep(0);
		if (lastStep == null || lastStep.getRoleId() == roleId || lastStep.getPlayType() != PlayType.OPERATE_BU_GANG_A_CARD)
			return false;

		addScore(inoutHuContext.scoreData);
		return true;
	}
	
}