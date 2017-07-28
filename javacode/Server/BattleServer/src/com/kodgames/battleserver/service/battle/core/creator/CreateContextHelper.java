package com.kodgames.battleserver.service.battle.core.creator;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationFilter_LastOperator;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalculateType;
import com.kodgames.battleserver.service.battle.core.score.ScoreChecker;
import com.kodgames.battleserver.service.battle.core.score.battle.BattleScoreChecker;
import com.kodgames.battleserver.service.battle.core.score.filter.ScorePointFilter_LastOperator;
import com.kodgames.battleserver.service.battle.core.score.game.GameScoreFilter;
import com.kodgames.battleserver.service.battle.core.score.gang.GangScoreChecker;
import com.kodgames.battleserver.service.battle.core.score.hu.HuScoreChecker;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * CreatingContext的JSon结构的辅助解析函数
 * 
 * 所有出错情况都以ParseCreatingContextException返回
 */
public class CreateContextHelper
{
	public static final String KEY_CLASS_NAME = "className";

	/**
	 * 创建JSONObject, 不包含任何元素
	 */
	public static JSONObject createObject()
	{
		return new JSONObject();
	}

	/**
	 * 创建JSONObject, 并添加key为MahjongConstant.JSON_NAME, 值为name的元素
	 * 
	 * 主要用于构造具有特定名称的子类型
	 */
	public static JSONObject createObject(String name)
	{
		JSONObject object = new JSONObject();
		if (name != null)
			object.element(CreateContextHelper.KEY_CLASS_NAME, name);

		return object;
	}

	/**
	 * 创建JSONObject, 并添加key为MahjongConstant.JSON_NAME, 值为class simple name的元素
	 */
	public static JSONObject createObject(Class<?> c)
	{
		return createObject(c.getName());
	}

	/**
	 * 创建JSONArray
	 */
	public static JSONArray createArray()
	{
		return new JSONArray();
	}

	/**
	 * 构造JSONObject, 用于创建GameScoreFilter或者其派生类
	 */
	public static JSONObject createGameScoreFilter(Class<?> c, int scoreType, boolean addOperation, boolean calculateScorePoint)
	{
		// 强制要求c从是GameScoreCalculator或者派生于GameScoreCalculator
		Macro.AssetFalse(GameScoreFilter.class.isAssignableFrom(c));

		JSONObject object = createObject(c);

		// 添加GameScoreCalculator的参数
		object.element(GameScoreFilter.KEY_ADD_OPERATION, addOperation);
		object.element(GameScoreFilter.KEY_SCORE_TYPE, scoreType);
		object.element(GameScoreFilter.KEY_CALCULATE_SCORE_POINT, calculateScorePoint);
		return object;
	}

	/**
	 * 构造JSONObject, 用于创建ScorePointCaculator或者其派生类
	 */
	public static JSONObject createHuScoreChecker(Class<?> c, ScoreCalculateType modifierType, int modifierScore, int[] mutexScoreTypes)
	{
		// 强制要求c从是ScorePointChecker或者派生于ScorePointChecker
		Macro.AssetFalse(ScoreChecker.class.isAssignableFrom(c));

		JSONObject object = createObject(c);

		// 添加ScorePointChecker的参数
		object.element(HuScoreChecker.KEY_MODIFIER_TYPE, modifierType.toString());
		object.element(HuScoreChecker.KEY_MODIFIER_SCORE, (Integer)modifierScore);

		JSONArray jsonArray = createArray();
		if (mutexScoreTypes != null)
			for (int type : mutexScoreTypes)
				jsonArray.add(type);
		object.element(HuScoreChecker.KEY_MUTES_SCORE_TYPES, jsonArray);

		return object;
	}

	public static JSONObject createHuScoreChecker(Class<?> c, ScoreCalculateType modifierType, int modifierScore)
	{
		return createHuScoreChecker(c, modifierType, modifierScore, null);
	}

	/**
	 * 构造JSONObject, 用于创建GangScoreChecker
	 */
	public static JSONObject createGangScoreChecker(Class<?> c, ScoreCalculateType modifierType, int modifierScore)
	{
		// 强制要求c从是ScorePointChecker或者派生于ScorePointChecker
		Macro.AssetFalse(GangScoreChecker.class.isAssignableFrom(c));

		JSONObject object = createObject(c);

		// 添加ScorePointChecker的参数
		object.element(GangScoreChecker.KEY_MODIFIER_TYPE, modifierType.toString());
		object.element(GangScoreChecker.KEY_MODIFIER_SCORE, (Integer)modifierScore);
		return object;
	}

	/**
	 * 构造JSONObject, 用于创建BattleScoreChecker
	 */
	public static JSONObject createBattleScoreChecker(Class<?> c, ScoreCalculateType modifierType, int modifierScore)
	{
		// 强制要求c从是BattleScoreChecker派生
		Macro.AssetFalse(BattleScoreChecker.class.isAssignableFrom(c));

		JSONObject object = createObject(c);

		// 添加ScorePointChecker的参数
		object.element(BattleScoreChecker.KEY_CALCULATE_TYPE, modifierType.toString());
		object.element(BattleScoreChecker.KEY_SCORE_VALUE, (Integer)modifierScore);

		return object;
	}

	public static JSONObject createOperationFilter_LastOP(int sourceType)
	{
		JSONObject object = createObject(OperationFilter_LastOperator.class);

		// 添加ScorePointChecker的参数
		object.element(OperationFilter_LastOperator.KEY_SOURCE_TYPE, sourceType);

		return object;
	}

	public static JSONObject createGangScoreFilter_LastOP(int sourceType)
	{
		JSONObject object = createObject(ScorePointFilter_LastOperator.class);

		// 添加ScorePointChecker的参数
		object.element(ScorePointFilter_LastOperator.KEY_SOURCE_TYPE, sourceType);

		return object;
	}

	@SuppressWarnings("unchecked")
	public static <T> T instantiateClass(JSONObject json, Class<T> targetType)
	{
		try
		{
			Class<?> clazz = Class.forName(CreateContextParser.getString(json, CreateContextHelper.KEY_CLASS_NAME));
			return (T)clazz.newInstance();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}
}
