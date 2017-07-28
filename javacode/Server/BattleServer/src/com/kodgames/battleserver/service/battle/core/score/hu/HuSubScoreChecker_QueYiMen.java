package com.kodgames.battleserver.service.battle.core.score.hu;

import java.util.HashSet;
import java.util.Set;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;

/**
 * 检测牌型：缺一门
 * 
 * 缺一门：胡牌中缺少一种花色序数牌（筒子、条子、万子）。
 * 
 * 注意：缺两门或者三门时，得分累计。
 */
public class HuSubScoreChecker_QueYiMen extends HuSubScoreChecker
{

	private Set<CardType> checkTypes = new HashSet<>();

	public HuSubScoreChecker_QueYiMen()
	{
		super(PlayType.HU_QUE_YI_MEN);

		checkTypes.add(CardType.WAN);
		checkTypes.add(CardType.TIAO);
		checkTypes.add(CardType.TONG);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		Set<CardType> types = new HashSet<>();
		for (CardType type : checkTypes)
		{
			for (Byte t = type.Value(); t < type.MaxValue(); ++t)
			{
				if (inoutHuContext.allCardCountList[t] > 0)
				{
					types.add(type);
					break;
				}
			}
		}

		int queMen = checkTypes.size() - types.size();
		if (queMen > 0)
		{
			ScorePoint scorePoint = new ScorePoint();
			scorePoint.setScoreType(this.getScoreType());
			scorePoint.setCalcType(this.getModifierType().getValue());
			scorePoint.setScoreValue(this.getModiferScore() * queMen);
			inoutHuContext.scoreData.getPoints().add(scorePoint);

			return true;
		}

		return false;
	}

}
