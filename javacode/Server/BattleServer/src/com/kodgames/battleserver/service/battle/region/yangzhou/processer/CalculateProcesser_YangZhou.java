package com.kodgames.battleserver.service.battle.region.yangzhou.processer;

import java.util.HashMap;
import java.util.List;

import com.kodgames.battleserver.common.Constant.RoomDestroyReason;
import com.kodgames.battleserver.service.battle.BattleService;
import com.kodgames.battleserver.service.battle.common.xbean.BattleRoom;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.BattleState;
import com.kodgames.battleserver.service.battle.core.MahjongHelper;
import com.kodgames.battleserver.service.battle.processer.CalculateProcesser;
import com.kodgames.battleserver.service.battle.region.yangzhou.Rules_YangZhou;
import com.kodgames.battleserver.service.room.RoomService;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.message.proto.battle.BattleProtoBuf.BCMatchResultSYN;
import com.kodgames.message.proto.battle.BattleProtoBuf.PlayerMatchResultPROTO;

public class CalculateProcesser_YangZhou extends CalculateProcesser
{
	@Override
	protected void calculateScore()
	{
		BattleRoom roomInfo = battleHelper.getRoomInfo();
		int roomId = roomInfo.getRoomId();

		RoomService roomService = ServiceContainer.getInstance().getPublicService(RoomService.class);
		// 战斗结算数据
		battleScoreProcessor.process(context);
		HashMap<Integer, PlayerMatchResultPROTO.Builder> resultPROTOs = calculatePlayerMatchResult(context.getPlayers(), gameScoreCaculator);

		// 发送单局结算消息给玩家
		BCMatchResultSYN.Builder calcBuilder = BCMatchResultSYN.newBuilder();
		calcBuilder.setIsHuang(context.getBattleState() == BattleState.HUANGZHUANG);
		// 设置剩余牌
		calcBuilder.setLastCards(MahjongHelper.convert2ByteString(context.getCardPool().getCards()));
		calcBuilder.setIsRejoin(false);
		resultPROTOs.values().forEach(result -> {
			calcBuilder.addMatchResults(result.build());
		});

		// 发送
		context.getPlayerIds().stream().forEach(roleId -> {
			calcBuilder.setProtocolSeq(roomService.getAndSetNewPlayProtocolSequence(roomId, roleId));
			controller.sendMessage(roleId, calcBuilder.build());
		});

		// 添加到context中，复牌用
		this.calcBuilder = calcBuilder;

		// 计算下一庄家
		battleHelper.getZhuangCalculator().calculateNextZhuang(roomInfo);
		int zhuang = context.getZhuang();
		int nextZhuang = context.getNextZhuang();

		// 增加圈数或局数，并下发消息
		ServiceContainer.getInstance().getPublicService(BattleService.class).roundFinish(roomId, zhuang, nextZhuang, calcBuilder);

		// 如果房间不存在了
		if (roomService.isRoomExist(roomInfo.getRoomId()) == false)
		{
			this.endBattle();
			return;
		}

		// 检测有几个玩家进园子
		List<Integer> rules = roomInfo.getGameplays();
		int yuanZi = Rules_YangZhou.getYuanZi(rules);
		if (yuanZi != 0)
		{
			int inYuanZiCount = 0; // 进园子的玩家个数
			for (Integer roleId : resultPROTOs.keySet())
			{
				int totalPoint = resultPROTOs.get(roleId).getTotalPoint();
				if (totalPoint <= -yuanZi)
				{
					inYuanZiCount++;
				}
			}

			// 有两个玩家进园子就就结算房间
			if (inYuanZiCount >= 2)
			{
				this.endBattle();
				roomService.destroyRoom(roomId, RoomDestroyReason.GAMEOVER);
			}
		}
	}
}
