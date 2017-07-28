package com.kodgames.battleserver.service.battle.region.neimeng.wuhai.score.battle;

import java.util.List;
import java.util.stream.Collectors;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.BattleState;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalculateType;
import com.kodgames.battleserver.service.battle.core.score.battle.BattleScoreChecker;

/**
 * 赔杠
 */
public class BattleScoreChecker_PeiGang_WuHai extends BattleScoreChecker
{
	public BattleScoreChecker_PeiGang_WuHai()
	{
		super(PlayType.DISPLAY_PEI_GANG);
	}

	@Override
	public void calculate(BattleBean context)
	{
		if (context.getBattleState() != BattleState.HUANGZHUANG)
			return;

		peiGang(context);

		return;
	}

	public void peiGang(BattleBean context)
	{
		context.getPlayers().values().forEach(player -> {
			List<ScoreData> scoreData = player.getCards().getScoreDatas();
			if (scoreData != null)
			{
				scoreData.forEach(gangScoreData -> {
					// 获取杠的得分项
					ScorePoint gangPoint = gangScoreData.getPoints().get(0);
					if (PlayType.isGangOperator(gangPoint.getScoreType()))
					{
						context.getPlayers().values().forEach(otherPlayer -> {
							// 赔杠给除自己外的玩家
							if (otherPlayer.getRoleId() != player.getRoleId())
							{
								ScoreData tuigang = new ScoreData();
								tuigang.setAddOperation(false);
								tuigang.setSourceId(player.getRoleId());
								tuigang.getScoreTargetList().add(player.getRoleId());

								ScorePoint scorepoint = new ScorePoint();
								scorepoint.setCalcType(ScoreCalculateType.TOTAL_ADD.getValue());
								scorepoint.setScoreType(this.scoreType);
								scorepoint.setScoreValue(gangPoint.getScoreValue());

								tuigang.getPoints().add(scorepoint);
								otherPlayer.getCards().getScoreDatas().add(tuigang);
							}
						});
						// 删除自己的杠分
						player.getCards()
							.getScoreDatas()
							.stream()
							.filter(allscoreData -> allscoreData.getPoints().get(0).getScoreType() == gangPoint.getScoreType())
							.collect(Collectors.toList())
							.clear();
					}
				});
			}
		});

		return;
	}
}
