package com.kodgames.authserver.auth.wx;

import java.util.HashMap;

import com.kodgames.authserver.service.account.AccountService;
import com.kodgames.authserver.service.account.AuthCallback;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

import limax.zdb.Procedure;

public class WxAuthCodeCallback extends WxCallback
{
	public WxAuthCodeCallback(AuthCallback callback)
	{
		super(callback);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onResult(String json)
	{
		Procedure.call(() -> {
			HashMap<String, Object> map = null;
			try
			{
				map = mapper.readValue(json, HashMap.class);
			}
			catch (Throwable t)
			{
				logger.error("WxHelper refreshAccessToken : json parse error: {}", t);
				
				AccountService service = ServiceContainer.getInstance().getPublicService(AccountService.class);
				service.handleAuthResult(null, 0, PlatformProtocolsConfig.AI_AUTH_FAILED_WX_INVALID_CODE, callback, "", "");
				
				return false;
			}
	
			Object errorCode = map.get("errcode");
			if (null != errorCode)
			{
				AccountService service = ServiceContainer.getInstance().getPublicService(AccountService.class);
				service.handleAuthResult(null, 0, PlatformProtocolsConfig.AI_AUTH_FAILED_WX_INVALID_CODE, callback, "", "");
				return false;
			}
	
			WxAccessResult result = null;
			try
			{
				result = new WxAccessResult((String)map.get("openid"), (String)map.get("access_token"),
					(String)map.get("refresh_token"), (String)map.get("unionid"));
			}
			catch (Throwable t)
			{
				t.printStackTrace();
				
				AccountService service = ServiceContainer.getInstance().getPublicService(AccountService.class);
				service.handleAuthResult( null, 0, PlatformProtocolsConfig.AI_AUTH_FAILED_WX_INVALID_CODE, callback, "", "");
				
				return false;
			}
			
			if (!WxHelper.requestUserInfo(result.getOpenId(), result.getAccessToken(), callback, result))
			{
				AccountService service = ServiceContainer.getInstance().getPublicService(AccountService.class);
				service.handleAuthResult( null, 0, PlatformProtocolsConfig.AI_AUTH_FAILED_WX_INVALID_CODE, callback, "", "");
			}
		return true;
		});
	}
}
