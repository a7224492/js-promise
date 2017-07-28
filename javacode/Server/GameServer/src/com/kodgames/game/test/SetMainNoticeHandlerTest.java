package com.kodgames.game.test;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.game.service.gmtools.SetMainNoticeHandler;
import com.kodgames.game.service.notice.NoticeService;

import limax.xmlconfig.Service;
import limax.zdb.Procedure;
import xbean.MainNotice;

public class SetMainNoticeHandlerTest
{
	private static final Logger logger = LoggerFactory.getLogger(SetMainNoticeHandlerTest.class);
	public static void main(String[] args)
	{
		Service.addRunAfterEngineStartTask(() -> {
			Procedure.call(()->{
				table.Main_notice_table.delete(NoticeService.HEALTH);
				table.Main_notice_table.delete(NoticeService.RESIDENT);
				test3();
				
				return true;
			});
		});

		Service.run(Object.class.getResource("/zdb_config.xml").getPath());
	}
	
	public static void test()
	{
		SetMainNoticeHandler setMainNoticeHandler = new SetMainNoticeHandler();
		
		HashMap<String, Object> json = new HashMap<String, Object>();
		json.put("type", "health");
		json.put("id", System.currentTimeMillis());
		json.put("title", "健康公告");
		json.put("content", "健康游戏，拒绝沉迷");
		json.put("startTime", 1000000L);
		json.put("endTime", 2000000L);
		
		setMainNoticeHandler.getResult(json);
		
		MainNotice mainNotice = table.Main_notice_table.select(NoticeService.HEALTH);
		if (mainNotice != null)
		{
			System.out.println(mainNotice.getType().equals((String)json.get("type")));
			System.out.println(mainNotice.getContent().equals((String)json.get("content")));
			System.out.println(mainNotice.getTitle().equals((String)json.get("title")));
			System.out.println(mainNotice.getEndTime()==(long)json.get("endTime"));
			System.out.println(mainNotice.getId()==(long)json.get("id"));
			System.out.println(mainNotice.getIsCancel()==false);
			System.out.println(mainNotice.getStartTime()==(long)json.get("startTime"));
		}
		else
		{
			logger.debug("healthNotice is null");
		}
		
		MainNotice residentNotice = table.Main_notice_table.select(NoticeService.RESIDENT);
		if (residentNotice != null)
		{
			
		}
		else
		{
			logger.debug("residentNotice is null");
		}
	}
	
	public static void test2()
	{
		SetMainNoticeHandler setMainNoticeHandler = new SetMainNoticeHandler();
		
		HashMap<String, Object> json = new HashMap<String, Object>();
		json.put("type", "resident");
		json.put("id", System.currentTimeMillis());
		json.put("title", "健康公告");
		json.put("content", "健康游戏，拒绝沉迷");
		json.put("startTime", 1000000L);
		json.put("endTime", 2000000L);
		
		setMainNoticeHandler.getResult(json);
		
		MainNotice mainNotice = table.Main_notice_table.select(NoticeService.HEALTH);
		if (mainNotice != null)
		{
			System.out.println(mainNotice.getType().equals((String)json.get("type")));
			System.out.println(mainNotice.getContent().equals((String)json.get("content")));
			System.out.println(mainNotice.getTitle().equals((String)json.get("title")));
			System.out.println(mainNotice.getEndTime()==(long)json.get("endTime"));
			System.out.println(mainNotice.getId()==(long)json.get("id"));
			System.out.println(mainNotice.getIsCancel()==false);
			System.out.println(mainNotice.getStartTime()==(long)json.get("startTime"));
		}
		else
		{
			logger.debug("healthNotice is null");
		}
		
		MainNotice residentNotice = table.Main_notice_table.select(NoticeService.RESIDENT);
		if (residentNotice != null)
		{
			System.out.println(residentNotice.getType().equals((String)json.get("type")));
			System.out.println(residentNotice.getContent().equals((String)json.get("content")));
			System.out.println(residentNotice.getTitle().equals((String)json.get("title")));
			System.out.println(residentNotice.getEndTime()==(long)json.get("endTime"));
			System.out.println(residentNotice.getId()==(long)json.get("id"));
			System.out.println(residentNotice.getIsCancel()==false);
			System.out.println(residentNotice.getStartTime()==(long)json.get("startTime"));
		}
		else
		{
			logger.debug("residentNotice is null");
		}
	}
	
	public static void test3()
	{
		SetMainNoticeHandler setMainNoticeHandler = new SetMainNoticeHandler();
		
		HashMap<String, Object> json = new HashMap<String, Object>();
		json.put("type", "rwqe");
		json.put("id", System.currentTimeMillis());
		json.put("title", "健康公告");
		json.put("content", "健康游戏，拒绝沉迷");
		json.put("startTime", 1000000L);
		json.put("endTime", 2000000L);
		
		setMainNoticeHandler.getResult(json);
		
		MainNotice mainNotice = table.Main_notice_table.select(NoticeService.HEALTH);
		if (mainNotice != null)
		{
			System.out.println(mainNotice.getType().equals((String)json.get("type")));
			System.out.println(mainNotice.getContent().equals((String)json.get("content")));
			System.out.println(mainNotice.getTitle().equals((String)json.get("title")));
			System.out.println(mainNotice.getEndTime()==(long)json.get("endTime"));
			System.out.println(mainNotice.getId()==(long)json.get("id"));
			System.out.println(mainNotice.getIsCancel()==false);
			System.out.println(mainNotice.getStartTime()==(long)json.get("startTime"));
		}
		else
		{
			logger.debug("healthNotice is null");
		}
		
		MainNotice residentNotice = table.Main_notice_table.select(NoticeService.RESIDENT);
		if (residentNotice != null)
		{
			System.out.println(residentNotice.getType().equals((String)json.get("type")));
			System.out.println(residentNotice.getContent().equals((String)json.get("content")));
			System.out.println(residentNotice.getTitle().equals((String)json.get("title")));
			System.out.println(residentNotice.getEndTime()==(long)json.get("endTime"));
			System.out.println(residentNotice.getId()==(long)json.get("id"));
			System.out.println(residentNotice.getIsCancel()==false);
			System.out.println(residentNotice.getStartTime()==(long)json.get("startTime"));
		}
		else
		{
			logger.debug("residentNotice is null");
		}
	}
	
}
