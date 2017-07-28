package com.kodgames.club.action.common;

import java.util.List;

import com.kodgames.club.test.CreateClubTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.club.service.common.ClubCommonService;
import com.kodgames.club.start.CLProtobufMessageHandler;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.message.proto.club.ClubProtoBuf.CCLAllClubREQ;
import com.kodgames.message.proto.club.ClubProtoBuf.CLCAllClubRES;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

@ActionAnnotation(messageClass = CCLAllClubREQ.class, actionClass = CCLAllClubREQAction.class, serviceClass = ClubCommonService.class)
public class CCLAllClubREQAction extends CLProtobufMessageHandler<ClubCommonService, CCLAllClubREQ>
{
	private static final Logger logger = LoggerFactory.getLogger(CCLAllClubREQAction.class);

	@Override
	public void handleMessage(Connection connection, ClubCommonService service, CCLAllClubREQ message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

		int roleId = message.getRoleId();
		CLCAllClubRES.Builder builder = CLCAllClubRES.newBuilder();

		// 先获取自己所有的俱乐部id
		List<Integer> clubIds = service.getMyClubs(roleId);
		if (clubIds == null) {
			builder.setResult(PlatformProtocolsConfig.CLC_ALL_CLUB_SUCCESS);
			connection.write(callback, builder.build());

			logger.debug("allClub: player {} doesn't in any club", roleId);
			return;
		}

		builder = service.getAllClubProtoBuilder(clubIds, roleId);
		builder.setResult(PlatformProtocolsConfig.CLC_ALL_CLUB_SUCCESS);
		connection.write(callback, builder.build());

	}
}
