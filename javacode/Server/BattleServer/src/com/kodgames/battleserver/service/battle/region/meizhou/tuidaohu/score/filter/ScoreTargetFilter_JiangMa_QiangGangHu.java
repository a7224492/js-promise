package com.kodgames.battleserver.service.battle.region.meizhou.tuidaohu.score.filter;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.score.filter.ScoreTargetFilter;

/**
 * 广东推倒胡的奖马是抢杠胡收取分数目标判断
 */
public class ScoreTargetFilter_JiangMa_QiangGangHu extends ScoreTargetFilter
{
	@Override
	public boolean filter(BattleBean context, PlayerInfo player, PlayerInfo sourcePlayer, PlayerInfo checkingPlayer)
	{
		// 判断是否为抢杠胡
		for (ScoreData scoreData : player.getCards().getScoreDatas())
		{
			if (PlayType.isHuType(scoreData.getPoints().get(0).getScoreType()))
			{
				// 来源的上一步是补杠，这里需要一步一步判断到是不是抢杠胡，只有在胡的时候没有抢杠胡才会返回false
				if (context.getPlayerLastRecord(scoreData.getSourceId()).getPlayType() == PlayType.OPERATE_BU_GANG_A_CARD)
				{
					if (scoreData.getSourceId() != checkingPlayer.getRoleId())
						return false;
				}
			}
		}

		// 到这里证明有抢杠胡并且被抢杠的人是当前检查的人，或者是需要收取所有人的分数
		return true;
	}

}
