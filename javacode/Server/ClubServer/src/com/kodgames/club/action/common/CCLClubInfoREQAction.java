package com.kodgames.club.action.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.club.constant.ClubConstants;
import com.kodgames.club.service.common.ClubCommonService;
import com.kodgames.club.start.CLProtobufMessageHandler;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.message.proto.club.ClubProtoBuf;
import com.kodgames.message.proto.club.ClubProtoBuf.CCLClubInfoREQ;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

@ActionAnnotation(messageClass = CCLClubInfoREQ.class, actionClass = CCLClubInfoREQAction.class, serviceClass = ClubCommonService.class)
public class CCLClubInfoREQAction extends CLProtobufMessageHandler<ClubCommonService, CCLClubInfoREQ>
{
	private static final Logger logger = LoggerFactory.getLogger(CCLClubInfoREQAction.class);

	@Override
	public void handleMessage(Connection connection, ClubCommonService service, CCLClubInfoREQ message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

		int roleId = connection.getRemotePeerID();
		int clubId = message.getClubId();

		ClubProtoBuf.CLCClubInfoRES.Builder builder = ClubProtoBuf.CLCClubInfoRES.newBuilder();

		if (!service.checkClubId(clubId))
		{
			builder.setResult(PlatformProtocolsConfig.CLC_CLUB_NOT_FOUND);
			builder.setClubId(clubId);
			connection.write(callback, builder.build());
			return;
		}

		// 检查玩家是否已经退出俱乐部了
		if (!service.checkMemberInClub(clubId, roleId))
		{
			builder.setResult(PlatformProtocolsConfig.CLC_CLUB_KICK_OUT);
			builder.setClubId(clubId);
			connection.write(callback, builder.build());
			return;
		}

		// 如果被封停了，那么就提示封停
		if (service.checkClubStatus(clubId, ClubConstants.CLUB_STATUS.SAEL_FLAG))
		{
			builder.setResult(PlatformProtocolsConfig.CLC_CLUB_SEALED);
			builder.setClubId(clubId);
			connection.write(callback, builder.build());
			return;
		}

		// 成功了
		builder.setResult(PlatformProtocolsConfig.CLC_CLUB_INFO_SUCCESS);
		builder.setClubId(clubId);

		ClubProtoBuf.ClubInfoPROTO proto = service.getClubInfoProto(roleId, clubId);
		if (proto == null)
		{ // 理论上的检查！
			builder.setResult(PlatformProtocolsConfig.CLC_CLUB_NOT_FOUND);
			builder.setClubId(clubId);
			connection.write(callback, builder.build());
			return;
		}

		builder.setInfo(proto);

		connection.write(callback, builder.build());
	}
}
