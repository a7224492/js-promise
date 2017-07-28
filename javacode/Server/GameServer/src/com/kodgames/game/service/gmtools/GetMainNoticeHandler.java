package com.kodgames.game.service.gmtools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.notice.NoticeService;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

import xbean.MainNotice;

/**
 * 得到主页公告
 * @author jiangzhen
 *
 */
@GmtHandlerAnnotation(handler = "GetMainNoticeHandler")
public class GetMainNoticeHandler implements IGmtoolsHandler
{
	private static final Logger logger = LoggerFactory.getLogger(GetMainNoticeHandler.class);

	@Override
	public HashMap<String, Object> getResult(Map<String, Object> json)
	{
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", 1);
		
		logger.debug("getResult(), json={}", json);
		
		// 公告类
		NoticeService service = ServiceContainer.getInstance().getPublicService(NoticeService.class);
		
		// 得到gmt传来的参数
		String type = (String)json.get("type");
		
		// 返回查询到的数据
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		
		if (type.equals(NoticeService.HEALTH))
		{
			// 得到健康公告数据
			MainNotice healthNotice = table.Main_notice_table.select(NoticeService.HEALTH);
			if (healthNotice != null)
			{
				dataList.add(service.mainNotice2Map(healthNotice));
			}
		}
		else if (type.equals(NoticeService.RESIDENT))
		{
			// 得到常驻公告数据
			MainNotice residentNotice = table.Main_notice_table.select(NoticeService.RESIDENT);
			if (residentNotice != null)
			{
				dataList.add(service.mainNotice2Map(residentNotice));
			}
		}
		else if (type.equals("all"))
		{
			// 得到健康公告数据
			MainNotice healthNotice = table.Main_notice_table.select(NoticeService.HEALTH);
			if (healthNotice != null)
			{
				dataList.add(service.mainNotice2Map(healthNotice));
			}
			
			// 得到常驻公告数据
			MainNotice residentNotice = table.Main_notice_table.select(NoticeService.RESIDENT);
			if (residentNotice != null)
			{
				dataList.add(service.mainNotice2Map(residentNotice));
			}
		}
		else
		{
			// 类型错误
			result.put("data", dataList);
			
			logger.debug("getResult(), type error");
			
			return result;
		}
		
		result.put("data", dataList);
		
		logger.debug("Return dataList={}", dataList);
		
		return result;
	}

}
