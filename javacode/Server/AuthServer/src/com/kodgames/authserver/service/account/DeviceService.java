package com.kodgames.authserver.service.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.corgi.core.service.ServiceContainer;

import xbean.AccountInfo;
import xbean.DeviceidAccountidBean;
import xbean.DeviceidUnionidBean;
import xbean.UnionidAccountidBean;

public class DeviceService extends PublicService
{
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(DeviceService.class);

	public static final String ZERO_DEVICE_ID = "00000000-0000-0000-0000-000000000000";

	public void saveDeviceIdUnionId(String deviceId, String unionid)
	{
		DeviceidUnionidBean bean = table.Deviceid_unionid_table.insert(deviceId);
		if (bean == null)
		{
			bean = table.Deviceid_unionid_table.update(deviceId);
		}
		if (!bean.getUnionidList().contains(unionid))
		{
			bean.getUnionidList().add(unionid);
		}
	}

	public void saveAccountDeviceId(int appCode, String channel, String username, String deviceId)
	{
		AccountService accountService = ServiceContainer.getInstance().getPublicService(AccountService.class);
		AccountInfo account =
			accountService.updateAccountInfo(appCode, accountService.getAccountKey(channel, username));
		if (account != null)
		{
			account.setDeviceId(deviceId);
		}
	}

	public void saveDeviceIdAccountId(String deviceId, int accountId)
	{
		DeviceidAccountidBean bean = table.Deviceid_accountid_table.insert(deviceId);
		if (bean == null)
		{
			bean = table.Deviceid_accountid_table.update(deviceId);
		}
		if (!bean.getAccountIdList().contains(accountId))
		{
			bean.getAccountIdList().add(accountId);
		}
	}

	/**
	 * 获取玩家的跨渠道账号ID
	 */
	public int getJoinAccountId(AccountInfo accountInfo)
	{
		// 账户信息无效，返回默认无效账号ID
		if (accountInfo == null)
		{
			return 0;
		}

		// 客户端传上来的设备id是"00000000-0000-0000-0000-000000000000"
		if (accountInfo.getDeviceId().equals(ZERO_DEVICE_ID))
		{
			return accountInfo.getAccountId();
		}

		// 没有设备ID时，返回当前请求认证的账号ID
		DeviceidUnionidBean deviceidUnionidBean = table.Deviceid_unionid_table.select(accountInfo.getDeviceId());
		if (deviceidUnionidBean == null)
		{
			return accountInfo.getAccountId();
		}

		// 获取最后登录的可合并账号ID
		long maxLoginTime = 0;
		int joinAccountId = accountInfo.getAccountId();
		for (String unionId : deviceidUnionidBean.getUnionidList())
		{
			// 忽略不存在的联合账号
			UnionidAccountidBean unionidAccountidBean = table.Unionid_accountid_table.select(unionId);
			if (unionidAccountidBean == null)
			{
				continue;
			}

			// 记录最后登录的合并账号信息
			long loginTime = unionidAccountidBean.getLastLoginTime();
			if (loginTime >= maxLoginTime)
			{
				joinAccountId = unionidAccountidBean.getAccountId();
				maxLoginTime = loginTime;
			}
		}

		logger.warn("Jion accountid : accountid={}, deviceId={}, accountJid={}, unionid={}, nickname={}",
			accountInfo.getAccountId(),
			accountInfo.getDeviceId(),
			joinAccountId,
			accountInfo.getUnionid(),
			accountInfo.getNickname());

		return joinAccountId;
	}

}
