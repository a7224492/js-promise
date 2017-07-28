package com.kodgames.battleserver.service.battle.core.score.hu;

import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuCardGroup;
import com.kodgames.battleserver.service.battle.core.hu.data.HuCheckerMemoryPool;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;

/**
 * 三鬼胡牌
 * 
 * @author  王海亮
 * @version  [版本号, 2017年4月21日]
 */
public class HuMainScoreChecker_3MasterCard extends HuMainScoreChecker
{
	public HuMainScoreChecker_3MasterCard()
	{
		super(PlayType.HU_SAN_MASTER_CARD);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, byte[] cardCountList, int masterCardCount, List<Byte> supportedCardTypes, HuCheckerMemoryPool pool,
		List<HuScoreCheckContext> outHuContextList)
	{
		List<List<HuCardGroup>> huCardGroupCollector = pool.allocHuCardGroupListList();
		try
		{
			// 检查有多少已经固定的牌形
			int fixedTileCount = 0;
			for (Step step : context.getPlayers().get(roleId).getCards().getCardHeap())
				if (PlayType.isChiPengGang(step.getPlayType()))
					fixedTileCount++;

			if (huChecker.check(context, roleId, cardCountList, fixedTileCount, masterCardCount, supportedCardTypes, pool, huCardGroupCollector, true, 0) == false)
				return false;

			for (List<HuCardGroup> element : huCardGroupCollector)
			{
				HuScoreCheckContext huContext = pool.allocHuScoreCheckContext(element);

				// 添加吃/碰/杠等
				for (Step step : context.getPlayers().get(roleId).getCards().getCardHeap())
				{
					if (PlayType.isChiPengGang(step.getPlayType()) == false)
						continue;

					huContext.scoreData.getCardGroups().add(pool.allocCardGroup(step));
				}

				// 构造胡牌信息用于后续计算
				huContext.createCardCountList();

				context.getPlayers().get(roleId).getCards().getHandCards().forEach(card -> {
					huContext.cardCountList[card]++;
				});

				// 添加分数
				addScore(huContext.scoreData);

				outHuContextList.add(huContext);
			}

			return true;
		}
		finally
		{
			// 回收内存
			pool.deallocListList(huCardGroupCollector);
			huCardGroupCollector = null;
		}
	}
}
