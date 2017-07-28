package com.kodgames.club.action.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.club.service.common.ClubCommonService;
import com.kodgames.club.start.CLProtobufMessageHandler;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.message.proto.club.ClubProtoBuf.CCLClubMembersREQ;

@ActionAnnotation(messageClass = CCLClubMembersREQ.class, actionClass = CCLClubInfoREQAction.class, serviceClass = ClubCommonService.class)
public class CCLClubMembersREQAction extends CLProtobufMessageHandler<ClubCommonService, CCLClubMembersREQ>
{
	private static final Logger logger = LoggerFactory.getLogger(CCLClubMembersREQAction.class);

	@Override
	public void handleMessage(Connection connection, ClubCommonService service, CCLClubMembersREQ message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
		connection.write(callback, service.getClubMemberListProto(message.getClubId()));
	}
}