package com.kodgames.battleserver.service.battle.region.meizhou.common.score.battle.filter;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.BattleState;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.score.battle.filter.BattleScoreFilter;

/**
 * 荒庄不计算跟庄分
 */
public class BattleScoreFilter_GenZhuang_MeiZhou extends BattleScoreFilter
{

	@Override
	public void filter(BattleBean context)
	{
		// 判断是否荒庄
		if (context.getBattleState() != BattleState.HUANGZHUANG)
			return;

		context.getPlayers().values().forEach(player -> {

			// 删除跟庄分
			player.getCards().getScoreDatas().removeIf(score -> {

				if (score.getPoints().get(0).getScoreType() == PlayType.DISPLAY_FOLLOW_BANKER)
					return true;
				return false;
			});
		});
	}
}
