package com.kodgames.club.action.common;

import com.kodgames.club.service.common.ClubCommonService;
import com.kodgames.club.service.room.ClubRoomService;
import com.kodgames.club.start.CLProtobufMessageHandler;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.message.proto.club.ClubProtoBuf;
import com.kodgames.message.proto.club.ClubProtoBuf.CCLClubTableREQ;
import com.kodgames.message.protocol.PlatformProtocolsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xbean.ClubInfo;
import xbean.ClubRoomInfo;

import java.util.ArrayList;
import java.util.List;

@ActionAnnotation(messageClass = CCLClubTableREQ.class, actionClass = CCLClubTableREQAction.class, serviceClass = ClubRoomService.class)
public class CCLClubTableREQAction extends CLProtobufMessageHandler<ClubRoomService, CCLClubTableREQ>
{
	private static final Logger logger = LoggerFactory.getLogger(CCLClubTableREQAction.class);

	@Override
	public void handleMessage(Connection connection, ClubRoomService service, CCLClubTableREQ message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

		int roleId = message.getRoleId();
		int clubId = message.getClubId();

		ClubCommonService ccs = ServiceContainer.getInstance().getPublicService(ClubCommonService.class);

		ClubProtoBuf.CLCClubTableRES.Builder builder = ClubProtoBuf.CLCClubTableRES.newBuilder();

		// 先获取自己所有的俱乐部id
		List<Integer> clubIds = ccs.getMyClubs(roleId);
		if (clubIds == null) {
			builder.setResult(PlatformProtocolsConfig.CLC_CLUB_TABLE_NOT_IN_ANY_CLUB);
			connection.write(callback, builder.build());

			logger.debug("allClub: player {} doesn't in any club", roleId);
			return;
		}

		int pos = clubIds.indexOf(clubId);
		if (clubId > 0 && pos == -1) {
			builder.setResult(PlatformProtocolsConfig.CLC_CLUB_TABLE_NOT_IN_THIS_CLUB);
			connection.write(callback, builder.build());

			logger.warn("allClub: player {} doesn't in this club {}", roleId, clubId);
			return;
		}

		List<Integer> p_clubIds = new ArrayList<>();
		if (clubId > 0) {
			p_clubIds.add(clubId);
		} else {
			p_clubIds.addAll(clubIds);
		}


		List<ClubRoomInfo> rooms = service.getRooms(p_clubIds, clubId == 0); // 先取 room 锁，防止room表和club表死锁

		rooms.forEach( room -> {
			ClubInfo club = table.Clubs.select(room.getClubId());	// zdb 应该是会在内存里缓存，所以直接每次都读
			if (club != null) {
				ClubProtoBuf.ClubTableInfoPROTO.Builder tb = ClubProtoBuf.ClubTableInfoPROTO.newBuilder();
				tb.setClubId(room.getClubId());
				tb.setClubName(club.getClubName());
				tb.setRoomId(room.getRoomId());
				List<String> costList = service.getRoomCostDesc(room.getClubId(), room.getRoomId());
				tb.setCostDesc(costList.get(0));
				tb.setPayTypeDesc(costList.get(1));
				tb.setPlayerMax(room.getMaxPlayer());
				tb.addAllGameplays(room.getGameplays());
				tb.setRoundCount(room.getRoundCount());
				room.getPlayer().forEach( p -> tb.addPlayerName(p.getName()));

				builder.addClubTableList(tb.build());
			}
		});

		builder.setResult(PlatformProtocolsConfig.CLC_CLUB_TABLE_SUCCESS);
		builder.setClubId(clubId);

		connection.write(callback, builder.build());
	}
}
