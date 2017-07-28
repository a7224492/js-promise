package com.kodgames.battleserver.service.battle.region.meizhou.huizhou.score.hu;

import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

public class HuSubScoreChecker_Hu8Hua extends HuSubScoreChecker
{
	public HuSubScoreChecker_Hu8Hua()
	{
		super(PlayType.HU_SUB_8_HUA);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
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

		// 不是8张花牌就不能胡
		if (huaCards.size() != 8)
		{
			return false;
		}

		addScore(inoutHuContext.scoreData);
		return true;
	}
}
