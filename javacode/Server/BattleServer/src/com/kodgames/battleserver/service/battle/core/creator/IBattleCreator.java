package com.kodgames.battleserver.service.battle.core.creator;

import java.util.List;

import com.kodgames.battleserver.service.battle.constant.MahjongConstant;
import com.kodgames.battleserver.service.room.PosMapInfo;

import net.sf.json.JSONObject;

/**
 * 构造用于创建战斗实例的数据
 * 
 * 每个地区玩法都要实现这个借口来构造创建规则
 */
public interface IBattleCreator
{
	public static IBattleCreator create(String className)
	{
		try
		{
			Class<?> clazz = Class.forName(className);
			return (IBattleCreator)clazz.newInstance();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 通过指定的规则构造创建数据
	 */
	JSONObject create(List<Integer> rules);

	/**
	 * 规则是否合法
	 */
	boolean checkRules(List<Integer> rules);

	/**
	 * 获取玩家最大数量
	 */
	default int getMaxPlayerSize(List<Integer> rules)
	{
		return MahjongConstant.DEFAULT_ROOM_MEMBER_COUNT;
	}

	/**
	 * 获取玩家座位映射信息
	 */
	default PosMapInfo getPlayerPosition(int maxPlayerCount, int posision, List<Integer> rules)
	{
		if (maxPlayerCount == 2)
		{
			if (posision == 1)
			{
				return new PosMapInfo(1, 3);
			}
			else if (posision == 2)
			{
				return new PosMapInfo(0, 4);
			}
		}
		else if (maxPlayerCount == 3)
		{
			if (posision == 1)
			{
				return new PosMapInfo(2, 4);
			}
			else if (posision == 3)
			{
				return new PosMapInfo(0, 4);
			}
		}

		return null;
	}

}
