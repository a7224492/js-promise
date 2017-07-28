package com.kodgames.battleserver.service.battle.region.guangdong.shanwei.score.battle;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.score.battle.BattleScoreChecker;

/**
 * 汕尾检测花分
 */
public class BattleScoreChecker_Hua_ShanWei extends BattleScoreChecker
{

	public BattleScoreChecker_Hua_ShanWei()
	{
		super(PlayType.DISPLAY_HUA_JIA_FAN);
	}

	/**
	 * 计算花牌的加分
	 * 
	 * 重载方法
	 * 
	 * @param context
	 */
	@Override
	public void calculate(BattleBean context)
	{
		// 只有胡牌玩家计算花牌得分
		for (int huIndex : context.getHuIndex())
		{
			// 胡牌步数
			Step huStep = context.getRecords().get(huIndex);
			// 胡牌玩家信息
			PlayerInfo player = context.getPlayerById(huStep.getRoleId());
			// 正花有两个
			int zhengHuaCard1 = CardType.HUA.Value() + player.getPosition() - 1;
			int zhengHuaCard2 = CardType.HUA.Value() + player.getPosition() + 4 - 1;
			// 玩家的花牌数
			int huaCount = player.getCards().getExCards().size();
			// 正花个数
			int zhengHuaCount = 0;
			// 判断正花有几个
			for (byte card : player.getCards().getExCards())
			{
				if (zhengHuaCard1 == card || zhengHuaCard2 == card)
					++zhengHuaCount;
			}
			// 如果没有花牌
			if (huaCount == 0)
				continue;

			// 添加一个花牌的scoreData
			ScoreData huaData = new ScoreData();
			huaData.setSourceId(huIndex);
			huaData.setAddOperation(true);
			huaData.setSourceRecrodIndex(-1);

			// 遍历玩家的分数列表
			for (ScoreData scoreData : player.getCards().getScoreDatas())
			{
				// 是否为胡牌类型,花牌分也是一种胡牌分，所以直接用胡牌的scoreData，并删除掉胡牌的其他分值
				if (PlayType.isHuType(scoreData.getPoints().get(0).getScoreType()))
				{
					huaData.copyFrom(scoreData);
				}
			}
			
			huaData.getPoints().removeIf(point ->{
				if (point.getScoreType() == PlayType.HU_QIANG_GANG_HU || point.getScoreType() == PlayType.DISPLAY_BETTING_HOUSE)
					return false;
				
				return true;
			});
			
			{
				// 设置花牌分值
				ScorePoint valuePoint = new ScorePoint();
				valuePoint.setCalcType(this.calculateType.getValue());
				valuePoint.setScoreType(this.scoreType);
				valuePoint.setScoreValue(zhengHuaCount * 2 + (huaCount - zhengHuaCount));
				// 添加到scoreData
				huaData.getPoints().add(0, valuePoint);
			}
			
			// 添加到玩家身上
			player.getCards().getScoreDatas().add(huaData);
		}
	}

}
