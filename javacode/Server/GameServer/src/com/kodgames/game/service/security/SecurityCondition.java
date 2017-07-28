package com.kodgames.game.service.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import qqzeng.ip.IpInfo;

/**
 * Created by Liufei on 2017/5/6.
 */
public class SecurityCondition
{
	private Logger logger = LoggerFactory.getLogger(SecurityCondition.class);

	private SecurityGroupConfig.ConditionType conditionType; // 条件类型
	private SecurityGroupConfig.CompareType compareType; // 参数比较类型
	private int minValue; // 参数值
	private int maxValue;
	private String strValue;
	private int modValue; // 取余的模值

	public void setParams(SecurityGroupConfig.ConditionType conditionType, SecurityGroupConfig.CompareType compareType, int minValue, int maxValue, String strValue, int modValue)
	{
		this.conditionType = conditionType;
		this.compareType = compareType;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.strValue = strValue;
		this.modValue = modValue;
	}

	public SecurityGroupConfig.ConditionType getType()
	{
		return conditionType;
	}

	/**
	 * 检查给定的信息是否满足该组的所有条件
	 * 
	 * @param registDays 注册天数
	 * @param combat7Days 最近7天的局数
	 * @param combatToday 当天战斗局数
	 * @param combatTotal 战斗总局数
	 * @param agencyId 最后购买房卡的代理商id
	 * @param regionName ip所在地区
	 */
	public boolean matchCondition(int registDays, int combat7Days, int combatToday, int combatTotal, int agencyId, IpInfo ipInfo, int playerId)
	{
		switch (conditionType)
		{
			case REGISTER: // 注册天数
				return compareIntValue(registDays);

			case COMBAT: // 最近7天比赛局数
				return compareIntValue(combat7Days);

			case COMBATTODAY:
				return compareIntValue(combatToday);

			case COMBATTOTAL: // 战斗总局数
				return compareIntValue(combatTotal);

			case AGENCY: // 代理商id取余
				return compareIntValue(modValue > 0 ? agencyId % modValue : agencyId);

			case PLAYERID: // 玩家id取余
				return compareIntValue(modValue > 0 ? playerId % modValue : playerId);

			case REGION: // ip所属地区
				return compareStrValue(ipInfo.province) || compareStrValue(ipInfo.city);
		}

		logger.warn("unknown condition type {}", conditionType.toString());
		return false;
	}

	// 比较整数类型的参数
	private boolean compareIntValue(int value)
	{
		// 整数参数支持所有的比较类型
		switch (compareType)
		{
			case GE:
				return value >= this.minValue;

			case LE:
				return value <= this.minValue;

			case NE:
				return value != this.minValue;

			case LESS:
				return value < this.minValue;

			case EQUAL:
				return value == this.minValue;

			case GREATER:
				return value > this.minValue;

			case BETWEEN:
				return value >= this.minValue && value <= this.maxValue;
		}

		logger.warn("unknown compare type {}", compareType.toString());
		return false;
	}

	// 比较字符串类型的参数
	private boolean compareStrValue(String value)
	{
		if (value == null)
			return false;

		// 字符串参数只支持 == != IN 和 NOTIN
		switch (compareType)
		{
			case EQUAL:
				return value.equals(this.strValue);

			case NE:
				return !value.equals(this.strValue);

			case IN:
				return isInList(value, this.strValue);

			case NOTIN:
				return !isInList(value, this.strValue);
		}

		logger.warn("unsupported compare type {}", compareType.toString());
		return false;
	}

	// 检查str是否在list中 list使用,分隔
	private boolean isInList(String str, String list)
	{
		String[] values = list.split(",");
		for (String v : values)
		{
			v = v.trim();
			if (v.equals(str))
				return true;
		}
		return false;
	}
}
