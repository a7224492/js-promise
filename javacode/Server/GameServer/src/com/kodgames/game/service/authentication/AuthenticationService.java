package com.kodgames.game.service.authentication;

import xbean.AuthentictionInfo;

import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

/**
 * 实名认证
 * 
 * @author 毛建伟
 */
public class AuthenticationService extends PublicService
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 检查玩家是否已经认证过
	 * 
	 * @param roleId 玩家id
	 * @return
	 */
	public int checkAuthentication(int roleId)
	{
		if (table.Authentiction_info.select(roleId) == null)
			return PlatformProtocolsConfig.GC_QUERY_AUTHENTICATION_FAILED;
		return PlatformProtocolsConfig.GC_QUERY_AUTHENTICATION_SUCCESS;
	}

	/**
	 * 存储玩家认证信息
	 * 
	 * @param roleId 玩家id
	 * @param realName 玩家真实姓名
	 * @param idNumber 玩家身份证号
	 * @return
	 */
	public int save(int roleId, String realName, String idNumber)
	{
		// 检查是否已经插入过
		if (table.Authentiction_info.select(roleId) != null)
			return PlatformProtocolsConfig.GC_AUTHENTICATION_INFO_ALREADY;
		// 存储
		AuthentictionInfo authentictionInfo = table.Authentiction_info.insert(roleId);
		// 如果结果为空，则返回失败
		if (authentictionInfo == null)
			return PlatformProtocolsConfig.GC_AUTHENTICATION_INFO_FAILED;
		// 设置值
		authentictionInfo.setRealname(realName);
		authentictionInfo.setIdnumber(idNumber);
		// 返回成功
		return PlatformProtocolsConfig.GC_AUTHENTICATION_INFO_SUCCESS;
	}

}
