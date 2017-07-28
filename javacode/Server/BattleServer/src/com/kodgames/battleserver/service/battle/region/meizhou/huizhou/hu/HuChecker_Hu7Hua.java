package com.kodgames.battleserver.service.battle.region.meizhou.huizhou.hu;

import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.HuCheckerBase;
import com.kodgames.battleserver.service.battle.core.hu.data.HuCardGroup;
import com.kodgames.battleserver.service.battle.core.hu.data.HuCheckerMemoryPool;
import com.kodgames.battleserver.service.battle.core.hu.filter.CardGroupsFilter;

/**
 * 惠州7张花牌提示胡牌
 */
public class HuChecker_Hu7Hua extends HuCheckerBase
{

	public HuChecker_Hu7Hua()
	{
		super(PlayType.HU_MAIN_7_HUA);
	}

	@Override
	public boolean check(BattleBean context, byte[] cardCountArray, int fixedTileCount, int masterCardCount, List<Byte> supportedCardTypes, HuCheckerMemoryPool pool,
		List<List<HuCardGroup>> outHuCardGroupCollector, boolean fillMasterCard, CardGroupsFilter cardGroupCheck, int finishGroupsCount)
	{
		// 得到最后一步操作
		Step lastStep = context.getLastRecordStep(0);

		// 得到花牌列表
		List<Byte> huaCards = new ArrayList<>();

		// 不是抓牌就跳过
		if (lastStep == null || lastStep.getPlayType() != PlayType.OPERATE_DEAL)
		{
			return false;
		}

		// 得到花牌
		CardInfo cardInfo = context.getPlayerById(lastStep.getRoleId()).getCards();
		huaCards = cardInfo.getExCards();

		// 不是7张花牌就不能胡
		if (huaCards.size() != 7)
		{
			return false;
		}

		if (outHuCardGroupCollector != null)
		{
			// 收集胡牌牌形
			outHuCardGroupCollector.add(pool.allocHuCardGroupList());
		}

		return true;
	}

}
