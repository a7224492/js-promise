package com.kodgames.manageserver.action.purchase;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.AbstractMessageInitializer;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.corgi.core.net.message.InternalMessage;
import com.kodgames.manageserver.service.purchase.PurchaseService;
import com.kodgames.message.proto.server.ServerProtoBuf;
import com.kodgames.message.proto.server.ServerProtoBuf.PlatformPurchaseRES;

/**
 * Game处理订单返回
 *
 */
@ActionAnnotation(messageClass = PlatformPurchaseRES.class, actionClass = PlatfromPurchaseRESAction.class, serviceClass = PurchaseService.class)
public class PlatfromPurchaseRESAction extends ProtobufMessageHandler<PurchaseService, ServerProtoBuf.PlatformPurchaseRES>
{
	@Override
	public void handleMessage(Connection connection, PurchaseService service, PlatformPurchaseRES message, int callback)
	{
		service.recievePlatfromPurchaseRES(message);
	}

	/**
	 * 使用协议号排队，单线程处理
	 */
	@Override
	public Object getMessageKey(AbstractMessageInitializer msgInitializer, InternalMessage message)
	{
		return message.getProtocolID();
	}
}
