package com.kodgames.battleserver.service.battle.region.guangdong.shanwei.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.check.CheckHelper;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 检测牌形:杠上花（杠后补到花牌然后补牌能胡也算杠上花）
 */
public class HuSubScoreChecker_GangShangHua_ShanWei extends HuSubScoreChecker
{
	public HuSubScoreChecker_GangShangHua_ShanWei()
	{
		super(PlayType.HU_GANG_SHANG_HUA);
	}

	@Override
	public boolean skipCheckTing()
	{
		return false;
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 杠上花肯定是自摸
		if (CheckHelper.checkHu_ZiMo(context, roleId) == false)
			return false;

		int i = 1;
		// 检测前一个操作是这个玩家的杠（或者杠之后一直补到花牌）
		while (true)
		{
			// 获取上一步
			Step lastLastStep = context.getLastRecordStep(i);
			// 上一步是否为自己操作
			if (lastLastStep == null || lastLastStep.getRoleId() != roleId)
				return false;
			
			// 上一步是花牌
			if (lastLastStep.getPlayType() == PlayType.DISPLAY_EX_CARD)
			{
				++i;
				continue;
			}
			
			// 上一步是杠操作
			if (PlayType.isGangOperator(lastLastStep.getPlayType()))
				break;
		}

		addScore(inoutHuContext.scoreData);
		return true;
	}
}