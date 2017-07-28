package com.kodgames.game.service.gmtools;

import java.util.HashMap;
import java.util.Map;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.notice.NoticeService;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

@GmtHandlerAnnotation(handler = "ChangeNoticeHandler")
public class ChangeNoticeHandler implements IGmtoolsHandler
{

	@Override
	public HashMap<String, Object> getResult(Map<String, Object> json)
	{
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		result.put("result", 1);
		
		NoticeService service = ServiceContainer.getInstance().getPublicService(NoticeService.class);
		
		int data = service.changeNotice(json);
		
		result.put("data", data);
		
		return result;
	}
	
}
