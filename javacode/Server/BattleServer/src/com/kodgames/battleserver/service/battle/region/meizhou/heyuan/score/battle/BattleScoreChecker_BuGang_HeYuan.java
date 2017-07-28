package com.kodgames.battleserver.service.battle.region.meizhou.heyuan.score.battle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.score.battle.BattleScoreChecker;

public class BattleScoreChecker_BuGang_HeYuan extends BattleScoreChecker
{

	public BattleScoreChecker_BuGang_HeYuan()
	{
		super(PlayType.OPERATE_BU_GANG_A_CARD);
	}

	@Override
	public void calculate(BattleBean context)
	{
		// 用来存放冷杠的scoredata的索引
		List<Integer> indexList = new ArrayList<>();

		// 用来关联玩家和索引
		Map<Integer, List<Integer>> indexMap = new HashMap<>();

		// 把所有包杠的杠分翻3倍
		for (PlayerInfo player : context.getPlayers().values())
		{
			for (ScoreData scoreData : player.getCards().getScoreDatas())
			{
				for (ScorePoint scorePoint : scoreData.getPoints())
				{
					if (scorePoint.getScoreType() == PlayType.OPERATE_BU_GANG_A_CARD)
					{
						for (Step step : player.getCards().getCardHeap())
						{
							// 能满足补杠又是冷碰的牌就是冷杠的牌
							if (step.getPlayType() == PlayType.OPERATE_LENG_PENG_A_CARD && step.getCards().get(0) == scoreData.getSourceCard())
							{
								// 把冷杠的scoredata的index存起来
								indexList.add(player.getCards().getScoreDatas().indexOf(scoreData));

								// 把index与对应玩家关联起来
								indexMap.put(player.getRoleId(), indexList);
							}
						}
					}
				}
			}
		}

		// 这个是专门删除冷杠的ScoreData (一边查找一边删除是不安全的)
		for (Integer playerId : indexMap.keySet())
		{
			// 有冷杠玩家的index
			List<Integer> lengIndex = indexMap.get(playerId);

			if (lengIndex != null && !lengIndex.isEmpty())
			{
				// 把index降序排序一下，这样从后往前删除，安全的
				Collections.reverse(lengIndex);
			}

			for (int index : lengIndex)
			{
				List<ScoreData> scoreDatas = context.getPlayerById(playerId).getCards().getScoreDatas();
				if (scoreDatas == null || scoreDatas.isEmpty())
				{
					continue;
				}
				scoreDatas.remove(index);
			}

		}
	}
}