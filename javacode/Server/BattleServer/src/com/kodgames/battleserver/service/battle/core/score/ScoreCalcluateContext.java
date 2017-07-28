package com.kodgames.battleserver.service.battle.core.score;

import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultEventPROTO;

/**
 * 用于计算胡牌分数的辅助类
 */
public class ScoreCalcluateContext
{
	public boolean isHuScore = false;
	public int towIndex = 0;
	public int totalAdd = 0;
	public int totalMulti = 1;
	// 不受封顶影响的乘分
	public int totalMulti2nd = 1;

	public void copyFrom(ScoreCalcluateContext other)
	{
		this.isHuScore = other.isHuScore;
		this.towIndex = other.towIndex;
		this.totalAdd = other.totalAdd;
		this.totalMulti = other.totalMulti;
	}

	private void addScore(int modifyType, int scoreValue)
	{
		switch (ScoreCalculateType.getType(modifyType))
		{
			case TWO_INDEX:
				towIndex += scoreValue;
				break;
			case TOTAL_ADD:
				totalAdd += scoreValue;
				break;
			case TOTAL_MULTI:
				totalMulti *= scoreValue;
				break;
			case TOTAL_MULTI_2ND:
				totalMulti2nd *= scoreValue;
				break;
			default:
				break;
		}
	}

	/**
	 * 算分函数
	 *
	 * 只合并分数, 并不计算乘方
	 * 
	 * @param scoreData 算分数据
	 * @return 计算结果分数
	 */
	public static ScoreCalcluateContext sumScore(ScoreData scoreData)
	{
		ScoreCalcluateContext calcContext = new ScoreCalcluateContext();
		calcContext.isHuScore = PlayType.isHuType(scoreData.getPoints().get(0).getScoreType());
		scoreData.getPoints().forEach((scorePoint) -> {
			calcContext.addScore(scorePoint.getCalcType(), scorePoint.getScoreValue());
		});

		return calcContext;
	}

	/**
	 * 算分函数
	 *
	 * 只合并分数, 并不计算乘方
	 * 
	 * @param scoreData 算分数据
	 * @return 计算结果分数
	 */
	public static ScoreCalcluateContext sumScore(ResultEventPROTO.Builder event)
	{
		ScoreCalcluateContext calcContext = new ScoreCalcluateContext();
		calcContext.isHuScore = PlayType.isHuType(event.getScore().getType());
		event.getSubScoresBuilderList().forEach((subEvent) -> {
			calcContext.addScore(subEvent.getCalcType(), subEvent.getPoint());
		});
		
		return calcContext;
	}
}
