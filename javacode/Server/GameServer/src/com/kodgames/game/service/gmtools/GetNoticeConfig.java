package com.kodgames.game.service.gmtools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.message.MessageService;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

//http://10.23.1.53:13101/gmtools?{"handler":"GetNoticeConfig","server_id":16842753}
/**
 * 获取弹窗公告基本配置
 * @author Administrator
 *
 */
@GmtHandlerAnnotation(handler = "GetNoticeConfig")
public class GetNoticeConfig implements IGmtoolsHandler
{

	@Override
	public HashMap<String, Object> getResult(Map<String, Object> args)
	{
		MessageService service = ServiceContainer.getInstance().getPublicService(MessageService.class);
		HashMap<String, Object> result = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();
		xbean.PopUpMessageInfo info = service.selectOrInitTable();
		int mode = info.getMode();
		int pop = info.getPop();
		List<xbean.PopUpMessageTimes> times =info.getTimes();
		for(xbean.PopUpMessageTimes time : times)
		{
			HashMap<String, String> map = new HashMap<>();
			map.put("start", time.getStart());
			map.put("end", time.getEnd());
		}
		
		data.put("mode", mode);
		data.put("pop", pop);
		data.put("times", times);
		
		result.put("result", 1);
		result.put("data", data);
		return result;
	}

}
