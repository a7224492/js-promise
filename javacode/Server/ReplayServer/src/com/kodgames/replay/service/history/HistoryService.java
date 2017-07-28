package com.kodgames.replay.service.history;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.constant.DateTimeConstants;
import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.corgi.core.util.Converter;
import com.kodgames.message.proto.game.GameProtoBuf.BGMatchResultSYN;
import com.kodgames.message.proto.game.GameProtoBuf.CGHistoryPlaybackREQ;
import com.kodgames.message.proto.game.GameProtoBuf.GCHistoryPlaybackRES;
import com.kodgames.message.proto.game.GameProtoBuf.PlayerHistoryPROTO;
import com.kodgames.message.proto.game.GameProtoBuf.RoomHistoryPROTO;
import com.kodgames.message.proto.game.GameProtoBuf.RoundReportPROTO;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

import cbean.GlobalRoomId;
import xbean.RoleInfo;
import xbean.RoomHistory;
import xbean.RoomHistoryPlayerInfo;
import xbean.RoundRecord;

/**
 * 战绩服务
 */
public class HistoryService extends PublicService
{
	private static final Logger logger = LoggerFactory.getLogger(HistoryService.class);
	private static final int REQUEST_HISTORY_MAX_COUNT = 100;
	public static final long REQUEST_HISTORY_MAX_TIME_RANGE = 3 * DateTimeConstants.DAY;
	private static final long serialVersionUID = -89104538033089587L;

	/**
	 * 获取战绩版本号 (以房间创建时间作为版本号)
	 *
	 * @param roleId
	 * @return
	 */
	public long getVersion(final Integer roleId)
	{
		RoleInfo role = table.Role_history_rooms.select(roleId);
		if (role == null)
		{
			logger.warn("HistoryService::getVersion can't find role_info for roleId {}", roleId);
			return 0L;
		}

		List<GlobalRoomId> historyRooms = role.getHistoryRooms();
		if (historyRooms.isEmpty())
		{
			return 0L;
		}

		// 以房间创建时间作为版本号
		return historyRooms.get(historyRooms.size() - 1).getCreateTime();
	}

	/**
	 * 添加战绩到table
	 * 
	 * @param result
	 * @return
	 */
	private GlobalRoomId saveToRoomHistoryTable(final BGMatchResultSYN result)
	{
		RoundRecord roundBean = HistoryData.roundHistoryProtoToBean(result);
		GlobalRoomId roomGid = new GlobalRoomId(result.getCreateTime(), result.getRoomId());
		RoomHistory prevData = table.Room_history.update(roomGid);

		if (prevData == null)
		{
			prevData = table.Room_history.insert(roomGid);
			prevData.setCreateTime(result.getCreateTime());
			prevData.setRoomId(result.getRoomId());
			prevData.setRoundType(result.getRoundType());
			prevData.setRoundCount(result.getRoundCount());
			prevData.setPlayerMaxCardCount(result.getPlayerMaxCardCount());
			prevData.getGameplays().addAll(result.getGameplaysList());
			prevData.setEnableMutilHu(result.getEnableMutilHu());
			prevData.setCreatorId(result.getCreatorId());
			prevData.setPayType(result.getPayType());
			for (PlayerHistoryPROTO playerData : result.getPlayerRecordsList())
			{
				RoomHistoryPlayerInfo playerInfo = new RoomHistoryPlayerInfo();
				playerInfo.setRoleId(playerData.getRoleId());
				playerInfo.setNickname(playerData.getNickname());
				playerInfo.setPosition(playerData.getPosition());
				playerInfo.setHeadImgUrl(playerData.getHeadImgUrl());
				playerInfo.setSex(playerData.getSex());
				playerInfo.setTotalPoint(playerData.getTotalPoint());
				prevData.getPlayerInfo().put(playerData.getRoleId(), playerInfo);
			}
			prevData.getRoundRecord().add(roundBean);
		}
		else
		{
			// 如果有数据则不是第一局,更新分数信息,并添加新的结算数据
			for (PlayerHistoryPROTO playerData : result.getPlayerRecordsList())
			{
				prevData.getPlayerInfo().get(playerData.getRoleId()).setTotalPoint(playerData.getTotalPoint());
			}
			prevData.getRoundRecord().add(roundBean);
		}

		return roomGid;
	}

	/**
	 * 保存战绩
	 * 
	 * @param result
	 */
	public void saveHistory(final BGMatchResultSYN result)
	{
		GlobalRoomId roomGid = saveToRoomHistoryTable(result);

		for (PlayerHistoryPROTO playerProto : result.getPlayerRecordsList())
		{
			Integer roleId = playerProto.getRoleId();
			RoleInfo role = table.Role_history_rooms.update(roleId);
			if (role == null)
				role = table.Role_history_rooms.insert(roleId);
			List<GlobalRoomId> historyRoomGids = role.getHistoryRooms();

			if (!historyRoomGids.contains(roomGid))
			{
				// 添加完战绩后检查是否有战绩已超时
				historyRoomGids.add(roomGid);

				// 删除过期的战绩数据
				checkAndRemoveOldHistory(historyRoomGids);
			}
		}
	}

	private List<RoomHistoryPROTO> getHistoryListForTable(List<GlobalRoomId> roomGids, long versionTime)
	{
		// 限制战绩数量
		// 玩家身上的战绩列表可能超过了限制, 但是这些战绩还没有超时, 有可能还被其他玩家引用, 所以不能删除
		// 这里只取一定数量的记录
		int start = roomGids.size() > REQUEST_HISTORY_MAX_COUNT ? roomGids.size() - REQUEST_HISTORY_MAX_COUNT : 0;
		int end = roomGids.size();

		long now = System.currentTimeMillis();
		List<RoomHistoryPROTO> newHistoryRooms = new ArrayList<>();

		for (int i = start; i < end; i++)
		{
			GlobalRoomId roomGid = roomGids.get(i);

			long roomTime = roomGid.getCreateTime();
			// 不符合时间的战绩排除
			if (roomTime <= versionTime || now - roomTime > REQUEST_HISTORY_MAX_TIME_RANGE)
			{
				continue;
			}
			RoomHistory roomInfo = table.Room_history.select(roomGid);
			if (null == roomInfo)
			{
				// 有可能正处于一个时间点: 玩家检查战绩列表时该room还未超时, 构造战绩列表时超时了, 并且被该牌局内其他玩家删除, 这种情况是正确的
				logger.debug("can't find room history data for roomId {}, may be destroyed by other player", roomGid.getRoomId());
				continue;
			}
			// 设置本房间内的基础数据
			RoomHistoryPROTO.Builder roomProto = RoomHistoryPROTO.newBuilder();
			roomProto.setRoomId(roomInfo.getRoomId());
			roomProto.setCreateTime(roomInfo.getCreateTime());
			roomProto.setRoundType(roomInfo.getRoundType());
			roomProto.setRoundCount(roomInfo.getRoundCount());
			roomProto.setPlayerMaxCardCount(roomInfo.getPlayerMaxCardCount());
			roomProto.addAllGameplays(roomInfo.getGameplays());
			roomProto.setEnableMutilHu(roomInfo.getEnableMutilHu());
			roomProto.setCreatorId(roomInfo.getCreatorId());
			roomProto.setPayType(roomInfo.getPayType());

			// 设置玩家信息和战绩总览
			roomInfo.getPlayerInfo().values().forEach((player) -> {
				PlayerHistoryPROTO.Builder playerProto = PlayerHistoryPROTO.newBuilder();
				playerProto.setRoleId(player.getRoleId());
				playerProto.setPosition(player.getPosition());
				playerProto.setNickname(player.getNickname());
				playerProto.setHeadImgUrl(player.getHeadImgUrl());
				playerProto.setSex(player.getSex());
				playerProto.setTotalPoint(player.getTotalPoint());

				roomProto.addPlayerRecords(playerProto);
			});
			newHistoryRooms.add(roomProto.build());
		}
		return newHistoryRooms;
	}

	/** 给客户端战绩协议查找对应数据 */
	public List<RoomHistoryPROTO> getHistoryList(final int roleId, final long versionTime)
	{
		RoleInfo role = table.Role_history_rooms.select(roleId);
		List<GlobalRoomId> histroyRoomGids = role == null ? new ArrayList<>() : role.getHistoryRooms();

		// 删除过期的战绩数据
		checkAndRemoveOldHistory(histroyRoomGids);

		return getHistoryListForTable(histroyRoomGids, versionTime);
	}

	/**
	 * 获取每个房间内详细的每局信息
	 */
	public List<RoundReportPROTO> getRoundReportDetailList(final long createTime, final int roomId)
	{
		List<RoundReportPROTO> result = new ArrayList<RoundReportPROTO>();
		RoomHistory roomInfo = getRoomHistory(createTime, roomId);
		if (roomInfo != null)
		{
			roomInfo.getRoundRecord().forEach(roundBean -> {
				RoundReportPROTO roundProto = HistoryData.roundHistoryBeanToProto(roundBean);
				result.add(roundProto);
			});
		}
		return result;
	}

	public RoomHistory getRoomHistory(long createTime, int roomId)
	{
		GlobalRoomId roomGid = new GlobalRoomId(createTime, roomId);
		return table.Room_history.select(roomGid);
	}
	
	public GCHistoryPlaybackRES.Builder queryPlaybackData(final CGHistoryPlaybackREQ request)
	{
		GlobalRoomId roomGid = new GlobalRoomId(request.getCreatTime(), request.getRoomId());
		RoomHistory roomInfo = table.Room_history.select(roomGid);
		
		GCHistoryPlaybackRES.Builder result = GCHistoryPlaybackRES.newBuilder();
		if (roomInfo == null || roomInfo.getRoundRecord() == null || roomInfo.getRoundRecord().size() < request.getRecordIndex())
			result.setResult(PlatformProtocolsConfig.GC_HISTORY_PLAYBACK_FAILED);
		else
		{
			result.setResult(PlatformProtocolsConfig.GC_HISTORY_PLAYBACK_SUCCESS);
			result.setPlaybackDatas(Converter.byteListToByteString(roomInfo.getRoundRecord().get(request.getRecordIndex()).getPlaybackDatas()));
		}
		return result;
	}

	/**
	 * 检查并删除过期的战绩数据
	 */
	private void checkAndRemoveOldHistory(List<GlobalRoomId> roomGids)
	{
		if (roomGids.isEmpty())
			return;

		long now = System.currentTimeMillis();
		do
		{
			// 从ArryList第一项开始检查, 如果超时则删除, 否则退出检查 (后面的必然也没有超时)
			GlobalRoomId gid = roomGids.get(0);
			if (now - gid.getCreateTime() > REQUEST_HISTORY_MAX_TIME_RANGE)
			{
				// 该战绩有可能已被牌局内的其他玩家删除, delete会返回false, 属于正确的行为
				table.Room_history.delete(gid);
				roomGids.remove(0);
			}
			else
			{
				// 战绩记录都是按时间先后顺序添加, 如果当前战绩时间未超时, 后面的也不需要再检查
				break;
			}
		} while (roomGids.size() > 0);
	}
}
