package com.kodgames.club.service.room;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.kodgames.club.constant.ClubConstants;
import com.kodgames.message.proto.club.ClubProtoBuf;
import com.kodgames.message.proto.game.GameProtoBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.message.proto.club.ClubProtoBuf.ClubPlayerInfoPROTO;
import com.kodgames.message.proto.club.ClubProtoBuf.GCLDestroyRoomSYN;
import com.kodgames.message.proto.club.ClubProtoBuf.GCLEnterRoomSYN;
import com.kodgames.message.proto.club.ClubProtoBuf.GCLQuitRoomSYN;

import xbean.ClubRoleBaseInfo;
import xbean.ClubRoomInfo;
import xbean.ClubRooms;
import xbean.RoomCost;

public class ClubRoomService extends PublicService
{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5802752632228661129L;

	private static final Logger logger = LoggerFactory.getLogger(ClubRoomService.class);

	private static Map<Integer, GameProtoBuf.RoomConfigPROTO> roomConfigs = new ConcurrentHashMap<>();

	/**
	 * 获取俱乐部牌桌列表
	 * @param ids	俱乐部ids
	 * @param onlyNotFull  是否只要不满的牌桌
	 * @return
	 */
	public List<ClubRoomInfo> getRooms(List<Integer> ids, boolean onlyNotFull)
	{
		List<ClubRoomInfo> result = new ArrayList<>();
		for (Integer id : ids) {
			ClubRooms roomMap = table.Club_room_info.update(id);
			if (roomMap != null && roomMap.getRooms() != null && roomMap.getRooms().size() != 0) {
				if (onlyNotFull) {
					List<ClubRoomInfo> rs = new ArrayList<>();
					roomMap.getRooms().values().forEach( r -> {
						if (r.getPlayer().size() < r.getMaxPlayer()) {
							rs.add(r);
						}
					});
					result.addAll(rs);
				} else {
					result.addAll(roomMap.getRooms().values());
				}
			}
		}
		return result;
	}

	/**
	 * 添加一个新的房间到内存
	 * @param message game2club 创建房间的消息同步
	 */
	public synchronized void addNewRoomToClub(ClubProtoBuf.GCLCreateRoomRES message)
	{
		int roomId = message.getRoomId();
		int clubId = message.getClubId();
		int playerMax = message.getPlayerMax();
		ClubRooms club = this.getClubRoomMap(clubId, true);
		Map<Integer, ClubRoomInfo> roomMap = club.getRooms();
		ClubRoomInfo roomInfo = roomMap.get(roomId);
		if (roomInfo == null)
		{
			roomInfo = new ClubRoomInfo();
			roomInfo.setRoomId(roomId);
			roomInfo.setClubId(clubId);
			roomInfo.setMaxPlayer(playerMax);
			roomInfo.setEnableSubCard(false);
			roomInfo.setBattleId(message.getBattleId());
			roomInfo.getGameplays().addAll(message.getGameplaysList());
			RoomCost rc = new RoomCost();
			rc.setCost(message.getCost());
			rc.setPayType(message.getPayType());
			roomInfo.getRoomCostSnap().copyFrom(rc); // room cost 快照
			roomInfo.setRoundCount(message.getRoundCount());
			roomInfo.setCreator(message.getCreatorId());
			roomMap.put(roomId, roomInfo);
			return;
		}
		logger.error("ClubRoomService addNewRoomToClub : roomId {} has created in club {} ---- roomInfo: {}", roomId, clubId, roomInfo.toString());
	}

	/**
	 * 销毁一个房间
	 * @param message game2club 销毁房间的消息同步
	 * @return 删除的房间信息
	 */
	public ClubRoomInfo destroyRoom(GCLDestroyRoomSYN message)
	{
		int roomId = message.getRoomId();
		int clubId = message.getClubId();

		ClubRooms club = this.getClubRoomMap(clubId, false);
		if (club == null)
		{
			logger.error("ClubRoomService destroyRoom : club {} havn't any rooms", clubId);
			return null;
		}

		Map<Integer, ClubRoomInfo> roomMap = club.getRooms();
		ClubRoomInfo roomInfo = roomMap.get(roomId);
		if (roomInfo == null)
		{
			logger.error("ClubRoomService destroyRoom : club {} has no room {}", clubId, roomId);
			return null;
		}

		return roomMap.remove(roomId);
	}

	/**
	 * 添加一个玩家到房间
	 * @param message game2club 玩家进入房间的消息同步
	 */
	public void addPlayer(GCLEnterRoomSYN message)
	{
		int roomId = message.getRoomId();
		int clubId = message.getClubId();
		ClubPlayerInfoPROTO player = message.getPlayer();

		ClubRoomInfo roomInfo = this._getRoomInfo(clubId, roomId);
		if (roomInfo == null)
		{
			return;
		}

		this._addPlayer(roomInfo.getPlayer(), player);
	}

	/**
	 * 删除一个玩家到房间
	 * @param message game2club 玩家离开房间的消息同步
	 * @return 是否删除成功
	 */
	public boolean deletePlayer(GCLQuitRoomSYN message)
	{
		int roomId = message.getRoomId();
		int clubId = message.getClubId();
		ClubPlayerInfoPROTO player = message.getPlayer();

		ClubRoomInfo roomInfo = this._getRoomInfo(clubId, roomId);
		if (roomInfo == null)
		{
			return false;
		}

		return this._deletePlayer(roomInfo.getPlayer(), player);
	}

	/**
	 * 某个俱乐部房间可以扣卡了
	 * @param clubId
	 * @param roomId
	 */
	public void enableRoomSubCard(int clubId, int roomId)
	{
		ClubRoomInfo roomInfo = this._getRoomInfo(clubId, roomId);
		if (roomInfo == null)
		{
			logger.error("ClubRoomService enableRoomSubCard : no such roomId {} or clubId", roomId, clubId);
			return;
		}

		roomInfo.setEnableSubCard(true);
	}

	/**
	 * 获取俱乐部房间信息
	 * 读写权限
	 * @param clubId
	 * @param roomId
	 * @return
	 */
	public ClubRoomInfo getRoom(int clubId, int roomId)
	{
		return this._getRoomInfo(clubId, roomId);
	}

	/**
	 * 根据cid rid 获取roomInfo 没有的话返回null
	 * 
	 * @param clubId
	 * @param roomId
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	private ClubRoomInfo _getRoomInfo(int clubId, int roomId)
	{
		ClubRooms club = this.getClubRoomMap(clubId, false);
		if (club == null)
		{
			logger.error("ClubRoomService _getRoomInfo club : club {} havn't any rooms", clubId);
			return null;
		}

		Map<Integer, ClubRoomInfo> roomMap = club.getRooms();
		if (roomMap == null)
		{
			logger.error("ClubRoomService _getRoomInfo roomMap : club {} havn't any rooms", clubId);
			return null;
		}
		ClubRoomInfo roomInfo = roomMap.get(roomId);
		if (roomInfo == null)
		{
			logger.error("ClubRoomService _getRoomInfo : club {} has no room {}", clubId, roomId);
			return null;
		}

		return roomInfo;
	}

	/**
	 * 添加用户到列表
	 * 
	 * @param list
	 * @param roleProto
	 * @see [类、类#方法、类#成员]
	 */
	private void _addPlayer(List<ClubRoleBaseInfo> list, ClubPlayerInfoPROTO roleProto)
	{
		ClubRoleBaseInfo hasPlayer = null;
		for (ClubRoleBaseInfo v : list)
		{
			if (v.getRoleId() == roleProto.getRoleId())
			{
				logger.error("ClubRoomService _addPlayer : player {} has in list", roleProto.getRoleId());
				hasPlayer = new ClubRoleBaseInfo(v);
				break;
			}
		}

		if (hasPlayer != null)
			return;

		ClubRoleBaseInfo role = new ClubRoleBaseInfo();
		role.setName(roleProto.getRoleName());
		role.setRoleId(roleProto.getRoleId());
		list.add(role);
	}

	/**
	 * 从列表中删除某个玩家
	 * 
	 * @param list
	 * @param roleProto
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	private boolean _deletePlayer(List<ClubRoleBaseInfo> list, ClubPlayerInfoPROTO roleProto)
	{
		return list.removeIf(v -> v.getRoleId() == roleProto.getRoleId());
	}

	/**
	 * 获取俱乐部房间的map 如果没有并且需要insert，就insert，否则返回null
	 * 
	 * @param clubId
	 * @param insert
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	private ClubRooms getClubRoomMap(int clubId, boolean insert)
	{
		ClubRooms club = table.Club_room_info.update(clubId);
		if (club == null && insert)
		{
			club = table.Club_room_info.insert(clubId);
		}
		return club;
	}


	/**
	 * 更新全局房间类型配置
	 */
	public synchronized void updateRoomConfig(List<GameProtoBuf.RoomConfigPROTO> configs)
	{
		roomConfigs.clear();
		configs.forEach(config -> roomConfigs.put(config.getType(), config));
	}

	/**
	 * 获取玩法房间配置
	 * @param roomType
	 * @return
	 */
	public GameProtoBuf.RoomConfigPROTO getRoomConfig(int roomType)
	{
		return roomConfigs.get(roomType);
	}

	/**
	 * 获取俱乐部房间消耗的快照
	 * 房间的消耗按创建时的来结算，所以需要快照
	 * @param clubId
	 * @param roomId
	 * @return
	 */
	public RoomCost getRoomCostSnap(int clubId, int roomId)
	{
		ClubRoomInfo roomInfo = this._getRoomInfo(clubId, roomId);

		if (roomInfo != null) {
			return roomInfo.getRoomCostSnap();
		}
		return null;
	}

	/**
	 * 获取消耗快照的描述
	 * @param clubId
	 * @param roomId
	 * @return
	 */
	public List<String> getRoomCostDesc(int clubId, int roomId)
	{
		List<String> list = new ArrayList<>();
		RoomCost rc = this.getRoomCostSnap(clubId, roomId);
		ClubRoomInfo room = this.getRoom(clubId, roomId);
		if (rc == null || room == null) {
			list.add("");
			list.add("");
		} else {
			GameProtoBuf.RoomConfigPROTO roomConfig = this.getRoomConfig(room.getRoundCount());
			int roomCost = roomConfig.getCardCount();
			int cost = rc.getCost() * roomCost; // 最后所消耗的
			list.add(cost + "张");
			list.add(ClubConstants.PAY_TYPE.NAMES[rc.getPayType()]);
		}

		return list;
	}

	/**
	 * 根据房间玩家的输赢情况获取最后的赢家
	 * 1、赢得最多的优先
	 * 2、一样多的比较位置，靠前的是赢家
	 * 3、产品需求如此！这个锅我不背
	 * @param _players
	 * @return
	 */
	public int findWinner(List<ClubPlayerInfoPROTO> _players)
	{
		ArrayList<ClubPlayerInfoPROTO> players = new ArrayList<>();
		players.addAll(_players);
		players.sort( (p1, p2) -> {
			if (p1.getTotalPoint() > p2.getTotalPoint()) {
				return -1;
			} else if (p1.getTotalPoint() < p2.getTotalPoint()) {
				return 1;
			}
			if (p1.getPosition() < p2.getPosition()) {
				return -1;
			} else if (p1.getPosition() > p2.getPosition()) {
				return 1;
			}
			return 0;
		});
		return players.get(0).getRoleId();
	}
}
