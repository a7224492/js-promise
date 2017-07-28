package com.kodgames.authserver.action.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.authserver.config.AuthServerConfig;
import com.kodgames.authserver.config.AuthWxConfig;
import com.kodgames.authserver.service.account.AccountService;
import com.kodgames.authserver.service.account.AuthCallback;
import com.kodgames.authserver.service.global.GlobalService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.message.proto.auth.AuthProtoBuf;
import com.kodgames.message.proto.auth.AuthProtoBuf.IAAccountAuthREQ;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

import xbean.LibVersion;

@ActionAnnotation(actionClass = IAAccountAuthREQAction.class, messageClass = IAAccountAuthREQ.class, serviceClass = AccountService.class)
public class IAAccountAuthREQAction extends ProtobufMessageHandler<AccountService, AuthProtoBuf.IAAccountAuthREQ>
{

	final static Logger logger = LoggerFactory.getLogger(IAAccountAuthREQAction.class);

	@Override
	public void handleMessage(Connection connection, AccountService service, IAAccountAuthREQ message, int callback)
	{
		logger.info("IAAccountAuthREQAction message={}", message);
		if (null == connection)
		{
			logger.error("IAAccountAuthREQAction connection is null : message -> {}", message);
			return;
		}

		String channel = message.getChannel();
		String username = message.getUsername();
		String code = message.getCode();
		String refreshToken = message.getRefreshToken();
		logger.debug("IAAccountAuthREQAction : channel={}, username={}, code={}, refreshToken={}", channel, username, code, refreshToken);

		// channel = AuthServerConfig.CHANNEL_WX;
		// refreshToken =
		// "QQ6hl9BkzEozYKGCUGQ3U4idmXksZWAR7euIM9JzJUZMS6L4qyCXT5jn1hqM04gpoJgwLKuru7Dql82O7yr9PMByITxc_M6npSR84XRJnyk";
		// username = "or7A2wVuUymx0IRUk_UkGDQVL59o";

		AuthCallback authCallback = new AuthCallback(message.getPlatform(), message.getAppCode(), message.getProVersion(), message.getLibVersion(), connection, message.getClientConnectionId(),
			callback, message.getDeviceId());

		// 判断是否需要强制更新
		GlobalService globalService = ServiceContainer.getInstance().getPublicService(GlobalService.class);
		LibVersion lv = globalService.getLibraryVersion(message.getPlatform());
		boolean libForceUpdate = lv.getForceUpdate();
		if (globalService.isProductVersionNeedUpdate(message.getProVersion()) || (globalService.isLibraryVersionNeedUpdate(message.getPlatform(), message.getLibVersion()) && libForceUpdate))
		{
			service.handleAuthResult(null, 0, PlatformProtocolsConfig.AI_AUTH_FAILED_FORCE_UPDATE, authCallback, "", "");
			return;
		}

		// 根据渠道进行认证
		switch (channel)
		{
			// 微信渠道认证
			case AuthServerConfig.CHANNEL_WX:
				// 检验设备id的有效性
				if (message.getDeviceId() == null || message.getDeviceId().equals(""))
				{
					service.handleAuthResult(null, 0, PlatformProtocolsConfig.AI_AUTH_FAILED_INVALID_DEVICEID, authCallback, "", "");
					
					logger.info("deviceId is invalid, return : message={}", message);
					return;
				}
				
				/**
				 * @author 老陈 此处应该判断unionid是来自于新的微信平台还是老的微信平台，对于新的微信平台不执行合并过程，按照开关执行绑定过程
				 */
				if (!AuthWxConfig.getInstance().isValid(authCallback.getAppCode()))
				{
					service.handleAuthResult(null, 0, PlatformProtocolsConfig.AI_AUTH_FAILED_INVALID_APPCODE, authCallback, "", "");

					logger.info("appCode is invalid, return : message={}", message);
					return ;
				}
				
				service.authWx(code, username, refreshToken, authCallback);
				break;
				
			// 游客登录认证
			case AuthServerConfig.CHANNEL_TEST:
				service.authTest(username, authCallback);
				break;
				
			// 无效渠道
			default:
				logger.error("IAAccountAuthREQAction invalid channel : channel={}, username={}", channel, username);
				service.handleAuthResult(null, 0, PlatformProtocolsConfig.AI_AUTH_FAILED_INVALID_CHANNEL, authCallback, "", "");
		}
	}

	// 使用username做key进行消息排队, 避免因为断线重连等原因造成玩家两次登录的connection不同而引发多线程同时登录问题
	@Override
	public Object getMessageKey(Connection connection, AuthProtoBuf.IAAccountAuthREQ message)
	{
		return message.getUsername();
	}
}
