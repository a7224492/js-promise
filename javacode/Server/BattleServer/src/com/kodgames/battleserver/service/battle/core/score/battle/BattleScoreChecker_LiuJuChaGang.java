package com.kodgames.battleserver.service.battle.core.score.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.BattleState;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.check.CheckHelper;

/**
 * 流局查杠牌
 * 
 * 流局时未听牌玩家的所有杠不算分
 */
public class BattleScoreChecker_LiuJuChaGang extends BattleScoreChecker
{
	public BattleScoreChecker_LiuJuChaGang()
	{
		super(PlayType.UNKNOW);
	}

	@Override
	public void calculate(BattleBean context)
	{
		// 只有流局时，才进行计算
		if (context.getBattleState() != BattleState.HUANGZHUANG)
			return;

		// 获取听牌玩家
		List<Integer> tingPlayers = new ArrayList<>();
		for (PlayerInfo player : context.getPlayers().values())
		{
			// 忽略胡牌
			if (context.hasHuInfo(player.getRoleId()))
				continue;

			// 检查听牌
			if (CheckHelper.checkTing(context, player.getRoleId()) == false)
				continue;

			tingPlayers.add(player.getRoleId());
		}

		for (PlayerInfo player : context.getPlayers().values())
		{
			// 过滤听牌玩家
			if (tingPlayers.contains(player.getRoleId()))
				continue;

			// 未听牌玩家不收取杠牌分数
			List<ScoreData> scoreDatas = player.getCards().getScoreDatas().stream().filter(scoreData -> {

				boolean ignore = false;
				for (ScorePoint point : scoreData.getPoints())
				{
					if (PlayType.isGangOperator(point.getScoreType()))
					{
						ignore = true;
						break;
					}
				}
				return !ignore;
			}).collect(Collectors.toList());

			player.getCards().getScoreDatas().clear();
			player.getCards().getScoreDatas().addAll(scoreDatas);
		}
	}
}
