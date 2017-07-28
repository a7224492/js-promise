package com.kodgames.client.action.contact;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.client.service.client.ClientService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.contact.ContactProtoBuf.GCContactRES;

@ActionAnnotation(actionClass = GCContactRESAction.class, messageClass = GCContactRES.class, serviceClass = ClientService.class)
public class GCContactRESAction extends ProtobufMessageHandler<ClientService, GCContactRES>
{
	private static final Logger logger = LoggerFactory.getLogger(GCContactRESAction.class);

	@Override
	public void handleMessage(Connection connection, ClientService service, GCContactRES message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
	}
}