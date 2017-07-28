package com.kodgames.battleserver.service.battle.region.guangdong.shanwei.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 汕尾地胡，不准许吃碰杠，并且是在庄家第二次摸牌前
 */
public class HuSubScoreChecker_DiHu_ShanWei extends HuSubScoreChecker
{

	public HuSubScoreChecker_DiHu_ShanWei()
	{
		super(PlayType.HU_DI_HU);
	}
	
	/**
	 * 在检测ting牌的情况是否要忽略这个分数
	 */
	public boolean skipCheckTing()
	{
		return true;
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 没有吃碰杠操作（已排除其他人的操作）
		for (Step step : context.getRecords())
			if (PlayType.isChiPengGang(step.getPlayType()))
				return false;

		// 必须不是庄并且庄家只进行过一次摸牌
		if (context.getZhuang() == roleId || context.getPlayers().get(context.getZhuang()).getCards().getRecordIndices().size() > 2)
			return false;

		addScore(inoutHuContext.scoreData);
		return true;
	}

}
