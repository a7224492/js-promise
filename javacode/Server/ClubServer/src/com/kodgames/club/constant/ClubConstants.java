package com.kodgames.club.constant;

public class ClubConstants
{
	
	public static class ClubDefault	// 俱乐部的默认设置
	{
		public static final int ROOM_COST = 1;									// 房间默认消耗1张房卡
		
		public static final int ROOM_PAY_TYPE = PAY_TYPE.CREATOR_PAY;			// 支付方式 默认是 房主付
		
		public static final int CLUB_ID_START = 100212;							// 俱乐部id从这个数开始吧！！！
		public static final int CLUB_ID_SEED_KEY = 2017318;						// 俱乐部id的key

		public static final int MAX_CLUBS_FOR_PLAYER = 5;						// 玩家可加入的俱乐部数量
		public static final int MAX_CLUBS_FOR_MANAGER = 5;						// 经理可加入的俱乐部数量
		public static final int MAX_CLUBS_FOR_AGENT = 5;						// 代理商可创建的俱乐部数量

		public static final int MAX_MEMBERS_FOR_CLUB = 50;					// 俱乐部最多允许的成员数量

		public static final int MAX_CLUB_COST = 10;							// 俱乐部消耗最大倍数
	}

	public static class CLUB_STATUS
	{
		public final static int SAEL_FLAG = 0b0001;                // 封停标志

		public final static int CONGELATION_FLAG = 0b0010;         // 冻结标志
	}
	
	public static class PAY_TYPE	// 支付方式
	{
		public static final int CREATOR_PAY = 1;								// 房主付
		
		public static final int WINNER_PAY = 2;									// 赢家付

		public static final int MANAGER_PAY = 4;

		public static final String NAMES[] = {"", "房主出卡", "胜者出卡", "", "经理出卡"};				// 描述
	}
	
	public static class CLUB_MEMEBER_TITLE // 俱乐部职位
	{
		public static final int OBSERVER = 1;									// 初级会员，只能看，所以起个观察者的名字
		
		public static final int MEMBER = 2;										// 会员，部分权限
		
		public static final int MANAGER = 3;									// 经理，全部权限
	}

	public static class CLUB_APPLICANT_OPTYPE
	{
		public static final  int ACCEPT = 0;									// 同意
		public static final int REJECT = 1;										// 拒绝
	}

	public static class CLUB_MEMBER_OPTYPE
	{
		public static final int TITLE = 0;										// 修改会员等级
		public static final int KICK = 1;										// 踢人
	}

	public static class CLUB_MEMBER_STATUS // 俱乐部成员状态
	{
		public static final int LEAVE_OUT = 0;									// 已离开
		public static final int NORMAL = 1;										// 正常 (未离开)
	}

	public static class CLUB_GMT_ERROR // 俱乐部gmt错误码 给gmt返回的！
	{
		public static final int SUCCESS = 1;											// 成功
		public static final int CREATE_CLUB_ERROR_MANAGER_MAX_LIMIT = -1;				// 经理超过5个俱乐部
		public static final int CREATE_CLUB_ERROR_MANAGER_INVALID = -2;					// 经理是别的俱乐部的会员
		public static final int CREATE_CLUB_ERROR_AGENT_MAX_LIMIT = -3;					// 代理商超过5个俱乐部
		public static final int CREATE_CLUB_ERROR_SERVER_ERROR = -4;					// 异常，数据库错误
		public static final int CHANGE_STATUS_ERROR = -5;								// 修改俱乐部状态错误
		public static final int ADD_CLUB_CARD_ERROR_INVALID_AGENT = -6;					// 添加房卡错误，非法的agentId
		public static final int ADD_CLUB_CARD_ERROR_INVALID_CLUB = -7;					// 添加房卡错误，非法的clubId
		public static final int ADD_CLUB_CARD_ERROR_INVALID_MEMBER = -8;				// 添加房卡错误，不存在这个会员
		public static final int ADD_CLUB_CARD_ERROR_INVALID_CARD = -9;					// 添加房卡错误，添加值必须大于0
		public static final int CREATE_CLUB_ERROR_ROLEID_INVALID = -10;					// 经理的roleId不合法，没有或者小于0
		public static final int ADD_CLUB_CARD_ERROR_CLUB_CONGELATION = -11;				// 俱乐部被冻结
		public static final int ADD_CLUB_CARD_ERROR_SIGN_INVALID = -12;					// 添加房卡错误，签名不对
	}

	public enum CLUB_ACTIONS {
		CreateRoom,									// 创建房间
		EnterRoom,									// 进入房间
		QueryHistory,								// 查看战绩
		Invitation									// 邀请加入俱乐部
	}

	public static class CLUB_CARD_COST_TYPE // 俱乐部房卡消耗类型
	{
		public static final int ROOM_COST = 1;					// 房间消耗
	}
}
