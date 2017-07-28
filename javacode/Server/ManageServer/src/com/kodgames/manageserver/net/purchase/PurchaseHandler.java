package com.kodgames.manageserver.net.purchase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.constant.PurchaseResultType;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.httpserver.HttpReturnContent;
import com.kodgames.corgi.httpserver.IRequestListener;
import com.kodgames.corgi.httpserver.KodHttpMessage;
import com.kodgames.manageserver.service.purchase.PurchaseService;

/**
 * Billing服务器HTTP请求处理
 *
 */
public class PurchaseHandler implements IRequestListener
{
	private static Logger logger = LoggerFactory.getLogger(PurchaseHandler.class);

	@Override
	public String execute(KodHttpMessage message)
	{
		String uri = message.getUri();
		logger.info("reveive order uri: {}", uri);

		try
		{
			PurchaseService purchaseService = ServiceContainer.getInstance().getPublicService(PurchaseService.class);
			if (purchaseService.sendPlatformPurchaseREQ(message) != 0)
			{
				logger.error("execute: send platform purchase failed. message is " + uri);
				return createJsonObj(PurchaseResultType.INVAILD_AREAID);
			}
			// 收到Billing服务器的请求，不直接返回，等待Game的返回结果
			return HttpReturnContent.NO_RETURN;
		}
		catch (Throwable e)
		{
			logger.error("exception arised. ", e);
		}
		return null;
	}

	@Override
	public String sendError()
	{
		return createJsonObj(PurchaseResultType.SYSTEM_ERROR);
	}

	// 根据传递的字符串生成JSON串
	private String createJsonObj(String content)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("{\"status\":");
		sb.append(content);
		sb.append("}");
		return sb.toString();
	}
}
