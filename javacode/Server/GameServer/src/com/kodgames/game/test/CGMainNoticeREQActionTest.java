package com.kodgames.game.test;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.action.notice.CGMainNoticeREQAction;
import com.kodgames.game.service.notice.NoticeService;

import limax.xmlconfig.Service;
import limax.zdb.Procedure;
import xbean.MainNotice;

public class CGMainNoticeREQActionTest
{

	public static void main(String[] args)
	{
		Service.addRunAfterEngineStartTask(() -> {
			Procedure.call(()->{
				table.Main_notice_table.delete(NoticeService.HEALTH);
				table.Main_notice_table.delete(NoticeService.RESIDENT);
				test();
				return true;
			});
		});

		Service.run(Object.class.getResource("/zdb_config.xml").getPath());
	}

	public static void test()
	{
//		MainNotice healthNotice = table.Main_notice_table.insert(NoticeService.HEALTH);
//		healthNotice.setContent("health");
//		healthNotice.setEndTime(100000L);
//		healthNotice.setId(100L);
//		healthNotice.setIsCancel(false);
//		healthNotice.setStartTime(2000000L);
//		healthNotice.setTitle("health_title");
//		healthNotice.setType("health");
		
		MainNotice residentNotice = table.Main_notice_table.insert(NoticeService.RESIDENT);
		residentNotice.setContent("resident");
		residentNotice.setEndTime(100000L);
		residentNotice.setId(100L);
		residentNotice.setIsCancel(false);
		residentNotice.setStartTime(2000000L);
		residentNotice.setTitle("resident_title");
		residentNotice.setType("resident");
		
		CGMainNoticeREQAction action = new CGMainNoticeREQAction();
		action.handleMessage(null, ServiceContainer.getInstance().getPublicService(NoticeService.class), null, 0);
	}
}
