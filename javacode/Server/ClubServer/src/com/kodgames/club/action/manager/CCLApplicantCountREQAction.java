package com.kodgames.club.action.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.club.service.manager.ClubManagerService;
import com.kodgames.club.start.CLProtobufMessageHandler;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.message.proto.club.ClubProtoBuf.CCLApplicantCountREQ;
import com.kodgames.message.proto.club.ClubProtoBuf.CLCApplicantCountRES;
import com.kodgames.message.proto.club.ClubProtoBuf.CLCApplicantCountRES.Builder;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

@ActionAnnotation(messageClass = CCLApplicantCountREQ.class, actionClass = CCLApplicantCountREQAction.class, serviceClass = ClubManagerService.class)
public class CCLApplicantCountREQAction extends CLProtobufMessageHandler<ClubManagerService, CCLApplicantCountREQ>
{
	private static final Logger logger = LoggerFactory.getLogger(CCLApplicantCountREQAction.class);

	@Override
	public void handleMessage(Connection connection, ClubManagerService service, CCLApplicantCountREQ message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
		Builder builder = CLCApplicantCountRES.newBuilder();
		builder.setResult(PlatformProtocolsConfig.CLC_APPLICANT_COUNT_SUCCESS);
		int count = service.getApplicantCount(connection.getRemotePeerID());
		builder.setCount(count);
		connection.write(callback, builder.build());
	}
}