package com.kodgames.battleserver.service.battle.region.guangdong.chaozhou.score.battle.filter;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.score.battle.filter.BattleScoreFilter;

/**
 * 
 * <一句话功能简述>
 * 潮州麻将补杠分修改为收取被碰的那一家
 * <功能详细描述>
 * 循环玩家是否有补杠分，清空分的收分目标，找到碰的这一步，修改收分目标为被碰的人
 * 
 * @author  姓名		毛建伟
 * @version  [版本号, 2017年1月5日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class BattleScoreFilter_BuGang extends BattleScoreFilter
{

	@Override
	public void filter(BattleBean context)
	{
		// 循环玩家
		for (PlayerInfo player : context.getPlayers().values())
		{
			// 循环玩家的scoreData
			for (ScoreData scoreData : player.getCards().getScoreDatas())
			{
				// 是否为补杠的分，不是就退出
				if (scoreData.getPoints().get(0).getScoreType() != PlayType.OPERATE_BU_GANG_A_CARD)
					continue;
				
				// 清空玩家的收分目标
				scoreData.getScoreTargetList().clear();
				
				// 碰的是哪一张牌
				byte pengCard = scoreData.getSourceCard();
				
				// record的size
				int size = context.getRecords().size();
				
				// 循环record获取碰操作
				for (int i = 0; i < size; ++i)
				{
					Step step = context.getRecords().get(i);
					
					// 是碰操作并且是碰的这一张牌（一张牌只可能有一个碰，不算错误的配牌）
					if (step.getPlayType() == PlayType.OPERATE_PENG_A_CARD && step.getCards().get(0) == pengCard)
					{
						// 添加上一步的roleId到scoreTargetList中（碰的前一步一定是打牌，否则就是牌局碰逻辑错误了）
						scoreData.getScoreTargetList().add(context.getRecords().get(i - 1).getRoleId());
						break;
					}
				}
			}
		}
	}
	
}
