package com.kodgames.game.action.notice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.game.start.CGProtobufMessageHandler;
import com.kodgames.game.service.notice.NoticeService;
import com.kodgames.message.proto.notice.NoticeProtoBuf.CGNoticeREQ;

@ActionAnnotation(messageClass = CGNoticeREQ.class, actionClass = CGNoticeREQAction.class, serviceClass = NoticeService.class)
public class CGNoticeREQAction extends CGProtobufMessageHandler<NoticeService, CGNoticeREQ>
{
	private static Logger logger = LoggerFactory.getLogger(CGNoticeREQAction.class);

	@Override
	public void handleMessage(Connection connection, NoticeService service, CGNoticeREQ message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getRemotePeerID(), message);
		
		service.getNoticeREQ(connection, message, callback);
	}

}
