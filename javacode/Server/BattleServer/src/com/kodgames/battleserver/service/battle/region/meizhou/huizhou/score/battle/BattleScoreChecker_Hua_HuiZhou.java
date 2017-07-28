package com.kodgames.battleserver.service.battle.region.meizhou.huizhou.score.battle;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.score.battle.BattleScoreChecker;

/**
 * 非鸡胡的牌型胡牌则每张花牌可以带来额外的加分。<br>
 * 鸡胡胡牌则不计花牌加分。
 */
public class BattleScoreChecker_Hua_HuiZhou extends BattleScoreChecker
{
	public BattleScoreChecker_Hua_HuiZhou()
	{
		super(PlayType.DISPLAY_EX_CARD);
	}

	@Override
	public void calculate(BattleBean context)
	{
		// 财神牌（花牌）计分
		context.getHuIndex().forEach(huIndex -> {

			// 胡的牌型种类
			int huTypeCount = 0;

			// 是否是平胡
			boolean hasPingHu = false;

			// 是否是抢杠胡
			boolean hasQiangGangHu = false;
			Step huInfo = context.getRecords().get(huIndex);
			ScoreData huScoreData = context.getScoreData(huInfo.getRoleId(), huIndex);

			for (ScorePoint huPoint : huScoreData.getPoints())
			{
				if (PlayType.isHuType(huPoint.getScoreType()))
					huTypeCount++;

				if (huPoint.getScoreType() == PlayType.HU_PING_HU)
					hasPingHu = true;

				if (huPoint.getScoreType() == PlayType.HU_QIANG_GANG_HU)
					hasQiangGangHu = true;
			}

			// 鸡胡牌型不加花牌分，抢杠胡的鸡胡也不加花牌分。
			if (hasPingHu)
			{
				if (1 == huTypeCount)
				{
					return;
				}
				else if (2 == huTypeCount)
				{
					if (hasQiangGangHu)
						return;
				}
			}

			int huRoleId = huInfo.getRoleId();
			PlayerInfo huRole = context.getPlayers().get(huRoleId);
			int count = huRole.getCards().getExCards().size();
			if (0 == count)
				return;

			ScorePoint point = new ScorePoint();
			point.setScoreType(this.scoreType);
			point.setScoreValue(this.scoreValue * count);
			point.setCalcType(this.calculateType.getValue());

			huRole.getCards().getScoreDatas().stream().forEach(scoreData -> {

				// scoreData里面有杠分有胡牌分，让胡牌分进入下面判断
				if (PlayType.isHuType(scoreData.getPoints().get(0).getScoreType()))
					scoreData.getPoints().add(point);
			});

		});
	}
}
