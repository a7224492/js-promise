package com.kodgames.game.action.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.game.service.activity.WeiXinShareActivityService;
import com.kodgames.game.start.WxPromoterConfig;
import com.kodgames.game.start.WxPromoterConfig.RewardType;
import com.kodgames.game.util.TimeUtil;
import com.kodgames.message.proto.game.GameProtoBuf.CGPromoterRewardREQ;
import com.kodgames.message.proto.game.GameProtoBuf.GCPromoterRewardRES;
import com.kodgames.message.proto.game.GameProtoBuf.GCRoomCardModifySYNC;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

import xbean.PromoterInfo;
import xbean.ReceiveRewardInfo;
import xbean.RoleInfo;

/**
 * 推广员领取奖励
 * 
 * @author jiangzhen
 *
 */
@ActionAnnotation(messageClass = CGPromoterRewardREQ.class, actionClass = CGPromoterRewardREQAction.class, serviceClass = WeiXinShareActivityService.class)
public class CGPromoterRewardREQAction extends ProtobufMessageHandler<WeiXinShareActivityService, CGPromoterRewardREQ>
{
	private static final Logger logger = LoggerFactory.getLogger(CGPromoterRewardREQAction.class);

	@Override
	public void handleMessage(Connection connection, WeiXinShareActivityService service, CGPromoterRewardREQ message,
		int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

		GCPromoterRewardRES.Builder builder = GCPromoterRewardRES.newBuilder();
		if (!service.isActive())
		{
			builder.setResult(PlatformProtocolsConfig.GC_PROMOTER_REWARD_FAILED_ACTIVITY_INACTIVE);
			connection.write(callback, builder.build());
			logger.debug("Weixin share activity is inactive, return");
			return;
		}

		RewardType rewardType = WxPromoterConfig.getInstance().getRewardType();
		if (rewardType == WxPromoterConfig.RewardType.Cash)
		{
			// 奖励成功，设置给客户端发送微信号
			builder.setResult(PlatformProtocolsConfig.GC_PROMOTER_REWARD_SUCCESS);
			builder.setGongZhongHao(WxPromoterConfig.getInstance().getGMWeixin());
			
			PromoterInfo promoterInfo = service.getPromoterInfoByRoleId(connection.getRemotePeerID());
			if (promoterInfo != null)
			{
				builder.setUnreceivedRewardsCount(promoterInfo.getUnreceivedRewards());
				builder.setTotalRewardsCount(promoterInfo.getTotalRewards());
				
				logger.debug("Promoter reward, rewardType is Cash, builder={}", builder);
				connection.write(callback, builder.build());
			}
			else
			{
				builder.setResult(PlatformProtocolsConfig.GC_PROMOTER_REWARD_FAILED);
				connection.write(callback, builder.build());
			}
		}
		else if (rewardType == WxPromoterConfig.RewardType.Diamond)
		{
			int roleId = connection.getRemotePeerID();

			RoleInfo roleInfo = table.Role_info.update(roleId);
			if (roleInfo == null)
			{
				logger.error("Can't find role info, promoter reward fails, roleId={}", roleId);
				builder.setResult(PlatformProtocolsConfig.GC_PROMOTER_REWARD_FAILED);
				connection.write(callback, builder.build());
				return;
			}

			// 查询推广员信息
			String unionid = roleInfo.getUnionid();
			PromoterInfo promoterInfo = table.Promoter_info.update(unionid);
			if (promoterInfo == null)
			{
				logger.error("Can't find promoter info, reward fails, unionid={}", unionid);
				builder.setResult(PlatformProtocolsConfig.GC_PROMOTER_REWARD_FAILED);
				connection.write(callback, builder.build());
				return;
			}

			// 获取当前时间
			long currTime = System.currentTimeMillis();
			if (!TimeUtil.isOneDay(promoterInfo.getReceivedTimeToday(), currTime))
			{
				promoterInfo.setReceivedRewardsToday(0);
				promoterInfo.setReceivedCountToday(0);
			}
			
			float rewards = 0;
			float unreceivedRewards = promoterInfo.getUnreceivedRewards();
			float receivedRewardsToday = promoterInfo.getReceivedRewardsToday();
			float maxGetReward = WxPromoterConfig.getInstance().getMaxGetReward();

			// 如果今日领取数量没有达到今日上限
			if (receivedRewardsToday < maxGetReward)
			{
				// 如果未领取的钻石全部领取后，超过了今日上限
				if (Float.compare((receivedRewardsToday + unreceivedRewards), maxGetReward) >= 0)
				{
					rewards = maxGetReward - receivedRewardsToday;
				}
				else
				{
					rewards = unreceivedRewards;
				}
			}

			logger.debug(
				"Before promoter reward, rewards={}, unreceivedRewards={}, receivedRewardsToday={}, maxGetReward={}",
				rewards,
				unreceivedRewards,
				receivedRewardsToday,
				maxGetReward);

			// 给玩家加卡
			roleInfo.setCardCount(roleInfo.getCardCount() + (int)rewards);
			
			// 推送房卡改变的消息
			GCRoomCardModifySYNC.Builder sync = GCRoomCardModifySYNC.newBuilder();
			sync.setRoomCardCount(roleInfo.getCardCount());
			connection.write(GlobalConstants.DEFAULT_CALLBACK, sync.build());

			// 更新推广员数据
			promoterInfo.setUnreceivedRewards(unreceivedRewards - rewards);
			promoterInfo.setReceivedRewardsToday(receivedRewardsToday + rewards);
			promoterInfo.setTotalRewards(promoterInfo.getTotalRewards()+rewards);
			promoterInfo.setReceivedTimeToday(currTime);
			
			logger.debug(
				"After promoter reward, rewards={}, unreceivedRewards={}, receivedRewardsToday={}, maxGetReward={}",
				rewards,
				promoterInfo.getUnreceivedRewards(),
				promoterInfo.getReceivedRewardsToday(),
				maxGetReward);
			builder.setTotalRewardsCount(promoterInfo.getTotalRewards());
			builder.setUnreceivedRewardsCount(promoterInfo.getUnreceivedRewards());
			
			// 插入一条领奖记录
			Long key = table.Receive_reward_info.newKey();
			ReceiveRewardInfo rewardInfo = table.Receive_reward_info.insert(key);
			rewardInfo.setDate(currTime);
			rewardInfo.setId(key);
			rewardInfo.setReceivedThisTime(rewards);
			rewardInfo.setReceivedTotal(promoterInfo.getTotalRewards());
			
			// 推广员添加一条领奖记录
			promoterInfo.getRewardList().add(key);

			builder.setResult(PlatformProtocolsConfig.GC_PROMOTER_REWARD_SUCCESS);
			
			connection.write(callback, builder.build());
		}
	}

}
