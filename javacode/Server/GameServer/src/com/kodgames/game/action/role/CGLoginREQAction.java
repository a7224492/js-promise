package com.kodgames.game.action.role;

import java.util.Map;

import com.kodgames.corgi.core.util.IPUtils;
import com.kodgames.game.service.activity.*;
import com.kodgames.game.service.role.OnlineRecordTask;
import com.kodgames.game.service.room.RoomService;
import com.kodgames.game.service.security.SecurityService;
import com.kodgames.game.service.server.ServerService;
import com.kodgames.game.start.ServerConfigInitializer;
import com.kodgames.message.proto.club.ClubProtoBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.corgi.core.util.DateTimeUtil;
import com.kodgames.corgi.core.util.rsa.RSACoder;
import com.kodgames.corgi.core.util.rsa.RsaConfig;
import com.kodgames.game.service.button.ButtonService;
import com.kodgames.game.service.global.GlobalService;
import com.kodgames.game.service.marquee.MarqueeService;
import com.kodgames.game.service.notice.NoticeService;
import com.kodgames.game.service.role.RoleService;
import com.kodgames.game.start.CGProtobufMessageHandler;
import com.kodgames.message.proto.game.GameProtoBuf.ActivityInfoPROTO;
import com.kodgames.message.proto.game.GameProtoBuf.CGLoginREQ;
import com.kodgames.message.proto.game.GameProtoBuf.GCLoginRES;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

import limax.zdb.exception.ZdbRollbackException;
import xbean.RoleInfo;
import xbean.RoleMemInfo;
import xbean.RoomInfo;

@ActionAnnotation(messageClass = CGLoginREQ.class, actionClass = CGLoginREQAction.class, serviceClass = RoleService.class)
public class CGLoginREQAction extends CGProtobufMessageHandler<RoleService, CGLoginREQ>
{
	private static final Logger logger = LoggerFactory.getLogger(CGLoginREQAction.class);

	/**
	 * 客户端AuthServer登录成功后需要再到GameServer登录
	 * @param connection
	 * @param service
	 * @param message
	 * @param callback
	 * 
	 * zdb锁使用顺序: (不包括活动)
	 * 1. persist_global update (globalService.isChannelAllowed)
	 * 2. persist_global update (globalService.isRoleForbidden)
	 * 3. role_info select (service.getRoleInfoByRoleId)
	 * 4. role_info update (锁升级 service.roleLogin)
	 * 5. role_records insert (新用户 roleLogin)
	 * 6. runtime_global update (roleLogin)
	 * 7. role_mem_info update (roleLogin)
	 * 8. role_mem_info update (getRoleMemInfo)
	 * 9. room_info delete (battle服务器重启后 getRoleMemInfo)
	 * 10.role_records update (securityService.getGroup)
	 * 11.role_info select (securityService.getGroup)
	 * 12.room_info select (俱乐部房间)
	 * 13.unionid_2_roleid update
     */
	@Override
	public void handleMessage(Connection connection, RoleService service, CGLoginREQ message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

		// 是否开启客户端Sign检查 (TestClient不计算sign)
		if (ServerConfigInitializer.getInstance().getEnableSignCheck()) {
			try {
				// 判断客户端传上来的roleid是否有效
				String data = message.getRoleId() + message.getSex() + message.getNickname() + message.getHeadImageUrl()
						+ message.getChannel();
				if (!RSACoder.verify(data.getBytes(),
						RsaConfig.getRsaKey().getPublicKey(),
						message.getSignature())) {
					connection.write(callback,
							GCLoginRES.newBuilder().setResult(PlatformProtocolsConfig.GC_LOGIN_FAILED_INVALID_PLAYER_INFO).build());

					logger.info("Player info is modified, login fails!!!!!");
					return;
				}
			} catch (Exception e) {
				connection.write(callback,
						GCLoginRES.newBuilder().setResult(PlatformProtocolsConfig.GC_LOGIN_FAILED_INVALID_PLAYER_INFO).build());

				logger.info("RSA verify exception={}", e);
				return;
			}
		}

		Connection existedConnection =
			ConnectionManager.getInstance().getClientVirtualConnection(connection.getRemotePeerID());

		if (existedConnection == null)
		{
			ConnectionManager.getInstance().addToClientVirtualConnections(connection);
		}
		else if (existedConnection.getConnectionID() != connection.getConnectionID())
		{
			// 如果连接已存在, 先将之前的连接踢下线
			service.kickoffClient(existedConnection);
			ConnectionManager.getInstance().addToClientVirtualConnections(connection);
		}

		GCLoginRES.Builder builder = GCLoginRES.newBuilder();

		// 渠道封禁检查
		GlobalService globalService = ServiceContainer.getInstance().getPublicService(GlobalService.class);
		if (!globalService.isChannelAllowed(message.getChannel()))
		{
			builder.setResult(PlatformProtocolsConfig.GC_LOGIN_FAILED_INVALID_CHANNEL);
			connection.write(callback, builder.build());
			return;
		}

		// 用户封禁检查
		if (globalService.isRoleForbidden(message.getRoleId()))
		{
			builder.setResult(PlatformProtocolsConfig.GC_LOGIN_FAILED_FORBID_ROLE);
			connection.write(callback, builder.build());
			return;
		}
		
		// 判断用户是否是新用户
		if (service.getRoleInfoByRoleId(message.getRoleId()) == null)
		{
			builder.setIsNewPlayer(true);
			// 微信分享活动添加bi日志（邀请来的新玩家）
			WeiXinShareActivityService wxService = ServiceContainer.getInstance().getPublicService(WeiXinShareActivityService.class);
			// 活动不开启不统计
			if (wxService.isActive())
				wxService.inviteRegisterRecord(message.getAccountId(), message.getUnionid());
			logger.debug("roleId={} is new player", message.getRoleId());

			// 统计新注册人数
			OnlineRecordTask.getInstance().incRegisterCount();
		}
		else
		{
			builder.setIsNewPlayer(false);
			logger.debug("roleId={} is old player", message.getRoleId());
		}

		// 获取用户信息, 第一次登录会创建新角色
		RoleInfo info = service.roleLogin(message.getRoleId(),
			connection,
			message.getNickname(),
			message.getSex(),
			message.getHeadImageUrl(),
			message.getAccountId(),
			message.getChannel(),
			message.getUnionid());
		if (info == null)
		{
			builder.setResult(PlatformProtocolsConfig.GC_LOGIN_FAILED);
			String errStr = "RoleService.roleLogin return null";
			logger.error(errStr);
			connection.write(callback, builder.build());
			throw new ZdbRollbackException(errStr);
		}

		builder.setRoomCardCount(info.getCardCount());
		builder.setResult(PlatformProtocolsConfig.GC_LOGIN_SUCCESS);

		{
			Connection clubConnection =
					ServiceContainer.getInstance().getPublicService(ServerService.class).getClubConnection();
			// 向club同步role登陆的信息
			ClubProtoBuf.GCLLoginSYN.Builder loginSYNBuilder = ClubProtoBuf.GCLLoginSYN.newBuilder();
			loginSYNBuilder.setRoleId(message.getRoleId());
			loginSYNBuilder.setAppKey("appKey"); // 预留，未来没准有用
			loginSYNBuilder.setVersion("");		// 同上
			loginSYNBuilder.setChannel(info.getChannel());
			loginSYNBuilder.setName(info.getNickname());
			
			if (clubConnection != null)
				clubConnection.write(callback, loginSYNBuilder.build());
		}

		builder.setMarqueeVersion(ServiceContainer.getInstance().getPublicService(MarqueeService.class).getMarqueeVersion());
		builder.setNewMail(false);
		builder.setNoticeVersion(ServiceContainer.getInstance().getPublicService(NoticeService.class).getNoticeVersion());
		builder.setButtonValue(ServiceContainer.getInstance().getPublicService(ButtonService.class).buttonValue());

		// 判断是否有限免活动
		LimitedCostlessActivityService lcaService =
			ServiceContainer.getInstance().getPublicService(ActivityService.class).getLca();
		builder.setNewLimitedCostlessActivity(lcaService.hasLimitedCostlessActivity());

		// 发送服务器的当前时间
		builder.setTimeStamp(DateTimeUtil.getCurrentTimeMillis());

		RoleMemInfo memInfo = service.getRoleMemInfo(message.getRoleId());
		if (memInfo == null)
		{
			connection.write(callback,
					GCLoginRES.newBuilder().setResult(PlatformProtocolsConfig.GC_LOGIN_FAILED_INVALID_PLAYER_INFO).build());
			logger.error("can't find mem_role_info for role {}", message.getRoleId());
			return;
		}

		// 房间与牌桌信息
		builder.setRoomId(memInfo.getRoomId());
		builder.setBattleId(memInfo.getBattleServerId());

		// 连接组
		SecurityService securityService = ServiceContainer.getInstance().getPublicService(SecurityService.class);
		String groupName = securityService.getGroup(message.getRoleId(), IPUtils.ipToStr(connection.getRemotePeerIP()));
		logger.info("security group for role {} with ip {} is {}", message.getRoleId(), IPUtils.ipToStr(connection.getRemotePeerIP()), groupName);
		builder.setConnGroup(groupName);

		// 如果是俱乐部牌桌，需要俱乐部id
		if (memInfo.getRoomId() > 0) {
			RoomService rs = ServiceContainer.getInstance().getPublicService(RoomService.class);
			RoomInfo room = rs.getRoom(memInfo.getRoomId());
			if (room != null && room.getClubId() > 0) {
				builder.setClubId(room.getClubId());
			}
		}

		// 设置活动信息
		ActivityService activityService = ServiceContainer.getInstance().getPublicService(ActivityService.class);
		Map<Integer, ActivityConfig> configMap = activityService.getConfigMap();
		configMap.entrySet().forEach(configEntry -> {
			ActivityInfoPROTO.Builder configBuilder = ActivityInfoPROTO.newBuilder();
			configBuilder.setActivityId(configEntry.getKey());
			ActivityConfig config = configEntry.getValue();
			configBuilder.setStartTime(config.getQueryStartTime());
			configBuilder.setEndTime(config.getQueryEndTime());

			builder.addActivityInfo(configBuilder);
		});

		TurntableActivity turntableActivity = activityService.getTurntableActivity();
		ActivityInfoPROTO.Builder proto = ActivityInfoPROTO.newBuilder();
		proto.setActivityId(turntableActivity.getActivityId());
		proto.setStartTime(turntableActivity.getShowTime());
		proto.setEndTime(turntableActivity.getBlankTime());
		builder.addActivityInfo(proto);
		turntableActivity.synIfPlayerHasItemCount(connection, callback);

		connection.write(callback, builder.build());

		logger.debug("role {} login success! roomId {} battleId {}",
			message.getRoleId(),
			memInfo.getRoomId(),
			memInfo.getBattleServerId());
		// 更新unionid_2_roleid表
		Integer roleid = table.Unionid_2_roleid.update(message.getUnionid());
		if (roleid == null)
		{
			roleid = new Integer(message.getRoleId());
			table.Unionid_2_roleid.insert(message.getUnionid(), roleid);
		}

		// 打印在线人数，压测用
		logger.info("online num: {}", service.getOnlinePlayerCount());
	}
	
	@Override
	public Object getMessageKey(Connection connection, CGLoginREQ message)
	{
		return message.getRoleId() == 0 ? connection.getConnectionID() : message.getRoleId();
	}
}
