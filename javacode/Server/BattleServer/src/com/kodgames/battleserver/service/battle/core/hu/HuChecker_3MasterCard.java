package com.kodgames.battleserver.service.battle.core.hu;

import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuCardGroup;
import com.kodgames.battleserver.service.battle.core.hu.data.HuCheckerMemoryPool;
import com.kodgames.battleserver.service.battle.core.hu.filter.CardGroupsFilter;

/**
 * 手牌中有三张万能牌直接胡
 * 
 * @author  王海亮
 * @version  [版本号, 2017年4月21日]
 */
public class HuChecker_3MasterCard extends HuCheckerBase
{
	public HuChecker_3MasterCard()
	{
		super(PlayType.HU_SAN_MASTER_CARD);
	}
	
	@Override
	public boolean check(BattleBean context, byte[] cardCountArray, int fixedTileCount, int masterCardCount,
			List<Byte> supportedCardTypes, HuCheckerMemoryPool pool, List<List<HuCardGroup>> outHuCardGroupCollector,
			boolean fillMasterCard, CardGroupsFilter cardGroupCheck, int finishGroupsCount) 
	{
		if (masterCardCount < 3)
			return false;

		if (outHuCardGroupCollector != null)
		{
			// 收集胡牌牌形, 三鬼胡牌不计牌形
			outHuCardGroupCollector.add(pool.allocHuCardGroupList());
		}

		return true;
	}
}
