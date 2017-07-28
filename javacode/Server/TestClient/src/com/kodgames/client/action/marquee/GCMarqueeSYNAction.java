package com.kodgames.client.action.marquee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.client.service.client.ClientService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.marquee.MarqueeProtoBuf.GCMarqueeVersionSYNC;

@ActionAnnotation(actionClass = GCMarqueeSYNAction.class, messageClass = GCMarqueeVersionSYNC.class, serviceClass = ClientService.class)
public class GCMarqueeSYNAction extends ProtobufMessageHandler<ClientService, GCMarqueeVersionSYNC>
{
	private static final Logger logger = LoggerFactory.getLogger(GCMarqueeSYNAction.class);

	@Override
	public void handleMessage(Connection connection, ClientService service, GCMarqueeVersionSYNC message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
	}
}
