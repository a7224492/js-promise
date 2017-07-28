package com.kodgames.battleserver.service.battle.core.creator;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.battleserver.service.battle.core.check.CompareType;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalculateType;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 解析CreateContext出错时,统一的返回异常
 */
class ParseCreateContextException extends Exception
{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -209600153336241471L;
	private final static Logger logger = LoggerFactory.getLogger(ParseCreateContextException.class);

	public ParseCreateContextException(JSONObject json, String message)
	{
		super(message);
		logger.error("ParseCreatingContext failed json:{} message:{}", json, message);
	}

	public ParseCreateContextException(JSONObject json, String key, String message)
	{
		super(message);
		logger.error("ParseCreatingContext failed json:{} key:{} message:{}", json, key, message);
	}

	public ParseCreateContextException(JSONObject json, String key, int index, String message)
	{
		super(message);
		logger.error("ParseCreatingContext failed jsonarray:{} key:{} index:{} message:{}", json, key, index, message);
	}
}

/**
 * CreatingContext的json结构的辅助解析函数
 * 
 * 所有出错情况都以ParseCreatingContextException返回
 */
public class CreateContextParser
{
	/**
	 * 判断一个JSONObject中是否包含key数据
	 */
	public static boolean containsKey(JSONObject json, String key)
	{
		return json.containsKey(key);
	}

	/**
	 * 从JSONObject中获取制定key的JSONObject
	 * 
	 * @throws Exception 有任何错误都会抛异常
	 */
	public static JSONObject getJSONObject(JSONObject json, String key)
		throws Exception
	{
		try
		{
			return json.getJSONObject(key);
		}
		catch (Exception e)
		{
			throw new ParseCreateContextException(json, key, e.getMessage());
		}
	}

	/**
	 * 从JSONObject中获取制定key的int值
	 * 
	 * @throws Exception 有任何错误都会抛异常
	 */
	public static int getInt(JSONObject json, String key)
		throws Exception
	{
		try
		{
			return json.getInt(key);
		}
		catch (Exception e)
		{
			throw new ParseCreateContextException(json, key, e.getMessage());
		}
	}

	/**
	 * 从JSONObject中获取制定key的boolean值
	 * 
	 * @throws Exception 有任何错误都会抛异常
	 */
	public static boolean getBoolean(JSONObject json, String key)
		throws Exception
	{
		try
		{
			return json.getBoolean(key);
		}
		catch (Exception e)
		{
			throw new ParseCreateContextException(json, key, e.getMessage());
		}
	}

	/**
	 * 从JSONObject中获取制定key的String值
	 * 
	 * @throws Exception 有任何错误都会抛异常
	 */
	public static String getString(JSONObject json, String key)
		throws Exception
	{
		try
		{
			return json.getString(key);
		}
		catch (Exception e)
		{
			throw new ParseCreateContextException(json, key, e.getMessage());
		}
	}

	/**
	 * 从JSONObject中获取制定key的JSONArray, 并转为JSONObject数组
	 * 
	 * @throws Exception 有任何错误都会抛异常
	 */
	public static List<JSONObject> getJSONArray(JSONObject json, String key)
		throws Exception
	{
		try
		{
			// 将JSONArray转换为List<JSONObject>
			JSONArray jsonArray = json.getJSONArray(key);
			List<JSONObject> objArray = new ArrayList<>();

			for (int i = 0; i < jsonArray.size(); ++i)
			{
				Object obj = jsonArray.get(i);
				if (!(obj instanceof JSONObject))
					throw new Exception("paser json array error");

				objArray.add((JSONObject)obj);
			}

			return objArray;
		}
		catch (Exception e)
		{
			throw new ParseCreateContextException(json, key, e.getMessage());
		}
	}

	/**
	 * 从JSONObject中获取制定key的JSONArray大小
	 * 
	 * @throws Exception 有任何错误都会抛异常
	 */
	public static int getArraySize(JSONObject json, String key)
		throws Exception
	{
		try
		{
			return json.getJSONArray(key).size();
		}
		catch (Exception e)
		{
			throw new ParseCreateContextException(json, key, e.getMessage());
		}
	}

	/**
	 * 从JSONObject中获取制定key的JSONArray中第index元素, 并转为int
	 * 
	 * @throws Exception 有任何错误都会抛异常
	 */
	public static int getIntInArray(JSONObject json, String key, int index)
		throws Exception
	{
		try
		{
			return json.getJSONArray(key).getInt(index);
		}
		catch (Exception e)
		{
			throw new ParseCreateContextException(json, key, index, e.getMessage());
		}
	}

	/**
	 * 从JSONObject中获取制定key的JSONArray中第index元素, 并转为String
	 * 
	 * @throws Exception 有任何错误都会抛异常
	 */
	public static String getStringInArray(JSONObject json, String key, int index)
		throws Exception
	{
		try
		{
			return json.getJSONArray(key).getString(index);
		}
		catch (Exception e)
		{
			throw new ParseCreateContextException(json, key, index, e.getMessage());
		}
	}

	/**
	 * 从JSONObject中获取制定key的ScoreModiferType类型值
	 * 
	 * @throws Exception 有任何错误都会抛异常
	 */
	public static ScoreCalculateType getScoreCalculateType(JSONObject json, String key)
		throws Exception
	{
		try
		{
			return ScoreCalculateType.valueOf(getString(json, key));
		}
		catch (Exception e)
		{
			throw new ParseCreateContextException(json, key, e.getMessage());
		}
	}

	/**
	 * 从JSONObject中获取制定key的ScoreModiferType类型值
	 * 
	 * @throws Exception 有任何错误都会抛异常
	 */
	public static CompareType getCompareType(JSONObject json, String key)
		throws Exception
	{
		try
		{
			return CompareType.valueOf(getString(json, key));
		}
		catch (Exception e)
		{
			throw new ParseCreateContextException(json, key, e.getMessage());
		}
	}
}