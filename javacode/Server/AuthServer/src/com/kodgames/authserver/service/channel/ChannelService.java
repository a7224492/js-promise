package com.kodgames.authserver.service.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xbean.VersionUpdateBean;

import com.kodgames.authserver.service.global.GlobalService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.message.proto.auth.AuthProtoBuf.AIVersionUpdateRES;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

/**
 * 版本更新信息service
 * 
 * @author 毛建伟
 */
public class ChannelService extends PublicService
{

	private static Logger logger = LoggerFactory.getLogger(ChannelService.class);
	private static final long serialVersionUID = 8181261664236907184L;

	/**
	 * 判断更新信息
	 * 
	 * @param bean 服务器存的更新信息
	 * @param clientLibVersion 客户端的libVersion（运行库版本号）
	 * @return 返回对应的错误码
	 */
	public int checkLibUpdate(VersionUpdateBean bean, String clientLibVersion)
	{
		GlobalService service = ServiceContainer.getInstance().getPublicService(GlobalService.class);
		int result = 0;
		// 比较运行库版本
		boolean normalCompare = service.compareVersion(clientLibVersion, bean.getLibVersion());
		// 客户端运行库版本大于等于服务器版本
		if (!normalCompare)
		{
			// 相等为认证成功
			if (bean.getLibVersion().equals(clientLibVersion))
				result = PlatformProtocolsConfig.AI_VERSION_UPDATE_SUCCESS;
			else
			{
				// 审核版本
				result = PlatformProtocolsConfig.AI_VERSION_UPDATE_FAIL_REVIEW_VERSION;
			}
		}
		// 客户端的版本号低于服务器的版本号
		else
		{
			// 是否小于支持的最低版本号
			boolean lastCompare = service.compareVersion(clientLibVersion, bean.getLastLibVersion());
			if (lastCompare)
			{
				// 强更
				result = PlatformProtocolsConfig.AI_VERSION_UPDATE_FAIL_FORCE_UPDATE;
			}
			else
			{
				// 手动更新
				result = PlatformProtocolsConfig.AI_VERSION_UPDATE_FAIL_MANUAL_UPDATE;
			}
		}
		return result;
	}

	/**
	 * 判断是否需要热更
	 * 
	 * @param serverProVersion 客户端版本
	 * @param clientProVersion 服务器版本
	 * @return
	 */
	public boolean isProductVersionNeedUpdate(String serverProVersion, String clientProVersion)
	{
		try
		{
			int serverVersion = Integer.parseInt(serverProVersion);
			int clientVersion = Integer.parseInt(clientProVersion);
			return clientVersion < serverVersion;
		}
		catch (Throwable t)
		{
			return true;
		}
	}

	/**
	 * 回复interface获取更新信息
	 * 
	 * @param connection 连接
	 * @param channel interface传来的客户端channel
	 * @param subChannel interface传来的客户端subchannel
	 * @param proVersion interface传来的客户端传来的proVersion(产品版本号)
	 * @param libVersion interface传来的客户端传来的libVersion(运行库版本号)
	 * @param callBack
	 */
	public void onGetVersionInfo(Connection connection, String channel, String subChannel, String proVersion, String libVersion, int clientConnectId, int callBack)
	{
		VersionUpdateBean serverVersionInfo = table.Channel_version_table.select(channel + "_" + subChannel);
		AIVersionUpdateRES.Builder versionUpdateRES = AIVersionUpdateRES.newBuilder();
		// 没有渠道信息
		if (null == serverVersionInfo)
		{
			logger.warn("serverVersionInfo is null, channel is : {}, subchannel is : {}", channel, subChannel);
			connection.write(callBack, versionUpdateRES.setResult(PlatformProtocolsConfig.AI_VERSION_UPDATE_FAIL_NO_CHANNEL_INFO)
				.setChannel("")
				.setSubchannel("")
				.setLibUrl("")
				.setLibVersion("")
				.setLastLibVersion("")
				.setProNeedUpdate(false)
				.setProUrl("")
				.setProVersion("")
				.setReviewUrl("")
				.setReviewVersion("")
				.setClientConnectionId(clientConnectId)
				.build());
			return;
		}
		int libUpdate = checkLibUpdate(serverVersionInfo, libVersion);
		connection.write(callBack,
			versionUpdateRES.setResult(libUpdate)
				.setChannel(serverVersionInfo.getChannel())
				.setSubchannel(serverVersionInfo.getSubchannel())
				.setLibVersion(serverVersionInfo.getLibVersion())
				.setLastLibVersion(serverVersionInfo.getLastLibVersion())
				.setLibUrl(serverVersionInfo.getLibUrl())
				.setProVersion(serverVersionInfo.getProVersion())
				.setProUrl(serverVersionInfo.getProUrl())
				.setProNeedUpdate(isProductVersionNeedUpdate(serverVersionInfo.getProVersion(), proVersion))
				.setReviewVersion(libUpdate == PlatformProtocolsConfig.AI_VERSION_UPDATE_FAIL_REVIEW_VERSION ? serverVersionInfo.getReviewVersion() : "")
				.setReviewUrl(libUpdate == PlatformProtocolsConfig.AI_VERSION_UPDATE_FAIL_REVIEW_VERSION ? serverVersionInfo.getReviewUrl() : "")
				.setClientConnectionId(clientConnectId)
				.build());
	}
}
