package com.kodgames.battleserver.service.battle.region.guangdong.shantou.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 汕头的海底捞月（杠后补牌不算）
 * 
 * @author kod
 *
 */
public class HuSubScoreChecker_HaiLao_ShanTou extends HuSubScoreChecker
{

	public HuSubScoreChecker_HaiLao_ShanTou()
	{
		super(PlayType.HU_HAI_DI_LAO_YUE);
	}

	/**
	 * 计算是否为汕头的海底捞月（杠后补牌不算）
	 */
	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 当前最后一张
		if (context.getCardPool().getCards().size() > context.getCardPool().getStayCount())
			return false;
		
		// 获取自己摸牌的前一步
		Step lastLastStep = context.getLastRecordStep(1);
		// 是否为空或者是杠操作
		if (lastLastStep == null || PlayType.isGangOperator(lastLastStep.getPlayType()))
			return false;

		// 添加分数
		addScore(inoutHuContext.scoreData);
		return true;
	}

}
