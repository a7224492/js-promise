package com.kodgames.battleserver.service.battle.region.guangdong.common.score.game;

import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;

import net.sf.json.JSONObject;

/**
 * 房间结算的辅助类
 */
public class GameScoreFilter_GD
{
	public static final String KEY_NEED_ADD_TYPES = "key_needAddTypes";
	public static final String KEY_ADD_TO_TYPE = "key_addToType";
	public static final String KEY_ADD_TO_TYPE_OPER = "key_addToTypeOper";
	public static final String KEY_CHECK_STEP = "key_checkStep";
	public static final String KEY_CALCULATE_SCORE_POINT = "calculateScorePoint";
	/**
	 * 需要添加到什么分数类型中
	 */
	public int addToType = 0;
	/**
	 * 是否为加分
	 */
	public boolean addOperation = false;
	/**
	 * 是否检查step
	 */
	public boolean checkStep = false;
	/**
	 * 添加的类型
	 */
	public List<Integer> needAddTypes = new ArrayList<Integer>();
	/**
	 * 是否统计分数
	 */
	public boolean calculateScorePoint = false;
	
	public void createFromContext(JSONObject context) throws Exception
	{
		this.addToType = CreateContextParser.getInt(context, KEY_ADD_TO_TYPE);
		for (int i = 0; i < CreateContextParser.getArraySize(context, KEY_NEED_ADD_TYPES); ++i)
			this.needAddTypes.add(CreateContextParser.getIntInArray(context, KEY_NEED_ADD_TYPES, i));
		this.addOperation = CreateContextParser.getBoolean(context, KEY_ADD_TO_TYPE_OPER);
		this.checkStep = CreateContextParser.getBoolean(context, KEY_CHECK_STEP);
		this.calculateScorePoint = CreateContextParser.getBoolean(context, KEY_CALCULATE_SCORE_POINT);
	}
}
