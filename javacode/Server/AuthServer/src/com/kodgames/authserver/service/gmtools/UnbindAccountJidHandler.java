package com.kodgames.authserver.service.gmtools;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.authserver.service.account.AccountService;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

import xbean.JidBindRecordBean;
import xbean.RecordList;

@GmtHandlerAnnotation(handler = "UnbindAccountJidHandler")
public class UnbindAccountJidHandler implements IGmtoolsHandler
{
	private static final Logger logger = LoggerFactory.getLogger(UnbindAccountJidHandler.class);

	@Override
	public HashMap<String, Object> getResult(Map<String, Object> json)
	{
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", 1);

		int accountJid = (Integer)json.get("accountJid");
		int appCode = (Integer)json.get("appCode");
		String nickname = (String)json.get("nickname");
		AccountService service = ServiceContainer.getInstance().getPublicService(AccountService.class);
		nickname = service.filterEmoji(nickname);
		
		
		logger.debug("UnbindAccountJidHandler() : accountJid={}, appCode={}, nickname={}", accountJid, appCode, nickname);

		RecordList recordList = table.Jid_bind_record_table.select(accountJid);
		if (recordList == null)
		{
			result.put("data", 0);

			logger.info("can't find record by accountJid : accountJid={}", accountJid);
			return result;
		}

		// 对用户进行解绑
		for (JidBindRecordBean record : recordList.getRecord())
		{
			if (record.getAppCode() == appCode && record.getNickname().equals(nickname))
			{
				record.setStatus(AccountService.UN_BIND);
				result.put("data", 1);

				logger.info("unbind accountJid : accountJid={}, accountid={}, appCode={}, openid={}, nickname={}",
					accountJid,
					record.getAccountId(),
					appCode,
					record.getOpenid(),
					nickname);
				
				return result;
			}
		}
		
		result.put("data", 0);

		return result;
	}

}
