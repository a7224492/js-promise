package com.kodgames.authserver.auth.wx;

import com.kodgames.authserver.config.AuthServerConfig;
import com.kodgames.authserver.service.account.AccountService;
import com.kodgames.authserver.service.account.AuthCallback;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.core.util.StrUtils;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

public class WxAuthManager
{
	private static WxAuthManager instance = new WxAuthManager();
	public static final String channel = AuthServerConfig.CHANNEL_WX;
	
	private WxAuthManager()
	{
	}

	public static WxAuthManager getInstance()
	{
		return instance;
	}

	private void authWithCode(final String code, final AuthCallback callback)
	{
		WxHelper.requestAccessToken(code, callback);
//		if (!WxHelper.requestAccessToken(code, callback))
//		{
//			AccountService service = ServiceContainer.getInstance().getPublicService(AccountService.class);
//			service.handleAuthResult(channel, "", PlatformProtocolsConfig.AI_AUTH_FAILED_WX_INVALID_CODE, callback);
//		}
	}
	
	public void handleUserInfoResponse(final WxAccessResult result, final WxUserInfo wxInfo, final AuthCallback callback)
	{
		AccountService service = ServiceContainer.getInstance().getPublicService(AccountService.class);
		service.handleUserInfoResponse(result, wxInfo, callback);
	}

	private void authWithUsername(final String username, final String refreshToken, final AuthCallback callback)
	{
		AccountService service = ServiceContainer.getInstance().getPublicService(AccountService.class);
		service.authWithUsername(username, refreshToken, callback);
	}

	/**
	 * 客户端微信认证
	 */
	public void authClient(final String code, final String username,  final String refreshToken, final AuthCallback callback)
	{
		// code 认证
		if (!StrUtils.empty(code))
		{
			authWithCode(code, callback);
		}
		// openid 认证
		else if (!StrUtils.empty(username) && !StrUtils.empty(refreshToken))
		{
			authWithUsername(username, refreshToken, callback);
		}
		// 参数无效
		else
		{
			AccountService service = ServiceContainer.getInstance().getPublicService(AccountService.class);
			service.handleAuthResult(null, 0, PlatformProtocolsConfig.AI_AUTH_FAILED_INVALID_ARGUMENTS, callback, "", "");
		}
	}
	
}
