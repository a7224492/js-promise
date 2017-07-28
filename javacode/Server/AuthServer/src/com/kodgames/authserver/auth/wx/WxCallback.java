package com.kodgames.authserver.auth.wx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kodgames.authserver.config.AuthServerConfig;
import com.kodgames.authserver.service.account.AuthCallback;
import com.kodgames.core.net.http.HttpRequestCallback;

public abstract class WxCallback implements HttpRequestCallback
{
	protected static final ObjectMapper mapper = new ObjectMapper();
	protected static final Logger logger = LoggerFactory.getLogger(WxCallback.class);
	
	public String channel = AuthServerConfig.CHANNEL_WX;
	
	protected AuthCallback callback = null;

	public WxCallback(AuthCallback callback)
	{
		this.callback = callback;
	}
}
