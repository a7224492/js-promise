package com.kodgames.battleserver.service.battle.region.guangdong.jiexi.operation.filter;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.BattleHelper;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationResultFilter;

/**
 * 听牌十三幺可以抢暗杠
 * 
 * @author 毛建伟
 */
public class OperationResultFilter_QiangAnGang extends OperationResultFilter
{

	@Override
	public boolean filter(BattleBean context, Step result, byte card, boolean phaseDeal)
	{
		// 上一步不是暗杠，证明可以胡
		if (context.getLastRecordStep(0).getPlayType() != PlayType.OPERATE_AN_GANG)
			return true;
		// 计算分数
		ScoreData scoreData = BattleHelper.getInstance().getHuScoreProcessor().process(context, result.getRoleId(), phaseDeal ? 0 : card, false);
		// 分数为空证明不可胡
		if (scoreData == null || scoreData.getPoints().isEmpty())
			return false;
		// 循环判断如果是十三幺证明可以胡
		for (ScorePoint point : scoreData.getPoints())
		{
			if (point.getScoreType() == PlayType.HU_SHI_SAN_YAO)
				return true;
		}
		return false;
	}

}
