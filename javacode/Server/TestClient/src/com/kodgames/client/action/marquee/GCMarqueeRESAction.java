package com.kodgames.client.action.marquee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.client.service.room.RoomService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.marquee.MarqueeProtoBuf.GCMarqueeRES;

@ActionAnnotation(actionClass = GCMarqueeRESAction.class, messageClass = GCMarqueeRES.class, serviceClass = RoomService.class)
public class GCMarqueeRESAction extends ProtobufMessageHandler<RoomService, GCMarqueeRES>
{
	private static final Logger logger = LoggerFactory.getLogger(GCMarqueeRESAction.class);

	@Override
	public void handleMessage(Connection connection, RoomService service, GCMarqueeRES message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
	}
}
