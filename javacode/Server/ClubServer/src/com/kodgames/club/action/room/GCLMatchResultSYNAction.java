package com.kodgames.club.action.room;

import com.kodgames.club.constant.ClubConstants;
import com.kodgames.club.service.common.ClubCommonService;
import com.kodgames.club.service.room.ClubRoomService;
import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.message.proto.club.ClubProtoBuf;
import com.kodgames.message.proto.club.ClubProtoBuf.GCLMatchResultSYN;
import com.kodgames.message.proto.game.GameProtoBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xbean.ClubInfo;
import xbean.ClubRoomInfo;
import xbean.MemberInfo;
import xbean.RoomCost;


@ActionAnnotation(messageClass = GCLMatchResultSYN.class, actionClass = GCLMatchResultSYNAction.class, serviceClass = ClubRoomService.class)
public class GCLMatchResultSYNAction extends ProtobufMessageHandler<ClubRoomService, GCLMatchResultSYN>
{

	private static final Logger logger = LoggerFactory.getLogger(GCLMatchResultSYNAction.class);
	
	@Override
	public void handleMessage(Connection connection, ClubRoomService service, GCLMatchResultSYN message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

		int roomId = message.getRoomId();
		int clubId = message.getClubId();

		ClubRoomInfo room = service.getRoom(clubId, roomId);  // 先取 room 锁，防止room表和club表死锁
		if (room == null)
		{
			logger.warn("GCLFinalMatchResultSYNAction : no such room {} in club {}", roomId, clubId);
			return;
		}

		ClubCommonService ccs = ServiceContainer.getInstance().getPublicService(ClubCommonService.class);

		message.getPlayersList().forEach( p -> {
			MemberInfo member = ccs.getMemberInfo(p.getRoleId(), clubId);
			if (member != null) {
				member.setTotalGameCount(member.getTotalGameCount()+1);
				member.setTodayGameCount(member.getTodayGameCount()+1);
			}
		});

		ClubInfo club = ccs.getClub(clubId);
		club.setTodayGameCount(club.getTodayGameCount()+1);
		club.setGameCount(club.getGameCount()+1);
	}

	@Override
	public Object getMessageKey(Connection connection, GCLMatchResultSYN message)
	{
		return  message.getClubId();
	}

}
