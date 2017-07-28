package com.kodgames.battleserver.service.battle.region.guangdong.jiexi.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 检测牌形:揭西玩法抢杠胡
 */
public class HuSubScoreChecker_QiangGangHu_JieXi extends HuSubScoreChecker
{

	public HuSubScoreChecker_QiangGangHu_JieXi()
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
		// 前一个操作是其他玩家补杠或者暗杠
		Step lastStep = context.getLastRecordStep(0);
		if (lastStep == null || lastStep.getRoleId() == roleId || (lastStep.getPlayType() != PlayType.OPERATE_BU_GANG_A_CARD && lastStep.getPlayType() != PlayType.OPERATE_AN_GANG))
			return false;

		addScore(inoutHuContext.scoreData);
		return true;
	}

}