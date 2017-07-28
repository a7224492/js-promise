package com.kodgames.game.service.room;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.constant.BattleConstant.RoomConst;
import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.role.RoleService;
import com.kodgames.game.service.server.ServerService;
import com.kodgames.game.util.KodBiLogHelper;
import com.kodgames.message.proto.game.GameProtoBuf.BGDestroyRoomSYN;

import xbean.RoleInfo;
import xbean.RoomInfo;

/**
 * 房间服务
 */
public class RoomService extends PublicService
{
	private static final Logger logger = LoggerFactory.getLogger(RoomService.class);
	private static final long serialVersionUID = -2560124447811891082L;

	/**
	 * 获取新的房间号
	 * @return
     */
	public int getNewRoomId()
	{
		int roomId = RoomIdGenerator.Get();
		if (roomId == 0)
		{
			logger.error("get roomId failed");
		}
		return roomId;
	}

	/**
	 * 获取一个可用的BattleServer Id
	 * @return
     */
	public Integer getAvailableBattleId()
	{
		ServerService service = ServiceContainer.getInstance().getPublicService(ServerService.class);
		List<Integer> ids = service.getBattleIds();
		if (null == ids || 0 == ids.size())
		{
			return null;
		}

		Random random = new Random(System.currentTimeMillis());
		int index = (int)(random.nextDouble() * ids.size());
		return ids.get(index);
	}

	/**
	 * 创建房间, 房间状态为创建中 ROOM_STATUS_CREATING
	 * @param battleId
	 * @param roomId 需要调用方确保该roomId的唯一性
	 * @param roundCount
	 * @param isLca
	 * @param clubId
	 * @param cost
     * @param payType
     */
	public boolean creatingRoom(final int battleId, final int roomId, final int roundCount, boolean isLca, int clubId, int cost, int payType)
	{
		{
			// 检查该room是否有效
			RoomInfo info = table.Room_info.select(roomId);

			// 检查该battle是否存在
			if (info != null && info.getBattleId() != 0 && !ServiceContainer.getInstance().getPublicService(ServerService.class).isBattleActive(info.getBattleId()))
			{
				logger.warn("can't find battle {} for room {}, delete this room", info.getBattleId(), roomId);
				table.Room_info.delete(roomId);
			}
		}

		RoomInfo info = table.Room_info.insert(roomId);
		if (info != null)
		{
			info.setStatus(RoomConst.ROOM_STATUS_CREATING);
			info.setBattleId(battleId);
			info.setRoundCount(roundCount);
			info.setIsLca(isLca);
			info.setClubId(clubId);
			info.setCost(cost);
			info.setPayType(payType);
		}
		else
		{
			logger.error("room {} already exist, creatingRoom failed for battle {}", roomId, battleId);
			info = table.Room_info.select(roomId);
			if (info != null)
				logger.error("status {} battle {} roundCount {} isLca {} club {} cost {}", info.getStatus(), info.getBattleId(), info.getRoundCount(), info.getIsLca(), info.getClubId(), info.getCost());
			return false;
		}

		return true;
	}

	/**
	 * 删除创建中的房间
	 * @param roomId
     */
	public void deleteCreatingRoom(int roomId)
	{
		// 增加检测: 该房间的状态为创建中 ROOM_STATUS_CREATING
		// 避免内部错误导致误删除房间
		if (!isCreatingRoom(roomId))
		{
			logger.warn("room {} status not in creating, can't delete it", roomId);
			return;
		}
		table.Room_info.delete(roomId);

		// 回收RoomId
		RoomIdGenerator.Free(roomId);
	}

	/**
	 * 返回房间所属的俱乐部
	 * @param roomId
	 * @return
     */
	public int getRoomClubId(int roomId)
	{
		RoomInfo info = table.Room_info.select(roomId);
		if (info == null)
		{
			//logger.warn("can't find room_info for room {}", roomId);
			return 0;
		}
		return info.getClubId();
	}

	/**
	 * 返回房间信息
	 * @param roomId
	 * @return
     */
	public RoomInfo getRoom(int roomId)
	{
		return table.Room_info.select(roomId);
	}

	public RoomInfo getRoomForWrite(int roomId)
	{
		return table.Room_info.update(roomId);
	}

	/*
	 * 创建房间的具体逻辑
	 * 这里不会将房间插入room_info表, 因为此前creatingRoom已经在room_info表中添加过数据
	 * @param creatorId 房主id
	 * @param roomId 房间id
	 * @param gamePlays 玩法类型,0表示没有鬼牌，1表示有鬼牌
	 */
	public void createRoom(final int creatorId, final int roomId, List<Integer> gamePlays)
	{
		RoomInfo info = table.Room_info.update(roomId);
		if (info == null)
		{
			logger.error("can't find room info for id {}", roomId);
			return;
		}
		info.setStatus(RoomConst.ROOM_STATUS_DEFAULT);

		// 打印BI统计日志
		RoleService roleService = ServiceContainer.getInstance().getPublicService(RoleService.class);
		RoleInfo role = roleService.getRoleInfoByRoleId(creatorId);
		if (null == role)
		{
			logger.error("RoomService createRoom : role info is null");
			return;
		}

		KodBiLogHelper.roomCreateLog(creatorId, role.getAccountId(), roomId, info.getRoundCount(), gamePlays, role.getChannel());
	}

	/**
	 * 返回角色信息
	 * @param roleId
	 * @return
     */
	public RoleInfo getRoleInfoByRoleId(final int roleId)
	{
		return table.Role_info.select(roleId);
	}

	/**
	 * 销毁房间
	 * @param battleId
	 * @param message
     */
	public void destroyRoom(int battleId, BGDestroyRoomSYN message)
	{
		int roomId = message.getRoomId();

		// 直接用delete的返回值来检查roomId是否存在, 不需要先select再delete
		if (!table.Room_info.delete(roomId))
		{
			logger.warn("destoryRoom, can't find the room, roomID:{}  battleID:{}", roomId, battleId);
		}
		
		// 回收RoomId
		RoomIdGenerator.Free(roomId);

		KodBiLogHelper.roomDestroyLog(roomId, message.getReason(), "房间销毁");
	}

	/**
	 * 返回房间所在的BattleServer
	 * @param roomId
	 * @return
     */
	public int getBattleIdByRoomId(int roomId)
	{
		RoomInfo info = table.Room_info.update(roomId);
		if (info != null)
		{
			if (info.getBattleId() != 0 && !ServiceContainer.getInstance().getPublicService(ServerService.class).isBattleActive(info.getBattleId()))
			{
				table.Room_info.delete(roomId);
				return 0;
			}
			return info.getBattleId();
		}

		return 0;
	}

	/**
	 * 检查房间是否在创建中
	 * @param roomId
	 * @return
     */
	public boolean isCreatingRoom(int roomId)
	{
		RoomInfo info = table.Room_info.select(roomId);
		return info != null && info.getStatus() == RoomConst.ROOM_STATUS_CREATING;
	}

}
