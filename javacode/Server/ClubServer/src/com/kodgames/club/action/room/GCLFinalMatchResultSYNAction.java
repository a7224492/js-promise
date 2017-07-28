package com.kodgames.club.action.room;

import com.kodgames.club.constant.ClubConstants;
import com.kodgames.club.service.common.ClubCommonService;
import com.kodgames.club.service.room.ClubRoomService;
import com.kodgames.club.utils.ClubUtils;
import com.kodgames.club.utils.KodBiLogHelper;
import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.message.proto.club.ClubProtoBuf;
import com.kodgames.message.proto.club.ClubProtoBuf.GCLFinalMatchResultSYN;
import com.kodgames.message.proto.game.GameProtoBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xbean.*;


@ActionAnnotation(messageClass = GCLFinalMatchResultSYN.class, actionClass = GCLFinalMatchResultSYNAction.class, serviceClass = ClubRoomService.class)
public class GCLFinalMatchResultSYNAction extends ProtobufMessageHandler<ClubRoomService, GCLFinalMatchResultSYN>
{

	private static final Logger logger = LoggerFactory.getLogger(GCLFinalMatchResultSYNAction.class);
	
	@Override
	public void handleMessage(Connection connection, ClubRoomService service, GCLFinalMatchResultSYN message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

		int roomId = message.getRoomId();
		int clubId = message.getClubId();

		ClubRoomInfo room = service.getRoom(clubId, roomId);  // 先取 room 锁，防止room表和club表死锁

		ClubCommonService ccs = ServiceContainer.getInstance().getPublicService(ClubCommonService.class);

		GameProtoBuf.RoomConfigPROTO roomConfig = service.getRoomConfig(room.getRoundCount());
		int roomCost = roomConfig.getCardCount();
		// 获取roomCostSnap 创建时的，而不是当前的
		RoomCost rcSnap = service.getRoomCostSnap(clubId, roomId);

		int cost = rcSnap.getCost() * roomCost; // 最后所消耗的
		if (room == null)
		{
			logger.warn("GCLFinalMatchResultSYNAction : no such room {} in club {}", roomId, clubId);
			return;
		}
		// 检查是否需要支付房卡
		if (!room.getEnableSubCard())
		{
			logger.warn("GCLFinalMatchResultSYNAction : the room {} in club {}, disable sub card", roomId, clubId);
			return;
		}

		int roleId;
		int cardRemain;

		if (rcSnap.getPayType() == ClubConstants.PAY_TYPE.CREATOR_PAY) {
			// 房主出卡
			MemberInfo creator = ccs.getMemberInfo(room.getCreator(), clubId);
			if (creator == null)
			{
				logger.warn("GCLFinalMatchResultSYNAction : creator {} not find in club", room.getCreator(), clubId);
				return;
			}
			int now = creator.getCardCount();
			cardRemain = now - cost;
			creator.setCardCount(cardRemain);
			roleId = room.getCreator();
		} else if (rcSnap.getPayType() == ClubConstants.PAY_TYPE.WINNER_PAY) {
			int winnerId = service.findWinner(message.getPlayersList());
			// 赢家出卡
			MemberInfo winner = ccs.getMemberInfo(winnerId, clubId);
			if (winner == null)
			{
				logger.warn("GCLFinalMatchResultSYNAction : winner {} not find in club", winnerId, clubId);
				return;
			}
			int now = winner.getCardCount();
			cardRemain = now - cost;
			winner.setCardCount(cardRemain);
			roleId = winnerId;
		} else if (rcSnap.getPayType() == ClubConstants.PAY_TYPE.MANAGER_PAY){
			int managerId = ccs.getManagerId(clubId);
			MemberInfo manager = ccs.getMemberInfo(managerId, clubId);
			if (manager == null)
			{
				logger.warn("GCLFinalMatchResultSYNAction : manager {} not find in club", managerId, clubId);
				return;
			}

			int now = manager.getCardCount();
			cardRemain = now - cost;
			if (cardRemain < 0) {
				logger.warn("GCLFinalMatchResultSYNAction : manager {} sub over {} card", managerId, cardRemain * -1);
				cardRemain = 0;
			}
			manager.setCardCount(cardRemain);
			roleId = managerId;
		} else {
			return; // 经理扣卡之后的就不需要执行了！
		}

		ClubInfo club = table.Clubs.select(clubId);
		RoleClubs roleClubs = table.Role_clubs.select(roleId);
		if (club != null && roleClubs != null) {
			KodBiLogHelper.clubMemberCostLog(clubId, club.getAgentId(), club.getManager().getRoleId(),
					roleId, ClubConstants.CLUB_CARD_COST_TYPE.ROOM_COST, cost, cardRemain,
					roleClubs.getVersion(), roleClubs.getChannel());
		}

		// 通知客户端更新俱乐部房卡数量
		ClubProtoBuf.CLCClubRoomCardModifySYN.Builder builder = ClubProtoBuf.CLCClubRoomCardModifySYN.newBuilder();
		builder.setRoleId(roleId);
		builder.setClubId(clubId);
		builder.setRoomCardCount(cardRemain);

		// game转播
		ClubUtils.broadcastMsg2Game(callback, roleId, builder.build());
	}

	@Override
	public Object getMessageKey(Connection connection, GCLFinalMatchResultSYN message)
	{
		return  message.getClubId();
	}

}
