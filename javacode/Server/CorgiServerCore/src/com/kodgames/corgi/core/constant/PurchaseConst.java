package com.kodgames.corgi.core.constant;

public class PurchaseConst
{
	/** DB订单状态： 补单 */
	public static int ORDER_STATUS_BUDAN = -1;
	/** DB订单状态： 成功 */
	public static int ORDER_STATUS_SUCCESS = 1;

	/** 订单处理返回码 ： 成功 */
	public static final String SUCCESS = "0";
	/** 订单处理返回码 ： 错误 */
	public static final String SYSTEM_ERROR = "1";
	/** 订单处理返回码 ： 订单签名验证失败 */
	public static final String MD5FAILED = "2";
	/** 订单处理返回码 ： 订单信息解析错误 */
	public static final String INVAILD_ORDRE_DATA = "3";
	/** 订单处理返回码 ： 无效玩家id */
	public static final String INVAILD_ROLEID = "4";
	/** 订单处理返回码 ： 无效区域id */
	public static final String INVAILD_AREAID = "6";
	/** 订单处理返回码 ： 无效商品id */
	public static final String INVAILD_GOODID = "7";

	/** 订单信息 Key： 订单号（Billing服务器生成） */
	public static final String GMT_PURCHASE_PARAM_ORDERID = "orderId";
	/** 订单信息 Key：渠道订单号（三方生成） */
	public static final String GMT_PURCHASE_PARAM_CHANNELORDERID = "channelOrderId";
	/** 订单信息 Key：玩家id */
	public static final String GMT_PURCHASE_PARAM_PLAYERID = "playerId";
	/** 订单信息 Key：区域id */
	public static final String GMT_PURCHASE_PARAM_AREADID = "areaId";
	/** 订单信息 Key：设备类型 */
	public static final String GMT_PURCHASE_PARAM_DEVICETYPE = "deviceType";
	/** 订单信息 Key：渠道id */
	public static final String GMT_PURCHASE_PARAM_CHANNELID = "channelId";
	/** 订单信息 Key：渠道UID */
	public static final String GMT_PURCHASE_PARAM_CHANNELUID = "channelUid";
	/** 订单信息 Key：充值金额 */
	public static final String GMT_PURCHASE_PARAM_RMB = "rmb";
	/** 订单信息 Key：充值商品id */
	public static final String GMT_PURCHASE_PARAM_ITEMID = "itemId";
	/** 订单信息 Key：订单签名 */
	public static final String GMT_PURCHASE_PARAM_SIGN = "sign";
	/** 订单信息 Key：订单状态 */
	public static final String GMT_PURCHASE_PARAM_STATUS = "status";
	/** 订单信息 Key：订单创建时间 */
	public static final String GMT_PURCHASE_PARAM_CREATETIME = "createTime";
	
	/** 订单信息Key： 代理商id */
	public static final String GMT_PURCHASE_PARAM_AGENTID = "agentID";
}
