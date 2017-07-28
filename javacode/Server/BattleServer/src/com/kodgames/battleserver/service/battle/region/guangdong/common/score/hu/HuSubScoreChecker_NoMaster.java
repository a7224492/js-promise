package com.kodgames.battleserver.service.battle.region.guangdong.common.score.hu;

import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 胡牌的时候如果没有万能牌, 分数加倍
 */
public class HuSubScoreChecker_NoMaster extends HuSubScoreChecker
{
	public HuSubScoreChecker_NoMaster()
	{
		super(PlayType.DISPLAY_NO_MASTER_CARD);
	}
	
	/**
	 * 在检测ting牌的情况是否要忽略这个分数
	 */
	public boolean skipCheckTing()
	{
		return true;
	}

	/**
	 * 计算分数, 结果保存到相应的player身上
	 */
	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{

		PlayerInfo huPlayer = context.getPlayers().get(roleId);
		List<Byte> masterCards = context.getPlayerMasterCards(roleId);

		if (masterCards != null)
		{
			// 检查玩家不包括吃碰杠的牌中是否有鬼牌
			for (byte card : huPlayer.getCards().getHandCards())
				if (masterCards.contains(card))
					return false;
		}

		addScore(inoutHuContext.scoreData);
		return true;

	}
}
