package com.kodgames.battleserver.service.battle.core.score.battle;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.creator.ICreateContextHandler;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalculateType;

import net.sf.json.JSONObject;

/**
 * 战斗分数计算器基类
 */
public abstract class BattleScoreChecker implements ICreateContextHandler
{
	public static final String KEY_CALCULATE_TYPE = "calculateType";
	public static final String KEY_SCORE_VALUE = "scoreValue";

	protected int scoreType;
	protected ScoreCalculateType calculateType;
	protected int scoreValue;

	/**
	 * 构造一个ScorePointCaculator实例
	 */
	public static BattleScoreChecker create(JSONObject context)
		throws Exception
	{
		// 反射构造实例
		BattleScoreChecker instance = CreateContextHelper.instantiateClass(context, BattleScoreChecker.class);
		instance.createFromContext(context);
		return instance;
	}

	protected BattleScoreChecker(int scoreType)
	{
		this.scoreType = scoreType;
	}

	public void createFromContext(JSONObject context)
		throws Exception
	{
		// 解析参数
		calculateType = CreateContextParser.getScoreCalculateType(context, KEY_CALCULATE_TYPE);
		scoreValue = CreateContextParser.getInt(context, KEY_SCORE_VALUE);
	}

	/**
	 * 向inoutScoreList中添加分数
	 */
	protected void addScore(ScoreData inoutScoreData)
	{
		// 构造ScoreData
		ScorePoint scorePoing = new ScorePoint();
		scorePoing.setScoreType(this.scoreType);
		scorePoing.setCalcType(this.calculateType.getValue());
		scorePoing.setScoreValue(this.scoreValue);
		inoutScoreData.getPoints().add(scorePoing);
	}

	/**
	 * 计算分数, 结果保存到相应的player身上
	 */
	public abstract void calculate(BattleBean context);
}