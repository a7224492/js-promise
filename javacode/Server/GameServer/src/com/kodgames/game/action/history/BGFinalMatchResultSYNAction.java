package com.kodgames.game.action.history;

import com.kodgames.message.proto.game.GameProtoBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.common.Constant.ActivityId;
import com.kodgames.game.service.activity.ActivityService;
import com.kodgames.game.service.role.RoleService;
import com.kodgames.message.proto.game.GameProtoBuf.BGFinalMatchResultSYN;
import com.kodgames.message.proto.game.GameProtoBuf.PlayerHistoryPROTO;
import xbean.RoleMemInfo;

@ActionAnnotation(messageClass = BGFinalMatchResultSYN.class, actionClass = BGFinalMatchResultSYNAction.class, serviceClass = RoleService.class)
public class BGFinalMatchResultSYNAction extends ProtobufMessageHandler<RoleService, BGFinalMatchResultSYN>
{
	private static final Logger logger = LoggerFactory.getLogger(BGMatchResultSYNAction.class);

	@Override
	public void handleMessage(Connection connection, RoleService service, BGFinalMatchResultSYN message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

		// 更新活动排名
		ActivityService activityService = ServiceContainer.getInstance().getPublicService(ActivityService.class);
		if (activityService.isRankSupported(message.getRoundType()) && activityService.isActivated(ActivityId.SCORE_RANK.getId()))
		{
			// 如果该房间的玩法规则支持排行榜，且当前处于活动期间，才更新排行榜
			activityService.updateRank(message);
		}

		// 向玩家发出防沉迷警告
		RoleService roleService = ServiceContainer.getInstance().getPublicService(RoleService.class);
		for (PlayerHistoryPROTO playerHistory : message.getPlayerRecordsList())
		{
			int roleId = playerHistory.getRoleId();
			if (service.needAlertAddiction(roleId))
			{
				service.syncAddictionAlertToPlayer(roleId);
			}
		}

		GameProtoBuf.ActivityEventParam.Builder param = GameProtoBuf.ActivityEventParam.newBuilder();

	}

	@Override
	public Object getMessageKey(Connection connection, BGFinalMatchResultSYN message)
	{
		return message.getRoomId() == 0 ? connection.getConnectionID() : message.getRoomId();
	}
}
