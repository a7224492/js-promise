package com.kodgames.activityserver.action.activity;

/**
 * Created by jiangzhen on 2017/7/26
 */

import com.kodgames.activityserver.common.activity.turntable.ITurntableActivity;
import com.kodgames.activityserver.common.activity.turntable.ITurntablePlayerCondition;
import com.kodgames.activityserver.common.reward.TurntableReward;
import com.kodgames.activityserver.service.activity.TurntableActivityService;
import com.kodgames.activityserver.start.CGProtobufMessageHandler;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.util.DateTimeUtil;
import com.kodgames.message.proto.activity.ActivityProtoBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@ActionAnnotation(messageClass = ActivityProtoBuf.CGQueryTurntableInfoREQ.class, actionClass = CGQueryTurntableInfoREQAction.class, serviceClass = TurntableActivityService.class)
public class CGQueryTurntableInfoREQAction extends CGProtobufMessageHandler<TurntableActivityService, ActivityProtoBuf.CGQueryTurntableInfoREQ>
{
	private static final Logger logger = LoggerFactory.getLogger(CGQueryTurntableInfoREQAction.class);

	@Override
	public void handleMessage(Connection connection, TurntableActivityService service, ActivityProtoBuf.CGQueryTurntableInfoREQ message, int callback)
	{
		logger.info("{} : {} -> {}.", "CGQueryTurntableInfoREQAction", connection.getConnectionID(), message);

		ITurntableActivity turntableActivity = service.getTurntableActivity();

		// 判断活动是否开启
		if (turntableActivity.timeCondition().isSatisfy())
		{
			// 查询转盘的奖品
			long now = System.currentTimeMillis();
			List<TurntableReward> rewards = turntableActivity.queryTurntableRewards(DateTimeUtil.getZeroTime(now));

			// 查询玩家的抽奖次数
			int roleId = message.getClientId();
			ITurntablePlayerCondition playerCondition = turntableActivity.playerCondition();
			int itemCount = playerCondition.queryPlayerItemCount(roleId);

			// TODO 查询成功
		}
		else
		{
			// TODO 活动没有开启
		}
	}

}

