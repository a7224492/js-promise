package com.kodgames.battleserver.service.battle.region.guangdong.common.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 潮汕汕尾项目的吃杠杠爆全包分数
 * 
 * @author kod
 */
public class HuSubScoreChecker_ChiGangGangBao extends HuSubScoreChecker
{

	public HuSubScoreChecker_ChiGangGangBao()
	{
		super(PlayType.HU_CHI_GANG_GANG_BAO);
	}

	/**
	 * 计算玩家是否有吃杠杠爆全包的分
	 */
	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 不是自摸
		if (inoutHuContext.scoreData.getSourceId() != roleId)
			return false;

		// 获取上上一步
		Step lastLastStep = context.getLastRecordStep(1);
		// 判断上上一步是否为补杠
		if (lastLastStep == null || lastLastStep.getRoleId() != roleId || lastLastStep.getPlayType() != PlayType.OPERATE_GANG_A_CARD)
			return false;

		// 添加分数
		addScore(inoutHuContext.scoreData);
		return true;
	}

}
