package com.kodgames.battleserver.service.battle.region.guangdong.shanwei.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardGroup;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.CardGroupType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalculateType;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 汕尾检测风牌分
 */
public class HuSubScoreChecker_FengKe extends HuSubScoreChecker
{

	public HuSubScoreChecker_FengKe()
	{
		super(PlayType.DISPLAY_FENG_JIA_FAN);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 胡牌玩家信息
		PlayerInfo player = context.getPlayerById(roleId);
		// 获取正风牌
		byte zhengFengCard = (byte)(CardType.FENG.Value() + player.getPosition() - 1);
		// 风刻（杠）计数
		int fengKeCount = 0;
		// 正风刻（杠）计数
		int zhengFengKeCount = 0;
		// 创建一个新的scoreData

		// 判断玩家是否有风刻，凤杠
		for (CardGroup cardGroup : inoutHuContext.scoreData.getCardGroups())
		{
			// 是否为杠，刻，并且是风牌
			if ((CardGroupType.isKe(cardGroup.getGroupType()) || CardGroupType.isGang(cardGroup.getGroupType())) && CardType.FENG == CardType.getFengJianType(cardGroup.getCardList().get(0)))
			{
				// 风刻+1
				++fengKeCount;
				// 是否为正风的刻或杠
				if (cardGroup.getCardList().get(0) == zhengFengCard)
					++zhengFengKeCount;
			}
		}
		
		if (fengKeCount == 0)
			return false;

		ScorePoint point = new ScorePoint();
		point.setCalcType(ScoreCalculateType.TOTAL_ADD.getValue());
		point.setScoreType(PlayType.DISPLAY_FENG_JIA_FAN);
		point.setScoreValue(zhengFengKeCount * 2 + (fengKeCount - zhengFengKeCount));

		inoutHuContext.scoreData.getPoints().add(point);
		return true;
	}

}
