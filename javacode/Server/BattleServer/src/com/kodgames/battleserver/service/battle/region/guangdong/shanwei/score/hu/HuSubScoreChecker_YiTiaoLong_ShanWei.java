package com.kodgames.battleserver.service.battle.region.guangdong.shanwei.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 检测牌型：一条龙（胡牌牌型中有同花色的1-9各一张的至少九张牌）
 */
public class HuSubScoreChecker_YiTiaoLong_ShanWei extends HuSubScoreChecker
{

	public HuSubScoreChecker_YiTiaoLong_ShanWei()
	{
		super(PlayType.HU_YI_TIAO_LONG);
	}

	/**
	 * 一条龙的检测方法,判断手牌中是否包含123456789
	 * 
	 * 重载方法
	 * 
	 * @param context
	 * @param roleId
	 * @param inoutHuContext
	 * @return
	 */
	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 判断万条筒三张牌中是否含有一条龙
		if (!check(inoutHuContext, CardType.WAN) && !check(inoutHuContext, CardType.TIAO) && !check(inoutHuContext, CardType.TONG))
			return false;
		
		
		addScore(inoutHuContext.scoreData);
		return true;
	}
	
	/**
	 * 判断是否检测出一条龙
	 * 
	 * @param inoutHuContext
	 * @param cardType
	 * @return	true:检测一条龙成功
	 * 			false:检测一条龙失败
	 * @see [类、类#方法、类#成员]
	 */
	private boolean check(HuScoreCheckContext inoutHuContext, CardType cardType)
	{
		// 循环便来这个cardType，判断是否每张牌都有，只要有一张牌没有，就证明检测失败
		for (byte card = cardType.Value(); card < cardType.MaxValue(); ++card)
		{
			// 没有牌，检测失败
			if (inoutHuContext.allCardCountList[card] == 0)
				return false;
		}
		
		return true;
	}
	
}
