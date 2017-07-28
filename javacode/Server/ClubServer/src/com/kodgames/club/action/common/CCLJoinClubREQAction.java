package com.kodgames.club.action.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.club.constant.ClubConstants;
import com.kodgames.club.constant.ClubRights;
import com.kodgames.club.service.common.ClubCommonService;
import com.kodgames.club.start.CLProtobufMessageHandler;
import com.kodgames.club.utils.InviteCodeEncoder;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.message.proto.club.ClubProtoBuf;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

import xbean.ApplicantInfo;
import xbean.ClubInfo;
import xbean.ClubManager;
import xbean.MemberInfo;

/**
 * Created by 001 on 2017/3/17.
 */

@ActionAnnotation(messageClass = ClubProtoBuf.CCLJoinClubREQ.class, actionClass = CCLJoinClubREQAction.class, serviceClass = ClubCommonService.class)
public class CCLJoinClubREQAction extends CLProtobufMessageHandler<ClubCommonService, ClubProtoBuf.CCLJoinClubREQ>
{
	private static final Logger logger = LoggerFactory.getLogger(CCLJoinClubREQAction.class);

	@Override
	public void handleMessage(Connection connection, ClubCommonService service, ClubProtoBuf.CCLJoinClubREQ message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
		String code = message.getInvitationCode();
		ClubProtoBuf.CLCJoinClubRES.Builder builder = ClubProtoBuf.CLCJoinClubRES.newBuilder();

		int roleId = connection.getRemotePeerID();
		int clubId = 0;
		int inviterId = 0;
		try
		{
			InviteCodeEncoder.ClubCodeInfo clubInfo = InviteCodeEncoder.getClubInfo(code);
			inviterId = clubInfo.getRoleId();
			clubId = clubInfo.getClubId();
		}
		catch (Exception e)
		{
			logger.error("get club info from invitation code failed: {}", code);
			builder.setResult(PlatformProtocolsConfig.CLC_CLUB_WRONG_CODE);
			builder.setInfo("invalid invitation code");
			connection.write(callback, builder.build());
			return;
		}

		ClubInfo club = service.getClubForWrite(clubId);

		if (club == null)
		{
			builder.setResult(PlatformProtocolsConfig.CLC_CLUB_WRONG_CODE);
			builder.setInfo("invalid invitation code");
			connection.write(callback, builder.build());
			return;
		}

		// 检查邀请人是否有权限
		MemberInfo inviterMember = service.getMemberInfo(inviterId, clubId);
		if (inviterMember == null || !ClubRights.hasRight(inviterMember, ClubConstants.CLUB_ACTIONS.Invitation))
		{
			builder.setResult(PlatformProtocolsConfig.CLC_CLUB_WRONG_CODE);
			builder.setInfo("inviter not permitted");
			connection.write(callback, builder.build());
			return;
		}

		// 经理不允许加入其他俱乐部
		ClubManager managerInfo = table.Club_manager.select(roleId);
		if (managerInfo != null)
		{
			builder.setResult(PlatformProtocolsConfig.CLC_CLUB_JOIN_NOT_PERMITTED);
			builder.setInfo("manager can't join other club");
			connection.write(callback, builder.build());
			return;
		}

		// 检查是否已在申请人列表中
		for (ApplicantInfo aplt : club.getApplicants())
		{
			if (aplt.getRole().getRoleId() == roleId)
			{
				builder.setResult(PlatformProtocolsConfig.CLC_CLUB_JOIN_ALREADY_JOINED_APPLICANT);
				builder.setInfo("aleady in applicant list");
				connection.write(callback, builder.build());
				return;
			}
		}

		// 是否已在成员列表中
		for (MemberInfo member : club.getMembers())
		{
			if (member.getRole().getRoleId() == roleId && member.getStatus() == ClubConstants.CLUB_MEMBER_STATUS.NORMAL)
			{
				builder.setResult(PlatformProtocolsConfig.CLC_CLUB_JOIN_ALREADY_JOINED);
				builder.setInfo("aleady in applicant list");
				connection.write(callback, builder.build());
				return;
			}
		}

		// 如果被封停了，那么就提示封停
		if (service.checkClubStatus(clubId, ClubConstants.CLUB_STATUS.SAEL_FLAG))
		{
			builder.setResult(PlatformProtocolsConfig.CLC_CLUB_SEALED);
			builder.setInfo("club is Sealed");
			connection.write(callback, builder.build());
			return;
		}

		if (club.getMemberCount() >= ClubConstants.ClubDefault.MAX_MEMBERS_FOR_CLUB)
		{
			builder.setResult(PlatformProtocolsConfig.CLC_CLUB_APPLICANT_MAX_MEMBERS);
			builder.setInfo("club is Sealed");
			connection.write(callback, builder.build());
			return;
		}

		// 自己的角色信息 和 邀请人信息一定在缓存中
		ClubCommonService.GameRoleInfo roleInfo = service.getRoleInfo(roleId);
		ClubCommonService.GameRoleInfo inviterInfo = service.getRoleInfo(inviterId);
		if (roleInfo == null || inviterInfo == null)
		{
			builder.setResult(PlatformProtocolsConfig.CLC_CLUB_NOT_FOUND);
			if (roleInfo == null)
				builder.setInfo("can't find role info");
			else
				builder.setInfo("can't find inviter info");
			connection.write(callback, builder.build());
			return;
		}

		ApplicantInfo applicant = new ApplicantInfo();
		applicant.getRole().setRoleId(roleId);
		applicant.getRole().setName(roleInfo.roleName);
		applicant.getInviter().setRoleId(inviterId);
		applicant.getInviter().setName(inviterInfo.roleName);
		applicant.setGameCount(roleInfo.gameCount);
		applicant.setApplyTimestamp(System.currentTimeMillis());

		club.getApplicants().add(applicant);

		// 返回加入俱乐部成功给客户端
		builder.setResult(PlatformProtocolsConfig.CLC_CLUB_JOIN_SUCCESS);
		connection.write(callback, builder.build());
	}
}
