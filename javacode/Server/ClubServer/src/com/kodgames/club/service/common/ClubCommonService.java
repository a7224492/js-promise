package com.kodgames.club.service.common;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.club.constant.ClubConstants;
import com.kodgames.club.utils.ClubUtils;
import com.kodgames.club.utils.InviteCodeEncoder;
import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.message.proto.club.ClubProtoBuf;
import com.kodgames.message.proto.club.ClubProtoBuf.CLCClubMembersRES;
import com.kodgames.message.proto.club.ClubProtoBuf.ClCBanClubSYN;
import com.kodgames.message.proto.club.ClubProtoBuf.ClCBeKickClubSYN;
import com.kodgames.message.proto.club.ClubProtoBuf.ClubInfoPROTO;
import com.kodgames.message.proto.club.ClubProtoBuf.ClubMemberInfoPROTO;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

import xbean.ClubInfo;
import xbean.ClubRoleBaseInfo;
import xbean.MemberInfo;
import xbean.RoleClubInfo;
import xbean.RoleClubs;
import xbean.RoomCost;

public class ClubCommonService extends PublicService
{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8459048121837825757L;

	private static final Logger logger = LoggerFactory.getLogger(ClubCommonService.class);

	// 保存查询到的角色名和头像信息
	// 这些信息需要到GameServer上去查询, 每次查询到之后都缓存到本地服务器
	public class GameRoleInfo
	{
		public int roleId;
		public String roleName;
		public int gameCount;
		public String roleIcon;
		public long addTime; // 添加时间
	}

	private ConcurrentHashMap<Integer, GameRoleInfo> gameRoleInfoList = new ConcurrentHashMap<Integer, GameRoleInfo>();
	private static final long roleInfoMaxCacheTime = 5 * 60 * 60; // 用户信息缓存时间

	/**
	 * 添加用户信息到本地缓存
	 * 
	 * @param roleId
	 * @param roleName
	 * @param gameCount
	 * @param roleIcon
	 */
	public void addRoleInfo(int roleId, String roleName, int gameCount, String roleIcon)
	{
		if (gameRoleInfoList.containsKey(roleId))
		{
			logger.debug("role {}'s info already in cache");
			return;
		}

		GameRoleInfo roleInfo = new GameRoleInfo();
		roleInfo.roleId = roleId;
		roleInfo.roleName = roleName;
		roleInfo.gameCount = gameCount;
		roleInfo.roleIcon = roleIcon;
		roleInfo.addTime = System.currentTimeMillis();
		gameRoleInfoList.put(roleId, roleInfo);
	}

	/**
	 * 获取缓存的用户信息
	 * 
	 * @param roleId
	 * @return
	 */
	public GameRoleInfo getRoleInfo(int roleId)
	{
		GameRoleInfo roleInfo = gameRoleInfoList.get(roleId);
		if (roleInfo != null)
		{
			if (System.currentTimeMillis() - roleInfo.addTime > roleInfoMaxCacheTime)
			{
				gameRoleInfoList.remove(roleId);
				return null;
			}
		}
		return roleInfo;
	}

	/**
	 * 获取俱乐部的cost信息，如果没有则设为默认 读写权限
	 * 
	 * @param clubId
	 * @return
	 */
	public RoomCost getRoomCost(int clubId)
	{
		ClubInfo club = table.Clubs.update(clubId);
		if (club == null)
		{
			logger.error("ClubCommonService getRoomCost : no such club {}", clubId);
			return null;
		}

		RoomCost cost = club.getRoomCost();
		return cost;
	}

	/**
	 * 检查clubId，是否存在在库中
	 * 
	 * @param clubId
	 * @return
	 */
	public boolean checkClubId(int clubId)
	{
		ClubInfo club = table.Clubs.select(clubId);
		return club == null ? false : true;
	}

	public ClubInfo getClub(int clubId)
	{
		return table.Clubs.update(clubId);
	}

	public ClubInfo getClubForWrite(int clubId)
	{
		return table.Clubs.update(clubId);
	}

	/**
	 * 获取俱乐部简要信息
	 * 
	 * @param clubId
	 * @return
	 */
	public ClubProtoBuf.ClubInfoPROTO.Builder getClubLiteInfo(int clubId)
	{
		ClubInfo club = table.Clubs.select(clubId);
		if (club == null)
			return null;

		ClubProtoBuf.ClubInfoPROTO.Builder protoBuilder = ClubProtoBuf.ClubInfoPROTO.newBuilder();
		protoBuilder.setClubId(clubId);
		protoBuilder.setClubName(club.getClubName());
		protoBuilder.setMemberCount(club.getMemberCount());
		protoBuilder.setMyRoomCardCount(0);
		protoBuilder.setMyTitle(0);
		protoBuilder.setMyInvitationCode("");
		protoBuilder.setCreateTimestamp(club.getCreateTimestamp());
		protoBuilder.setCreator(club.getCreator().getName());
		protoBuilder.setManager(club.getManager().getName());
		protoBuilder.setClubLevel(club.getLevel());
		protoBuilder.setGameCount(club.getGameCount());
		protoBuilder.setTodayGameCount(club.getTodayGameCount());
		protoBuilder.setNotice(club.getNotice());
		protoBuilder.setNoticeTime(club.getNoticeTime());
		return protoBuilder;
	}

	/**
	 * 获取某个俱乐部中，某个成员的数据 读写权限
	 * 
	 * @param memberId
	 * @param clubId
	 * @return
	 */
	public MemberInfo getMemberInfo(int memberId, int clubId)
	{

		ClubInfo club = table.Clubs.update(clubId);
		if (club == null)
		{
			logger.error("ClubCommonService getMemberInfo : no such club {}", clubId);
			return null;
		}

		List<MemberInfo> memberList = club.getMembers();
		for (MemberInfo v : memberList)
		{
			if (v.getRole().getRoleId() == memberId)
			{
				return v;
			}
		}

		logger.error("ClubCommonService getMemberInfo : no such player {} in club", memberId, clubId);

		return null;
	}

	/**
	 * 获取某个俱乐部的经理id
	 * @param clubId
	 * @return
	 */
	public int getManagerId(int clubId)
	{
		ClubInfo club = table.Clubs.select(clubId);

		if (club == null)
		{
			logger.error("ClubCommonService getManagerId : no such club {}", clubId);
			return 0;
		}

		return club.getManager().getRoleId();
	}

	/**
	 * 根据roleId和clubId获取邀请码
	 * 
	 * @param roleId
	 * @param clubId
	 * @return
	 */
	private String getInvitationCode(int roleId, int clubId)
	{
		try
		{
			return InviteCodeEncoder.generateCode(clubId, roleId);
		}
		catch (Exception e)
		{
			logger.error("get invitation code failed for club {} and role {}", clubId, roleId);
		}

		// return roleId + "" + clubId;
		return "INVALID!";
	}

	/**
	 * 生成某个玩家的俱乐部详细信息 这里需要添加一个清理今日俱乐部数据的东西，因为目前的定时器是以服务器重启时间来算24小时的。。
	 * 
	 * @param roleId
	 * @param clubId
	 * @return
	 */
	public ClubInfoPROTO getClubInfoProto(int roleId, int clubId)
	{
		ClubInfo club = table.Clubs.update(clubId);
		if (club == null)
		{
			return null;
		}

		// 每天第一个请求的人，清理昨天的数据
		boolean isYesterday = ClubUtils.isBeforeYesterday(club.getTodayClearTime());
		if (isYesterday)
		{
			club.setTodayClearTime(System.currentTimeMillis());
			club.setTodayGameCount(0);
			club.getMembers().forEach(m -> m.setTodayGameCount(0));
		}

		ClubProtoBuf.ClubInfoPROTO.Builder protoBuilder = ClubProtoBuf.ClubInfoPROTO.newBuilder();
		MemberInfo memberInfo = this.getMemberInfo(roleId, clubId);
		{
			protoBuilder.setClubId(clubId);
			protoBuilder.setClubName(club.getClubName());
			protoBuilder.setMemberCount(club.getMemberCount());
			protoBuilder.setMyRoomCardCount(memberInfo.getCardCount());
			protoBuilder.setMyTitle(memberInfo.getRole().getTitle());
			protoBuilder.setMyInvitationCode(this.getInvitationCode(roleId, clubId));
			protoBuilder.setCreateTimestamp(club.getCreateTimestamp());
			protoBuilder.setCreator(club.getCreator().getName());
			protoBuilder.setManager(club.getManager().getName());
			protoBuilder.setClubLevel(club.getLevel());
			protoBuilder.setGameCount(club.getGameCount());
			protoBuilder.setNotice(club.getNotice());
			protoBuilder.setNoticeTime(club.getNoticeTime());
			protoBuilder.setRoomCost(club.getRoomCost().getCost());
			protoBuilder.setPayType(club.getRoomCost().getPayType());
			ClubRoleBaseInfo manager = club.getManager();
			if (manager != null && manager.getRoleId() == roleId)
			{
				protoBuilder.setApplicantCount(club.getApplicants().size());
			}
		}

		return protoBuilder.build();
	}

	/**
	 * 生成某个俱乐部的玩家列表
	 * 
	 * @param clubId
	 * @return
	 */
	public CLCClubMembersRES getClubMemberListProto(int clubId)
	{
		CLCClubMembersRES.Builder builder = CLCClubMembersRES.newBuilder();
		builder.setClubId(clubId);
		ClubInfo club = table.Clubs.select(clubId);
		if (club == null)
		{
			builder.setResult(PlatformProtocolsConfig.CLC_CLUB_NOT_FOUND);
			return builder.build();
		}

		builder.setResult(PlatformProtocolsConfig.CLC_CLUB_MEMBERS_SUCCESS);

		club.getMembers().forEach(member -> {
			if (member.getStatus() != ClubConstants.CLUB_MEMBER_STATUS.LEAVE_OUT)
			{
				ClubMemberInfoPROTO.Builder clubMemberBuild = ClubMemberInfoPROTO.newBuilder();
				clubMemberBuild.setRoleId(member.getRole().getRoleId());
				clubMemberBuild.setRoleName(member.getRole().getName());
				clubMemberBuild.setTitle(member.getRole().getTitle());
				clubMemberBuild.setOnlineFlag(0);
				clubMemberBuild.setOfflineTimestamp(0);
				clubMemberBuild.setJoinTimestamp(member.getJoinTimestamp());
				clubMemberBuild.setGameCount(member.getTotalGameCount());
				clubMemberBuild.setTodayGameCount(member.getTodayGameCount());

				clubMemberBuild.setInviterName(member.getInviter().getName());
				builder.addMembers(clubMemberBuild);
			}
		});
		return builder.build();
	}

	/**
	 * 获取自己的所有俱乐部id
	 * 
	 * @param roleId
	 * @return
	 */
	public List<Integer> getMyClubs(int roleId)
	{
		RoleClubs roleclubs = table.Role_clubs.select(roleId);
		if (roleclubs == null || roleclubs.getClubs().size() == 0)
		{
			// 没有自己的俱乐部信息,或者没有俱乐部
			return null;
		}

		List<RoleClubInfo> clubs = roleclubs.getClubs();
		List<Integer> ids = new ArrayList<>();
		clubs.forEach(v -> ids.add(v.getClubId()));
		return ids;
	}

	/**
	 * 生成某个玩家所有俱乐部的基础数据
	 * 
	 * @param ids
	 * @param roleId
	 * @return
	 */
	public ClubProtoBuf.CLCAllClubRES.Builder getAllClubProtoBuilder(List<Integer> ids, int roleId)
	{

		ClubProtoBuf.CLCAllClubRES.Builder builder = ClubProtoBuf.CLCAllClubRES.newBuilder();

		ids.forEach(id -> {
			ClubInfoPROTO.Builder pb = ClubInfoPROTO.newBuilder();
			ClubInfo club = table.Clubs.select(id);
			if (club != null)
			{
				pb.setClubId(id);
				pb.setClubName(club.getClubName());
				pb.setMemberCount(club.getMemberCount());
				pb.setGameCount(club.getGameCount());
				pb.setNotice(club.getNotice());
				ClubRoleBaseInfo manager = club.getManager();
				if (manager != null && manager.getRoleId() == roleId)
				{
					pb.setApplicantCount(club.getApplicants().size());
				}

				MemberInfo me = this.getMemberInfo(roleId, id);
				pb.setMyRoomCardCount(me.getCardCount());
				pb.setMyTitle(me.getRole().getTitle());
				pb.setIsBan(checkClubStatus(id, ClubConstants.CLUB_STATUS.SAEL_FLAG));

				builder.addClubList(pb.build());
			}
		});
		return builder;
	}

	public boolean checkClubStatus(int clubId, int statusFlag)
	{
		ClubInfo club = table.Clubs.select(clubId);
		if (club == null)
		{
			logger.error("ClubCommonService checkClubStatus : no such club {}", clubId);
			return false;
		}

		return ClubUtils.checkClubStatus(club.getStatus(), statusFlag);
	}

	public boolean checkMemberInClub(int clubId, int roleId)
	{
		ClubInfo club = table.Clubs.select(clubId);
		if (club == null)
		{
			logger.error("ClubCommonService checkMemberStatus : no such club {}", clubId);
			return false;
		}

		for (MemberInfo memberInfo : club.getMembers())
		{
			if (memberInfo.getRole().getRoleId() == roleId)
			{
				return memberInfo.getStatus() == ClubConstants.CLUB_MEMBER_STATUS.NORMAL;
			}
		}

		logger.error("ClubCommonService checkMemberStatus : member {} not found", roleId);
		return false;
	}

	/**
	 * 向俱乐部所有成员广播俱乐部封停消息
	 * 
	 * @param clubId
	 */
	public void synClubBanToClient(int clubId)
	{
		ClubInfo club = table.Clubs.select(clubId);
		if (club == null)
		{
			return;
		}

		ClCBanClubSYN.Builder builder = ClCBanClubSYN.newBuilder();
		builder.setClubId(clubId);
		builder.setClubName(club.getClubName());

		List<Integer> roleIds = new ArrayList<>();
		club.getMembers().forEach(member -> {
			roleIds.add(member.getRole().getRoleId());
		});

		// game转播
		ClubUtils.broadcastMsg2Game(GlobalConstants.DEFAULT_CALLBACK, roleIds, builder.build());
	}

	/**
	 * 向玩家推送被T消息
	 * 
	 * @param clubId
	 */
	public void synKickToClient(int roleId, int clubId)
	{
		ClubInfo club = table.Clubs.select(clubId);
		if (club == null)
		{
			return;
		}

		ClCBeKickClubSYN.Builder builder = ClCBeKickClubSYN.newBuilder();
		builder.setClubId(clubId);
		builder.setClubName(club.getClubName());
		// game转播
		ClubUtils.broadcastMsg2Game(GlobalConstants.DEFAULT_CALLBACK, roleId, builder.build());
	}
}
