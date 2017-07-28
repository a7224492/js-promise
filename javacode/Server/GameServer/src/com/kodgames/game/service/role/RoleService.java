package com.kodgames.game.service.role;

import java.util.Collection;
import java.util.Date;

import com.kodgames.game.service.global.GlobalService;
import com.kodgames.game.service.room.RoomIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.constant.DateTimeConstants;
import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.constant.ServerType;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.corgi.core.util.IPUtils;
import com.kodgames.game.service.server.ServerService;
import com.kodgames.game.start.ServerConfigInitializer;
import com.kodgames.game.util.KodBiLogHelper;
import com.kodgames.message.proto.auth.AuthProtoBuf.GIMergePlayerInfoRES;
import com.kodgames.message.proto.auth.AuthProtoBuf.IGMergePlayerInfoREQ;
import com.kodgames.message.proto.auth.AuthProtoBuf.MergePlayerInfoPROTO;
import com.kodgames.message.proto.game.GameProtoBuf.GCAntiAddictionSYN;
import com.kodgames.message.proto.game.GameProtoBuf.GCKickoffSYNC;
import com.kodgames.message.proto.server.ServerProtoBuf.ClientDisconnectSYN;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

import xbean.RoleInfo;
import xbean.RoleMemInfo;
import xbean.RoleRecord;

/**
 * 角色服务
 */
public class RoleService extends PublicService
{
	private static final Logger logger = LoggerFactory.getLogger(RoleService.class);
	private static final long serialVersionUID = -4154500502776810384L;
	private int defaultRoomCardNum = 0;

	private static final long ADDICTION_ALERT_TIME = DateTimeConstants.HOUR * 4;

	/**
	 * 角色登录, 角色第一次登录时全注册新角色，之后登录时并不会修改数据库中的注册信息
	 * 
	 * @param roleId
	 * @param connection
	 * @param nickname
	 * @param sex
	 * @param headImgUrl
	 * @param accountId
	 * @param channel
	 * @return
	 */
	public RoleInfo roleLogin(final int roleId, Connection connection, final String nickname, final int sex, final String headImgUrl, final int accountId, final String channel, String unionId)
	{
		RoleInfo info = table.Role_info.update(roleId);
		String playerIP = IPUtils.ipToStr(connection.getRemotePeerIP());

		// 设置玩家初始信息
		if (info == null)
		{
			info = table.Role_info.insert(roleId);
			info.setCardCount(defaultRoomCardNum);
			info.setRoleCreateTime(System.currentTimeMillis());
			info.setAccountId(accountId);
			
			RoleRecord records = table.Role_records.insert(roleId);
			if (records != null)
			{
				records.setRole_id(roleId);
				records.setAgencyId(0);
			}
			else
			{
				logger.warn("Role_record for roleId {} already exist", roleId);
			}

			KodBiLogHelper.registerLog(info, roleId, playerIP, channel);
		}

		// 设置玩家实时信息
		info.setChannel(channel);
		info.setNickname(nickname);
		info.setSex(sex);
		info.setHeadImgUrl(headImgUrl);
		info.setUnionid(unionId);

		// 记录玩家登录时间
		long now = System.currentTimeMillis();
        KodBiLogHelper.loginLog(info, roleId, playerIP, info.getLastLoginTime(), now);
		info.setLastLoginTime(now);

		// 设置角色非持久数据
		GlobalService service = ServiceContainer.getInstance().getPublicService(GlobalService.class);
		long serverStartupTime = service.getServerStartupTime();

		RoleMemInfo memInfo = table.Role_mem_info.update(roleId);
		if (memInfo == null)
		{
			memInfo = table.Role_mem_info.insert(roleId);
			memInfo.setRoomId(0);
			memInfo.setBattleServerId(0);
			memInfo.setServerStartupTime(serverStartupTime);
		}
		else
		{
			// 如果重启了服务器, 清空role_mem_info
			if (memInfo.getServerStartupTime() != serverStartupTime)
			{
				memInfo.setRoomId(0);
				memInfo.setBattleServerId(0);
				memInfo.setOnlineTimeInDay(0);
				memInfo.setAddictionAlertTimesInDay(0);
				memInfo.setLastAddictionAlertTime(0);
				memInfo.setLastLogoutTime(0);
				memInfo.setServerStartupTime(serverStartupTime);
			}

			// 跨天重置在线时长
			if (!DateTimeConstants.isDateSame(memInfo.getLastLogoutTime(), now))
			{
				memInfo.setOnlineTimeInDay(0);
			}
		}
		memInfo.setConnectionId(connection.getConnectionID());

		return info;
	}

	/**
	 * 断开旧连接，通知Battle，不通知Interface和managerserver
	 * 
	 * @param oldClientConnection
	 */
	public void kickoffClient(Connection oldClientConnection)
	{
		ClientDisconnectSYN.Builder builder = ClientDisconnectSYN.newBuilder();
		builder.setMixId(oldClientConnection.getMixID());
		ServerService serverService = ServiceContainer.getInstance().getPublicService(ServerService.class);
		builder.setFounder(serverService.getServerId());
		builder.setRoleId(oldClientConnection.getRemotePeerID());
		Collection<Connection> servers = ConnectionManager.getInstance().getAllServerConnections();
		for (Connection server : servers)
		{
			if (ServerType.getType(server.getRemotePeerID()) == ServerType.BATTLE_SERVER)
			{
				server.write(GlobalConstants.DEFAULT_CALLBACK, builder.build());
			}
		}

		// 踢掉客户端
		GCKickoffSYNC.Builder kickBuilder = GCKickoffSYNC.newBuilder();
		kickBuilder.setReason(PlatformProtocolsConfig.GC_KICKOFF_SAME_LOGIN);
		oldClientConnection.write(GlobalConstants.DEFAULT_CALLBACK, kickBuilder.build());

		ConnectionManager.getInstance().removeConnection(oldClientConnection);
	}

	/**
	 * 玩家主动退出，清空缓存信息
	 * 
	 * @param connection
	 */
	public void roleLogout(Connection connection)
	{
		int roleId = connection.getRemotePeerID();
		RoleMemInfo info = table.Role_mem_info.update(roleId);
		if (info != null)
		{
			// 记录玩家logout
			RoleInfo roleInfo = table.Role_info.select(roleId);
			String playerIP = IPUtils.ipToStr(connection.getRemotePeerIP());
			long logoutTime = System.currentTimeMillis();
			KodBiLogHelper.logoutLog(roleInfo, roleId, playerIP, new Date(logoutTime));

			info.setBattleServerId(0);
			info.setRoomId(0);
			info.setConnectionId(0);
		}
		else
			logger.warn("can't find role_mem_info for role {}", roleId);
	}

	/**
	 * 玩家离开房间
	 * 
	 * @param roleId
	 */
	public void roleQuitRoom(int roleId, int roomId)
	{
		RoleMemInfo info = table.Role_mem_info.update(roleId);
		if (info != null)
		{
			if (roomId == info.getRoomId()) {
				info.setBattleServerId(0);
				info.setRoomId(0);
			} else {
				logger.warn("role {} already in another room {}, can't quit room {}", roleId, info.getRoomId(), roomId);
			}
		}
		else
			logger.warn("roleQuitRoom can't find role_mem_info for role {} room {}", roleId, roomId);
	}

	/**
	 * client断开连接
	 * 
	 * @param roleId
	 * @param connectionId
	 */
	public void roleDisconnect(int roleId, int connectionId)
	{
		// 更新离线时间
		RoleMemInfo memInfo = table.Role_mem_info.update(roleId);
		if (memInfo == null)
		{
			logger.warn("can't find role_mem_info for role {}", roleId);
			return;
		}

		memInfo.setLastLogoutTime(System.currentTimeMillis());

		// 重置玩家连接ID
		memInfo.setConnectionId(0);

		// 记录玩家当日在线时长
		long onlineTimeInDay = calcOnlineTimeInDay(roleId);
		memInfo.setOnlineTimeInDay(onlineTimeInDay);

		// 记录玩家在线时长的BI日志
        RoleInfo info = table.Role_info.select(roleId);
        if (info == null) {
            logger.warn("can't find role_info for role {}", roleId);
            return;
        }
		
        long time = System.currentTimeMillis() - info.getLastLoginTime();
        KodBiLogHelper.onlineTimeLog(info.getChannel(), roleId, (int) time);
	}

	/**
	 * 检查玩家是否已有房间
	 * 
	 * @param roleId
	 * @return
	 */
	public int getRoomIdByRoleId(final Integer roleId)
	{
		RoleMemInfo info = table.Role_mem_info.select(roleId);
		if (info != null)
		{
			// 检查该battle是否存在
			if (info.getBattleServerId() != 0 && !ServiceContainer.getInstance()
					.getPublicService(ServerService.class)
					.isBattleActive(info.getBattleServerId()))
			{
				// Login时发现该battle不存在，清除玩家所在房间
				table.Room_info.delete(info.getRoomId());

				// 回收RoomId
				RoomIdGenerator.Free(info.getRoomId());

				info.setBattleServerId(0);
				info.setRoomId(0);
			}

			return info.getRoomId();
		}

		logger.warn("can't find role_mem_info for role {}", roleId);
		return 0;
	}

	/**
	 * 玩家加入房间成功后, 设置mem_info
	 * 
	 * @param roleId
	 * @param roomId
	 * @param battleId
	 */
	public void joinRoom(final Integer roleId, final Integer roomId, int battleId)
	{
		RoleMemInfo info = table.Role_mem_info.update(roleId);
		if (info == null)
		{
			logger.warn("can't find role_mem_info for role {}", roleId);
			info = table.Role_mem_info.insert(roleId);
		}

		info.setRoomId(roomId);
		info.setBattleServerId(battleId);
	}

	// 返回RoleInfo用于getXXX
	public RoleInfo getRoleInfoByRoleId(final Integer roleId)
	{
		return table.Role_info.select(roleId);
	}

	// 返回RoleInfo用于setXXX
	public RoleInfo getRoleInfoByRoleIdForWrite(final Integer roleId)
	{
		return table.Role_info.update(roleId);
	}

	/**
	 * 获取角色的mem_info (未结束的房间号与桌子号)
	 * 
	 * @param roleId
	 * @return
	 */
	public RoleMemInfo getRoleMemInfo(int roleId)
	{
		RoleMemInfo info = table.Role_mem_info.update(roleId);
		if (info == null)
		{
			logger.warn("can't find role_mem_info for role {}", roleId);
			return null;
		}

		// 检查该battle是否存在
		if (info.getBattleServerId() != 0 && !ServiceContainer.getInstance().getPublicService(ServerService.class).isBattleActive(info.getBattleServerId()))
		{
			// Login时发现该battle不存在，清除玩家所在房间
			table.Room_info.delete(info.getRoomId());

			info.setBattleServerId(0);
			info.setRoomId(0);
		}

		return info;
	}

	/**
	 * 玩家进入房间后更新mem_info
	 * @param roleId
	 * @param roomId
	 * @param battleId
     */
	public void roleEnterRoom(int roleId, int roomId, int battleId)
	{
		RoleMemInfo info = table.Role_mem_info.update(roleId);
		if (info != null)
		{
			info.setRoomId(roomId);
			info.setBattleServerId(battleId);
		}
		else
			logger.warn("can't find role_mem_info for role {}", roleId);
	}

	/**
	 * 在线玩家数
	 * 
	 * @return
	 */
	public int getOnlinePlayerCount()
	{
		return ConnectionManager.getInstance().getClientVirutalConnectionNumber();
	}

	/**
	 * 设置玩家的默认房卡数
	 * 
	 * @param defaultRoomCardNum
	 */
	public void setDefaultRoomCardNum(int defaultRoomCardNum)
	{
		this.defaultRoomCardNum = defaultRoomCardNum;
	}

	/**
	 * 判断是否需要对玩家发出防沉迷警告
	 */
	public boolean needAlertAddiction(int roleId)
	{
		// 当日在线未超过沉迷时长，就不警告
		long onlineTimeInDay = calcOnlineTimeInDay(roleId);
		if (onlineTimeInDay < ADDICTION_ALERT_TIME)
		{
			return false;
		}

		// 当日警告2次以后，就不再警告
		int alertTimes = calcAddictionAlertTimesInDay(roleId);
		if (alertTimes >= 2)
		{
			return false;
		}

		return true;
	}

	/**
	 * 计算玩家当日防沉迷警告次数
	 */
	private int calcAddictionAlertTimesInDay(int roleId)
	{
		RoleMemInfo info = table.Role_mem_info.select(roleId);
		if (info == null)
		{
			logger.warn("can't find role_mem_info for role {}", roleId);
			return 0;
		}
		long lastAlertTime = info.getLastAddictionAlertTime();
		long now = System.currentTimeMillis();

		// 没有跨天，返回当日警告次数
		if (DateTimeConstants.isDateSame(lastAlertTime, now))
		{
			return info.getAddictionAlertTimesInDay();
		}

		// 已跨天，警告次数置0
		return 0;
	}

	/**
	 * 计算玩家当日在线时长
	 */
	private long calcOnlineTimeInDay(int roleId)
	{
		RoleInfo role = table.Role_info.select(roleId);
		if (role == null)
		{
			logger.warn("can't find role_info for role {}", roleId);
			return 0;
		}

		long loginTime = role.getLastLoginTime();
		long now = System.currentTimeMillis();

		// 没有跨天，在线时间累加
		if (DateTimeConstants.isDateSame(loginTime, now))
		{
			RoleMemInfo info = table.Role_mem_info.select(roleId);
			if (info == null)
			{
				logger.warn("can't find role_mem_info for role {}", roleId);
				return now - loginTime;
			}

			long onlineTime = now - loginTime;
			return info.getOnlineTimeInDay() + onlineTime;
		}

		// 已跨天，从零点重新计时
		return DateTimeConstants.getHourAndMinute(now);
	}

	/**
	 * 向玩家发出防沉迷警告
	 */
	public void syncAddictionAlertToPlayer(int roleId)
	{
		Connection connection = ConnectionManager.getInstance().getClientVirtualConnection(roleId);
		if (connection != null)
		{
			connection.write(GlobalConstants.DEFAULT_CALLBACK, GCAntiAddictionSYN.newBuilder().build());

			// 累加警告次数
			RoleMemInfo info = table.Role_mem_info.select(roleId);
			if (info == null)
			{
				logger.warn("can't find role_mem_info for role {}", roleId);
				return;
			}

			int alertTimes = info.getAddictionAlertTimesInDay();
			info.setAddictionAlertTimesInDay(alertTimes + 1);

			// 记录最后警告时间
			info.setLastAddictionAlertTime(System.currentTimeMillis());
		}
	}

	/**
	 * 合并两个玩家数据
	 *
	 */
	public void mergePlayerInfo(Connection connection, IGMergePlayerInfoREQ message, int callback)
	{
		RoleInfo unionidRoleInfo = table.Role_info.update(message.getUnionidAccountid());
		RoleInfo openidRoleInfo = table.Role_info.update(message.getOpenidAccountid());

		if (openidRoleInfo == null)
		{
			// 走创建流程，创建这个RoleInfo,根据message.getUnionidAccountid()
			openidRoleInfo = table.Role_info.insert(message.getOpenidAccountid());
			openidRoleInfo.setAccountId(message.getOpenidAccountid());
			openidRoleInfo.setCardCount(ServerConfigInitializer.getInstance().getDefaultRoomCardNum());
			openidRoleInfo.setChannel(message.getPlayerInfo().getChannel());
			openidRoleInfo.setHeadImgUrl(message.getPlayerInfo().getHeadImageUrl());
			openidRoleInfo.setLastLoginTime(System.currentTimeMillis());
			openidRoleInfo.setNickname(message.getPlayerInfo().getNickname());
			openidRoleInfo.setPoints(0);
			openidRoleInfo.setRoleCreateTime(System.currentTimeMillis());
			openidRoleInfo.setSex(0);
			openidRoleInfo.setTotalCostCardCount(0);
			openidRoleInfo.setTotalGameCount(0);
			openidRoleInfo.setUsername("");
		}
		if (unionidRoleInfo == null)
		{
			// 走创建流程，创建这个RoleInfo,根据message.getOpenidAccountid()
			unionidRoleInfo = table.Role_info.insert(message.getUnionidAccountid());
			unionidRoleInfo.setAccountId(message.getUnionidAccountid());
			unionidRoleInfo.setCardCount(ServerConfigInitializer.getInstance().getDefaultRoomCardNum());
			unionidRoleInfo.setChannel(message.getPlayerInfo().getChannel());
			unionidRoleInfo.setHeadImgUrl(message.getPlayerInfo().getHeadImageUrl());
			unionidRoleInfo.setLastLoginTime(System.currentTimeMillis());
			unionidRoleInfo.setNickname(message.getPlayerInfo().getNickname());
			unionidRoleInfo.setPoints(0);
			unionidRoleInfo.setRoleCreateTime(System.currentTimeMillis());
			unionidRoleInfo.setSex(0);
			unionidRoleInfo.setTotalCostCardCount(0);
			unionidRoleInfo.setTotalGameCount(0);
			unionidRoleInfo.setUsername("");
		}

		int oldAccountid = 0;
		int newAccountid = 0;
		RoleInfo oldRoleInfo = null;

		if (unionidRoleInfo.getRoleCreateTime() < openidRoleInfo.getRoleCreateTime())
		{
			if (!unionidRoleInfo.getMergeList().contains(message.getOpenidAccountid()))
			{
				startMerge(openidRoleInfo, unionidRoleInfo, message.getOpenidAccountid(), message.getUnionidAccountid());
			}
			newAccountid = message.getOpenidAccountid();
			oldAccountid = message.getUnionidAccountid();
			oldRoleInfo = unionidRoleInfo;
		}
		else
		{
			if (!openidRoleInfo.getMergeList().contains(message.getUnionidAccountid()))
			{
				startMerge(unionidRoleInfo, openidRoleInfo, message.getUnionidAccountid(), message.getOpenidAccountid());
			}
			newAccountid = message.getUnionidAccountid();
			oldAccountid = message.getOpenidAccountid();
			oldRoleInfo = openidRoleInfo;
		}

		MergePlayerInfoPROTO.Builder proto = MergePlayerInfoPROTO.newBuilder();
		proto.setAppCode(message.getPlayerInfo().getAppCode());
		proto.setChannel(unionidRoleInfo.getChannel());
		proto.setClientConectionId(message.getPlayerInfo().getClientConectionId());
		proto.setPlatform(message.getPlayerInfo().getPlatform());
		proto.setUsername(message.getPlayerInfo().getUsername());
		proto.setHeadImageUrl(oldRoleInfo.getHeadImgUrl());
		proto.setNickname(oldRoleInfo.getNickname());
		proto.setSex(oldRoleInfo.getSex());
		connection.write(callback,
			GIMergePlayerInfoRES.newBuilder().setResult(PlatformProtocolsConfig.GI_MERGE_PLAYER_INFO_SUCCESS).setOldAccountid(oldAccountid).setNewAccountid(newAccountid).setPlayerInfo(proto).build());
		logger.info("merge player info success!!! : unionidRoleInfo={}", unionidRoleInfo);
	}

	private void startMerge(RoleInfo newRoleInfo, RoleInfo oldRoleInfo, int newAccountid, int oldAccountid)
	{
		if (!oldRoleInfo.getMergeList().contains(newAccountid))
		{
			int oldCardNum = oldRoleInfo.getCardCount();
			oldRoleInfo.setCardCount(newRoleInfo.getCardCount() + oldRoleInfo.getCardCount());
			oldRoleInfo.getMergeList().add(newAccountid);

			KodBiLogHelper.mgergePlayerInfo(oldAccountid, oldCardNum, newAccountid, newRoleInfo.getCardCount());
		}
	}
}
