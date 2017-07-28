package com.kodgames.battleserver.service.battle.region.neimeng.baotou.score.hu;

import java.util.List;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;
import com.kodgames.battleserver.service.battle.region.neimeng.common.score.hu.HuSubScoreChecker_DanDiao;
import com.kodgames.battleserver.service.battle.region.neimeng.common.score.hu.HuSubScoreChecker_KanZhang;

/**
 * 牌型算分：坎五魁
 * 
 * 砍五魁，5分。（单口坎、钓的牌为5万）
 */
public class HuSubScoreChecker_KanWuKui extends HuSubScoreChecker
{
	private static HuSubScoreChecker_KanZhang checker_KanZhang = new HuSubScoreChecker_KanZhang();
	private static HuSubScoreChecker_DanDiao checker_DanDiao = new HuSubScoreChecker_DanDiao();

	public HuSubScoreChecker_KanWuKui()
	{
		super(PlayType.HU_KAN_WU_KUI);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 获取所胡的牌
		List<Step> records = context.getRecords();
		Step lastStep = records.get(records.size() - 1);
		List<Byte> lastCards = lastStep.getCards();
		Macro.AssetFalse(lastCards.size() == 1);

		// 最后一张必须是索引为4的万（五万）
		byte card = lastCards.get(0);
		if (!CardType.WAN.isBelongTo(card) || 4 != CardType.convertToCardIndex(card))
			return false;

		// 符合坎张即为坎五魁
		if (checker_KanZhang.check(context, roleId, inoutHuContext))
		{
			addScore(inoutHuContext.scoreData);
			return true;
		}

		// 符合单钓即为坎五魁
		if (checker_DanDiao.check(context, roleId, inoutHuContext))
		{
			addScore(inoutHuContext.scoreData);
			return true;
		}

		return false;
	}
}
