package com.kodgames.battleserver.service.battle.region.yangzhou.score.hu;

import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalculateType;
import com.kodgames.battleserver.service.battle.core.score.hu.HuScoreProcessor;
import com.kodgames.battleserver.service.battle.region.yangzhou.Rules_YangZhou;

public class HuScoreProcessor_YangZhou extends HuScoreProcessor
{
	/** 计算当前胡牌分数, 产生一个ScoreData保存在PlayerInfo中
	 * 
	 * @param context 牌局上下文
	 * @param roleId 检测胡牌的玩家Id
	 * @param huCard 要胡的Card
	 * @param isSelfOperation 当前胡牌行为是否
	 * @param checkTing 当前是检测听牌
	 * @return 如果有分数返回分数数据, 如果没有返回null
	 */
	@Override
	public ScoreData process(BattleBean context, int roleId, byte huCard, boolean checkTing)
	{
		ScoreData resultScore = super.process(context, roleId, huCard, checkTing);
		
		//添加配子和搬子加的分数
		if(resultScore != null && resultScore.getPoints().size() > 0)
		{
			List<Integer> rules = context.getGameRules();
			if(rules.contains(Rules_YangZhou.GAME_PLAY_BAN_PEI_ZI) || rules.contains(Rules_YangZhou.GAME_PLAY_BAI_BAN_PEI_ZI))
			{
				// 确定手牌中配子的数量
				int peiZiNum = 0;
				byte peiZi = context.getCardPool().getMasterCards().get(0);
				PlayerInfo playerInfo = context.getPlayerById(roleId);
				for(byte handCard : playerInfo.getCards().getHandCards())
				{
					if(handCard == peiZi)
						peiZiNum++;
				}
				
				// 添加配子分数
				if(peiZiNum >= 3)
				{
					int peiZiScore = (peiZiNum == 3 ? 2 : 4);
					ScorePoint scorePoint = new ScorePoint();
					scorePoint.setScoreType(PlayType.DISPLAY_PEI_ZI);
					scorePoint.setScoreValue(peiZiScore);
					scorePoint.setCalcType(ScoreCalculateType.TOTAL_MULTI.getValue());
					resultScore.getPoints().add(scorePoint);
				}
				
				if(!context.getGameRules().contains(Rules_YangZhou.GAME_PLAY_BAI_BAN_PEI_ZI))
				{
					int banZiNum = 0;
					byte banZi = CardType.getPreCard(peiZi, true);	
					for(byte handCard : playerInfo.getCards().getHandCards())
					{
						if(handCard == banZi)
							peiZiNum++;
					}
					
					// 添加搬子分数
					if(banZiNum >= 3)
					{
						int banZiScore = (banZiNum == 3 ? 2 : 4);
						ScorePoint scorePoint = new ScorePoint();
						scorePoint.setScoreType(PlayType.DISPLAY_BAN_ZI);
						scorePoint.setScoreValue(banZiScore);
						scorePoint.setCalcType(ScoreCalculateType.TOTAL_MULTI.getValue());
						resultScore.getPoints().add(scorePoint);
					}
				}
			}
		}
		
		return resultScore;
	}
}
