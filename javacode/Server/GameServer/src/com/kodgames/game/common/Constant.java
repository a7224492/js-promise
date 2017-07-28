package com.kodgames.game.common;

/**
 * Created by lz on 2016/8/27. game使用的常量
 */
public class Constant
{
	public static final class ColorConstant
	{
		public static final int COLOR_MIN = 0x0;
		public static final int COLOR_MAX = 0xFFFFFF;

		private static final String PREFIX = "#";

		public static boolean isValidColor(final String color)
		{
			if (!color.startsWith(PREFIX))
			{
				return false;
			}

			int value;
			try
			{
				value = Integer.parseInt(color.substring(1), 0x10);
			}
			catch (Throwable e)
			{
				return false;
			}

			if (value < COLOR_MIN || value > COLOR_MAX)
			{
				return false;
			}

			return true;
		}
	}

	public static final class MarqueeConstant
	{
		public static final int TYPE_IMMEDIATE = 1;
		public static final int TYPE_DATE = 2;
		public static final int TYPE_WEEK = 3;
		
		public static final int SHOW_TYPE_MAINSCENE = 1;
		public static final int SHOW_TYPE_PLAYSCENE = 2;
		public static final int SHOW_TYPE_ALL = 3;
		
		public static final int ERROR_OK = 1;
		public static final int ERROR_ARGS = 0;
		public static final int ERROR_DATE = -1;
		public static final int ERROR_HOUR_MINUTE = -2;
		public static final int ERROR_WEEK = -3;
		public static final int ERROR_COLOR = -4;
	}

	public static final class RoomCardConstant
	{
		public static final int ERROR_ROLE_NOT_EXIST = -1;
	}

	// Zdb String_tables表的key
	public static final class StringTablesKey
	{
		public static final int KEY_SECURITY_CONFIG = 1;		// SecurityGroup的配置文件
	}
	
	public static class PAY_TYPE // 支付方式
	{
		public static final int CREATOR_PAY = 11; // 房主付

		public static final int WINNER_PAY = 12; // 赢家付

		public static final int AA_PAY = 13; // AA付

		public static final String NAMES[] = {"", "房主出卡", "胜者出卡", "AA出卡"}; // 描述
	}
	
	public static class RoomContinueStatu
	{
		public static final int CONTINUING	= 1;	// 续房创建中
		public static final int NOCONTINUE	= 2;	// 未创建续房
	}
	
	public enum ActivityId
	{
		INVALID(0, "无效活动ID"),
		SCORE_RANK(300001, "排行榜"),
		TURN_TABLE(300002, "大转盘");

		private static final ActivityId[] activityIds = new ActivityId[] {SCORE_RANK, TURN_TABLE};

		ActivityId(int type, String name)
		{
			this.id = type;
			this.name = name;
		}
		
		public static ActivityId getId(int id)
		{
			for(ActivityId activityId : ActivityId.values())
			{
				if (activityId.getId() == id)
				{
					return activityId;
				}
			}
			
			return INVALID;
		}

		public int getId()
		{
			return id;
		}

		public String getName()
		{
			return name;
		}

		private int id;
		private String name;

		public static ActivityId[] getActivityIds()
		{
			return activityIds;
		}
	}

	public enum ActivityType
	{
		INVALID(0, "无效活动类型"),
		DAILY_RANK(1, "单日排行"),
		WEEKLY_RANK(2, "七日排行");

		private static final ActivityType[] rankTypes = new ActivityType[] {DAILY_RANK, WEEKLY_RANK};

		ActivityType(int type, String name)
		{
			this.type = type;
			this.name = name;
		}
		
		public static ActivityType getType(int type)
		{
			for(ActivityType t : ActivityType.values())
			{
				if (t.getType() == type)
				{
					return t;
				}
			}
			
			return INVALID;
		}

		public int getType()
		{
			return type;
		}

		public String getName()
		{
			return name;
		}

		private int type;
		private String name;

		public static ActivityType[] getRankTypes()
		{
			return rankTypes;
		}

		/**
		 * 判断是否为累计活动
		 */
		public static boolean isAccumulated(int id)
		{
			return id == WEEKLY_RANK.getType();
		}
	}

	public enum PayType
	{
		CREATOR_PAY(11, "房主出卡"),
		WINNER_PAY(12, "赢家出卡"),
		AA_PAY(13, "AA出卡");
//		MANAGER_PAY(14, "经理出卡");

		/** 支付值 */
		private int value;
		/** 支付名称 */
		private String name;

		private PayType(int value, String name)
		{
			this.value = value;
			this.name = name;
		}

		public int getValue()
		{
			return value;
		}

		public String getName()
		{
			return name;
		}

		public static String getName(int value)
		{
			for (PayType type : values())
			{
				if (type.getValue() == value)
				{
					return type.getName();
				}
			}

			return "";
		}
	}
}
