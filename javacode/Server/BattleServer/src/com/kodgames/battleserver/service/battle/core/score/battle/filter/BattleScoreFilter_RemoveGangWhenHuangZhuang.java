package com.kodgames.battleserver.service.battle.core.score.battle.filter;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.BattleState;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;

/**
 * 黄庄之后, 所有杠的分数不算
 */
public class BattleScoreFilter_RemoveGangWhenHuangZhuang extends BattleScoreFilter
{
	public void filter(BattleBean context)
	{
		// 判断是否黄庄
		if (context.getBattleState() != BattleState.HUANGZHUANG)
			return;

		context.getPlayers().values().forEach(player -> {
			
			// 删除掉杠分
			player.getCards().getScoreDatas().removeIf(score ->{
				if (PlayType.isGangOperator(score.getPoints().get(0).getScoreType()))
					return true;
				return false;
			});
		});
	}
}
