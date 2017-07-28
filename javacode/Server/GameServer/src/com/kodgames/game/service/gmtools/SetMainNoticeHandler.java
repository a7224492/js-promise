package com.kodgames.game.service.gmtools;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.notice.NoticeService;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

import xbean.MainNotice;

/**
 * 设置健康公告，健康公告是主页常驻公告
 * @author jiangzhen
 *
 */
@GmtHandlerAnnotation(handler = "SetMainNoticeHandler")
public class SetMainNoticeHandler implements IGmtoolsHandler
{
	private static final Logger logger = LoggerFactory.getLogger(SetMainNoticeHandler.class);

	@Override
	public HashMap<String, Object> getResult(Map<String, Object> json)
	{
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", 1);
		
		logger.debug("SetMainNoticeHandler getResult(), json={}", json);
		
		// 得到公告类
		NoticeService service = ServiceContainer.getInstance().getPublicService(NoticeService.class);

		// 得到gmt参数
		String type = (String)json.get("type");
		
		if (type.equals(NoticeService.HEALTH))
		{
			// 添加健康公告
			MainNotice mainNotice = table.Main_notice_table.update(NoticeService.HEALTH);
			if (mainNotice == null)
			{
				mainNotice = table.Main_notice_table.insert(NoticeService.HEALTH);
			}
			
			// 设置公告内容
			service.json2MainNotice(json, mainNotice);
			
			logger.debug("Main notice type is HEALTH");
		}
		else if (type.equals(NoticeService.RESIDENT))
		{
			// 添加常驻公告
			MainNotice mainNotice = table.Main_notice_table.update(NoticeService.RESIDENT);
			if (mainNotice == null)
			{
				mainNotice = table.Main_notice_table.insert(NoticeService.RESIDENT);
			}
			
			// 设置公告内容
			service.json2MainNotice(json, mainNotice);
			
			logger.debug("Main notice type is RESIDENT");
		}
		else
		{
			// 类型错误
			result.put("data", 0);
			
			logger.debug("Main notice type error");
			
			return result;
		}
		
		result.put("data", 1);
		
		return result;
	}
}
