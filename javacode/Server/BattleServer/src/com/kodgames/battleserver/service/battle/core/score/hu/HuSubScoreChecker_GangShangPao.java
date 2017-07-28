package com.kodgames.battleserver.service.battle.core.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.check.CheckHelper;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;

/**
 * 检测牌形:杠上炮
 */
public class HuSubScoreChecker_GangShangPao extends HuSubScoreChecker
{
	public HuSubScoreChecker_GangShangPao()
	{
		super(PlayType.HU_GANG_SHANG_PAO);
	}

	@Override
	public boolean skipCheckTing()
	{
		return false;
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 不是自摸
		if (CheckHelper.checkHu_ZiMo(context, roleId))
			return false;

		// 前一个操作是打牌
		Step lastStep = context.getLastRecordStep(0);
		if (lastStep == null || lastStep.getPlayType() != PlayType.OPERATE_PLAY_A_CARD)
			return false;

		// 在前一个操作是前一个操作玩家杠牌
		Step lastLastStep = context.getLastRecordStep(2);
		if (lastLastStep == null || lastLastStep.getRoleId() != lastStep.getRoleId() || PlayType.isGangOperator(lastLastStep.getPlayType()) == false)
			return false;

		addScore(inoutHuContext.scoreData);
		return true;
	}
}