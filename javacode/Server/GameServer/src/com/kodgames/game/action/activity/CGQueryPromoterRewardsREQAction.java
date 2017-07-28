package com.kodgames.game.action.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.game.service.activity.WeiXinShareActivityService;
import com.kodgames.game.start.WxPromoterConfig;
import com.kodgames.message.proto.game.GameProtoBuf.CGQueryPromoterRewardsREQ;
import com.kodgames.message.proto.game.GameProtoBuf.GCQueryPromoterRewardsRES;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

import xbean.PromoterInfo;
import xbean.RoleInfo;

/**
 * 查询推广员的领奖信息
 * 
 * @author jiangzhen
 *
 */
@ActionAnnotation(messageClass = CGQueryPromoterRewardsREQ.class, actionClass = CGQueryPromoterRewardsREQAction.class, serviceClass = WeiXinShareActivityService.class)
public class CGQueryPromoterRewardsREQAction
	extends ProtobufMessageHandler<WeiXinShareActivityService, CGQueryPromoterRewardsREQ>
{
	private static final Logger logger = LoggerFactory.getLogger(CGQueryPromoterRewardsREQAction.class);

	@Override
	public void handleMessage(Connection connection, WeiXinShareActivityService service, CGQueryPromoterRewardsREQ message,
		int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

		// 定义给客户端的回复
		GCQueryPromoterRewardsRES.Builder builder = GCQueryPromoterRewardsRES.newBuilder();
		
		// 如果不在活动时间范围内
		if (!service.isActive())
		{
			builder.setResult(PlatformProtocolsConfig.GC_QUERY_PROMOTER_REWARD_FAILED_ACTIVITY_INACTIVE);
			connection.write(callback, builder.build());

			logger.debug("WeiXin share activity is inactive, return");
			return;
		}

		// 得到玩家id
		int roleId = connection.getRemotePeerID();

		// 在role_info表中查询推广员
		RoleInfo roleInfo = table.Role_info.select(roleId);
		if (roleInfo == null)
		{
			builder.setResult(PlatformProtocolsConfig.GC_QUERY_PROMOTER_REWARD_FAILED_PROMOTER_ABSENT);
			connection.write(callback, builder.build());

			logger.debug("Promoter is absent in role_info table, roleId={}, return", roleId);
			return;
		}

		// 得到推广员的unionid
		String unionid = roleInfo.getUnionid();
		
		// 查询推广员的奖励信息
		PromoterInfo promoterInfo = table.Promoter_info.select(unionid);
		if (promoterInfo == null)
		{
			promoterInfo = service.insertAndInitPromoterInfo(unionid);
		}
		
		builder.setInviteeCount(promoterInfo.getTotalEffectiveInvitee());
		builder.setMaxRewardsCountPerDay(WxPromoterConfig.getInstance().getMaxGetCount());
		builder.setResult(PlatformProtocolsConfig.GC_QUERY_PROMOTER_REWARD_SUCCESS);
		
		builder.setTotalRewardsCount(promoterInfo.getTotalRewards());
		builder.setUnreceivedRewardsCount(promoterInfo.getUnreceivedRewards());
		
		logger.debug("Promoter query reward, builder={}", builder);
		
		connection.write(callback, builder.build());
	}

}
