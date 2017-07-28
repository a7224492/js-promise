package com.kodgames.battleserver.service.battle.core.score.hu;

import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.BattleHelper;
import com.kodgames.battleserver.service.battle.core.hu.HuChecker;
import com.kodgames.battleserver.service.battle.core.hu.HuCheckerBase;
import com.kodgames.battleserver.service.battle.core.hu.data.HuCardGroup;
import com.kodgames.battleserver.service.battle.core.hu.data.HuCheckerMemoryPool;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;

public abstract class HuMainScoreChecker extends HuScoreChecker
{
	protected HuCheckerBase huChecker;

	protected HuMainScoreChecker(int scoreType)
	{
		super(scoreType);

		HuChecker huChecker = BattleHelper.getInstance().getHuCheckProcessor();
		this.huChecker = huChecker.getCheckerByScoreType(scoreType);
	}

	/**
	 * 判断是否可以胡牌
	 * 
	 * 注意, 函数有可能是由于听牌检测引起, 如果使用record来判断前置操作, 不能用于检测听牌 , 需要重载skipCheckTing()
	 * 
	 * @param context 战斗上线文
	 * @param roleId 要检测的玩家Id
	 * @param cardCountList 当前手牌数量数组(不包括吃碰杠等)
	 * @param masterCardCount 当前有多少张万能牌
	 * @param supportedCardTypes 当前牌局支持那些牌类型
	 * @param outHuContextList 用于返回所有可胡牌形
	 * @return 是否可以胡牌
	 * @see [类、类#方法、类#成员]
	 */
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