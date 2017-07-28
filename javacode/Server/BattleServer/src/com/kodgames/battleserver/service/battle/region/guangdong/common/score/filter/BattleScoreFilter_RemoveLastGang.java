package com.kodgames.battleserver.service.battle.region.guangdong.common.score.filter;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.BattleState;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.score.battle.filter.BattleScoreFilter;

/**
 * 删除掉最后一个杠（黄庄的时候）
 * 
 * @author 毛建伟
 */
public class BattleScoreFilter_RemoveLastGang extends BattleScoreFilter
{

	@Override
	public void filter(BattleBean context)
	{
		// 判断是否黄庄
		if (context.getBattleState() != BattleState.HUANGZHUANG)
			return;

		// 获取最后一步
		Step lastStep = context.getLastRecordStep(0);
		if (PlayType.isGangOperator(lastStep.getPlayType()))
		{
			// 这里会删除掉最后一个杠产生的分数
			context.getPlayerById(lastStep.getRoleId()).getCards().getScoreDatas().removeIf(data -> {
				if (data.getSourceCard() == lastStep.getCards().get(0))
					return true;
				return false;
			});
		}

	}

}
