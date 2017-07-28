package com.kodgames.game.service.gmtools;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.constant.PurchaseConst;
import com.kodgames.game.service.purchase.OrderData;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

/**
 * 重置订单状态
 */
@GmtHandlerAnnotation(handler = "ManualFillHandler")
public class ManualFillHandler implements IGmtoolsHandler
{
	private static Logger logger = LoggerFactory.getLogger(ManualFillHandler.class);

	@Override
	public HashMap<String, Object> getResult(Map<String, Object> json)
	{
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", 1);

		// 获取订单id
		String orderId = (String)json.get("order_id");

		// 将订单状态重置为-1（补单状态）
		if (OrderData.updateStatus(orderId, PurchaseConst.ORDER_STATUS_BUDAN))
			result.put("data", 1);
		else
			result.put("data", -1);

		logger.info("Gmtools ManualFillHander, order_id = {}, result = {}", orderId, result);
		return result;
	}
}
