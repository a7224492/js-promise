package com.kodgames.battleserver.service.battle.region.neimeng.wulanchabu.score.battle;

import java.util.List;
import java.util.stream.Collectors;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.BattleState;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalculateType;
import com.kodgames.battleserver.service.battle.core.score.battle.BattleScoreChecker;
import com.kodgames.battleserver.service.battle.region.neimeng.common.Rules_NeiMeng;

/**
 * 赔杠
 */
public class BattleScoreChecker_PeiGang_WuLanChaBu extends BattleScoreChecker
{
	public BattleScoreChecker_PeiGang_WuLanChaBu()
	{
		super(PlayType.DISPLAY_PEI_GANG);
	}

	@Override
	public void calculate(BattleBean context)
	{
		if (context.getBattleState() != BattleState.HUANGZHUANG)
			return;

		List<Integer> rules = context.getGameRules();

		if (rules.contains(Rules_NeiMeng.PEI_DIAN_GANG_REN))
		{
			peiDianGangRen(context);
		}

		if (rules.contains(Rules_NeiMeng.PEI_SAN_JIA))
		{
			peiSanJia(context);
		}

		return;
	}

	public void peiDianGangRen(BattleBean context)
	{
		context.getPlayers().values().forEach(player -> {
			List<ScoreData> scoreData = player.getCards().getScoreDatas();
			if (scoreData != null)
			{
				scoreData.forEach(gangScoreData -> {
					ScorePoint gangPoint = gangScoreData.getPoints().get(0);
					// 获取明杠，进行赔分
					if (gangPoint.getScoreType() == PlayType.OPERATE_GANG_A_CARD)
					{
						ScoreData tuigang = new ScoreData();
						tuigang.setAddOperation(false);
						tuigang.setSourceId(player.getRoleId());
						tuigang.getScoreTargetList().add(player.getRoleId());

						ScorePoint scorepoint = new ScorePoint();
						scorepoint.setCalcType(ScoreCalculateType.TOTAL_ADD.getValue());
						scorepoint.setScoreType(this.scoreType);
						scorepoint.setScoreValue(3);

						tuigang.getPoints().add(scorepoint);
						context.getPlayerById(gangScoreData.getSourceId()).getCards().getScoreDatas().add(tuigang);
					}
					// 获取暗杠和补杠，进行赔分
					if (gangPoint.getScoreType() == PlayType.OPERATE_AN_GANG || gangPoint.getScoreType() == PlayType.OPERATE_BU_GANG_A_CARD)
					{
						context.getPlayers().values().forEach(players -> {
							if (players.getRoleId() != player.getRoleId())
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
								players.getCards().getScoreDatas().add(tuigang);
							}
						});
					}
				});
			}
		});

		return;
	}

	public void peiSanJia(BattleBean context)
	{
		context.getPlayers().values().forEach(player -> {
			List<ScoreData> scoreData = player.getCards().getScoreDatas();
			if (scoreData != null)
			{
				scoreData.forEach(gangScoreData -> {
					ScorePoint gangPoint = gangScoreData.getPoints().get(0);
					if (PlayType.isGangOperator(gangPoint.getScoreType()))
					{
						// 获取杠分，进行赔分
						context.getPlayers().values().forEach(players -> {
							if (players.getRoleId() != player.getRoleId())
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
								players.getCards().getScoreDatas().add(tuigang);
							}
						});
						// 删除玩家的杠分
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
