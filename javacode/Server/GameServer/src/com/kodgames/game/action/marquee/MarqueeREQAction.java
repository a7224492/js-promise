package com.kodgames.game.action.marquee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.game.start.CGProtobufMessageHandler;
import com.kodgames.game.service.marquee.MarqueeService;
import com.kodgames.message.proto.marquee.MarqueeProtoBuf;

@ActionAnnotation(messageClass = MarqueeProtoBuf.CGMarqueeREQ.class,
				  actionClass = MarqueeREQAction.class,
				  serviceClass = MarqueeService.class)
public class MarqueeREQAction extends CGProtobufMessageHandler<MarqueeService, MarqueeProtoBuf.CGMarqueeREQ>
{
	private static final Logger logger = LoggerFactory.getLogger(MarqueeREQAction.class);
	
	@Override public void handleMessage(Connection connection, MarqueeService service, MarqueeProtoBuf.CGMarqueeREQ message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
		service.requestMarqueeList(connection, callback);
	}
}
