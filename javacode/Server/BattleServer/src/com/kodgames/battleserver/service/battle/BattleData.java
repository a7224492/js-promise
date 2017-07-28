package com.kodgames.battleserver.service.battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.kodgames.battleserver.common.Constant;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.BattleRoom;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.core.BattleHelper;
import com.kodgames.battleserver.service.room.RoomService;
import com.kodgames.corgi.core.constant.DateTimeConstants;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.message.proto.battle.BattleProtoBuf.BCMatchResultSYN;
import com.kodgames.message.proto.battle.BattleProtoBuf.PlayerMatchFinalResultPROTO;
import com.kodgames.message.proto.battle.BattleProtoBuf.PlayerMatchResultPROTO;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultGamePROTO;
import com.kodgames.message.proto.game.GameProtoBuf;
import com.kodgames.message.proto.game.GameProtoBuf.BGFinalMatchResultSYN;
import com.kodgames.message.proto.game.GameProtoBuf.BGMatchResultSYN;
import com.kodgames.message.proto.game.GameProtoBuf.PlayerHistoryPROTO;
import com.kodgames.message.proto.game.GameProtoBuf.RoundReportPROTO;

public class BattleData
{

	// 将房间内的总战斗信息通知给gameserver
	public static BGFinalMatchResultSYN finalResultToBGFinalSYN(BattleRoom room)
	{
		BGFinalMatchResultSYN.Builder gameBuilder = BGFinalMatchResultSYN.newBuilder();

		gameBuilder.setRoomId(room.getRoomId());
		gameBuilder.setCreateTime(room.getCreateTime());
		gameBuilder.setRoundType(room.getCountType());

		// 构造玩家所有战局的数据
		room.forEachPlayers(player -> {
			PlayerHistoryPROTO.Builder playerBuilder = PlayerHistoryPROTO.newBuilder();
			playerBuilder.setRoleId(player.getRoleId());
			playerBuilder.setPosition(player.getPosition());
			playerBuilder.setNickname(player.getNickname());
			playerBuilder.setHeadImgUrl(player.getHeadImageUrl());
			playerBuilder.setTotalPoint(player.getTotalPoint());
			playerBuilder.setSex(player.getSex());
			gameBuilder.addPlayerRecords(playerBuilder);
		});

		return gameBuilder.build();
	}

	// 将房间内的战斗信息转为历史战绩信息，用于发往game服务器
	public static BGMatchResultSYN roundBeanToRoomHistoryProto(BCMatchResultSYN.Builder calcBuilder, BattleRoom room, BattleHelper battleHelper)
	{
		BGMatchResultSYN.Builder gameBuilder = BGMatchResultSYN.newBuilder();
		RoundReportPROTO.Builder roundReport = RoundReportPROTO.newBuilder();
		Map<Integer, Integer> playerPoint = new HashMap<>();
		roundReport.setStartTime(battleHelper.getBattleBean().getStartTime());
		for (PlayerMatchResultPROTO playerResult : calcBuilder.getMatchResultsList())
		{
			roundReport.addPlayerRecords(playerResult.toByteString());
			playerPoint.put(playerResult.getRoleId(), playerResult.getTotalPoint());
		}
		// 设置剩余牌
		roundReport.setLastCards(calcBuilder.getLastCards());
		gameBuilder.setCreateTime(room.getCreateTime());
		gameBuilder.setRoomId(room.getRoomId());
		gameBuilder.addAllGameplays(room.getGameplays());
		gameBuilder.setEnableMutilHu(battleHelper.enableMutilHu());
		gameBuilder.setPlaybackDatas(battleHelper.generatePlaybackData().toByteString());
		gameBuilder.setPayType(room.getPayType());
		gameBuilder.setCreatorId(room.getCreatorId());

		room.forEachPlayers(player -> {
			PlayerHistoryPROTO.Builder playerBuilder = PlayerHistoryPROTO.newBuilder();
			playerBuilder.setRoleId(player.getRoleId());
			playerBuilder.setPosition(player.getPosition());
			playerBuilder.setNickname(player.getNickname());
			playerBuilder.setHeadImgUrl(player.getHeadImageUrl());
			playerBuilder.setTotalPoint(playerPoint.get(player.getRoleId()));
			playerBuilder.setSex(player.getSex());
			gameBuilder.addPlayerRecords(playerBuilder.build());
		});

		gameBuilder.setRoundType(room.getCountType());
		gameBuilder.setRoundCount(room.getTotalGameCount());
		gameBuilder.setPlayerMaxCardCount(battleHelper.getPlayCardCount());
		gameBuilder.setRoundReportRecord(roundReport.build());
		return gameBuilder.build();
	}

	// 获取房间的最终结算信息
	public static List<PlayerMatchFinalResultPROTO> roomBeanToMatchResultProtos(BattleRoom room)
	{
		List<PlayerMatchFinalResultPROTO> results = new ArrayList<>();

		room.forEachPlayers(playerInfo -> {
			PlayerMatchFinalResultPROTO.Builder finalResultBuilder = PlayerMatchFinalResultPROTO.newBuilder();
			finalResultBuilder.setRoleId(playerInfo.getRoleId());
			finalResultBuilder.setTotalPoint(playerInfo.getTotalPoint());

			playerInfo.getCards().getGameScore().forEach(gameScore -> {
				ResultGamePROTO.Builder resultGame = ResultGamePROTO.newBuilder();
				resultGame.setType(gameScore.getScoreType());
				resultGame.setAddOperation(gameScore.getAddOperation());
				resultGame.setTimes(gameScore.getTimes());
				finalResultBuilder.addGameResult(resultGame);
			});

			results.add(finalResultBuilder.build());
		});

		return results;
	}
	// 房间销毁BI
	public static GameProtoBuf.BGRoomDestroyBiSYN roomInfoToDestroyRoomBi(BattleRoom room, int destroyReason)
	{
		// 不是正常销毁并且房间局数为1
		if (destroyReason != Constant.RoomDestroyReason.GAMEOVER && room.getGames().size() == 1)
			return null;
		GameProtoBuf.BGRoomDestroyBiSYN.Builder builder = GameProtoBuf.BGRoomDestroyBiSYN.newBuilder();
		builder.setRoomId(room.getRoomId()); // 房间ID
		builder.setCreatorId(room.getCreatorId()); // 房主ID
		// 非正常结束下玩家没有离开
		room.forEachPlayers(player -> {
			builder.addRoleIds(player.getRoleId());
		});
		builder.setCreateTime(room.getCreateTime()); // 房间创建时间
		long destroyTime = System.currentTimeMillis();
		builder.setDestroyTime(destroyTime); // 房间销毁时间

		// 获取房间局数
		RoomService roomService = ServiceContainer.getInstance().getPublicService(RoomService.class);
		GameProtoBuf.RoomConfigPROTO roomConfig = roomService.getRoomConfig(room.getCountType());
		int gameCount = roomConfig.getGameCount();
		builder.setMaxRoundCount(gameCount); // 按圈算时，局数配置为0
		builder.setRealRoundCount(room.getGames().size());

		// 计算牌局时间数据
		long totalBattleTime = 0;
		long totalIdleTime = 0;
		BattleBean preBattle = null;
		List<BattleBean> battleList = room.getGames();
		int size = battleList.size();
		if (destroyReason != Constant.RoomDestroyReason.GAMEOVER)
			size = size - 1;
		for (int index = 0; index < size; ++index)
		{
			BattleBean currentBattle = battleList.get(index);
			// 牌局中时间总和
			totalBattleTime += currentBattle.getEndTime() - currentBattle.getStartTime();

			// 牌局间时间总和
			if (preBattle != null)
			{
				totalIdleTime += currentBattle.getStartTime() - preBattle.getEndTime();
			}

			// 缓存当前牌局
			preBattle = currentBattle;
		}

		int battleCount = room.getGames().size(); // 局数
		long averageBattleTime = battleCount == 0 ? 0 : totalBattleTime / size; // 平均牌局中时间（豪秒）
		builder.setAverageBattleTime(averageBattleTime / DateTimeConstants.SECOND); // 平均牌局中时间（秒）
		
		long averageIdleTime = battleCount < 2 ? 0 : totalIdleTime / (battleCount - 1); // 平均牌局间空闲时间（毫秒）
		builder.setAverageIdleTime(averageIdleTime / DateTimeConstants.SECOND); // 平均牌局间空闲时间（秒）
		
		long startIdleTime = (battleCount == 0 ? 0 : battleList.get(0).getStartTime() - room.getCreateTime()); // 开局前空闲时间（毫秒）
		builder.setStartIdleTime(startIdleTime / DateTimeConstants.SECOND); // 开局前空闲时间（秒）
		
		long roomLifeTime = destroyTime - room.getCreateTime(); // 房间续存时间（毫秒）
		builder.setRoomLifeTime(roomLifeTime / DateTimeConstants.SECOND); // 房间续存时间（秒）

		List<PlayerInfo> losers = new ArrayList<>();

		room.forEachPlayers(player -> {
			if (player.getTotalPoint() < 0)
				losers.add(player);
		});

		builder.setLosePlayerCount(losers.size()); // 房间失败人数（分数少于0的人）
		builder.setTotalLoseScore(losers.stream().mapToInt(p -> -p.getTotalPoint()).sum()); // 房间失败分数总和（赢的分数）
		
		builder.addAllGameplays(room.getGameplays()); // 玩法规则列表
		builder.setDestroyReason(destroyReason); // 房间销毁类型

		return builder.build();
	}
}