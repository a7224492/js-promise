package com.kodgames.game.service.purchase;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.constant.PurchaseConst;
import com.kodgames.game.util.Md5Util;

import xbean.Purchase_order_item;

/**
 * 订单工具类
 */
public class OrderData
{
	private static Logger logger = LoggerFactory.getLogger(OrderData.class);

	/**
	 * 解析订单数据
	 * 
	 * @param orderDatas
	 * @throws Exception
	 */
	public static Purchase_order_item analyzeOrderMap(Map<String, Object> orderDatas)
		throws Exception
	{
		Purchase_order_item temp = new Purchase_order_item();

		temp.setOrderId(((String)orderDatas.get(PurchaseConst.GMT_PURCHASE_PARAM_ORDERID)).trim()); // 去除末尾空格
		temp.setChannelOrderId((String)orderDatas.get(PurchaseConst.GMT_PURCHASE_PARAM_CHANNELORDERID));
		temp.setPlayerId(Integer.valueOf((String)orderDatas.get(PurchaseConst.GMT_PURCHASE_PARAM_PLAYERID)));
		temp.setAreaId(Integer.valueOf((String)orderDatas.get(PurchaseConst.GMT_PURCHASE_PARAM_AREADID)));
		temp.setDeviceType((String)orderDatas.get(PurchaseConst.GMT_PURCHASE_PARAM_DEVICETYPE));
		temp.setChannelId((String)orderDatas.get(PurchaseConst.GMT_PURCHASE_PARAM_CHANNELID));
		temp.setChannelUid((String)orderDatas.get(PurchaseConst.GMT_PURCHASE_PARAM_CHANNELUID));
		temp.setItemId((String)orderDatas.get(PurchaseConst.GMT_PURCHASE_PARAM_ITEMID));
		temp.setRmb(Integer.valueOf((String)orderDatas.get(PurchaseConst.GMT_PURCHASE_PARAM_RMB)));
		temp.setSign((String)orderDatas.get(PurchaseConst.GMT_PURCHASE_PARAM_SIGN));

		return temp;
	}

	// 将订单生成到订单表中
	public static int insertOrder(Purchase_order_item orderInfo)
	{
		if (orderInfo == null)
			return -1;

		Purchase_order_item newItem = table.Purchase_order_table.insert(orderInfo.getOrderId());
		newItem.copyFrom(orderInfo);
		newItem.setCreateTime(System.currentTimeMillis());
		return 0;
	}

	// 判断订单是否使用过,如果使用过，是否满足补单要求，如果满足，那么通过检查，如果没有，写入数据库
	public static boolean checkOrderAndInsert(Purchase_order_item orderInfo)
	{
		String orderId = orderInfo.getOrderId();
		Purchase_order_item temp = table.Purchase_order_table.select(orderId);
		if (temp == null)
		{
			insertOrder(orderInfo);
			return true;
		}

		if (temp.getStatus() == PurchaseConst.ORDER_STATUS_BUDAN)
			return true;

		return false;
	}

	/**
	 * 更新订单状态
	 * 
	 * @param orderInfo
	 * @return
	 */
	public static boolean updateStatus(String orderId, int status)
	{
		Purchase_order_item temp = table.Purchase_order_table.select(orderId);
		if (temp == null)
			return false;
		else
		{
			temp.setStatus(status);
			return true;
		}
	}

	/**
	 * 验证订单签名是否有效
	 *
	 * @return
	 * @throws Exception
	 */
	public static boolean isValidSign(Purchase_order_item orderInfo, String billingKey)
	{
		boolean result = true;

		try
		{
			result = createSign(orderInfo, billingKey).equals(orderInfo.getSign());
		}
		catch (Exception e)
		{
			result = false;
			logger.error("Order md5 info error. {}", e);
		}

		return result;
	}

	/**
	 * 生成订单签名， 用于校验
	 *
	 * @return
	 * @throws Exception
	 */
	private static String createSign(Purchase_order_item orderInfo, String billingKey)
		throws Exception
	{
		StringBuilder sb = new StringBuilder();
		sb.append(orderInfo.getOrderId());
		sb.append(orderInfo.getChannelOrderId());
		sb.append(orderInfo.getPlayerId());
		sb.append(orderInfo.getDeviceType());
		sb.append(orderInfo.getAreaId());
		sb.append(orderInfo.getChannelId());
		sb.append(orderInfo.getRmb());
		sb.append(orderInfo.getItemId());
		sb.append(billingKey);
		return Md5Util.createMd5(sb.toString());
	}

	/**
	 * 查询订单
	 *
	 * @param orderId
	 * @return
	 */
	public static Purchase_order_item queryOrder(String orderId)
	{
		Purchase_order_item item = table.Purchase_order_table.select(orderId);
		return item;
	}
}
