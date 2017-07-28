package com.kodgames.club.service.manager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.club.constant.ClubConstants;
import com.kodgames.club.service.common.ClubCommonService;
import com.kodgames.club.utils.InviteCodeEncoder;
import com.kodgames.club.utils.KodBiLogHelper;
import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.message.proto.club.ClubProtoBuf;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

import xbean.ApplicantInfo;
import xbean.ClubInfo;
import xbean.ClubManager;
import xbean.ClubRoleBaseInfo;
import xbean.MemberInfo;
import xbean.RoleClubInfo;
import xbean.RoleClubs;

/**
 * Created by 001 on 2017/3/16.
 */
public class ClubManagerService extends PublicService
{
	private static final long serialVersionUID = 4743159966778900091L;

	private static final Logger logger = LoggerFactory.getLogger(ClubManagerService.class);

	/**
	 * 获取俱乐部信息用于只读
	 * 
	 * @param clubId
	 * @return
	 */
	public ClubInfo getClubInfo(int clubId)
	{
		return table.Clubs.select(clubId);
	}

	/**
	 * 获取俱乐部信息, 可修改数据
	 * 
	 * @param clubId
	 * @return
	 */
	public ClubInfo getClubInfoForWrite(int clubId)
	{
		return table.Clubs.update(clubId);
	}

	public ClubProtoBuf.CLCClubMemberModifyRES getClubMemberModifyProto(ClubProtoBuf.CCLClubMemberModifyREQ req, int managerId)
	{
		ClubProtoBuf.CLCClubMemberModifyRES.Builder builder = ClubProtoBuf.CLCClubMemberModifyRES.newBuilder();
		int clubId = req.getClubId();
		int roleId = req.getRoleId();
		builder.setClubId(clubId);
		builder.setRoleId(roleId);
		builder.setOptype(req.getOptype());

		ClubInfo club = table.Clubs.update(clubId);
		if (club == null)
		{
			builder.setResult(PlatformProtocolsConfig.CLC_CLUB_APPLICANT_CLUB_NOT_FOUND);
			return builder.build();
		}

		ClubRoleBaseInfo manager = club.getManager();
		if (manager == null || manager.getRoleId() != managerId)
		{
			builder.setResult(PlatformProtocolsConfig.CLC_CLUB_APPLICANT_NOT_PERMITED);
			return builder.build();
		}

		if (managerId == roleId)
		{
			builder.setResult(PlatformProtocolsConfig.CLC_CLUB_MEMBERMODIFY_NOT_PERMITED);
			return builder.build();
		}

		if (req.getOptype() == ClubConstants.CLUB_MEMBER_OPTYPE.TITLE)
		{
			for (MemberInfo member : club.getMembers())
			{
				if (member.getRole().getRoleId() == roleId)
				{
					member.getRole().setTitle(req.getTitle());
					builder.setResult(PlatformProtocolsConfig.CLC_CLUB_MEMBERMODIFY_SUCCESS);
					return builder.build();
				}
			}

			builder.setResult(PlatformProtocolsConfig.CLC_CLUB_QUIT_NOT_IN_CLUB);
			return builder.build();
		}
		else
		{
			for (MemberInfo member : club.getMembers())
			{
				if (member.getRole().getRoleId() == roleId)
				{
					// 推送玩家被T消息
					ClubCommonService ccs = ServiceContainer.getInstance().getPublicService(ClubCommonService.class);
					ccs.synKickToClient(roleId, clubId);
					// 删除玩家俱乐部信息
					RoleClubs roleClubs = table.Role_clubs.update(roleId);
					if (roleClubs != null)
					{
						roleClubs.getClubs().removeIf(s -> s.getClubId() == clubId);
					}

					member.setStatus(ClubConstants.CLUB_MEMBER_STATUS.LEAVE_OUT);
					this.clubMemberCountChange(club, -1); // 有效玩家-1

					KodBiLogHelper.clubLeaveLog(clubId, club.getAgentId(), club.getManager().getRoleId(), roleId, roleClubs.getVersion(), roleClubs.getChannel());

					builder.setResult(PlatformProtocolsConfig.CLC_CLUB_MEMBERMODIFY_SUCCESS);
					return builder.build();
				}
			}

			builder.setResult(PlatformProtocolsConfig.CLC_CLUB_QUIT_NOT_IN_CLUB);
			return builder.build();
		}
	}

	/**
	 * 通过申请
	 * 
	 * @param clubId
	 * @param roleId
	 * @param applicantRoleId
	 * @return
	 */
	public ClubProtoBuf.CLCClubApplicantRES getClubApplicantAcceptProto(int clubId, int roleId, int applicantRoleId, int title)
	{
		ClubProtoBuf.CLCClubApplicantRES.Builder builder = ClubProtoBuf.CLCClubApplicantRES.newBuilder();
		builder.setOptype(ClubConstants.CLUB_APPLICANT_OPTYPE.ACCEPT);
		builder.setClubId(clubId);
		builder.setRoleId(applicantRoleId);

		boolean isFirst = false;
		boolean isThisFirst = false;

		ClubInfo club = table.Clubs.update(clubId);
		if (club == null)
		{
			builder.setResult(PlatformProtocolsConfig.CLC_CLUB_APPLICANT_CLUB_NOT_FOUND);
			return builder.build();
		}

		ClubRoleBaseInfo manager = club.getManager();
		if (manager == null || manager.getRoleId() != roleId)
		{
			builder.setResult(PlatformProtocolsConfig.CLC_CLUB_APPLICANT_NOT_PERMITED);
			return builder.build();
		}

		// 只能将成员设置为初级或会员
		if (title != ClubConstants.CLUB_MEMEBER_TITLE.OBSERVER && title != ClubConstants.CLUB_MEMEBER_TITLE.MEMBER)
		{
			builder.setResult(PlatformProtocolsConfig.CLC_CLUB_APPLICANT_NOT_PERMITED);
			return builder.build();
		}

		// 如果玩家是经理, 不能再加入其他俱乐部
		ClubManager managerInfo = table.Club_manager.select(applicantRoleId);
		if (managerInfo != null)
		{
			builder.setResult(PlatformProtocolsConfig.CLC_CLUB_APPLICANT_NOT_PERMITED);
			return builder.build();
		}

		List<ApplicantInfo> applicants = club.getApplicants();
		ApplicantInfo apltInfo = null;
		int result = 0;
		for (int i = 0; i < applicants.size(); i++)
		{
			apltInfo = applicants.get(i);
			if (apltInfo.getRole().getRoleId() == applicantRoleId)
			{
				result = canAcceptApplication(club, applicantRoleId);
				if (result != PlatformProtocolsConfig.CLC_CLUB_APPLICANT_SUCCESS)
				{
					builder.setResult(result);
					return builder.build();
				}

				// 更新玩家的俱乐部列表
				RoleClubs roleClubs = table.Role_clubs.update(applicantRoleId);
				if (roleClubs == null)
				{
					roleClubs = table.Role_clubs.insert(applicantRoleId);
					isFirst = true; // 没有roleClubs数据就是第一次进入俱乐部系统
					isThisFirst = true;
				}
				RoleClubInfo clubInfo = new RoleClubInfo();
				clubInfo.setClubId(clubId);
				try
				{
					clubInfo.setInvitationCode(InviteCodeEncoder.generateCode(clubId, applicantRoleId));
				}
				catch (Exception e)
				{
					logger.error("generate invitation code failed for role {} in club {}", applicantRoleId, roleId);
				}
				roleClubs.getClubs().add(clubInfo);
				roleClubs.setVersion("");
				roleClubs.setApp_key("appKey");
				roleClubs.setChannel("");

				// 更新俱乐部的成员列表 (有可能已经在成员列表中, 被标记为LEAVE_OUT
				boolean foundMember = modifyMemberStatus(club, applicantRoleId, ClubConstants.CLUB_MEMBER_STATUS.NORMAL);
				this.clubMemberCountChange(club, 1); // 有效玩家+1
				if (!foundMember)
				{
					isThisFirst = true; // 没发现，就是第一次入这个俱乐部
					addApplicantToMembers(club, apltInfo);
				}

				// 更新玩家的title
				if (!modifyMemberTitle(club, applicantRoleId, title))
					logger.debug("can't find member {} in club {}", applicantRoleId, clubId);
				// 删除请求
				applicants.remove(i);

				KodBiLogHelper.clubJoinLog(clubId, club.getAgentId(), club.getManager().getRoleId(), applicantRoleId, isFirst, isThisFirst, roleClubs.getVersion(), roleClubs.getChannel());

				builder.setResult(PlatformProtocolsConfig.CLC_CLUB_APPLICANT_SUCCESS);
				return builder.build();
			}
		}

		logger.debug("applicant accept failed, target role {} not in applicant list", applicantRoleId);
		builder.setResult(PlatformProtocolsConfig.CLC_CLUB_APPLICANT_NOT_IN_LIST);
		return builder.build();
	}

	/**
	 * 修改玩家在俱乐部成员列表中的状态
	 * 
	 * @param club 该值需要是通过table.update()获取到的返回值
	 * @param roleId
	 * @param status
	 * @return
	 */
	private boolean modifyMemberStatus(ClubInfo club, int roleId, int status)
	{
		for (MemberInfo member : club.getMembers())
		{
			if (member.getRole().getRoleId() == roleId)
			{
				if (member.getStatus() == status)
				{
					logger.warn("role {} already in status {}", roleId, status);
				}
				else
				{
					member.setStatus(status);
				}

				return true;
			}
		}

		return false;
	}

	private boolean modifyMemberTitle(ClubInfo club, int roleId, int title)
	{
		for (MemberInfo member : club.getMembers())
		{
			if (member.getRole().getRoleId() == roleId)
			{
				member.getRole().setTitle(title);
				return true;
			}
		}

		return false;
	}

	/**
	 * 是否可允许指定的玩家加入俱乐部
	 * 
	 * @return
	 */
	private int canAcceptApplication(ClubInfo club, int applicantRoleId)
	{
		int memberCount = club.getMemberCount();
		if (memberCount >= ClubConstants.ClubDefault.MAX_MEMBERS_FOR_CLUB)
			return PlatformProtocolsConfig.CLC_CLUB_APPLICANT_MAX_MEMBERS;

		RoleClubs roleClubs = table.Role_clubs.select(applicantRoleId);
		if (roleClubs != null && roleClubs.getClubs().size() >= ClubConstants.ClubDefault.MAX_CLUBS_FOR_PLAYER)
			return PlatformProtocolsConfig.CLC_CLUB_APPLICANT_MAX_CLUBS;

		return PlatformProtocolsConfig.CLC_CLUB_APPLICANT_SUCCESS;
	}

	/**
	 * 将申请人加入到成员列表
	 * 
	 * @param club 该值需要是通过table.update()获取到的返回值
	 * @param applicant
	 */
	private void addApplicantToMembers(ClubInfo club, ApplicantInfo applicant)
	{
		MemberInfo member = new MemberInfo();
		member.setCardCount(0);
		member.setStatus(ClubConstants.CLUB_MEMBER_STATUS.NORMAL);
		member.setJoinTimestamp(System.currentTimeMillis());

		ClubRoleBaseInfo memberInfo = member.getRole();
		memberInfo.copyFrom(applicant.getRole());
		memberInfo.setName(memberInfo.getName() + "#" + (club.getMembers().size() + 1));

		ClubRoleBaseInfo inviterInfo = member.getInviter();
		inviterInfo.copyFrom(applicant.getInviter());

		club.getMembers().add(member);
	}

	/**
	 * 拒绝申请
	 * 
	 * @param clubId
	 * @param roleId
	 * @param rejectRoleId
	 * @return
	 */
	public ClubProtoBuf.CLCClubApplicantRES getClubApplicantRejectProto(int clubId, int roleId, int rejectRoleId)
	{
		ClubProtoBuf.CLCClubApplicantRES.Builder builder = ClubProtoBuf.CLCClubApplicantRES.newBuilder();
		builder.setOptype(ClubConstants.CLUB_APPLICANT_OPTYPE.REJECT);
		builder.setClubId(clubId);
		builder.setRoleId(rejectRoleId);
		ClubInfo club = table.Clubs.update(clubId);
		if (club == null)
		{
			builder.setResult(PlatformProtocolsConfig.CLC_CLUB_APPLICANT_CLUB_NOT_FOUND);
			return builder.build();
		}

		ClubRoleBaseInfo manager = club.getManager();
		if (manager == null || manager.getRoleId() != roleId)
		{
			builder.setResult(PlatformProtocolsConfig.CLC_CLUB_APPLICANT_NOT_PERMITED);
			return builder.build();
		}

		List<ApplicantInfo> applicants = club.getApplicants();
		boolean found = false;
		for (int i = 0; i < applicants.size(); i++)
		{
			if (applicants.get(i).getRole().getRoleId() == rejectRoleId)
			{
				applicants.remove(i);
				found = true;
				break;
			}
		}

		if (found)
			builder.setResult(PlatformProtocolsConfig.CLC_CLUB_APPLICANT_SUCCESS);
		else
			builder.setResult(PlatformProtocolsConfig.CLC_CLUB_APPLICANT_NOT_IN_LIST);
		return builder.build();
	}

	/**
	 * 获取申请人列表
	 * 
	 * @param clubId
	 * @param roleId
	 * @return
	 */
	public ClubProtoBuf.CLCClubApplicantListRES getClubApplicantsProto(int clubId, int roleId)
	{
		ClubProtoBuf.CLCClubApplicantListRES.Builder builder = ClubProtoBuf.CLCClubApplicantListRES.newBuilder();
		builder.setClubId(clubId);
		ClubInfo club = table.Clubs.select(clubId);
		if (club == null)
		{
			builder.setResult(PlatformProtocolsConfig.CLC_CLUB_APPLICANTS_CLUB_NOT_FOUND);
			return builder.build();
		}

		ClubRoleBaseInfo manager = club.getManager();
		if (manager == null || manager.getRoleId() != roleId)
		{
			builder.setResult(PlatformProtocolsConfig.CLC_CLUB_APPLICANTS_NOT_PERMITED);
			return builder.build();
		}

		builder.setResult(PlatformProtocolsConfig.CLC_CLUB_APPLICANTS_SUCCESS);
		club.getApplicants().forEach(member -> {
			ClubProtoBuf.ClubMemberInfoPROTO.Builder clubMemberBuild = ClubProtoBuf.ClubMemberInfoPROTO.newBuilder();
			clubMemberBuild.setRoleId(member.getRole().getRoleId());
			clubMemberBuild.setRoleName(member.getRole().getName());
			clubMemberBuild.setTitle(member.getRole().getTitle());
			clubMemberBuild.setApplyTimestamp(member.getApplyTimestamp());
			clubMemberBuild.setGameCount(member.getGameCount());
			clubMemberBuild.setOnlineFlag(0);
			clubMemberBuild.setOfflineTimestamp(0);

			clubMemberBuild.setInviterName(member.getInviter().getName());
			builder.addMembers(clubMemberBuild);
		});

		logger.debug("getClubApplicantsProto-------applicants: {}", club.getApplicants());
		return builder.build();
	}

	public int clubMemberCountChange(ClubInfo club, int change)
	{
		club.setMemberCount(club.getMemberCount() + change);
		// 输出bilog
		KodBiLogHelper.clubAllCountLog(club.getClubId(), club.getAgentId(), club.getManager().getRoleId(), club.getMemberCount());
		return club.getMemberCount();
	}

	/**
	 * 获得某经理下所有俱乐部的申请人数
	 * 
	 * @param roleId
	 * @return
	 */
	public int getApplicantCount(int roleId)
	{
		int count = 0;
		if (table.Club_manager.select(roleId) == null)
			return count;

		RoleClubs roleclubs = table.Role_clubs.select(roleId);
		List<RoleClubInfo> clubs = roleclubs.getClubs();
		if (roleclubs == null || clubs.size() == 0)
		{
			return count;
		}

		for (RoleClubInfo clubId : clubs)
		{
			ClubInfo clubInfo = table.Clubs.select(clubId.getClubId());
			ClubRoleBaseInfo manager = clubInfo.getManager();
			if (manager != null && manager.getRoleId() == roleId)
			{
				count += clubInfo.getApplicants().size();
			}
		}

		return count;

	}

}
