package com.kodgames.battleserver.service.battle.region.neimeng.common.score.hu;

import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardGroup;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.check.CheckHelper;
import com.kodgames.battleserver.service.battle.core.hu.CardGroupType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 检测牌型：单钓
 * 
 * 将钓单张牌作将成胡。1分。
 */
public class HuSubScoreChecker_DanDiao extends HuSubScoreChecker
{

	public HuSubScoreChecker_DanDiao()
	{
		super(PlayType.HU_DAN_DIAO);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		boolean result = check(context, roleId, inoutHuContext);
		if (result)
			addScore(inoutHuContext.scoreData);

		return result;
	}

	/**
	 * 只检查，不算分
	 */
	public boolean check(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 最后一步操作只能有一张牌
		List<Step> records = context.getRecords();
		Step lastStep = records.get(records.size() - 1);
		List<Byte> lastCards = lastStep.getCards();
		if (1 != lastCards.size())
			return false;

		// 只能胡单张
		List<Byte> handCards = context.getPlayers().get(roleId).getCards().getHandCards();
		boolean isZiMo = roleId == lastStep.getRoleId();
		if (isZiMo)
			handCards.remove(lastCards.get(0));
		List<Byte> tingCards = CheckHelper.getTingCards(context, roleId);
		if (isZiMo)
			handCards.add(lastCards.get(0));
		if (1 != tingCards.size())
			return false;

		byte card = lastCards.get(0);
		List<CardGroup> cardGroups = inoutHuContext.scoreData.getCardGroups();
		for (CardGroup group : cardGroups)
		{
			if (CardGroupType.JIANG == group.getGroupType() && group.getCardList().get(0) == card)
				return true;
		}

		return false;
	}

}
