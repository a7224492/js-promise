package com.kodgames.client.util.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用来测试打牌玩家的响应时间
 * 
 * @author jiangzhen
 *
 */
public class PlayerResponseTimeUtil extends ResponseTimeUtilAdapter<Integer>
{
	/**
	 * 用于统计玩家一段时间内的平均响应时间
	 */
	private Map<Integer, List<Long>> avgMap = new ConcurrentHashMap<Integer, List<Long>>();

	/**
	 * 单例模式
	 */
	private static PlayerResponseTimeUtil prtu = null;

	private PlayerResponseTimeUtil()
	{
	}

	public static PlayerResponseTimeUtil getInstance()
	{
		if (prtu == null)
		{
			synchronized (PlayerResponseTimeUtil.class)
			{
				if (prtu == null)
				{
					prtu = new PlayerResponseTimeUtil();
				}
			}
		}

		return prtu;
	}

	@Override
	public void setRecvTime(Integer id, long time)
	{
		super.setRecvTime(id, time);
		if (!avgMap.containsKey(id))
		{
			List<Long> list = new ArrayList<Long>();
			list.add(super.getResponseTime(id));
			avgMap.put(id, list);
		}
		else
		{
			List<Long> list = avgMap.get(id);
			if (list == null)
			{
				throw new NullPointerException("list in avgMap is null");
			}
			list.add(super.getResponseTime(id));
		}
	}

	public float getAvgResponseTime(Integer id)
	{
		if (id < 0)
		{
			throw new IllegalArgumentException("id is illegal");
		}
		List<Long> list = this.avgMap.get(id);
		if (list == null)
		{
			throw new NullPointerException("list in avgMap is null");
		}
		
		float sum = 0;
		for (Long time : list)
		{
			sum += time;
		}
		
		if (list.size() == 0)
		{
			return 0;
		}
		else
		{
			return sum/list.size();
		}
	}
	
	public float getAvgResponseTimeAndClear(Integer id)
	{
		if (id < 0)
		{
			throw new IllegalArgumentException("id is negative");
		}
		float time = this.getAvgResponseTime(id);
		List<Long> list = this.avgMap.get(id);
		list.clear();
		
		return time;
	}
}
