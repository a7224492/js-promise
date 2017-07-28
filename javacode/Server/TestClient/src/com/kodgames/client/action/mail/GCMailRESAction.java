package com.kodgames.client.action.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.client.service.account.RoleService;
import com.kodgames.client.service.room.RoomService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.mail.MailProtoBuf.GCMailRES;
import com.kodgames.message.proto.marquee.MarqueeProtoBuf.GCMarqueeRES;

@ActionAnnotation(actionClass = GCMailRESAction.class, messageClass = GCMailRES.class, serviceClass = RoleService.class)
public class GCMailRESAction extends ProtobufMessageHandler<RoleService, GCMailRES>
{
	private static final Logger logger = LoggerFactory.getLogger(GCMailRESAction.class);

	@Override
	public void handleMessage(Connection connection, RoleService service, GCMailRES message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
	}
}
