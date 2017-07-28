package com.kodgames.club.action.common;

import com.kodgames.club.service.common.ClubCommonService;
import com.kodgames.club.utils.KodBiLogHelper;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.club.ClubProtoBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xbean.ClubInfo;
import xbean.RoleClubs;


@ActionAnnotation(messageClass = ClubProtoBuf.GCLLoginSYN.class, actionClass = GCLLoginSYNAction.class, serviceClass = ClubCommonService.class)
public class GCLLoginSYNAction extends ProtobufMessageHandler<ClubCommonService, ClubProtoBuf.GCLLoginSYN>
{

	private static final Logger logger = LoggerFactory.getLogger(GCLLoginSYNAction.class);
	
	@Override
	public void handleMessage(Connection connection, ClubCommonService service, ClubProtoBuf.GCLLoginSYN message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

		int roleId = message.getRoleId();

		RoleClubs roleClubs = table.Role_clubs.update(roleId);
		if (roleClubs == null) {
			// 这个人不曾进过俱乐部，不输出log
			return;
		}
		roleClubs.setChannel(message.getChannel());
		roleClubs.setVersion(message.getVersion());
		roleClubs.setApp_key(message.getAppKey());

		roleClubs.getClubs().forEach( c -> {
			int clubId = c.getClubId();

			ClubInfo club = table.Clubs.select(clubId);
			if (club != null) {
				int agentId = club.getAgentId();
				int managerId = club.getManager().getRoleId();
				KodBiLogHelper.clubLoginLog(clubId, agentId, managerId,
						roleId, message.getVersion(), message.getChannel());
			}
		});
	}

	@Override
	public Object getMessageKey(Connection connection, ClubProtoBuf.GCLLoginSYN message)
	{
		return  message.getRoleId();
	}
}
