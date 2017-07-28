package com.kodgames.battleserver.service.battle.region.neimeng.common.score.battle;

import java.util.HashMap;
import java.util.Map;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalcluateContext;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalculateType;
import com.kodgames.battleserver.service.battle.core.score.battle.BattleScoreCalculator;
import com.kodgames.message.proto.battle.BattleProtoBuf.PlayerMatchResultPROTO;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultEventPROTO;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultScorePROTO;

/**
 * 牌局分数计算器
 */
public class BattleScoreCalculator_NeiMeng extends BattleScoreCalculator
{

	/**
	 * 计算所有玩家的牌局最终得分,
	 * 
	 * @param players 所有玩家
	 * @param inoutResultBuilders 输出计算结果
	 */
	@Override
	public void calculatePlayerScore(Map<Integer, PlayerInfo> players, HashMap<Integer, PlayerMatchResultPROTO.Builder> inoutResultBuilders)
	{
		// 收集分数计算数据
		collectScore(players, inoutResultBuilders);

		// 计算玩家总分
		inoutResultBuilders.values().forEach((result) -> {
			result.getEventsBuilderList().forEach((event) -> {

				// 计算分数
				int score = calculateScore(event, ScoreCalcluateContext.sumScore(event), getMaxValue());

				// 合并后的分数
				int combinedPoint = score * (event.getCombinedTimes() + 1);

				// 修正输牌人的牌型计分
				int scoreType = event.getScore().getType();
				if (PlayType.isHuType(scoreType) && !event.getAddOperation())
					combinedPoint = -combinedPoint;

				// 设置合并后的分数
				event.setCombinedPoint(combinedPoint);

				// 总分
				event.setEventPoint(combinedPoint * event.getTargetsList().size());

				// 计算分数
				result.setPointInGame(result.getPointInGame() + event.getEventPoint());
			});

			result.setTotalPoint(result.getTotalPoint() + result.getPointInGame());
		});
	}

	/**
	 * 构建用于每局计算分数的结构
	 *
	 * 整理分数, 将分数附加于分数目标, 并合并可以合并显示的计分项
	 */
	@Override
	protected void collectScore(Map<Integer, PlayerInfo> players, HashMap<Integer, PlayerMatchResultPROTO.Builder> inoutResultBuilders)
	{
		// 先收集和牌相关的分数
		players.values().forEach((player) -> {
			player.getCards().getScoreDatas().forEach((scoreData) -> {
				// 找到胡牌相关的类型
				if (scoreData.getPoints().size() == 0 || PlayType.isHuType(scoreData.getPoints().get(0).getScoreType()) == false)
					return;

				/*
				 * 构建加分人数据
				 */
				ResultEventPROTO.Builder eventProto = createResultEventBuilder();

				// 设置加/减分数
				eventProto.setAddOperation(scoreData.getAddOperation());

				// 构建自摸/胡类型
				ResultScorePROTO.Builder mainScoreProto = ResultScorePROTO.newBuilder();
				boolean isQiangGangHu = false;
				for (ScoreData qiangganghu : player.getCards().getScoreDatas())
				{
					for (ScorePoint scorePoint : qiangganghu.getPoints())
					{
						if (scorePoint.getScoreType() == PlayType.HU_QIANG_GANG_HU)
							isQiangGangHu = true;
					}
				}
				if (!isQiangGangHu)
				{
					boolean isZiMo = scoreData.getSourceId() == player.getRoleId();
					mainScoreProto.setType(isZiMo ? PlayType.HU_ZI_MO : PlayType.HU_DIAN_PAO);
				}
				else
					mainScoreProto.setType(PlayType.HU_QIANG_GANG_HU);
				mainScoreProto.setCalcType(ScoreCalculateType.TOTAL_ADD.getValue());
				mainScoreProto.setPoint(0); // 这个值会通过子运算计算修改
				eventProto.setScore(mainScoreProto);

				// 构建牌形翻数
				scoreData.getPoints().forEach((scorePoint) -> {
					ResultScorePROTO.Builder subScoreProto = ResultScorePROTO.newBuilder();
					subScoreProto.setType(scorePoint.getScoreType());
					subScoreProto.setCalcType(scorePoint.getCalcType());
					subScoreProto.setPoint(scorePoint.getScoreValue());
					eventProto.addSubScores(subScoreProto);
				});

				// 设置目标
				eventProto.addAllTargets(scoreData.getScoreTargetList());

				// 添加计分
				inoutResultBuilders.get(player.getRoleId()).addEvents(eventProto);

				/*
				 * 反向构建目标者的分数
				 */
				scoreData.getScoreTargetList().forEach((targetId) -> {

					// 如果只为计算一个玩家的分数. 目标者有可能没在容器中
					if (inoutResultBuilders.containsKey(targetId) == false)
						return;

					ResultEventPROTO.Builder targetEventProto = eventProto.clone();

					// 设置加/减分数, 与发起者相反
					targetEventProto.setAddOperation(!eventProto.getAddOperation());

					// 设置目标为发起者
					targetEventProto.clearTargets();
					targetEventProto.addTargets(player.getRoleId());

					// 添加计分到target
					inoutResultBuilders.get(targetId).addEvents(targetEventProto);
				});
			});
		});

		// 统计其他分数
		players.values().forEach((player) -> {
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

				// 默认使用第一个ScorePoint作为类型
				Macro.AssetFalse(scoreData.getPoints().size() == 1);
				ScorePoint scorePoint = scoreData.getPoints().get(0);
				ResultScorePROTO.Builder scoreProto = ResultScorePROTO.newBuilder();
				scoreProto.setType(scorePoint.getScoreType());
				scoreProto.setCalcType(scorePoint.getCalcType());
				scoreProto.setPoint(scorePoint.getScoreValue());
				eventProto.setScore(scoreProto);
				eventProto.setAddOperation(scoreData.getAddOperation());

				// 构建牌形翻数
				scoreData.getPoints().forEach((pointData) -> {
					ResultScorePROTO.Builder subScoreProto = ResultScorePROTO.newBuilder();
					subScoreProto.setType(pointData.getScoreType());
					subScoreProto.setCalcType(pointData.getCalcType());
					subScoreProto.setPoint(pointData.getScoreValue());
					eventProto.addSubScores(subScoreProto);
				});

				// 设置目标
				eventProto.addAllTargets(scoreData.getScoreTargetList());

				// 添加计分
				inoutResultBuilders.get(player.getRoleId()).addEvents(eventProto);

				/*
				 * 反向构建目标者的分数
				 */
				scoreData.getScoreTargetList().forEach((targetId) -> {

					// 如果只为计算一个玩家的分数. 目标者有可能没在容器中
					if (inoutResultBuilders.containsKey(targetId) == false)
						return;

					ResultEventPROTO.Builder targetEventProto = eventProto.clone();

					// 设置加/减分数, 与发起者相反
					targetEventProto.setAddOperation(!eventProto.getAddOperation());
					targetEventProto.clearSubScores();
					eventProto.getSubScoresList().forEach(subScore -> targetEventProto.addSubScores(ResultScorePROTO.newBuilder(subScore).setPoint(-subScore.getPoint())));

					// 设置目标为发起者
					targetEventProto.clearTargets();
					targetEventProto.addTargets(player.getRoleId());

					// 添加计分到target
					inoutResultBuilders.get(targetId).addEvents(targetEventProto);
				});
			});
		});

		// 合并形同类型分数
		inoutResultBuilders.values().forEach((result) -> {
			for (int i = 0; i < result.getEventsBuilderList().size(); ++i)
			{
				ResultEventPROTO.Builder eventProto = result.getEventsBuilder(i);
				eventProto.setCombinedTimes(0);
				for (int j = i + 1; j < result.getEventsBuilderList().size(); ++j)
				{
					ResultEventPROTO.Builder compairEventProto = result.getEventsBuilder(j);
					if (canBeCombined(eventProto, compairEventProto))
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
	 * 计算牌局个人得分
	 *
	 * 不同玩法算分方式不同, 可以重载这个函数
	 */
	@Override
	protected int calculateScore(ResultEventPROTO.Builder event, ScoreCalcluateContext context, int maxValue)
	{
		if (0 == maxValue)
			return context.totalMulti2nd == 1 ? context.totalAdd : context.totalAdd * context.totalMulti2nd;

		return Math.min(context.totalAdd, maxValue);
	}

}
