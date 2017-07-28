package com.kodgames.authserver.service.account;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cbean.AccountTableKey;
import limax.zdb.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.authserver.auth.test.TestAuthManager;
import com.kodgames.authserver.auth.wx.WxAccessResult;
import com.kodgames.authserver.auth.wx.WxAuthManager;
import com.kodgames.authserver.auth.wx.WxUserInfo;
import com.kodgames.authserver.config.AuthServerConfig;
import com.kodgames.authserver.config.AuthWxConfig;
import com.kodgames.authserver.config.Wxapp;
import com.kodgames.authserver.config.Wxdev;
import com.kodgames.authserver.service.global.GlobalData;
import com.kodgames.authserver.service.global.GlobalService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.core.util.rsa.RSACoder;
import com.kodgames.corgi.core.util.rsa.RsaConfig;
import com.kodgames.message.proto.auth.AuthProtoBuf.AIAccountAuthRES;
import com.kodgames.message.proto.auth.AuthProtoBuf.AIMergePlayerInfoREQ;
import com.kodgames.message.proto.auth.AuthProtoBuf.MergePlayerInfoPROTO;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

import table.Account_table;
import table.Account_table_1;
import table.Account_table_10;
import table.Account_table_2;
import table.Account_table_3;
import table.Account_table_4;
import table.Account_table_5;
import table.Account_table_6;
import table.Account_table_7;
import table.Account_table_8;
import table.Account_table_9;
import xbean.*;

public class AccountService extends PublicService
{
	private static final long serialVersionUID = -4412747051426230163L;

	private static Logger logger = LoggerFactory.getLogger(AccountService.class);

	private static final int scale = 100;

	/**
	 * 1：绑定过 2：解绑过
	 */
	public static final int BIND = 1;
	public static final int UN_BIND = 2;

	/**
	 * 用来存储appCode对应的uid表名
	 * 
	 */
	private Map<Integer, String> appCodeMapTableName = new ConcurrentHashMap<Integer, String>();

	@SuppressWarnings("unused")
	private synchronized int getNewAccountId()
	{
		AccountIDSeed accoundIDSeed = table.Global_accountid.update(AuthServerConfig.TABLE_ACCOUNTID_SEED_KEY);
		if (null == accoundIDSeed)
		{
			accoundIDSeed = table.Global_accountid.insert(AuthServerConfig.TABLE_ACCOUNTID_SEED_KEY);
			accoundIDSeed.setSeed(AuthServerConfig.ACCOUNT_ID_INIT_SEED);
		}

		if (accoundIDSeed == null)
		{
			logger.error("fatal error in the ZDB, cant insert record in Global_accountid");
			return 0;
		}

		accoundIDSeed.setSeed(accoundIDSeed.getSeed() + 1);

		int code = (int)(Math.random() * scale);
		int accountId = (accoundIDSeed.getSeed() * scale + code);
		return accountId;
	}

	public String getAccountKey(final String channel, final String username)
	{
		if (null == channel || null == username)
		{
			return null;
		}

		return channel + "@" + username;
	}

	public AccountInfo createAccount(final String channel, final int appCode, final String username)
	{
		String key = getAccountKey(channel, username);
		if (null == key)
		{
			logger.error("AccountManager createAccount -> key is null : channel={}, username={}.", channel, username);
			return null;
		}

		AccountInfo accountInfo = null;
		accountInfo = insertAccountInfo(appCode, key);
		if (null == accountInfo)
		{
			return selectAccountInfo(appCode, key);
		}

		int accountId = getNewAccountId();
		if (accountId == 0)
		{
			return null;
		}
		accountInfo.setAccountId(accountId);
		accountInfo.setChannel(channel);
		accountInfo.setUsername(username);
		accountInfo.setCreateTime(System.currentTimeMillis());
		accountInfo.setAuthTime(System.currentTimeMillis());

		return accountInfo;
	}

	public AccountInfo getAccountInfoByUsername(final String channel, final int appCode, final String username)
	{
		String key = getAccountKey(channel, username);
		if (null == key)
		{
			return null;
		}

		return selectAccountInfo(appCode, key);
	}

	public void authTest(final String username, final AuthCallback callback)
	{
		TestAuthManager.getInstance().authClient(username, callback);
	}

	public void authWx(final String code, final String username, final String freshtoken,
		final AuthCallback authCallback)
	{
		WxAuthManager.getInstance().authClient(code, username, freshtoken, authCallback);
	}

	public void handleAuthResult(AccountInfo account, int accountJid, final int error, final AuthCallback authcallback,
		String openid, String unionid)
	{
		int result = error;
		if (PlatformProtocolsConfig.AI_AUTH_SUCCESS == result && null == account)
		{
			logger.error("Result is AI_AUTH_SUCCESS, but account is null. clientConnection={}, error={}",
				authcallback.getClientConnectionId(),
				result);
			result = PlatformProtocolsConfig.AI_AUTH_FAILED_UNKNOWN;
		}

		GlobalService globalService = ServiceContainer.getInstance().getPublicService(GlobalService.class);
		ProVersion pv = globalService.getProductVersion();
		LibVersion lv = globalService.getLibraryVersion(authcallback.getPlatform());

		if (result == PlatformProtocolsConfig.AI_AUTH_SUCCESS && account != null)
		{
			logger.debug("Enter handlerAuthResult() -> accountId={}, appCode={}, openid={}, unionid={}, deviceId={}",
				account.getAccountId(),
				authcallback.getAppCode(),
				openid,
				account.getUnionid(),
				authcallback.getDeviceId());
			
			// 为玩家绑定可跨APP的账号
			if (AuthWxConfig.getInstance().allowBind(authcallback.getAppCode())
				&& !isJidBind(accountJid, authcallback.getAppCode(), openid)
				&& !isDeviceIdBind(authcallback.getDeviceId()))
			{
				DeviceService deviceService = ServiceContainer.getInstance().getPublicService(DeviceService.class);
				int jid = deviceService.getJoinAccountId(account);

				// 记录设备id已经被绑定过
				DeviceIdBindRecordBean record = table.Deviceid_bind_record_table.insert(authcallback.getDeviceId());
				if (record == null)
				{
					jid = account.getAccountId();
					logger.warn("deviceid bind record table update : deviceId={}", authcallback.getDeviceId());
				}
				else
				{
					record.setAccountId(jid);
					logger.warn("Record deviceId bind : deviceId={}, appCode={}, openid={}, jid={}",
							authcallback.getDeviceId(),
							authcallback.getAppCode(),
							openid,
							jid);
				}

				// 把绑定的结果，当成新平台的jid
				UnionidAccountidBean bean =
					updateUnionidAccountidTable(authcallback.getAppCode(), account.getUnionid());
				if (bean == null)
				{
					bean = insertUnionidAccountidTable(authcallback.getAppCode(), account.getUnionid());
				}
				if (bean != null)
				{
					bean.setAccountId(jid);
				}
				logger.warn("Set uid_jid : uid={}, jid={}", account.getUnionid(), jid);

				// 得到绑定后的jid
				accountJid = jid;

				// 记录一下这个用户绑定过了
				RecordList list =  table.Jid_bind_record_table.update(accountJid);
				if (list == null)
				{
					list = table.Jid_bind_record_table.insert(accountJid);
				}
				JidBindRecordBean jidBindRecord = new JidBindRecordBean();
				jidBindRecord.setAccountId(account.getAccountId());
				jidBindRecord.setAppCode(authcallback.getAppCode());
				jidBindRecord.setDeviceId(authcallback.getDeviceId());
				jidBindRecord.setStatus(BIND);
				jidBindRecord.setNickname(account.getNickname());
				jidBindRecord.setOpenid(openid);
				if (!list.getRecord().contains(jidBindRecord))
				{
					list.getRecord().add(jidBindRecord);
					logger.warn("Record jid bind : jid={}, appCode={}, openid={}",
						accountJid,
						authcallback.getAppCode(),
						openid);
				}

				logger.warn(
					"AccountService handleBind : error={}, refreshToken = {} accountId={}, nickname={}, sex={}, headImgUrl={}",
					result,
					account.getRefreshToken(),
					accountJid,
					account.getNickname(),
					account.getSex(),
					account.getHeadImgUrl());
			}
			else if (isUnbind(accountJid, authcallback.getAppCode(), openid))
			{
				accountJid = account.getAccountId();
			}
			
			if (isZeroDeviceId(account.getDeviceId()))
			{
				accountJid = account.getAccountId();
			}

			logger.debug(
				"AccountService handleAuthResult : error={}, refreshToken = {} accountId={}, nickname={}, sex={}, headImgUrl={}",
				result,
				account.getRefreshToken(),
				accountJid,
				account.getNickname(),
				account.getSex(),
				account.getHeadImgUrl());
			
			// 对jid进行加密
			String sign = rsaSign(accountJid, account.getSex(), account.getNickname(), account.getHeadImgUrl(), account.getChannel());
			if (sign == null)
			{
				result = PlatformProtocolsConfig.AI_AUTH_FAILED_UNKNOWN;
				sign = "";
			}
			/**
			 * 白名单不放开的时候如果玩家登录失败，这个时候account会存入无效的空字符串
			 *
			 * by 毛建伟
			 */
			String username = account.getUsername();
			// 白名单判断
			WhiteListService whiteListService = ServiceContainer.getInstance().getPublicService(WhiteListService.class);
			if (whiteListService.getEnabled() && !whiteListService.inList(accountJid)) {
				result = PlatformProtocolsConfig.AI_AUTH_FAILED_INVALID_USERNAME;

				// 返回无效的信息给客户端, 这样客户端就无法绕过AuthServer直接去GameServer登录
				sign = "";
				accountJid = 0;
				unionid = "";
				username = "";
			}
			
			 authcallback.getInterfaceConnection().write(authcallback.getClientCallback(),
			 AIAccountAuthRES.newBuilder()
			 .setResult(result)
			 .setChannel(account.getChannel())
			 .setUsername(username)
			 .setRefreshToken(account.getRefreshToken())
			 .setAccountId(accountJid)
			 .setNickname(account.getNickname())
			 .setHeadImageUrl(account.getHeadImgUrl())
			 .setSex(account.getSex())
			 .setClientConnectionId(authcallback.getClientConnectionId())
			 .setProVersion(GlobalData.proVersionBeanToProto(pv, authcallback.getProVersion()))
			 .setLibVersion(
			 GlobalData.libVersionBeanToProto(lv, authcallback.getPlatform(), authcallback.getLibVersion()))
			 .setTimestamp(System.currentTimeMillis())
			 .setSignature(sign)
			 .setUnionid(unionid)
			 .build());
		}
		else
		{
			logger.info("clientConnection={}, error={}", authcallback.getClientConnectionId(), result);
			 authcallback.getInterfaceConnection().write(authcallback.getClientCallback(),
			 AIAccountAuthRES.newBuilder()
			 .setResult(result)
			 .setChannel("")
			 .setUsername("")
			 .setRefreshToken("")
			 .setAccountId(0)
			 .setNickname("")
			 .setHeadImageUrl("")
			 .setSex(0)
			 .setClientConnectionId(authcallback.getClientConnectionId())
			 .setProVersion(GlobalData.proVersionBeanToProto(pv, authcallback.getProVersion()))
			 .setLibVersion(
			 GlobalData.libVersionBeanToProto(lv, authcallback.getPlatform(), authcallback.getLibVersion()))
			 .setTimestamp(System.currentTimeMillis())
			 .setSignature("")
			 .build());
		}
	}

	private boolean isZeroDeviceId(String deviceId)
	{
		return deviceId.equals(DeviceService.ZERO_DEVICE_ID);
	}

	private boolean isUnbind(int accountJid, int appCode, String openid)
	{
		RecordList list = table.Jid_bind_record_table.select(accountJid);
		if (list == null)
		{
			logger.debug("account is not unbind : jid={}, appCode={}, openid={}", accountJid, appCode, openid);
			return false;
		}

		for (JidBindRecordBean record : list.getRecord())
		{
			if (record.getAppCode() == appCode && record.getOpenid().equals(openid) && record.getStatus() == UN_BIND)
			{
				logger.debug("account is unbind : jid={}, appCode={}, openid={}", accountJid, appCode, openid);
				return true;
			}
		}

		logger.debug("account is not unbind : jid={}, appCode={}, openid={}", accountJid, appCode, openid);
		return false;
	}

	/**
	 * 判断这个用户有没有被绑定过
	 * 
	 * @param jid 用户的jid
	 * @return true 被绑定过
	 */
	private boolean isJidBind(int jid, int appCode, String openid)
	{
		// handleAuthResult后续还会update该表, 避免锁升级的死锁BUG, 这里先对其加写锁
		RecordList recordList = table.Jid_bind_record_table.update(jid);
		if (recordList == null)
		{
			logger.debug("accountJid is not bind : jid={}, appCode={}, openid={}", jid, appCode, openid);
			return false;
		}

		for (JidBindRecordBean record : recordList.getRecord())
		{
			if (record.getAppCode() == appCode && record.getOpenid().equals(openid) && record.getStatus() == BIND)
			{
				logger.debug("accountJid is bind : jid={}, appCode={}, openid={}", jid, appCode, openid);
				return true;
			}
		}

		logger.debug("accountJid is not bind : jid={}, appCode={}, openid={}", jid, appCode, openid);
		return false;
	}

	/**
	 * 判断设备id是否被绑定过
	 * 
	 * @param deviceId 设备id
	 * @return true 被绑定过
	 */
	private boolean isDeviceIdBind(String deviceId)
	{
		// handleAuthResult后续会升级该锁, 为避免锁升级引起死锁的BUG, 直接获取写锁
		DeviceIdBindRecordBean record = table.Deviceid_bind_record_table.update(deviceId);
		if (record == null)
		{
			logger.debug("Device is not bind : deviceId={}", deviceId);
			return false;
		}
		else
		{
			logger.debug("Device is bind : deviceId={}", deviceId);
			return true;
		}
	}

	private AccountInfo selectAccountInfo(int appCode, String key)
	{
		AccountTableKey tableKey = new AccountTableKey(key, appCode);
		return table.Account_table_new.select(tableKey);
	}

	private AccountInfo insertAccountInfo(int appCode, String key)
	{
		switch (appCode)
		{
			case AuthServerConfig.APP_CODE_DEFAULT:
				return Account_table.insert(key);
			case AuthServerConfig.APP_CODE_QIYE:
				return Account_table_1.insert(key);
			case AuthServerConfig.APP_CODE_PURCHASE:
				return Account_table_2.insert(key);
			case AuthServerConfig.APP_CODE_3:
				return Account_table_3.insert(key);
			case AuthServerConfig.APP_CODE_4:
				return Account_table_4.insert(key);
			case AuthServerConfig.APP_CODE_5:
				return Account_table_5.insert(key);
			case AuthServerConfig.APP_CODE_6:
				return Account_table_6.insert(key);
			case AuthServerConfig.APP_CODE_7:
				return Account_table_7.insert(key);
			case AuthServerConfig.APP_CODE_8:
				return Account_table_8.insert(key);
			case AuthServerConfig.APP_CODE_9:
				return Account_table_9.insert(key);
			case AuthServerConfig.APP_CODE_10:
				return Account_table_10.insert(key);
		}
		return null;
	}

	public AccountInfo updateAccountInfo(int appCode, String key)
	{
		switch (appCode)
		{
			case AuthServerConfig.APP_CODE_DEFAULT:
				return Account_table.update(key);
			case AuthServerConfig.APP_CODE_QIYE:
				return Account_table_1.update(key);
			case AuthServerConfig.APP_CODE_PURCHASE:
				return Account_table_2.update(key);
			case AuthServerConfig.APP_CODE_3:
				return Account_table_3.update(key);
			case AuthServerConfig.APP_CODE_4:
				return Account_table_4.update(key);
			case AuthServerConfig.APP_CODE_5:
				return Account_table_5.update(key);
			case AuthServerConfig.APP_CODE_6:
				return Account_table_6.update(key);
			case AuthServerConfig.APP_CODE_7:
				return Account_table_7.update(key);
			case AuthServerConfig.APP_CODE_8:
				return Account_table_8.update(key);
			case AuthServerConfig.APP_CODE_9:
				return Account_table_9.update(key);
			case AuthServerConfig.APP_CODE_10:
				return Account_table_10.update(key);
		}
		return null;
	}

	public UnionidAccountidBean selectUnionidAccountidTable(int appCode, String unionid)
	{
		String tableName = getTableNameByAppCode(appCode);
		if (tableName == null)
		{
			logger.info("can't find table name by appCode, return null : appCode={}", appCode);
			return null;
		}

		switch (tableName)
		{
			case "unionid_accountid_table":
				return table.Unionid_accountid_table.select(unionid);
			case "unionid_accountid_table_1":
				return table.Unionid_accountid_table_1.select(unionid);
		}

		return null;
	}

	public UnionidAccountidBean updateUnionidAccountidTable(int appCode, String unionid)
	{
		String tableName = getTableNameByAppCode(appCode);
		if (tableName == null)
		{
			logger.info("can't find table name by appCode, return null : appCode={}", appCode);
			return null;
		}

		switch (tableName)
		{
			case "unionid_accountid_table":
				return table.Unionid_accountid_table.update(unionid);
			case "unionid_accountid_table_1":
				return table.Unionid_accountid_table_1.update(unionid);
		}

		return null;
	}

	public UnionidAccountidBean insertUnionidAccountidTable(int appCode, String unionid)
	{
		String tableName = getTableNameByAppCode(appCode);
		if (tableName == null)
		{
			logger.info("can't find table name by appCode, return null : appCode={}", appCode);
			return null;
		}

		switch (tableName)
		{
			case "unionid_accountid_table":
				return table.Unionid_accountid_table.insert(unionid);
			case "unionid_accountid_table_1":
				return table.Unionid_accountid_table_1.insert(unionid);
		}

		return null;
	}

	/**
	 * 通过appCode得到unionid的表名
	 * 
	 * @return
	 */
	private String getTableNameByAppCode(int appCode)
	{
		if (appCodeMapTableName.containsKey(appCode))
		{
			return appCodeMapTableName.get(appCode);
		}

		for (Wxdev dev : AuthWxConfig.getInstance().mapWxdev.values())
		{
			for (Wxapp app : dev.wxapps)
			{
				if (Integer.valueOf(app.getAppcode()) == appCode)
				{
					appCodeMapTableName.put(appCode, dev.getTable());
					return dev.getTable();
				}
			}
		}

		return null;
	}

	/**
	 * 处理微信用户信息的回复
	 * 
	 * @param result 微信的token
	 * @param wxInfo 用户信息
	 * @param callback 微信验证后需要使用的数据
	 */
	public void handleUserInfoResponse(WxAccessResult result, WxUserInfo wxInfo, AuthCallback callback)
	{
		AccountInfo accountInfo =
			getAccountInfoByUsername(AuthServerConfig.CHANNEL_WX, callback.getAppCode(), result.getOpenId());

		// 保存设备id和unionid的对应关系
		DeviceService service = ServiceContainer.getInstance().getPublicService(DeviceService.class);
		service.saveDeviceIdUnionId(callback.getDeviceId(), wxInfo.getUnionid());

		if (accountInfo == null)
		{
			/**
			 * @author 老陈 需求： 5. 老的微信平台不再接受新用户注册, 并提示专项新APP. 注意这个需要有配置开关, 默认为关闭状态
			 *         此处根据开关，编写老微信平台的流程，如果允许注册，则按现有流程进行，如果不允许注册，应该直接回复错误码，让客户端处理 配合此处逻辑流程应该修改客户端协议中的错误码，客户端应该修改界面提示。
			 */
			// 判断是否禁止用户注册
			if (AuthWxConfig.getInstance().isOld(callback.getAppCode())
				&& !AuthWxConfig.getInstance().allowRegister(callback.getAppCode()))
			{
				handleAuthResult(null,
					0,
					PlatformProtocolsConfig.AI_AUTH_FAILED_FORBID_REGISTER,
					callback,
					result.getOpenId(),
					"");

				logger.debug("forbid register, appCode={}", callback.getAppCode());
				return;
			}

			accountInfo = initAccountInfo(result, wxInfo, callback);
			UnionidAccountidBean bean = selectUnionidAccountidTable(callback.getAppCode(), result.getUnionid());

			// uid_aid表没有记录
			if (bean == null)
			{
				bean = insertUnionidAccountidTable(callback.getAppCode(), result.getUnionid());
				bean.setAccountId(getNewAccountId());
			}
			bean.setLastLoginTime(System.currentTimeMillis());

			accountInfo.setAccountId(bean.getAccountId());
			service.saveDeviceIdAccountId(callback.getDeviceId(), accountInfo.getAccountId());
			service.saveAccountDeviceId(callback.getAppCode(),
				AuthServerConfig.CHANNEL_WX,
				result.getOpenId(),
				callback.getDeviceId());
			addIsMerge(accountInfo.getAccountId(), bean);
			handleAuthResult(accountInfo,
				bean.getAccountId(),
				PlatformProtocolsConfig.AI_AUTH_SUCCESS,
				callback,
				result.getOpenId(),
				result.getUnionid());
		}
		// openid表有记录
		else
		{
			service.saveAccountDeviceId(callback.getAppCode(),
				AuthServerConfig.CHANNEL_WX,
				result.getOpenId(),
				callback.getDeviceId());
			service.saveDeviceIdAccountId(callback.getDeviceId(), accountInfo.getAccountId());
			accountInfo.setUnionid(result.getUnionid());
			accountInfo.setNickname(filterEmoji(wxInfo.getNickname()));
			accountInfo.setSex(wxInfo.getSex());
			accountInfo.setHeadImgUrl(wxInfo.getHeadImgUrl());
			accountInfo.setRefreshToken(result.getRefreshToken());
			accountInfo.setTokenTime(System.currentTimeMillis());
			processUnionid(accountInfo, callback, result.getOpenId());
		}
	}

	/**
	 * Filter Emoji, 去除微信昵称中的表情符号
	 * 
	 * @param nickName
	 * @return
	 */
	public String filterEmoji(String nickName)
	{
		if (isBlank(nickName))
			return nickName;
		else
			return nickName.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "");
	}

	/**
	 * Checks if a CharSequence is whitespace, empty ("") or null. StringUtils.isBlank(null) = true
	 * StringUtils.isBlank("") = true StringUtils.isBlank(" ") = true StringUtils.isBlank("bob") = false
	 * StringUtils.isBlank("  bob  ") = false
	 */
	public boolean isBlank(final CharSequence cs)
	{
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0)
		{
			return true;
		}
		for (int i = 0; i < strLen; i++)
		{
			if (Character.isWhitespace(cs.charAt(i)) == false)
			{
				return false;
			}
		}
		return true;
	}

	
	private void processUnionid(AccountInfo accountInfo, AuthCallback callback, String openid)
	{
		UnionidAccountidBean bean = updateUnionidAccountidTable(callback.getAppCode(), accountInfo.getUnionid());
		// unionid表没有accountId
		if (bean == null)
		{
			bean = insertUnionidAccountidTable(callback.getAppCode(), accountInfo.getUnionid());
			bean.setAccountId(accountInfo.getAccountId());
			bean.setLastLoginTime(System.currentTimeMillis());

			addIsMerge(accountInfo.getAccountId(), bean);
			handleAuthResult(accountInfo,
				bean.getAccountId(),
				PlatformProtocolsConfig.AI_AUTH_SUCCESS,
				callback,
				openid,
				accountInfo.getUnionid());
		}
		// unionid表有accountId
		else
		{
			// 设置账号的上次登录时间
			bean.setLastLoginTime(System.currentTimeMillis());

			// 若用户来自新微信平台，则不执行合并，直接返回，判断是否禁止用户绑定及具体绑定过程在handleAuthResult
			if (AuthWxConfig.getInstance().isNew(callback.getAppCode()))
			{
				handleAuthResult(accountInfo,
					bean.getAccountId(),
					PlatformProtocolsConfig.AI_AUTH_SUCCESS,
					callback,
					openid,
					accountInfo.getUnionid());
			}

			if (AuthWxConfig.getInstance().isOld(callback.getAppCode()))
			{
				// openid表中的accountId和unionId中的accountId相同且还没有被合并
				if (accountInfo.getAccountId() != bean.getAccountId() && !isMerge(accountInfo.getAccountId(), bean))
				{
					// 请求game调整玩家数据
					mergePlayerInfo(bean.getAccountId(), accountInfo, callback, accountInfo.getUsername());
				}
				else
				{
					addIsMerge(accountInfo.getAccountId(), bean);
					handleAuthResult(accountInfo,
						bean.getAccountId(),
						PlatformProtocolsConfig.AI_AUTH_SUCCESS,
						callback,
						openid,
						accountInfo.getUnionid());
				}
			}
		}
	}

	/**
	 * 向game发送合并玩家信息的请求
	 *
	 */
	private void mergePlayerInfo(int unionidAccountId, AccountInfo accountInfo, AuthCallback callback, String userName)
	{
		logger.debug("sendGameUpdatePlayerInfo");

		Connection interfaceConnection = callback.getInterfaceConnection();
		AIMergePlayerInfoREQ.Builder builder = AIMergePlayerInfoREQ.newBuilder();
		builder.setUnionidAccountid(unionidAccountId);
		builder.setOpenidAccountid(accountInfo.getAccountId());

		MergePlayerInfoPROTO.Builder proto = MergePlayerInfoPROTO.newBuilder();
		proto.setAppCode(callback.getAppCode());
		proto.setChannel(AuthServerConfig.CHANNEL_WX);
		proto.setUsername(userName);
		proto.setPlatform(callback.getPlatform());
		proto.setClientConectionId(callback.getClientConnectionId());

		builder.setPlayerInfo(proto.build());
		interfaceConnection.write(callback.getClientCallback(), builder.build());
	}

	/**
	 * 设置AccountInfo数据
	 *
	 * @param result
	 * @param wxInfo
	 * @param callback
	 */
	private AccountInfo initAccountInfo(WxAccessResult result, WxUserInfo wxInfo, AuthCallback callback)
	{
		AccountInfo accountInfo = insertAccountInfo(callback.getAppCode(),
			new StringBuilder().append(AuthServerConfig.CHANNEL_WX).append("@").append(result.getOpenId()).toString());
		accountInfo.setAuthTime(System.currentTimeMillis());
		accountInfo.setChannel(AuthServerConfig.CHANNEL_WX);
		accountInfo.setCity(wxInfo.getCity());
		accountInfo.setCountry(wxInfo.getCountry());
		accountInfo.setCreateTime(System.currentTimeMillis());
		accountInfo.setHeadImgUrl(wxInfo.getHeadImgUrl());
		accountInfo.setNickname(filterEmoji(wxInfo.getNickname()));
		accountInfo.setProvince(wxInfo.getCity());
		accountInfo.setRefreshToken(result.getRefreshToken());
		accountInfo.setSex(wxInfo.getSex());
		accountInfo.setTokenTime(System.currentTimeMillis());
		accountInfo.setUnionid(wxInfo.getUnionid());
		accountInfo.setUsername(result.getOpenId());
		return accountInfo;
	}

	/**
	 * 处理已授权用户的验证请求
	 * 
	 * @param username
	 * @param refreshToken
	 * @param callback
	 */
	public void authWithUsername(String username, String refreshToken, AuthCallback callback)
	{
		final String channel = AuthServerConfig.CHANNEL_WX;
		AccountInfo accountInfo = getAccountInfoByUsername(channel, callback.getAppCode(), username);

		if (null == accountInfo)
		{
			handleAuthResult(null, 0, PlatformProtocolsConfig.AI_AUTH_FAILED_INVALID_USERNAME, callback, "", "");
			return;
		}

		long interval = Math.abs(System.currentTimeMillis() - accountInfo.getTokenTime());

		if (!refreshToken.equals(accountInfo.getRefreshToken()) || interval > AuthServerConfig.WX_REFRESHTOKEN_TIMEOUT)
		{
			handleAuthResult(null, 0, PlatformProtocolsConfig.AI_AUTH_FAILED_WX_INVALID_REFRESH_TOKEN, callback, "", "");
		}
		else if (accountInfo.getUnionid() == null || accountInfo.getUnionid().equals(""))
		{
			// 如果用户没有unionid，让客户端重新微信验证
			logger.debug("unionid doesn't exist!!!!");
			handleAuthResult(null, 0, PlatformProtocolsConfig.AI_AUTH_FAILED_WX_INVALID_REFRESH_TOKEN, callback, "", "");
		}
		else
		{
			logger.debug("accountId={} update deviceId={}", callback.getDeviceId());
			DeviceService deviceService = ServiceContainer.getInstance().getPublicService(DeviceService.class);
			deviceService.saveDeviceIdUnionId(callback.getDeviceId(), accountInfo.getUnionid());
			deviceService.saveAccountDeviceId(callback.getAppCode(),
				AuthServerConfig.CHANNEL_WX,
				username,
				callback.getDeviceId());
			deviceService.saveDeviceIdAccountId(callback.getDeviceId(), accountInfo.getAccountId());
			processUnionid(accountInfo, callback, username);
		}
	}

	/**
	 * 判断appCode对应的accountid是否已经合并过了
	 *
	 * @param bean
	 * @return true 合并过了
	 */
	private boolean isMerge(int accountId, UnionidAccountidBean bean)
	{
		return bean.getMergeList().contains(accountId);
	}

	private void addIsMerge(int accountId, UnionidAccountidBean bean)
	{
		if (!isMerge(accountId, bean))
		{
			bean.getMergeList().add(accountId);
		}
	}
	
	/**
	 * 对一个字符串进行签名
	 * @param data
	 * @return
	 */
//	public String rsaSign(String data)
//	{
//		String sign = "";
//		try
//		{
//			sign = RSACoder.sign(data.getBytes(), RsaConfig.getRsaKey().getPrivateKey());
//		}
//		catch (Exception e)
//		{
//			logger.error("RSA encryptByPublicKey exception={}", e);
//			return null;
//		}
//		
//		return sign;
//	}
	
	/**
	 * 对账号id，性别，昵称，头像，渠道进行rsa签名
	 * @param accountId
	 * @param sex
	 * @param nickname
	 * @param headImgurl
	 * @param channel
	 * @return 签名
	 */
	public String rsaSign(int accountId, int sex, String nickname, String headImgurl, String channel)
	{
		String sign = "";
		
		String data = accountId+sex+nickname+headImgurl+channel;
		try
		{
			sign = RSACoder.sign(data.getBytes(), RsaConfig.getRsaKey().getPrivateKey());
		}
		catch (Exception e)
		{
			logger.error("RSA encryptByPublicKey exception={}", e);
			return null;
		}
		
		return sign;
	}
}