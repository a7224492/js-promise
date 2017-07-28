package com.kodgames.club.utils;

import com.kodgames.kodbilog.KodBiLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * BI 统计log
 *
 * http://wiki.kodgames.net/lib/bef7eecb-be81-4c14-8646-c68a63c220e6/file/BI文档/BIlog_H5_V1.0.0.md
 *
 */
public class KodBiLogHelper
{
	/**
	 * 登陆俱乐部log
	 * @param clubId
	 * @param agentId
	 * @param managerId
	 * @param memberId
	 * @param version
	 * @param channel
	 */
	public static void clubLoginLog(int clubId, int agentId, int managerId, int memberId, String version, String channel)
	{
		List<Object> args = new ArrayList<>();
		args.add("club_login");
		args.add(channel);
		args.add(clubId);
		args.add(agentId);
		args.add(managerId);
		args.add(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date().getTime())); // 记录生成时间
		args.add(memberId);
		KodBiLog.getInstance().doLog(args);
	}

	/**
	 * 只要人数有变化就输出log
	 * @param clubId
	 * @param agentId
	 * @param managerId
	 * @param memberCount
	 */
	public static void clubAllCountLog(int clubId, int agentId, int managerId, int memberCount)
	{
		List<Object> args = new ArrayList<>();
		args.add("club_all_count");
		args.add(clubId);
		args.add(agentId);
		args.add(managerId);
		args.add(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date().getTime())); // 记录生成时间
		args.add(memberCount);
		KodBiLog.getInstance().doLog(args);
	}

	/**
	 * 加入俱乐部时的log
	 * @param clubId
	 * @param agentId
	 * @param managerId
	 * @param memberId
	 * @param isFirst
	 * @param isThisFirst
	 * @param version
	 * @param channel
	 */
	public static void clubJoinLog(int clubId, int agentId, int managerId, int memberId, boolean isFirst, boolean isThisFirst, String version, String channel)
	{
		List<Object> args = new ArrayList<>();
		args.add("club_join");
		args.add(channel);
		args.add(clubId);
		args.add(agentId);
		args.add(managerId);
		args.add(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date().getTime())); // 记录生成时间
		args.add(memberId);
		args.add(isFirst ? 1 : 0);
		args.add(isThisFirst ? 1 : 0);
		KodBiLog.getInstance().doLog(args);
	}

	/**
	 * 玩家退出(踢出)时的log
	 * @param clubId
	 * @param agentId
	 * @param managerId
	 * @param memberId
	 * @param version
	 * @param channel
	 */
	public static void clubLeaveLog(int clubId, int agentId, int managerId, int memberId, String version, String channel)
	{
		List<Object> args = new ArrayList<>();
		args.add("club_leave");
		args.add(channel);
		args.add(clubId);
		args.add(agentId);
		args.add(managerId);
		args.add(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date().getTime())); // 记录生成时间
		args.add(memberId);
		KodBiLog.getInstance().doLog(args);
	}

	/**
	 * 记录玩家消耗的log
	 * @param clubId
	 * @param agentId
	 * @param managerId
	 * @param memberId
	 * @param costType
	 * @param costCount
	 * @param cardRemain
	 * @param version
	 * @param channel
	 */
	public static void clubMemberCostLog(int clubId, int agentId, int managerId, int memberId, int costType, int costCount, int cardRemain, String version, String channel)
	{
		List<Object> args = new ArrayList<>();
		args.add("club_member_cost_card");
		args.add(channel);
		args.add(clubId);
		args.add(agentId);
		args.add(managerId);
		args.add(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date().getTime())); // 记录生成时间
		args.add(costType);
		args.add(memberId);
		args.add(costCount);
		args.add(cardRemain);
		KodBiLog.getInstance().doLog(args);
	}
}