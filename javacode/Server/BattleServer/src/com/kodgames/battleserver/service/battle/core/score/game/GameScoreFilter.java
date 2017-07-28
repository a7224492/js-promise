package com.kodgames.battleserver.service.battle.core.score.game;

import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.creator.ICreateContextHandler;

import net.sf.json.JSONObject;

/**
 * 房间结算中需要显示项目的过滤器
 * 
 * 房间结算界面, 总共需要显示那些项的计分, 通过这个类, 在GameScoreCalculator中设置
 */
public class GameScoreFilter implements ICreateContextHandler
{
	public static final String KEY_SCORE_TYPE = "scoreType";
	public static final String KEY_ADD_OPERATION = "addOperation";
	public static final String KEY_CALCULATE_SCORE_POINT = "calculateScorePoint";

	/**
	 * 需要统计的计分类型
	 */
	public int scoreType;

	/**
	 * 需要统计计分类型的应用方式(主动还是被动获取得分)
	 */
	public boolean addOperation;

	/**
	 * 是否计算分数
	 */
	public boolean calculateScorePoint;

	public static GameScoreFilter create(JSONObject context)
		throws Exception
	{
		// 反射构造实例
		GameScoreFilter instance = new GameScoreFilter();

		// 初始化实例
		if (instance != null)
			instance.createFromContext(context);

		return instance;
	}

	public void createFromContext(JSONObject context)
		throws Exception
	{
		scoreType = CreateContextParser.getInt(context, KEY_SCORE_TYPE);
		addOperation = CreateContextParser.getBoolean(context, KEY_ADD_OPERATION);
		calculateScorePoint = CreateContextParser.getBoolean(context, KEY_CALCULATE_SCORE_POINT);
	}
}