package com.kodgames.battleserver.service.battle.region.yangzhou.score.battle;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.BattleHelper;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalculateType;
import com.kodgames.battleserver.service.battle.core.score.battle.BattleScoreCalculator;
import com.kodgames.battleserver.service.battle.region.yangzhou.Rules_YangZhou;
import com.kodgames.message.proto.battle.BattleProtoBuf.PlayerMatchResultPROTO;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultEventPROTO;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultScorePROTO;

/**
 * 扬州分数计算器
 */
public class BattleScoreCalculator_YangZhou extends BattleScoreCalculator
{
	public HashMap<Integer, PlayerMatchResultPROTO.Builder> createResultBuilders(Map<Integer, PlayerInfo> players)
	{
		// 构造所有玩家的结果
		HashMap<Integer, PlayerMatchResultPROTO.Builder> resultBuilders = new HashMap<>();
		for (PlayerInfo player : players.values())
		{
			PlayerMatchResultPROTO.Builder builder = PlayerMatchResultPROTO.newBuilder();
			builder.setRoleId(player.getRoleId());
			builder.setTotalPoint(player.getTotalPoint());
			builder.setPointInGame(0);
			builder.setStatus(player.getStatus());
			resultBuilders.put(player.getRoleId(), builder);
		}

		return resultBuilders;
	}
	
	/**
	 * 计算所有玩家的牌局最终得分
	 * @param players 所有玩家
	 * @param inoutResultBuilders 输出计算结果
	 */
	public void calculatePlayerScore(Map<Integer, PlayerInfo> players, HashMap<Integer, PlayerMatchResultPROTO.Builder> inoutResultBuilders)
	{
		// 收集分数计算数据
		collectScore(players, inoutResultBuilders);
		
		//获取园子大小
		BattleBean context = BattleHelper.getInstance().getBattleBean();
		List<Integer> rules = context.getGameRules();
		int yuanZi = Rules_YangZhou.getYuanZi(rules);
		
		// 计算事件总分
		inoutResultBuilders.values().forEach((result) -> {
			result.getEventsBuilderList().forEach((event) -> {

				// 合并后的分数
				int combinedPoint = event.getEventPoint() * (event.getCombinedTimes() + 1);

				// 设置合并后的分数
				event.setCombinedPoint(combinedPoint);

				// 设置事件总分
				int targetCount = event.getTargetsList().size();
				if(yuanZi != 0)
				{
					if(event.getAddOperation() == false)
					{
						PlayerInfo playerInfo = players.get(result.getRoleId());
						if(playerInfo.getTotalPoint() <= -yuanZi)
						{
							//不会向进园子的玩家收分
							event.setEventPoint(0);
						}
						else
							event.setEventPoint(combinedPoint * targetCount);	
					}
					else
					{
						for(Integer targetId : event.getTargetsList())
						{
							PlayerInfo playerInfo = players.get(targetId);
							if(playerInfo.getTotalPoint() <= -yuanZi)
							{
								//不会向进园子的玩家收分
								targetCount--;
							}
						}
						event.setEventPoint(combinedPoint * targetCount);
					}
				}
				else 
				{
					event.setEventPoint(combinedPoint * targetCount);
				}

				// 计算单局总分
				result.setPointInGame(result.getPointInGame() + event.getEventPoint());
			});

			// 计算房间总分
			result.setTotalPoint(result.getTotalPoint() + result.getPointInGame());
		});
		
		if(yuanZi != 0)
		{
			//如果有园子大小限制，则需要检测一下玩家最后的总分
			for(Integer roleId : inoutResultBuilders.keySet())
			{
				this.checkScore(roleId, context, yuanZi, players, inoutResultBuilders);
			}
			
			//对于进园子的玩家要添加一个非计分的分数项，用于通知客户端那个玩家进园子了
			for(Integer roleId : inoutResultBuilders.keySet())
			{
				PlayerMatchResultPROTO.Builder result = inoutResultBuilders.get(roleId);
				if(result.getTotalPoint() <= -yuanZi)
				{
					ResultEventPROTO.Builder eventProto = createResultEventBuilder();
					ResultScorePROTO.Builder scoreProto = ResultScorePROTO.newBuilder();
					scoreProto.setType(PlayType.DISPLAY_GENERAL_HEAD_IMAGE_FLAG);
					scoreProto.setCalcType(0);
					scoreProto.setPoint(0);
					eventProto.setScore(scoreProto);
					result.addEvents(eventProto);
				}
			}
		}
	}

	/**
	 * 构建用于每局计算分数的结构
	 * 整理分数, 将分数附加于分数目标, 并合并可以合并显示的计分项
	 */
	protected void collectScore(Map<Integer, PlayerInfo> players, HashMap<Integer, PlayerMatchResultPROTO.Builder> inoutResultBuilders)
	{
		// 计算牌型分数
		calcHuScore(players.values(), inoutResultBuilders);

		// 计算非牌型分数
		calcSpecialScore(players.values(), inoutResultBuilders);

		// 合并形同类型分数
		combineSamePoints(inoutResultBuilders);
	}

	/**
	 * 按分数顺序进行加或乘
	 */
	private void calculateScore(ResultEventPROTO.Builder event)
	{
		int count = event.getSubScoresCount();
		int score = 0;

		for (int index = 0; index < count; ++index)
		{
			ResultScorePROTO subScore = event.getSubScores(index);
			ScoreCalculateType type = ScoreCalculateType.getType(subScore.getCalcType());
			switch (type)
			{
				case TOTAL_ADD:
					score += subScore.getPoint();
					break;
				case TOTAL_MULTI:
					score *= subScore.getPoint();
					break;
				default:
					Macro.AssetTrue(true, "calculateScore : unsupported type -> " + type);
			}
		}

		event.setEventPoint(score);
	}

	/**
	 * 计算牌型分数
	 */
	private void calcHuScore(Collection<PlayerInfo> players, HashMap<Integer, PlayerMatchResultPROTO.Builder> inoutResultBuilders)
	{
		for (PlayerInfo player : players)
		{
			player.getCards().getScoreDatas().forEach((scoreData) -> {

				// 找到胡牌相关的类型
				Macro.AssetTrue(scoreData.getPoints().isEmpty());
				int eventType = scoreData.getPoints().get(0).getScoreType();
				if (PlayType.isHuType(eventType) == false)
					return;

				// 计算点炮胡/自摸胡的分数
				int roleId = player.getRoleId();
				ResultEventPROTO.Builder eventProto = createResultEventBuilder();
				ResultEventPROTO.Builder normalBuilder = calcNormalHuScore(roleId, scoreData, inoutResultBuilders, eventProto);

				// 计算单人总分
				calculateScore(normalBuilder);

				// 添加计分
				inoutResultBuilders.get(roleId).addEvents(normalBuilder);

				// 反向构建目标者的分数
				calcTargetScore(roleId, normalBuilder, inoutResultBuilders);
			});
		}
	}

	/**
	 * 计算正常胡牌分数（相对于对未胡牌且未点炮的玩家的扣分）
	 */
	private ResultEventPROTO.Builder calcNormalHuScore(int roleId, ScoreData scoreData, HashMap<Integer, PlayerMatchResultPROTO.Builder> inoutResultBuilders, ResultEventPROTO.Builder eventProto)
	{
		// 设置加/减分数
		eventProto.setAddOperation(scoreData.getAddOperation());

		// 构建牌型分
		ResultScorePROTO.Builder mainScoreProto = ResultScorePROTO.newBuilder();
		mainScoreProto.setType(getMainHuType(scoreData));
		mainScoreProto.setCalcType(ScoreCalculateType.TOTAL_ADD.getValue());
		mainScoreProto.setPoint(0); // 这个值会通过子运算计算修改
		eventProto.setScore(mainScoreProto);

		// 构建牌形子分数
		scoreData.getPoints().forEach(scorePoint -> eventProto.addSubScores(scorePoint.toResultScoreProto()));

		// 设置目标
		eventProto.addAllTargets(scoreData.getScoreTargetList());

		return eventProto;
	}

	/**
	 * 获取主胡牌类型
	 */
	private int getMainHuType(ScoreData data)
	{
		return data.getScoreTargetList().size() == 1 ? PlayType.HU_DIAN_PAO : PlayType.HU_ZI_MO;
		// 参考 BattleScoreCalculator
		// return 自摸 或 点炮 （PlayType）
	}

	/**
	 * 计算非牌型分数
	 */
	private void calcSpecialScore(Collection<PlayerInfo> players, HashMap<Integer, PlayerMatchResultPROTO.Builder> inoutResultBuilders)
	{
		players.forEach((player) -> {
			player.getCards().getScoreDatas().forEach((scoreData) -> {
				if (Macro.AssetTrue(scoreData.getPoints().size() == 0))
					return;

				// 忽略胡牌类型
				if (PlayType.isHuType(scoreData.getPoints().get(0).getScoreType()))
					return;

				/*
				 * 构建加分人数据
				 */
				ResultEventPROTO.Builder eventProto = createResultEventBuilder();
				eventProto.setAddOperation(scoreData.getAddOperation());

				// 默认使用第一个ScorePoint作为类型
				ScorePoint scorePoint = scoreData.getPoints().get(0);
				ResultScorePROTO.Builder scoreProto = ResultScorePROTO.newBuilder();
				scoreProto.setType(scorePoint.getScoreType());
				scoreProto.setCalcType(scorePoint.getCalcType());
				scoreProto.setPoint(scorePoint.getScoreValue());
				eventProto.setScore(scoreProto);

				// 构建子分数
				scoreData.getPoints().forEach((pointData) -> {
					ResultScorePROTO.Builder subScoreProto = ResultScorePROTO.newBuilder();
					subScoreProto.setType(pointData.getScoreType());
					subScoreProto.setCalcType(pointData.getCalcType());
					subScoreProto.setPoint(pointData.getScoreValue());
					eventProto.addSubScores(subScoreProto);
				});

				// 设置目标
				eventProto.addAllTargets(scoreData.getScoreTargetList());

				// 计算单人总分
				calculateScore(eventProto);

				// 添加计分
				inoutResultBuilders.get(player.getRoleId()).addEvents(eventProto);

				// 反向构建目标者的分数
				calcTargetScore(player.getRoleId(), eventProto, inoutResultBuilders);
			});
		});
	}

	/**
	 * 计算目标玩家分数
	 */
	private void calcTargetScore(int scourceId, ResultEventPROTO.Builder eventProto, HashMap<Integer, PlayerMatchResultPROTO.Builder> inoutResultBuilders)
	{	
		eventProto.getTargetsList().forEach((targetId) -> {

			// 如果只为计算一个玩家的分数. 目标者有可能没在容器中
			if (inoutResultBuilders.containsKey(targetId) == false)
				return;

			ResultEventPROTO.Builder targetEventProto = eventProto.clone();

			// 设置加/减分数, 与发起者相反
			targetEventProto.setAddOperation(!eventProto.getAddOperation());

			// 分数取反
			targetEventProto.setEventPoint(-targetEventProto.getEventPoint());

			// 设置目标为发起者
			targetEventProto.clearTargets();
			targetEventProto.addTargets(scourceId);

			// 添加计分到target
			inoutResultBuilders.get(targetId).addEvents(targetEventProto);
		});
	}

	/**
	 * 合并形同类型分数
	 */
	private void combineSamePoints(HashMap<Integer, PlayerMatchResultPROTO.Builder> inoutResultBuilders)
	{
		inoutResultBuilders.values().forEach((result) -> {
			for (int i = 0; i < result.getEventsBuilderList().size(); ++i)
			{
				ResultEventPROTO.Builder eventProto = result.getEventsBuilder(i);

				// 同一首歌不在合并
				if (eventProto.getSubScoresBuilderList().get(0).getType() == PlayType.DISPLAY_TONG_YI_SHOU_GE_END)
					break;

				eventProto.setCombinedTimes(0);

				for (int j = i + 1; j < result.getEventsBuilderList().size(); ++j)
				{
					ResultEventPROTO.Builder compairEventProto = result.getEventsBuilder(j);

					if (canBeCombined(eventProto, compairEventProto) && PlayType.isHuType(eventProto.getScore().getType()) == false)
					{
						// 增加合并计数
						eventProto.setCombinedTimes(eventProto.getCombinedTimes() + 1);

						// 删除被合并者
						result.removeEvents(j);
						--j;
					}
				}
			}
		});
	}
	
	/**
	 * 检测玩家分数是否能够进园子，如果能进园子，则要修正玩家总分
	 * @param roleId:被检测玩家id
	 * @param yuanZi:园子大小
	 */
	private void checkScore(int roleId, BattleBean context, int yuanZi, Map<Integer, PlayerInfo> players, HashMap<Integer, PlayerMatchResultPROTO.Builder> inoutResultBuilders)
	{
		PlayerMatchResultPROTO.Builder result = inoutResultBuilders.get(roleId);
		
		//玩家分数小于园子分，需要修正
		if(result.getTotalPoint() < -yuanZi)
		{
			int addScore = 0;
			for(ResultEventPROTO.Builder event : result.getEventsBuilderList())
			{
				if(event.getAddOperation())
					addScore += event.getEventPoint();
			}
			
			PlayerInfo roleInfo = players.get(roleId);
			int canPayScore = addScore + (roleInfo.getTotalPoint() + yuanZi); //可支付分数
			
			int roleIndex = context.getPlayerIds().indexOf(roleId);
			int index = roleIndex + 1;
			
			//逆时针遍历玩家，检测分数是否够付，如果不够付的则修正玩家分数
			while((index % context.getPlayerSize()) != roleIndex)
			{
				int checkRoleId = context.getPlayerIds().get(index % context.getPlayerSize());
				PlayerMatchResultPROTO.Builder checkResult = inoutResultBuilders.get(checkRoleId);
				int needPayScore = 0;
				for(ResultEventPROTO.Builder event : checkResult.getEventsBuilderList())
				{
					if(event.getAddOperation() && event.getTargetsList().contains(roleId))
					{
						needPayScore += event.getCombinedPoint();
					}
				}
				
				if(needPayScore > canPayScore)
				{
					//不够付，修正玩家分数
					checkResult.setPointInGame(checkResult.getPointInGame() - (needPayScore - canPayScore));
					checkResult.setTotalPoint(checkResult.getTotalPoint() - (needPayScore - canPayScore));
					this.checkScore(checkRoleId, context, yuanZi, players, inoutResultBuilders);
					canPayScore = 0;
				}
				else 
				{
					//够付，则从可支付的分数中减掉已支付的分数
					canPayScore = canPayScore - needPayScore;
				}
				index++;
			}
			
			//重新设置进园子的玩家分数
			result.setPointInGame(-roleInfo.getTotalPoint() - yuanZi);
			result.setTotalPoint(-yuanZi);
		}
	}
}
