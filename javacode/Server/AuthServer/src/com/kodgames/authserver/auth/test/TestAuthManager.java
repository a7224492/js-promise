package com.kodgames.authserver.auth.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.authserver.config.AuthServerConfig;
import com.kodgames.authserver.service.account.AccountService;
import com.kodgames.authserver.service.account.AuthCallback;
import com.kodgames.authserver.service.account.DeviceService;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

import xbean.AccountInfo;

public class TestAuthManager
{
	final static Logger logger = LoggerFactory.getLogger(TestAuthManager.class);
	private static final TestAuthManager instance = new TestAuthManager();
	public static final String channel = AuthServerConfig.CHANNEL_TEST;

	private TestAuthManager()
	{
	}

	public static TestAuthManager getInstance()
	{
		return instance;
	}

	public void authClient(final String username, final AuthCallback callback)
	{
		AccountService service = ServiceContainer.getInstance().getPublicService(AccountService.class);
		AccountInfo accountInfo = service.getAccountInfoByUsername(channel, callback.getAppCode(), username);
		if (null == accountInfo)
		{
			accountInfo = service.createAccount(channel, callback.getAppCode(), username);
			if (accountInfo != null)
			{
				accountInfo.setNickname(username);
				accountInfo.setSex(1);		// male
				accountInfo.setHeadImgUrl("head" + "-" + username);
				accountInfo.setRefreshToken("testToken");
			}
		}
		DeviceService deviceService = ServiceContainer.getInstance().getPublicService(DeviceService.class);
		deviceService.saveDeviceIdUnionId(callback.getDeviceId(), "");
		deviceService.saveDeviceIdAccountId(callback.getDeviceId(), accountInfo.getAccountId());
		service.handleAuthResult(accountInfo, accountInfo.getAccountId(), PlatformProtocolsConfig.AI_AUTH_SUCCESS, callback, "", "");
	}
}
