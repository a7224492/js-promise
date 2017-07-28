package com.kodgames.game.service.gmtools;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.game.service.notice.NoticeService;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

/**
 * 删除主页公告
 * @author jiangzhen
 *
 */
@GmtHandlerAnnotation(handler = "DeleteMainNoticeHandler")
public class DeleteMainNoticeHandler implements IGmtoolsHandler
{
	private static final Logger logger = LoggerFactory.getLogger(DeleteMainNoticeHandler.class);

	@Override
	public HashMap<String, Object> getResult(Map<String, Object> json)
	{
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", 1);
		
		logger.debug("getResult(), json={}", json);
		
		// 得到公告类型
		String type = (String)json.get("type");
		
		if (type.equals(NoticeService.HEALTH))
		{
			// 删除健康公告
			table.Main_notice_table.delete(NoticeService.HEALTH);
			
			logger.debug("Delete HEALTH notice");
		}
		else if (type.equals(NoticeService.RESIDENT))
		{
			// 删除常驻公告
			table.Main_notice_table.delete(NoticeService.RESIDENT);
			
			logger.debug("Delete RESIDENT notice");
		}
		else
		{
			// 类型错误
			result.put("data", 0);
			
			logger.debug("getResult(), type error");
			
			return result;
		}
		
		result.put("data", 1);
		
		return result;
	}

}
