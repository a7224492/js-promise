package com.kodgames.game.action.notice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.game.service.notice.NoticeService;
import com.kodgames.game.start.CGProtobufMessageHandler;
import com.kodgames.message.proto.notice.NoticeProtoBuf.CGMainNoticeREQ;
import com.kodgames.message.proto.notice.NoticeProtoBuf.GCMainNoticeRES;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

import xbean.MainNotice;

@ActionAnnotation(messageClass = CGMainNoticeREQ.class, actionClass = CGMainNoticeREQAction.class, serviceClass = NoticeService.class)
public class CGMainNoticeREQAction extends CGProtobufMessageHandler<NoticeService, CGMainNoticeREQ>
{
	private static Logger logger = LoggerFactory.getLogger(CGNoticeREQAction.class);

	@Override
	public void handleMessage(Connection connection, NoticeService service, CGMainNoticeREQ message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getRemotePeerID(), message);
		
		GCMainNoticeRES.Builder builder = GCMainNoticeRES.newBuilder();
		
		// 查询健康公告
		MainNotice healthNotice = table.Main_notice_table.select(NoticeService.HEALTH);
		if (healthNotice != null)
		{
			builder.addNotices(service.getMainNoticePROTO(healthNotice));
		}
		
		// 查询常驻公告
		MainNotice residentNotice = table.Main_notice_table.select(NoticeService.RESIDENT);
		if (residentNotice != null)
		{
			builder.addNotices(service.getMainNoticePROTO(residentNotice));
		}
		
		logger.debug("GCMainNoticeRES : {}", builder);
		
		builder.setResult(PlatformProtocolsConfig.GC_MAIN_NOTICE_SUCCESS);
		
		connection.write(callback, builder.build());
	}

}
