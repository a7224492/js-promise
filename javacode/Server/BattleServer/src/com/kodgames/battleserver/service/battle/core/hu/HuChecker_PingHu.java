package com.kodgames.battleserver.service.battle.core.hu;

import java.util.List;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuCardGroup;
import com.kodgames.battleserver.service.battle.core.hu.data.HuCheckerMemoryPool;
import com.kodgames.battleserver.service.battle.core.hu.filter.CardGroupsFilter;

import net.sf.json.JSONObject;

/**
 * 平胡
 */
public class HuChecker_PingHu extends HuCheckerBase
{
	// 是否允许风箭成KAN
	public static final String KEY_ENABLE_FENG_JIAN = "enableFengJian";
	protected boolean enableFengJianAnKan = false;

	public HuChecker_PingHu()
	{
		super(PlayType.HU_PING_HU);
	}

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		super.createFromContext(context);
		if (context.containsKey(KEY_ENABLE_FENG_JIAN))
			enableFengJianAnKan = context.getBoolean(KEY_ENABLE_FENG_JIAN);
	}

	@Override
	public int getTotalTileCount(BattleBean context)
	{
		int playCardCount = context.getCardPool().getPlayCardCount() + 1;
		int tileCount = 0;

		// 平胡需要一组将牌
		playCardCount -= 2;
		tileCount++;

		tileCount += playCardCount / 3;
		return tileCount;
	}

	@Override
	public boolean check(BattleBean context, byte[] cardCountArray, int fixedTileCount, int masterCardCount, List<Byte> supportedCardTypes, HuCheckerMemoryPool pool,
		List<List<HuCardGroup>> outHuCardGroupCollector, boolean fillMasterCard, CardGroupsFilter cardGroupsFilter, int finishGroupsCount)
	{
		List<List<HuCardGroup>> outHuCardGroups = outHuCardGroupCollector != null ? pool.allocHuCardGroupListList() : null;

		try
		{
			// 执行检测
			int totalTileCount = getTotalTileCount(context);
			if (checkPinHu(totalTileCount,
				cardCountArray,
				fixedTileCount,
				supportedCardTypes,
				pool,
				outHuCardGroups,
				cardGroupsFilter,
				finishGroupsCount,
				enableFengJianAnKan,
				masterCardCount) == false)
				return false;

			if (outHuCardGroupCollector != null)
			{
				// 需要输出牌形
				for (List<HuCardGroup> list : outHuCardGroups)
					if (fillMasterCard)
						// 填充万能牌, 生成胡牌牌形
						fillMasterCard(list, masterCardCount, supportedCardTypes, pool, outHuCardGroupCollector, enableFengJianAnKan);
					else
						// 直接输出可胡牌形
						generateOutputCardGroups(list, pool, outHuCardGroupCollector);
			}

			return true;
		}
		finally
		{
			// System.out.println("Time Elapse:HuChecker_PingHu.check " + (System.currentTimeMillis() - checkTime));
			// 回收内存
			pool.deallocListList(outHuCardGroups);
			outHuCardGroups = null;
		}
	}

	/**
	 * 检测平胡算法
	 * 
	 * 將cardCountArray中的牌填充到33332构成的组中
	 */
	private static boolean checkPinHu(int tileTotalCount, byte[] cardCountArray, int fixedTileCount, List<Byte> supportedCardTypes, HuCheckerMemoryPool pool,
		List<List<HuCardGroup>> outHuCardGroupCollector, CardGroupsFilter cardGroupsFilter, int finishGroupsCount, boolean enableFengJianAnKan, int masterCardCount)
	{
		// 检测所有坎刻将组合
		for (int keCount = 0; keCount < tileTotalCount - fixedTileCount; ++keCount)
		{
			List<HuCardGroup> cardGroupList = pool.allocHuCardGroupList();
			cardGroupList.add(pool.allocHuCardGroup(CardGroupType.JIANG, (byte)0, (byte)0));
			for (int i = 0; i < tileTotalCount - fixedTileCount - 1; ++i)
			{
				if (i < keCount)
					cardGroupList.add(pool.allocHuCardGroup(CardGroupType.AN_KAN, (byte)0, (byte)0, (byte)0));
				else
					cardGroupList.add(pool.allocHuCardGroup(CardGroupType.AN_KE, (byte)0, (byte)0, (byte)0));
			}

			// long checkTime = System.currentTimeMillis();

			try
			{
				if (doCheckCardGroups(cardCountArray,
					supportedCardTypes,
					cardGroupList,
					pool,
					outHuCardGroupCollector,
					cardGroupsFilter,
					finishGroupsCount,
					enableFengJianAnKan,
					masterCardCount) == false)
					// 没有胡牌继续检测
					continue;

				// 检测到了胡牌
				if (outHuCardGroupCollector == null)
					// 只用于判断是否可胡, 不再继续检测
					return true;

				Macro.AssetFalse(outHuCardGroupCollector.size() != 0);
			}
			finally
			{
				// 回收内存
				pool.deallocList(cardGroupList);
				cardGroupList = null;
			}

		}

		if (outHuCardGroupCollector != null)
			return outHuCardGroupCollector.size() != 0;
		else
			return false;
	}
}
