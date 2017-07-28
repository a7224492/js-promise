package com.kodgames.game.service.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.game.start.WxPromoterConfig;
import com.kodgames.game.util.KodBiLogHelper;
import com.kodgames.game.util.TimeUtil;

import xbean.InviteeInfo;
import xbean.PromoterInfo;
import xbean.RoleInfo;

public class WeiXinShareActivityService extends PublicService
{
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(WeiXinShareActivityService.class);

	/**
	 * 判断微信分享活动是否处于活动期间
	 * 
	 * @return boolean true 处于活动期间,false 不处于活动期间
	 */
	public boolean isActive()
	{
		long currTime = System.currentTimeMillis();

		if (currTime > WxPromoterConfig.getInstance().getStartTime()
			&& currTime < WxPromoterConfig.getInstance().getEndTime())
		{
			return true;
		}

		return false;
	}

	/**
	 * 插入并且初始化推广员的信息
	 * 
	 * @param unionid 推广员unionid
	 * @return 推广员信息
	 */
	public PromoterInfo insertAndInitPromoterInfo(String unionid)
	{
		PromoterInfo promoterInfo = table.Promoter_info.insert(unionid);
		promoterInfo.setCycleStartTime(0);
		promoterInfo.setReceivedRewardsToday(0);
		promoterInfo.setReceivedTimeToday(0);
		promoterInfo.setRewardsThisCycle(0);
		promoterInfo.setTotalRewards(0);
		promoterInfo.setUnreceivedRewards(0);
		promoterInfo.setReceivedCountToday(0);

		return promoterInfo;
	}

	/**
	 * 是否可提现
	 * 
	 * @param promoterUnionId
	 * @return
	 */
	public boolean canTakeReward(String promoterUnionId)
	{
		WxPromoterConfig config = WxPromoterConfig.getInstance();
		PromoterInfo promoterInfo = table.Promoter_info.update(promoterUnionId);
		if (promoterInfo == null)
		{
			logger.debug("canTakeReward(): can't find promoterInfo");
			return false;
		}

		// 领取次数限制
		long now = System.currentTimeMillis();
		if (!TimeUtil.isOneDay(now, promoterInfo.getReceivedTimeToday()))
		{
			// 今日领取时间超过一天, 清除记录
			promoterInfo.setReceivedCountToday(0);
			promoterInfo.setReceivedTimeToday(0);
			promoterInfo.setReceivedRewardsToday(0f);
		}

		int count = GetRewardCountToday(promoterUnionId);
		if (count >= config.getMaxGetCount())
		{
			logger.debug("canTakeReward(): reward count more than limit today!");
			return false;
		}

		// 领取金额限制
		if (promoterInfo.getReceivedRewardsToday() >= config.getMaxGetReward())
		{
			logger.debug("canTakeReward(): received rewards more than limite today!");
			return false;
		}

		// 是否有可领取额
		if (promoterInfo.getUnreceivedRewards() > 0)
		{
			logger.debug("canTakeReward(): promoter can take reward");
			return true;
		}

		logger.debug("canTakeReward(): promoter's unreceived rewards aren't more than zero");
		return false;
	}

	/**
	 * 统计今日领取奖励的次数
	 * 
	 * @param promoterUnionId
	 * @return
	 */
	public int GetRewardCountToday(String promoterUnionId)
	{
		PromoterInfo promoterInfo = table.Promoter_info.select(promoterUnionId);
		if (promoterInfo == null)
			return 0;

		return promoterInfo.getReceivedCountToday();

		// int count = 0;
		// long now = System.currentTimeMillis();
		//
		// List<Long> rewardList = promoterInfo.getRewardList();
		// for (int i = rewardList.size() - 1; i >=0; i--)
		// {
		// ReceiveRewardInfo info = table.Receive_reward_info.select(rewardList.get(i));
		// if (info == null)
		// {
		// logger.warn("can't find received_reward_info for {}", rewardList.get(i));
		// continue;
		// }
		//
		// if (now - info.getDate() >= DateTimeConstants.DAY)
		// break;
		// else
		// count++;
		// }
		//
		// return count;
	}

	/**
	 * 提现操作
	 * 
	 * @param promoterUnionId
	 * @return
	 *
	 * 		注意: 执行提现前需要先调用canTakeReward()进行检查
	 */
	public boolean takeReward(String promoterUnionId)
	{
		PromoterInfo promoterInfo = table.Promoter_info.update(promoterUnionId);
		if (promoterInfo == null)
		{
			logger.debug("takeReward(), promoterInfo is null, promoterUnionId={}", promoterUnionId);
			return false;
		}

		// 计算奖励数量
		float rewards = 0;
		float unreceivedRewards = promoterInfo.getUnreceivedRewards();
		float receivedRewardsToday = promoterInfo.getReceivedRewardsToday();
		float maxRewardsToday = WxPromoterConfig.getInstance().getMaxGetReward();
		if (Float.compare(unreceivedRewards + receivedRewardsToday, maxRewardsToday) >= 0)
		{
			rewards = maxRewardsToday - receivedRewardsToday;
		}
		else
		{
			rewards = unreceivedRewards;
		}

		logger.debug(
			"Before promoter take reward, rewards={}, unreceivedRewards={}, receivedRewardsToday={}, maxRewardsToday={}, totalRewards={}",
			rewards,
			unreceivedRewards,
			receivedRewardsToday,
			maxRewardsToday,
			promoterInfo.getTotalRewards());

		// 更新推广员数据
		promoterInfo.setUnreceivedRewards(unreceivedRewards - rewards);
		promoterInfo.setReceivedRewardsToday(receivedRewardsToday + rewards);
		promoterInfo.setTotalRewards(promoterInfo.getTotalRewards() + rewards);
		
		logger.debug(
			"After promoter take reward, rewards={}, unreceivedRewards={}, receivedRewardsToday={}, maxRewardsToday={}, totalRewards={}",
			rewards,
			promoterInfo.getUnreceivedRewards(),
			promoterInfo.getReceivedRewardsToday(),
			maxRewardsToday,
			promoterInfo.getTotalRewards()
			);

		// 新建一条领奖记录
		long now = System.currentTimeMillis();
		Long id = table.Receive_reward_info.newKey();
		xbean.ReceiveRewardInfo rewardInfo = table.Receive_reward_info.insert(id);
		rewardInfo.setDate(now);
		rewardInfo.setId(id);
		rewardInfo.setReceivedThisTime(rewards);
		rewardInfo.setReceivedTotal(promoterInfo.getTotalRewards());
		rewardInfo.setIsHandled(0);

		// 更新推广员的领奖记录
		promoterInfo.getRewardList().add(id);
		return true;
	}
	
	/**
	 * 根据roleId得到PromoterInfo
	 * @param roleId 玩家id
	 * @return
	 */
	public PromoterInfo getPromoterInfoByRoleId(int roleId)
	{
		RoleInfo roleInfo = table.Role_info.select(roleId);
		if (roleInfo == null)
		{
			return null;
		}
		
		return table.Promoter_info.select(roleInfo.getUnionid());
	}

	/**
	 * 由time得到周期的开始时间
	 * 
	 * @param time
	 * @return 周期的开始时间
	 */
	public long cycleStartTime(long time)
	{
		// 活动开始时间
		long startTime = WxPromoterConfig.getInstance().getStartTime();

		// 一个周期有多少毫秒
		int cycleHours = WxPromoterConfig.getInstance().getHoursPerCycle() * 3600 * 1000;

		return startTime + (time - startTime) / cycleHours * cycleHours;
	}

	/**
	 * 测试这个类的方法
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		testCycleStartTime();
	}

	/**
	 * 测试cycleStartTime()
	 */
	public static void testCycleStartTime()
	{
		WxPromoterConfig.getInstance().setStartTime(TimeUtil.time(2017, 4, 12, 16, 0, 0));
		WxPromoterConfig.getInstance().setHoursPerCycle(10);

		WeiXinShareActivityService service = new WeiXinShareActivityService();

		System.out.println(TimeUtil.timeString(service.cycleStartTime(System.currentTimeMillis()), null));
	}

	/**
	 * 判断一个玩家是否完成了任务，如果完成了任务，给推广员奖励
	 * 
	 * @param roleId
	 */
	public void rewardPromoterIfFinishTask(int roleId)
	{
		// 查询玩家信息
		RoleInfo roleInfo = table.Role_info.select(roleId);
		if (roleInfo == null)
		{
			logger.debug("rewardPromoterIfFinishTask(): can't find roleInfo, roleId={}", roleId);
			return;
		}

		// 查询被邀请者信息
		String unionid = roleInfo.getUnionid();
		InviteeInfo inviteeInfo = table.Invitee_info.update(unionid);
		if (inviteeInfo == null)
		{
			logger.debug("rewardPromoterIfFinishTask(): can't find inviteeInfo, unionid={}", unionid);
			return;
		}

		// 如果被邀请者已经完成了任务
		if (inviteeInfo.getFinished() == 1)
		{
			logger.debug("rewardPromoterIfFinishTask(): invitee's task is finished!");
			return;
		}

		// 判断被邀请者打的局数是否达到奖励要求
		inviteeInfo.setRoundCount(inviteeInfo.getRoundCount() + 1);
		int gameCountForActivity = WxPromoterConfig.getInstance().getGameCountForActivity();
		if (inviteeInfo.getRoundCount() < gameCountForActivity)
		{
			logger.debug("rewardPromoterIfFinishTask(): invitee game count is not enough!");
			return;
		}

		// 得到推广员unionid
		String promoterUnionid = inviteeInfo.getPromoterUnionId();
		PromoterInfo promoterInfo = table.Promoter_info.update(promoterUnionid);
		if (promoterInfo == null)
		{
			logger.debug(
				"rewardPromoterIfFinishTask(): con't find promoter info, roleid isn't invitee : roleId={}, unionid={}",
				roleId,
				promoterUnionid);
			return;
		}

		// 被邀请者完成了任务
		long now = System.currentTimeMillis();
		inviteeInfo.setFinished(1);
		inviteeInfo.setFinishTime(now);

		// 判断当前时间是否还在推广员的当前周期内
		long currCycleStartTime = cycleStartTime(now);
		if (currCycleStartTime != promoterInfo.getCycleStartTime())
		{
			promoterInfo.setCycleStartTime(currCycleStartTime);
			promoterInfo.setRewardsThisCycle(0);
			promoterInfo.setInviteeCountThisCycle(0);
		}

		// 增加推广员当前周期内的邀请成功数量
		promoterInfo.setInviteeCountThisCycle(promoterInfo.getInviteeCountThisCycle() + 1);
		promoterInfo.setTotalEffectiveInvitee(promoterInfo.getTotalEffectiveInvitee() + 1);

		// 得到推广员的奖励数量
		int inviteeCountThisCycle = promoterInfo.getInviteeCountThisCycle();
		float rewards = WxPromoterConfig.getInstance().GetRewardCount(inviteeCountThisCycle);

		logger.debug("Reward promoter, unionId={}, rewards={}", promoterUnionid, rewards);

		if (rewards > 0)
			promoterInfo.setUnreceivedRewards(promoterInfo.getUnreceivedRewards() + rewards);

		// bi日志
		Integer promoterRoleId = table.Unionid_2_roleid.select(promoterUnionid);
		if (promoterRoleId != null)
		{
			KodBiLogHelper.inviteSuccessRecord(promoterRoleId, promoterUnionid, WxPromoterConfig.getInstance().GetRewardCode(inviteeCountThisCycle), rewards, roleId, unionid);
		}
		else
		{
			logger.info("promoter role info is absent, can't record bi, promoterRoleId={}", promoterRoleId);
		}
	}

	/**
	 * 邀请过来的新玩家
	 * 
	 * @param roleId 新玩家id
	 * @param unionid 新玩家unionid
	 */
	public void inviteRegisterRecord(int roleId, String unionid)
	{
		// 查询被邀请者信息
		InviteeInfo inviteeInfo = table.Invitee_info.select(unionid);
		if (inviteeInfo == null)
		{
			logger.debug("inviteRegisterRecord(): can't find inviteeInfo, unionid={}", unionid);
			return;
		}

		// 得到邀请者unionid
		String promoterUnionid = inviteeInfo.getPromoterUnionId();
		PromoterInfo promoterInfo = table.Promoter_info.select(promoterUnionid);
		if (promoterInfo == null)
		{
			logger.debug("inviteRegisterRecord(): con't find promoter info, roleid isn't invitee : roleId={}, unionid={}", roleId, promoterUnionid);
			return;
		}

		// 邀请者玩家id
		Integer promoterRoleId = table.Unionid_2_roleid.select(promoterUnionid);
		if (promoterRoleId != null)
		{
			KodBiLogHelper.inviteRecordRegister(promoterRoleId, roleId);
		}
		else
		{
			logger.info("promoter role info is absent, can't record bi, promoterRoleId={}", promoterRoleId);
		}
	}
}
