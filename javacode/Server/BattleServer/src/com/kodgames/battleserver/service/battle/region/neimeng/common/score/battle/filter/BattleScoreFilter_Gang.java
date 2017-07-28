package com.kodgames.battleserver.service.battle.region.neimeng.common.score.battle.filter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.score.battle.filter.BattleScoreFilter;

/**
 * 杠牌分数过滤器
 */
public class BattleScoreFilter_Gang extends BattleScoreFilter
{

	@Override
	public void filter(BattleBean context)
	{
		// 统计胡牌、输牌的人
		Set<Integer> winners = new HashSet<>();
		Set<Integer> losers = new HashSet<>();
		context.getHuIndex().forEach(index -> {
			Step step = context.getRecords().get(index);
			ScoreData huScoreData = context.getScoreData(step.getRoleId(), index);
			winners.add(step.getRoleId());
			if (step.getRoleId() != huScoreData.getSourceId())
			{
				// 点炮的人
				losers.add(huScoreData.getSourceId());
			}
			else
			{
				// 被自摸的人
				context.getPlayers().keySet().stream().filter(id -> id != step.getRoleId()).forEach(id -> losers.add(id));
			}
		});

		context.getPlayers().values().forEach(player -> {
			List<ScoreData> scores = player.getCards().getScoreDatas();
			List<ScoreData> toDelete = new ArrayList<>();
			scores.forEach(score -> {
				ScorePoint point = score.getPoints().get(0);

				// 补杠按明杠计分
				if (PlayType.OPERATE_BU_GANG_A_CARD == point.getScoreType())
					point.setScoreType(PlayType.OPERATE_GANG_A_CARD);

				int scoreType = point.getScoreType();
				if (PlayType.OPERATE_GANG_A_CARD == scoreType || PlayType.OPERATE_AN_GANG == scoreType)
				{
					// 如果胡牌了，向输家扣取杠牌得分
					if (winners.contains(player.getRoleId()))
					{
						score.getScoreTargetList().clear();
						score.getScoreTargetList().addAll(losers);
					}
					// 如果没胡牌，不计杠牌得分
					else
					{
						toDelete.add(score);
					}
				}
			});

			toDelete.forEach(delete -> scores.remove(delete));
		});
	}

}
