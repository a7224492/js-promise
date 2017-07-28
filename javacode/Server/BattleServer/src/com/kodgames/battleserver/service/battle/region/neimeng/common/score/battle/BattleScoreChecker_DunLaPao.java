package com.kodgames.battleserver.service.battle.region.neimeng.common.score.battle;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.score.battle.BattleScoreChecker;
import com.kodgames.battleserver.service.battle.region.neimeng.common.CardType_DunLaPao;

/**
 * “蹲拉跑”分数检查器
 */
public class BattleScoreChecker_DunLaPao extends BattleScoreChecker
{

	protected BattleScoreChecker_DunLaPao(int playType)
	{
		super(playType);
	}

	@Override
	public void calculate(BattleBean context)
	{
		// 统计胡牌、输牌的人
		Set<Integer> winners = new HashSet<>();
		Set<Integer> losers = new HashSet<>();
		getWinnersAndLosers(context, winners, losers);

		// 黄庄不计蹲拉跑
		if (winners.isEmpty() && losers.isEmpty())
		{
			return;
		}

		// 用于合并蹲拉跑分数，false 时，losers.size() 必为1
		final boolean combineByWinner = winners.size() == 1;
		Macro.AssetFalse(combineByWinner || losers.size() == 1);

		int zhuangId = context.getZhuang();

		// 计算每个人的分数
		for (PlayerInfo player : context.getPlayers().values())
		{
			if (!hasTargetSelection(player.getCards().getCardHeap()))
				continue;

			// 计算胡牌人的分数
			if (winners.contains(player.getRoleId()))
			{
				if (combineByWinner)
				{
					losers.forEach(loser -> saveScore(player.getRoleId(), loser, zhuangId, scoreValue, true, player.getCards().getScoreDatas()));
				}
				else
				{
					losers.forEach(loser -> saveScore(loser, player.getRoleId(), zhuangId, -scoreValue, false, context.getPlayers().get(loser).getCards().getScoreDatas()));
				}
			}

			// 计算输牌人的分数
			if (losers.contains(player.getRoleId()))
			{
				if (combineByWinner)
				{
					winners.forEach(winner -> saveScore(winner, player.getRoleId(), zhuangId, scoreValue, false, context.getPlayers().get(winner).getCards().getScoreDatas()));
				}
				else
				{
					winners.forEach(winner -> saveScore(player.getRoleId(), winner, zhuangId, -scoreValue, true, player.getCards().getScoreDatas()));
				}
			}
		}
	}

	/**
	 * 是否选择了要判别的蹲拉跑类型
	 */
	private boolean hasTargetSelection(List<Step> steps)
	{
		for (Step step : steps)
		{
			if (step.getPlayType() != PlayType.OPERATE_DUN_LA_PAO)
				continue;

			for (Byte card : step.getCards())
			{
				if (this.scoreType == CardType_DunLaPao.getDunLaPaoDisplayType(card))
					return true;
			}
		}

		return false;
	}

	/**
	 * 保存分数
	 */
	protected void saveScore(int sourceId, int targetId, int zhuangId, int score, boolean add, List<ScoreData> targetScoreDatas)
	{
		// 判断是否有可以合并的分数
		ScoreData data = null;
		for (ScoreData sd : targetScoreDatas)
		{
			if (sd.getSourceId() != sourceId)
				continue;

			if (sd.getAddOperation() != add)
				continue;

			List<ScorePoint> points = sd.getPoints();
			if (points.isEmpty())
				continue;

			ScorePoint point = points.get(0);
			if (point.getScoreType() != this.scoreType || point.getScoreValue() != score)
				continue;

			data = sd;
		}

		// 新建分数
		if (null == data)
		{
			ScorePoint point = new ScorePoint();
			point.setScoreType(this.scoreType);
			point.setScoreValue(score);
			point.setCalcType(this.calculateType.getValue());

			data = new ScoreData();
			data.getPoints().add(point);
			data.setAddOperation(add);
			data.setSourceId(sourceId);
			data.getScoreTargetList().add(targetId);
			targetScoreDatas.add(data);
		}
		else // 合并分数
		{
			data.getScoreTargetList().add(targetId);
		}
	}

	/**
	 * 计算牌局赢家及输家
	 * 
	 * @param context
	 * @param winners
	 * @param losers
	 */
	private void getWinnersAndLosers(BattleBean context, Set<Integer> winners, Set<Integer> losers)
	{
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
	}

}
