package com.kodgames.battleserver.service.battle.region.meizhou.huizhou.score.hu;

import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

public class HuSubScoreChecker_Hu7Hua extends HuSubScoreChecker
{
	public HuSubScoreChecker_Hu7Hua()
	{
		super(PlayType.HU_SUB_7_HUA);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 得到最后一步操作
		Step lastStep = context.getLastRecordStep(0);

		// 得到花牌列表
		List<Byte> huaCards = new ArrayList<>();

		if (lastStep == null || lastStep.getRoleId() == roleId || lastStep.getPlayType() != PlayType.OPERATE_BU_GANG_A_CARD)
			return false;

		// 七花胡自摸
		if (lastStep != null && lastStep.getPlayType() == PlayType.OPERATE_DEAL)
		{
			// 得到花牌
			CardInfo cardInfo = context.getPlayerById(lastStep.getRoleId()).getCards();
			huaCards = cardInfo.getExCards();

			// 不是7张花牌就不能胡
			if (huaCards.size() != 7)
			{
				return false;
			}
		}
		// 七花胡可以吃胡
		else if (lastStep != null && lastStep.getRoleId() != roleId)
		{
			// 得到花牌
			CardInfo cardInfo = context.getPlayerById(roleId).getCards();
			huaCards = cardInfo.getExCards();

			// 不是7张花牌就不能胡
			if (huaCards.size() != 7)
			{
				return false;
			}
		}

		addScore(inoutHuContext.scoreData);
		return true;
	}
}
