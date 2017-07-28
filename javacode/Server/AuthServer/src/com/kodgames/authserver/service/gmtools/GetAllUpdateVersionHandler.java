package com.kodgames.authserver.service.gmtools;

import java.util.HashMap;
//import java.util.Iterator;
import java.util.Map;
//import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import com.kodgames.authserver.service.global.GlobalService;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

//http://localhost:13101/gmtools?{"handler":"GetAllUpdateVersionHandler","server_id":17104897}

@GmtHandlerAnnotation(handler = "GetAllUpdateVersionHandler")
public class GetAllUpdateVersionHandler implements IGmtoolsHandler
{
	private static final Logger logger = LoggerFactory.getLogger(GetAllUpdateVersionHandler.class);
	
	public HashMap<String, Object> getResult(Map<String, Object> args)
	{
		HashMap<String, Object> result = new HashMap<>();

		GlobalService service = ServiceContainer.getInstance().getPublicService(GlobalService.class);
		List<HashMap<String, Object>> records = service.getUpdateVersion();
		
		//测试，输出每一项
//		for(HashMap<String, Object> hs:records)
//		{
//			Set<Map.Entry<String, Object>> es = hs.entrySet();
//			Iterator<Map.Entry<String, Object>> it = es.iterator();
//			
//			logger.info("GetAllUpdateVersionHandler Each Item: ");
//			while (it.hasNext())
//			{
//				Map.Entry<String, Object> en = it.next();
//				String key = en.getKey();
//				Object value = en.getValue(); 
//				logger.info("key=" + key + " value=" + value);
//			}
//		}

		if (records.isEmpty())
		{
			result.put("result", 1);
			result.put("data", "");
			
			logger.info("GetAllUpdateVersionHandler  Failed");
		}
		else
		{
			result.put("result", 1);
			result.put("data", records);
			
			logger.info("GetAllUpdateVersionHandler  Success");
		}
		
		return result;
	}
}
