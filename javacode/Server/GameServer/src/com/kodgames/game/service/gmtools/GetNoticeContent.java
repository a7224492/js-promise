package com.kodgames.game.service.gmtools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.message.MessageService;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

import xbean.PopUpMessageTypes;

//http://10.23.1.53:13101/gmtools?{"handler":"GetNoticeContent", "tab":1,"server_id":16842753}
/**
 * 获取弹窗公告的内容
 * @author 洪冲
 *
 */
@GmtHandlerAnnotation(handler = "GetNoticeContent")
public class GetNoticeContent implements IGmtoolsHandler
{

	@Override
	public HashMap<String, Object> getResult(Map<String, Object> args)
	{
		MessageService service = ServiceContainer.getInstance().getPublicService(MessageService.class);
		HashMap<String, Object> result = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();
		int tab = (int)args.get("tab");
		
		List<PopUpMessageTypes> types = service.selectOrInitTable().getTypes();
		for(PopUpMessageTypes type: types) {
			//tab是文字形式
			if(type.getTab() == tab) {
				if(MessageService.TEXT_TYPE == type.getStyle()) {			
					data.put("type", type.getStyle());
					data.put("body", type.getContent().get(0));
				}else {
					data.put("type", type.getStyle());
					data.put("urls", type.getContent());
				}
				data.put("tab", tab);
				result.put("result", 1);
				result.put("data", data);
				break;
			}
		}
	
		
		//数据库中没有匹配到对应类型
		if(result.size() == 0) {		
			result.put("result", 0);
			result.put("data", "");
		}
		
		return result;
	}
	
}
