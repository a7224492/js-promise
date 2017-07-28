package com.kodgames.game.action.history;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.role.OnlineRecordTask;
import com.kodgames.game.service.role.RoleService;
import com.kodgames.game.service.security.SecurityService;
import com.kodgames.game.service.server.ServerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.game.service.activity.WeiXinShareActivityService;
import com.kodgames.message.proto.game.GameProtoBuf.BGMatchResultSYN;
import com.kodgames.message.proto.game.GameProtoBuf.PlayerHistoryPROTO;
import xbean.RoleInfo;
import xbean.RoleMemInfo;

@ActionAnnotation(messageClass = BGMatchResultSYN.class, actionClass = BGMatchResultSYNAction.class, serviceClass = RoleService.class)
public class BGMatchResultSYNAction extends ProtobufMessageHandler<RoleService, BGMatchResultSYN>
{
	private static final Logger logger = LoggerFactory.getLogger(BGMatchResultSYNAction.class);

	@Override
	public void handleMessage(Connection connection, RoleService service, BGMatchResultSYN message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

		WeiXinShareActivityService weiXinShareActivityService = ServiceContainer.getInstance().getPublicService(WeiXinShareActivityService.class);
		
		// 微信分享活动处于开启状态
		if (weiXinShareActivityService.isActive())
		{
			for (PlayerHistoryPROTO proto : message.getPlayerRecordsList())
			{
				int roleId = proto.getRoleId();
				weiXinShareActivityService.rewardPromoterIfFinishTask(roleId);
			}
		}

		// 记录战局次数
		for (PlayerHistoryPROTO playerProto : message.getPlayerRecordsList())
		{
			Integer roleId = playerProto.getRoleId();
			RoleInfo role = service.getRoleInfoByRoleIdForWrite(roleId);
			role.setTotalGameCount(role.getTotalGameCount() + 1);
		}

		// 统计游戏局数, 用于判断登录安全组
		SecurityService securityService = ServiceContainer.getInstance().getPublicService(SecurityService.class);
		for (PlayerHistoryPROTO playerProto : message.getPlayerRecordsList()) {
			int rid = playerProto.getRoleId();
			securityService.updateCombatRecords(rid);
		}

		// 通知ReplayServer保存回放数据
		ServerService serverService = ServiceContainer.getInstance().getPublicService(ServerService.class);
		Connection replay = serverService.getReplayConnection();
		if (replay == null)
			logger.error("replay server connection is null, drop BGMatchResultSYN packet. roomId {}", message.getRoomId());
		else
			replay.write(callback, message);



		// BI统计游戏局数
		OnlineRecordTask.getInstance().incRoundCount();
	}

	@Override
	public Object getMessageKey(Connection connection, BGMatchResultSYN message)
	{
		return message.getRoomId() == 0 ? connection.getConnectionID() : message.getRoomId();
	}

}
