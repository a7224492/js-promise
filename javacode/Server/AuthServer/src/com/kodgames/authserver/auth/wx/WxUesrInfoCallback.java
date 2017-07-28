package com.kodgames.authserver.auth.wx;

import java.util.HashMap;

import com.kodgames.authserver.service.account.AccountService;
import com.kodgames.authserver.service.account.AuthCallback;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

import limax.zdb.Procedure;

public class WxUesrInfoCallback extends WxCallback
{
	private WxAccessResult wxAuthResult = null;

	public WxUesrInfoCallback(AuthCallback callback, WxAccessResult wxAuthResult)
	{
		super(callback);

		this.wxAuthResult = wxAuthResult;
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
				logger.error("WxHelper refreshAccessToken : json parse error : {}", t);

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

			WxUserInfo wxUserInfo = null;
			try
			{
				wxUserInfo = new WxUserInfo(map);
			}
			catch (Throwable t)
			{
				logger.error("WxHelper refreshAccessToken : wxUserInfo parse error : {}", t);

				AccountService service = ServiceContainer.getInstance().getPublicService(AccountService.class);
				service.handleAuthResult(null, 0, PlatformProtocolsConfig.AI_AUTH_FAILED_WX_INVALID_CODE, callback, "", "");

				return false;
			}

			AccountService service = ServiceContainer.getInstance().getPublicService(AccountService.class);
			try
			{
				WxAuthManager.getInstance().handleUserInfoResponse(wxAuthResult, wxUserInfo, callback);
			}
			catch (Throwable t)
			{
				logger.error("WxHelper refreshAccessToken : handleUserInfoResponse error : {}", t);

				service.handleAuthResult(null, 0, PlatformProtocolsConfig.AI_AUTH_FAILED_WX_INVALID_CODE, callback, "", "");

				return false;
			}

			//service.handleAuthResult( null, PlatformProtocolsConfig.AI_AUTH_SUCCESS, callback);
			
			return true;
		});
		
	}

}
