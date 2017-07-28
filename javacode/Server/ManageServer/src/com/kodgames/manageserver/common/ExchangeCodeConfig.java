package com.kodgames.manageserver.common;

import java.util.HashSet;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExchangeCodeConfig
{
	private static Logger logger = LoggerFactory.getLogger(ExchangeCodeConfig.class);

	public static final String LIMIT_SEPARATOR = ",";
	public static final String UNTIL_SEPARATOR = ":";

	public static final int NO_LIMIT = -1;

	// 兑换码前缀（批次信息）
	private String codePrefix;
	// 该批次兑换码生成数量
	private int genCount;
	// 渠道限制
	private HashSet<Integer> channelLimit = new HashSet<Integer>();
	// 服务器开区限制
	private HashSet<Integer> areaLimit = new HashSet<Integer>();
	// 绝对时间限制，在此时间端内生效
	private long absoluteTimeStart;
	private long absoluteTimeEnd;
	// 该批次兑换码一个玩家可以使用的个数
	private int codeMaxUseNum;
	// 该批次中一个兑换码可被使用的玩家数
	private int codeMaxPlyerUseNum;
	// 奖励ID
	private int rewardId;
	// 描述
	private String description;

	/**
	 * 判断渠道是否不限制
	 */
	public boolean isChannelUnlimit()
	{
		return channelLimit.contains(NO_LIMIT);
	}

	/**
	 * 判断区是否不限制
	 */
	public boolean isAreaUnlimit()
	{
		return areaLimit.contains(NO_LIMIT);
	}

	/**
	 * 解析 形如"1,2,4:100,102"这样的字符串配置参数为整形
	 */
	public HashSet<Integer> parseConfig(String config)
	{
		HashSet<Integer> limit = new HashSet<Integer>();
		String[] ls = config.split(LIMIT_SEPARATOR);
		for (String s : ls)
		{
			if (!s.trim().equals(""))
			{
				if (s.contains(UNTIL_SEPARATOR))
				{
					String[] as = s.split(UNTIL_SEPARATOR);
					if (as.length == 2)
					{
						for (int i = Integer.parseInt(as[0]); i <= Integer.parseInt(as[1]); i++)
							limit.add(i);
					}
					else
					{
						logger.error(ExceptionUtils.getStackTrace(new Throwable(
							"parse exchangeCode config error. wrong config.")));
					}
				}
				else
				{
					limit.add(Integer.parseInt(s));
				}
			}
		}
		return limit;
	}

	public String getCodePrefix()
	{
		return codePrefix;
	}

	public void setCodePrefix(String codePrefix)
	{
		this.codePrefix = codePrefix.toUpperCase();
	}

	public int getGenCount()
	{
		return genCount;
	}

	public void setGenCount(int genCount)
	{
		this.genCount = genCount;
	}

	public HashSet<Integer> getChannelLimit()
	{
		return channelLimit;
	}

	public void setChannelLimit(String channelLimit)
	{
		this.channelLimit = parseConfig(channelLimit);
	}

	public HashSet<Integer> getAreaLimit()
	{
		return areaLimit;
	}

	public void setAreaLimit(String areaLimit)
	{
		this.areaLimit = parseConfig(areaLimit);
	}

	public long getAbsoluteTimeStart()
	{
		return absoluteTimeStart;
	}

	public void setAbsoluteTimeStart(long absoluteTimeStart)
	{
		this.absoluteTimeStart = absoluteTimeStart;
	}

	public long getAbsoluteTimeEnd()
	{
		return absoluteTimeEnd;
	}

	public void setAbsoluteTimeEnd(long absoluteTimeEnd)
	{
		this.absoluteTimeEnd = absoluteTimeEnd;
	}

	public int getCodeMaxUseNum()
	{
		return codeMaxUseNum;
	}

	public void setCodeMaxUseNum(int codeMaxUseNum)
	{
		this.codeMaxUseNum = codeMaxUseNum;
	}

	public int getCodeMaxPlyerUseNum()
	{
		return codeMaxPlyerUseNum;
	}

	public void setCodeMaxPlyerUseNum(int codeMaxPlyerUseNum)
	{
		this.codeMaxPlyerUseNum = codeMaxPlyerUseNum;
	}

	public int getRewardId()
	{
		return rewardId;
	}

	public void setRewardId(int rewardId)
	{
		this.rewardId = rewardId;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

}
