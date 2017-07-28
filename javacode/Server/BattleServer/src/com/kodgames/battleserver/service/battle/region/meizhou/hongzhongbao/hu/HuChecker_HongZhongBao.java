package com.kodgames.battleserver.service.battle.region.meizhou.hongzhongbao.hu;

import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.check.CheckHelper;
import com.kodgames.battleserver.service.battle.core.hu.HuCheckerBase;
import com.kodgames.battleserver.service.battle.core.hu.data.HuCardGroup;
import com.kodgames.battleserver.service.battle.core.hu.data.HuCheckerMemoryPool;
import com.kodgames.battleserver.service.battle.core.hu.filter.CardGroupsFilter;

public class HuChecker_HongZhongBao extends HuCheckerBase
{

	public HuChecker_HongZhongBao()
	{
		super(PlayType.HU_MAIN_HONG_ZHONG_BAO);
	}

	@Override
	public boolean check(BattleBean context, byte[] cardCountArray, int fixedTileCount, int masterCardCount, List<Byte> supportedCardTypes, HuCheckerMemoryPool pool,
		List<List<HuCardGroup>> outHuCardGroupCollector, boolean fillMasterCard, CardGroupsFilter cardGroupCheck, int finishGroupsCount)
	{
		// 得到最后一步操作
		Step lastStep = context.getLastRecordStep(0);

		// 不是抓牌就跳过
		if (lastStep == null || lastStep.getPlayType() != PlayType.OPERATE_DEAL)
		{
			return false;
		}

		// 只进行过抓牌
		if (context.getPlayers().get(lastStep.getRoleId()).getCards().getRecordIndices().size() > 1)
		{
			return false;
		}

		// 得到手牌中是红中
		CardInfo cardInfo = context.getPlayerById(lastStep.getRoleId()).getCards();
		byte[] handArray = CheckHelper.converToCardCountArray(cardInfo);

		// 获取手牌中红中的张数
		int hongZhongCard = handArray[CardType.JIAN.Value()];

		// 获取手牌中红中的张数
		// int hongZhongCard = cardCountArray[CardType.JIAN.Value()];
		if (hongZhongCard != 4)
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
