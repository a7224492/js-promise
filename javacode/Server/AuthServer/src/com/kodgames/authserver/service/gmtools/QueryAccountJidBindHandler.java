package com.kodgames.authserver.service.gmtools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

import xbean.JidBindRecordBean;
import xbean.RecordList;

@GmtHandlerAnnotation(handler = "QueryAccountJidBindHandler")
public class QueryAccountJidBindHandler implements IGmtoolsHandler
{

	@Override
	public HashMap<String, Object> getResult(Map<String, Object> json)
	{
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", 1);
		
		// 得到gmt发来的参数
		int accountJid = (Integer)json.get("accountJid");
		
		// 返回给gmt的参数
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

		RecordList recordList = table.Jid_bind_record_table.select(accountJid);
		if (recordList == null)
		{
			result.put("data", returnList);
			return result;
		}
		
		for (JidBindRecordBean record : recordList.getRecord())
		{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("deviceId", record.getDeviceId());
			map.put("appCode", record.getAppCode());
			map.put("accountId", record.getAccountId());
			map.put("openid", record.getOpenid());
			map.put("nickname", record.getNickname());
			map.put("status", record.getStatus());
			
			returnList.add(map);
		}
		
		result.put("data", returnList);
		
		return result;
	}

}
