package com.kodgames.battleserver.service.battle.region.guangdong.shanwei.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 汕尾检测点炮
 * @author kod
 *
 */
public class HuSubScoreChecker_DianPao_ShanWei extends HuSubScoreChecker
{

	public HuSubScoreChecker_DianPao_ShanWei()
	{
		super(PlayType.HU_DIAN_PAO);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 是否为点炮
		if (inoutHuContext.scoreData.getSourceId() == roleId)
			return false;
		// 添加分数
		addScore(inoutHuContext.scoreData);
		return true;
	}

}
