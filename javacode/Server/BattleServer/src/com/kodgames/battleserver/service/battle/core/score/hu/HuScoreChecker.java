package com.kodgames.battleserver.service.battle.core.score.hu;

import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.score.ScoreChecker;

import net.sf.json.JSONObject;

public abstract class HuScoreChecker extends ScoreChecker
{
	public static final String KEY_MUTES_SCORE_TYPES = "mutexScoreTypes";

	private List<Integer> mutexScoreTypes = new ArrayList<>();

	/**
	 * 构造一个ScorePointCaculator实例
	 */
	public static HuScoreChecker create(JSONObject context)
		throws Exception
	{
		// 反射构造实例
		HuScoreChecker instance = CreateContextHelper.instantiateClass(context, HuScoreChecker.class);
		instance.createFromContext(context);
		return instance;
	}

	protected HuScoreChecker(int scoreType)
	{
		super(scoreType);
	}

	/**
	 * 在检测ting牌的情况是否要忽略这个分数
	 */
	public boolean skipCheckTing()
	{
		return false;
	}

	public List<Integer> getMutexScoreTypes()
	{
		return this.mutexScoreTypes;
	}

	public void createFromContext(JSONObject context)
		throws Exception
	{
		super.createFromContext(context);

		// 强制要求互斥
		if (CreateContextParser.containsKey(context, KEY_MUTES_SCORE_TYPES))
		{
			int mutexScoreTypeCount = CreateContextParser.getArraySize(context, KEY_MUTES_SCORE_TYPES);
			for (int i = 0; i < mutexScoreTypeCount; ++i)
				mutexScoreTypes.add(CreateContextParser.getIntInArray(context, KEY_MUTES_SCORE_TYPES, i));
		}
	}
}
