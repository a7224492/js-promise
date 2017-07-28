package com.kodgames.game.service.gmtools;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.contact.ContactService;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

@GmtHandlerAnnotation(handler = "ChangeLimitContactHandler")
public class ChangeLimitContactHandler implements IGmtoolsHandler
{
	Logger logger = LoggerFactory.getLogger(ChangeLimitContactHandler.class);

	public HashMap<String, Object> getResult(Map<String, Object> json)
	{
		HashMap<String, Object> result = new HashMap<String, Object>();
		Object idObj = json.get("ID");
		int id = (null == idObj) ? 0 : (int)idObj;
		
		Object agencyIdObj = json.get("agencyID");
		int agencyId = (null == agencyIdObj) ? 0 : (int)agencyIdObj;
		
		Object contentObj = json.get("content");
		String content = (null == contentObj) ? "" : (String)contentObj;
		
		Object senderObj = json.get("sender");
		String sender = (null == senderObj) ? "" : (String)senderObj;
		
		Object startTimeObject = json.get("startTime");
		long startTimeSecond = (null == startTimeObject) ? 0 : (long)startTimeObject;
		
		Object endTimeObject = json.get("endTime");
		long endTimeSecond = (null == endTimeObject) ? 0 : (long)endTimeObject;
		
		int operationType = (int)json.get("operationType"); // 0 增加， 1 修改, 2 删除, 3 查询

		ContactService contactService = ServiceContainer.getInstance().getPublicService(ContactService.class);
		Object data = contactService.changeLimitContact(id,
			agencyId,
			content,
			startTimeSecond * 1000,
			endTimeSecond * 1000,
			operationType,
			sender);
		result.put("result", 1);
		result.put("data", data);

		logger.info("Gmtools ChangeLimitContactHandler {}", result);
		return result;
	}
}
