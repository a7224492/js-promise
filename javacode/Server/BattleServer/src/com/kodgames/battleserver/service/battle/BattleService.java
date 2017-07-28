package com.kodgames.battleserver.service.battle;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessage;
import com.kodgames.battleserver.service.battle.common.xbean.BattleRoom;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.core.BattleHelper;
import com.kodgames.battleserver.service.battle.core.creator.IBattleCreator;
import com.kodgames.battleserver.service.room.RoomService;
import com.kodgames.battleserver.service.server.ServerService;
import com.kodgames.corgi.core.constant.BattleConstant.RoomConst;
import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.message.proto.battle.BattleProtoBuf.BCFinalMatchResultSYN;
import com.kodgames.message.proto.battle.BattleProtoBuf.BCMatchResultSYN;
import com.kodgames.message.proto.battle.BattleProtoBuf.PlayerMatchFinalResultPROTO;
import com.kodgames.message.proto.game.GameProtoBuf.BGFinalMatchResultSYN;
import com.kodgames.message.proto.game.GameProtoBuf.BGMatchResultSYN;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

public class BattleService extends PublicService
{
	private static final long serialVersionUID = -4778993931870087354L;
	private Logger logger = LoggerFactory.getLogger(BattleService.class);
	private IBattleCreator battleCreator;
	private final Map<Integer, BattleHelper> battle_maps = new ConcurrentHashMap<>(); // roomId->Battle

	public void createBattleCreateor(String regionName)
	{
		battleCreator = IBattleCreator.create(regionName);
		if (battleCreator == null)
			logger.error("INVALID regionName: {} ", regionName);
	}

	public IBattleCreator getBattleCreator()
	{
		return battleCreator;
	}

	public BattleHelper getBattleHelper(int roomId)
	{
		return battle_maps.get(roomId);
	}

	public boolean isBattleRunning(int roomId)
	{
		BattleHelper helper = battle_maps.get(roomId);
		return helper != null && helper.isRunning();
	}

	// 已经开始了第一局
	public boolean isHaveBeginFirstGame(int roomId)
	{
		BattleRoom room = ServiceContainer.getInstance().getPublicService(RoomService.class).getRoomInfo(roomId);
		if ((room != null) && (room.getOverGameCount() > 0))
			return true;
		return false;
	}

	// 在每局结算的时候会移除，但是投票解散房间的时候要主动调用
	public void removeBattle(int roomID)
	{
		battle_maps.remove(roomID);
	}

	public void rejoin(int roomId, int roleId)
	{
		BattleHelper battleHelper = battle_maps.get(roomId);
		if (battleHelper == null)
		{
			logger.error("role {} rejoin room {} but not found battle", roleId, roomId);
			return;
		}

		if (!battleHelper.isRunning())
		{
			// 如果战斗不在开始过程中，不复牌
			logger.debug("role {} rejoin room {} found battle is not running", roleId, roomId);
			return;
		}

		try
		{
			battleHelper.setCurrentInstance();
			battleHelper.rejoin(roleId);
		}
		finally
		{
			battleHelper.resetCurrentInstance();
		}
	}

	public void startGame(BattleRoom roomInfo)
	{
		int roomId = roomInfo.getRoomId();
		roomInfo.setStatus(RoomConst.ROOM_STATUS_RUNNING);

		BattleHelper battleHelper = new BattleHelper(roomInfo);
		battle_maps.put(roomId, battleHelper);

		try
		{
			battleHelper.setCurrentInstance();
			battleHelper.startBattle(battleCreator.create(roomInfo.getGameplays()));
		}
		finally
		{
			battleHelper.resetCurrentInstance();
		}
	}

	public int step(int callback, int roleId, int roomId, int playType, ByteString cards)
	{
		if (roomId == 0) {
			logger.info("BattleService::step roomId == 0, roleId {}", roleId);
			return PlatformProtocolsConfig.BC_PLAYCARD_FAILED_NOT_IN_ROOM;
		}

		BattleHelper battle = battle_maps.get(roomId);
		if (battle == null)
		{
			logger.error("role {} step room {} but not found battle", roleId, roomId);
			return PlatformProtocolsConfig.BC_PLAYCARD_FAILED_NOT_IN_ROOM;
		}
		else
		{
			try
			{
				battle.setCurrentInstance();
				boolean success = battle.processStep(roleId, playType, cards.toByteArray());
				return success ? PlatformProtocolsConfig.BC_PLAYCARD_SUCCESS : PlatformProtocolsConfig.BC_PLAYCARD_FAILED_NOT_YOUR_TURN;
			}
			finally
			{
				battle.resetCurrentInstance();
			}
		}
	}

	public void roundFinish(int roomId, int zhuang, int nextZhuang, BCMatchResultSYN.Builder calcBuilder)
	{
		// 通知game 也应该BGMatchResultSYN先于BGFinalMatchResultSYN
		// modify by: machicheng
		BattleHelper battleHelper = BattleHelper.getInstance();
		// 设置结束时间
		battleHelper.getBattleBean().setEndTime(System.currentTimeMillis());
		BattleRoom roomInfo = battleHelper.getRoomInfo();
		// 将本局结算数据发送给game
		BGMatchResultSYN resultToGame = BattleData.roundBeanToRoomHistoryProto(calcBuilder, roomInfo,  battleHelper);
		syncMatchResultToGame(resultToGame);

		// 增加房间局数
		ServiceContainer.getInstance().getPublicService(RoomService.class).addOverGameCount(roomId, zhuang, nextZhuang);
	}

	/**
	 * 同步单局的结算结果给GameServer
	 */
	public void syncMatchResultToGame(BGMatchResultSYN history)
	{
		ServerService serverService = ServiceContainer.getInstance().getPublicService(ServerService.class);
		serverService.getGameConnection().write(GlobalConstants.DEFAULT_CALLBACK, history);
	}

	/**
	 * 同步房间总的结算结果给GameServer
	 */
	public void syncFinalMatchResultToGame(BGFinalMatchResultSYN result)
	{
		ServerService serverService = ServiceContainer.getInstance().getPublicService(ServerService.class);
		serverService.getGameConnection().write(GlobalConstants.DEFAULT_CALLBACK, result);
	}

	/**
	 * 同步房间的结算结果给玩家（客户端）
	 */
	public void syncFinalMatchResultToPlayers(List<PlayerMatchFinalResultPROTO> results)
	{
		BCFinalMatchResultSYN.Builder builder = BCFinalMatchResultSYN.newBuilder();
		builder.addAllGameResults(results);
		for (PlayerMatchFinalResultPROTO player : results)
		{
			int roleId = player.getRoleId();
			Connection connection = ConnectionManager.getInstance().getClientVirtualConnection(roleId);
			if (null != connection)
			{
				GeneratedMessage message = builder.build();
				logger.info("Send BatttleMessage to {}, message is {}", roleId, message);
				connection.write(GlobalConstants.DEFAULT_CALLBACK, message);
			}
		}
	}

	/**
	 * 所有牌局结束时处理结算结果
	 */
	public void handleMatchResult(int roomId)
	{
		RoomService roomService = ServiceContainer.getInstance().getPublicService(RoomService.class);
		BattleRoom room = roomService.getRoomInfo(roomId);
		syncFinalMatchResultToGame(BattleData.finalResultToBGFinalSYN(room));
		syncFinalMatchResultToPlayers(BattleData.roomBeanToMatchResultProtos(room));
	}
}