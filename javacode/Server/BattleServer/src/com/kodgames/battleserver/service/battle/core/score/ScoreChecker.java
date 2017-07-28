package com.kodgames.battleserver.service.battle.core.score;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.creator.ICreateContextHandler;

import net.sf.json.JSONObject;

/**
 * 分数算器
 */
public abstract class ScoreChecker implements ICreateContextHandler
{
	public static final String KEY_MODIFIER_TYPE = "modifierType";
	public static final String KEY_MODIFIER_SCORE = "modifierScore";

	final static Logger logger = LoggerFactory.getLogger(ScoreChecker.class);

	private int scoreType;
	private ScoreCalculateType modifierType;
	private int modifierScore;

	/**
	 * <默认构造函数>
	 */
	protected ScoreChecker(int scoreType)
	{
		this.scoreType = scoreType;
	}

	public int getScoreType()
	{
		return scoreType;
	}

	public ScoreCalculateType getModifierType()
	{
		return this.modifierType;
	}

	public int getModiferScore()
	{
		return this.modifierScore;
	}

	public void createFromContext(JSONObject context)
		throws Exception
	{
		// 解析参数
		modifierType = CreateContextParser.getScoreCalculateType(context, KEY_MODIFIER_TYPE);
		modifierScore = CreateContextParser.getInt(context, KEY_MODIFIER_SCORE);
	}

	/**
	 * 向inoutScoreList中添加分数
	 */
	protected void addScore(ScoreData inoutScoreData)
	{
		// 构造ScoreData
		ScorePoint scorePoing = new ScorePoint();
		scorePoing.setScoreType(this.scoreType);
		scorePoing.setCalcType(this.modifierType.getValue());
		scorePoing.setScoreValue(this.modifierScore);
		inoutScoreData.getPoints().add(scorePoing);
	}

	// /**
	// * 计算得分
	// *
	// * @param context 战斗上下文
	// * @param roleId 需要计算的玩家Id
	// * @param inoutHuContext 计算结果存入的结构
	// * @return 如果产生结果, 返回true, 同时将结果存入inoutHuContext
	// * @see [类、类#方法、类#成员]
	// */
	// public abstract boolean calculate(BattleBean context, int roleId, HuCaculateContext inoutHuContext);
}