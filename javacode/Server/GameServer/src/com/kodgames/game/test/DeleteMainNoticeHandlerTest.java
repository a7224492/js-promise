package com.kodgames.game.test;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.game.service.gmtools.DeleteMainNoticeHandler;
import com.kodgames.game.service.notice.NoticeService;

import limax.xmlconfig.Service;
import limax.zdb.Procedure;
import xbean.MainNotice;

public class DeleteMainNoticeHandlerTest
{
	private static final Logger logger = LoggerFactory.getLogger(DeleteMainNoticeHandlerTest.class);
	public static void main(String[] args)
	{
		Service.addRunAfterEngineStartTask(() -> {
			Procedure.call(()->{
				table.Main_notice_table.delete(NoticeService.HEALTH);
				table.Main_notice_table.delete(NoticeService.RESIDENT);
//				test();
//				test2();
				testTypeError();
				
				return true;
			});
		});

		Service.run(Object.class.getResource("/zdb_config.xml").getPath());
	}

	public static void test()
	{
		MainNotice healthNotice = table.Main_notice_table.insert(NoticeService.HEALTH);
		healthNotice.setContent("content");
		healthNotice.setEndTime(100000L);
		healthNotice.setId(System.currentTimeMillis());
		healthNotice.setIsCancel(false);
		healthNotice.setStartTime(2000000L);
		healthNotice.setTitle("title1");
		healthNotice.setType("type");
		
		MainNotice selectHealthNotice = table.Main_notice_table.select(NoticeService.HEALTH);
		if (selectHealthNotice != null)
		{
			logger.debug("************************{}", selectHealthNotice.getTitle().equals("title1"));
		}
		else
		{
			logger.debug("before delete, ************selectHealthNotice is null");
		}
		
		HashMap<String, Object> deleteHealthJson = new HashMap<String, Object>();
		deleteHealthJson.put("type", "health");
		
		DeleteMainNoticeHandler handler = new DeleteMainNoticeHandler();
		handler.getResult(deleteHealthJson);
		
		MainNotice selectHealthNotice2 = table.Main_notice_table.select(NoticeService.HEALTH);
		if (selectHealthNotice2 != null)
		{
			logger.debug("************************{}", selectHealthNotice.getTitle().equals("title1"));
		}
		else
		{
			logger.debug("after delete, ************selectHealthNotice is null");
		}
	}
	
	public static void test2()
	{
		MainNotice residentNotice = table.Main_notice_table.insert(NoticeService.RESIDENT);
		residentNotice.setContent("content");
		residentNotice.setEndTime(100000L);
		residentNotice.setId(System.currentTimeMillis());
		residentNotice.setIsCancel(false);
		residentNotice.setStartTime(2000000L);
		residentNotice.setTitle("title2");
		residentNotice.setType("type");
		
		MainNotice selectResidentNotice = table.Main_notice_table.select(NoticeService.RESIDENT);
		if (selectResidentNotice != null)
		{
			logger.debug("************{}", selectResidentNotice.getTitle().equals("title2"));
		}
		else
		{
			logger.debug("before delete, ************selectResidentNotice is null");
		}
		
		HashMap<String, Object> deleteResidentJson = new HashMap<String, Object>();
		deleteResidentJson.put("type", "resident");
		
		DeleteMainNoticeHandler deleteResidentHandler = new DeleteMainNoticeHandler();
		deleteResidentHandler.getResult(deleteResidentJson);
		
		MainNotice selectResidentNotice2 = table.Main_notice_table.select(NoticeService.RESIDENT);
		if (selectResidentNotice2 != null)
		{
			logger.debug("************{}", selectResidentNotice2.getTitle().equals("title2"));
		}
		else
		{
			logger.debug("after delete, ************selectResidentNotice is null");
		}
	}
	
	public static void testTypeError()
	{
		MainNotice residentNotice = table.Main_notice_table.insert(NoticeService.RESIDENT);
		residentNotice.setContent("content");
		residentNotice.setEndTime(100000L);
		residentNotice.setId(System.currentTimeMillis());
		residentNotice.setIsCancel(false);
		residentNotice.setStartTime(2000000L);
		residentNotice.setTitle("title2");
		residentNotice.setType("tyewq");
		
		HashMap<String, Object> deleteResidentJson = new HashMap<String, Object>();
		deleteResidentJson.put("type", "redent");
		
		DeleteMainNoticeHandler deleteResidentHandler = new DeleteMainNoticeHandler();
		deleteResidentHandler.getResult(deleteResidentJson);
		
		MainNotice selectResidentNotice2 = table.Main_notice_table.select(NoticeService.RESIDENT);
		if (selectResidentNotice2 != null)
		{
			logger.debug("************{}", selectResidentNotice2.getTitle().equals("title2"));
		}
		else
		{
			logger.debug("after delete, ************selectResidentNotice is null");
		}
	}
}
