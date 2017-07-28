package com.kodgames.authserver.action.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.authserver.service.account.AccountService;
import com.kodgames.authserver.service.global.GlobalService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.message.proto.auth.AuthProtoBuf.AIAccountAuthRES;
import com.kodgames.message.proto.auth.AuthProtoBuf.IAMergePlayerInfoRES;
import com.kodgames.message.proto.auth.AuthProtoBuf.LibraryVersionPROTO;
import com.kodgames.message.proto.auth.AuthProtoBuf.ProductVersionPROTO;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

import xbean.AccountInfo;
import xbean.UnionidAccountidBean;

@ActionAnnotation(actionClass = IAMergePlayerInfoRESAction.class, messageClass = IAMergePlayerInfoRES.class, serviceClass = AccountService.class)
public class IAMergePlayerInfoRESAction extends ProtobufMessageHandler<AccountService, IAMergePlayerInfoRES>
{

	final static Logger logger = LoggerFactory.getLogger(IAMergePlayerInfoRESAction.class);

	@Override
	public void handleMessage(Connection connection, AccountService service, IAMergePlayerInfoRES message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

		if (message.getResult() == PlatformProtocolsConfig.GI_MERGE_PLAYER_INFO_FAIL_NO_PLAYER_INFO)
		{
			// 按严重错误处理，直接断开
			return;
		}

		int appCode = message.getPlayerInfo().getAppCode();
		String channel = message.getPlayerInfo().getChannel();
		String username = message.getPlayerInfo().getUsername();

		GlobalService globalService = ServiceContainer.getInstance().getPublicService(GlobalService.class);

		ProductVersionPROTO.Builder pvProto = ProductVersionPROTO.newBuilder();
		pvProto.setDescription(globalService.getProductVersion().getDescription());
		pvProto.setNeedUpdate(false);
		pvProto.setVersion(globalService.getProductVersion().getVersion());

		LibraryVersionPROTO.Builder lvProto = LibraryVersionPROTO.newBuilder();
		lvProto.setDescription(globalService.getLibraryVersion(message.getPlayerInfo().getPlatform()).getDescription());
		lvProto.setForceUpdate(false);
		lvProto.setNeedUpdate(false);
		lvProto.setVersion(globalService.getLibraryVersion(message.getPlayerInfo().getPlatform()).getVersion());
		lvProto.setUrl(globalService.getLibraryVersion(message.getPlayerInfo().getPlatform()).getDescription());

		AccountInfo accountInfo = service.getAccountInfoByUsername(channel, appCode, username);

		if (accountInfo != null && message.getResult() == PlatformProtocolsConfig.GI_MERGE_PLAYER_INFO_SUCCESS)
		{
			UnionidAccountidBean bean =
				service.updateUnionidAccountidTable(message.getPlayerInfo().getAppCode(), accountInfo.getUnionid());
			if (bean != null)
			{
				bean.setAccountId(message.getOldAccountid());
				if (!bean.getMergeList().contains(message.getOldAccountid()))
				{
					bean.getMergeList().add(message.getOldAccountid());
				}
				if (!bean.getMergeList().contains(message.getNewAccountid()))
				{
					bean.getMergeList().add(message.getNewAccountid());
				}
			}

			// 加密jid
			String sign = service.rsaSign(bean.getAccountId(),
				message.getPlayerInfo().getSex(),
				message.getPlayerInfo().getNickname(),
				message.getPlayerInfo().getHeadImageUrl(),
				message.getPlayerInfo().getChannel());
			if (sign != null)
			{
				connection.write(callback,
					AIAccountAuthRES.newBuilder()
						.setResult(PlatformProtocolsConfig.AI_AUTH_SUCCESS)
						.setChannel(message.getPlayerInfo().getChannel())
						.setUsername(accountInfo.getUsername())
						.setRefreshToken(accountInfo.getRefreshToken())
						.setAccountId(bean.getAccountId())
						.setNickname(message.getPlayerInfo().getNickname())
						.setHeadImageUrl(message.getPlayerInfo().getHeadImageUrl())
						.setSex(message.getPlayerInfo().getSex())
						.setProVersion(pvProto.build())
						.setLibVersion(lvProto.build())
						.setClientConnectionId(message.getPlayerInfo().getClientConectionId())
						.setTimestamp(System.currentTimeMillis())
						.setSignature(sign)
						.setUnionid(accountInfo.getUnionid())
						.build());
			}
			else
			{
				connection.write(callback,
					AIAccountAuthRES.newBuilder()
						.setResult(PlatformProtocolsConfig.AI_AUTH_FAILED_UNKNOWN)
						.setChannel("")
						.setUsername("")
						.setRefreshToken("")
						.setAccountId(0)
						.setNickname("")
						.setHeadImageUrl("")
						.setSex(0)
						.setProVersion(pvProto)
						.setLibVersion(lvProto)
						.setClientConnectionId(message.getPlayerInfo().getClientConectionId())
						.setTimestamp(System.currentTimeMillis())
						.setSignature("")
						.build());
				logger.error("unkown reason, there is no accountInfo : channel={}, appCode={}, username={}",
					channel,
					appCode,
					username);
			}
		}
		else
		{
			connection.write(callback,
				AIAccountAuthRES.newBuilder()
					.setResult(PlatformProtocolsConfig.AI_AUTH_FAILED_UNKNOWN)
					.setChannel("")
					.setUsername("")
					.setRefreshToken("")
					.setAccountId(0)
					.setNickname("")
					.setHeadImageUrl("")
					.setSex(0)
					.setProVersion(pvProto)
					.setLibVersion(lvProto)
					.setClientConnectionId(message.getPlayerInfo().getClientConectionId())
					.setTimestamp(System.currentTimeMillis())
					.setSignature("")
					.build());
			logger.error("unkown reason, there is no accountInfo : channel={}, appCode={}, username={}",
				channel,
				appCode,
				username);
		}
	}
}
