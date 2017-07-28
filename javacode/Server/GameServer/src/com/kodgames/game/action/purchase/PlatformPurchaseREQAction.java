package com.kodgames.game.action.purchase;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.AbstractMessageInitializer;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.corgi.core.net.message.InternalMessage;
import com.kodgames.game.service.purchase.PurchaseService;
import com.kodgames.message.proto.server.ServerProtoBuf.PlatformPurchaseREQ;

/**
 * Manager转发来的充值请求
 */
@ActionAnnotation(messageClass = PlatformPurchaseREQ.class, actionClass = PlatformPurchaseREQAction.class, serviceClass = PurchaseService.class)
public class PlatformPurchaseREQAction extends ProtobufMessageHandler<PurchaseService, PlatformPurchaseREQ>
{
	@Override
	public void handleMessage(Connection connection, PurchaseService service, PlatformPurchaseREQ message, int callback)
	{
		connection.write(callback, service.purchase(message).build());
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
