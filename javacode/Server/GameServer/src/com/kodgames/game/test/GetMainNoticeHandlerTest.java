package com.kodgames.game.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.game.service.gmtools.GetMainNoticeHandler;
import com.kodgames.game.service.gmtools.SetMainNoticeHandler;
import com.kodgames.game.service.notice.NoticeService;

import limax.xmlconfig.Service;
import limax.zdb.Procedure;

public class GetMainNoticeHandlerTest
{
	private static final Logger logger = LoggerFactory.getLogger(GetMainNoticeHandlerTest.class);
	public static void main(String[] args)
	{
		Service.addRunAfterEngineStartTask(() -> {
			Procedure.call(()->{
				table.Main_notice_table.delete(NoticeService.HEALTH);
				table.Main_notice_table.delete(NoticeService.RESIDENT);

				testQueryEmptyMainNotice();
//				testQueryHealthMainNotice();
//				testQueryResidentMainNotice();
//				testQueryAllMainNotice();
				
				return true;
			});
		});

		Service.run(Object.class.getResource("/zdb_config.xml").getPath());
	}
	
	public static void testQueryEmptyMainNotice()
	{
		HashMap<String, Object> queryHealthJson = new HashMap<String, Object>();
		queryHealthJson.put("type", "all");
		
		GetMainNoticeHandler getMainNoticeHandler = new GetMainNoticeHandler();
		
		HashMap<String, Object> result = getMainNoticeHandler.getResult(queryHealthJson);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> dataList = (List<Map<String, Object>>)result.get("data");
		logger.debug("************dataList={}", dataList);
	}
	
	public static void testQueryHealthMainNotice()
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
		
		HashMap<String, Object> queryHealthJson = new HashMap<String, Object>();
		queryHealthJson.put("type", "health");
		
		GetMainNoticeHandler getMainNoticeHandler = new GetMainNoticeHandler();
		
		HashMap<String, Object> result = getMainNoticeHandler.getResult(queryHealthJson);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> dataList = (List<Map<String, Object>>)result.get("data");
		logger.debug("************dataList={}", dataList);
	}
	
	public static void testQueryResidentMainNotice()
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
		
		HashMap<String, Object> queryHealthJson = new HashMap<String, Object>();
		queryHealthJson.put("type", "resident");
		
		GetMainNoticeHandler getMainNoticeHandler = new GetMainNoticeHandler();
		
		HashMap<String, Object> result = getMainNoticeHandler.getResult(queryHealthJson);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> dataList = (List<Map<String, Object>>)result.get("data");
		logger.debug("************dataList={}", dataList);
	}

	public static void testQueryAllMainNotice()
	{
		testQueryHealthMainNotice();
		testQueryResidentMainNotice();
		
		HashMap<String, Object> queryHealthJson = new HashMap<String, Object>();
		queryHealthJson.put("type", "all");
		
		GetMainNoticeHandler getMainNoticeHandler = new GetMainNoticeHandler();
		
		HashMap<String, Object> result = getMainNoticeHandler.getResult(queryHealthJson);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> dataList = (List<Map<String, Object>>)result.get("data");
		logger.debug("************dataList={}", dataList);
	}
}
