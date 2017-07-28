package com.kodgames.battleserver.service.battle.core.hu;

import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuCardGroup;
import com.kodgames.battleserver.service.battle.core.hu.data.HuCheckerMemoryPool;
import com.kodgames.battleserver.service.battle.core.hu.filter.CardGroupsFilter;

/**
 * 4张万能牌直接胡
 */
public class HuChecker_4MasterCard extends HuCheckerBase
{
	public HuChecker_4MasterCard()
	{
		super(PlayType.HU_SI_MASTER_CARD_HU);
	}

	@Override
	public boolean check(BattleBean context, byte[] cardCountArray, int fixedTileCount, int masterCardCount, List<Byte> supportedCardTypes, HuCheckerMemoryPool pool,
		List<List<HuCardGroup>> outHuCardGroupCollector, boolean fillMasterCard, CardGroupsFilter cardGroupsFilter, int finishGroupsCount)
	{
		if (masterCardCount < 4)
			return false;

		if (outHuCardGroupCollector != null)
		{
			// 收集胡牌牌形, 四鬼胡牌不计牌形
			outHuCardGroupCollector.add(pool.allocHuCardGroupList());
		}

		return true;
	}
}
