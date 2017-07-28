package com.kodgames.battleserver.service.battle.core.score.battle;

import java.util.HashMap;
import java.util.Map;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.creator.ICreateContextHandler;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalcluateContext;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalculateType;
import com.kodgames.message.proto.battle.BattleProtoBuf.PlayerMatchResultPROTO;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultEventPROTO;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultScorePROTO;

import net.sf.json.JSONObject;

/**
 * 牌局结束玩家分数计算器
 */
public class BattleScoreCalculator implements ICreateContextHandler
{
	public static final String KEY_MAX_VALUE = "maxValue";

	private int maxValue = 0;

	public static BattleScoreCalculator create(JSONObject context)
		throws Exception
	{
		// 反射构造实例
		BattleScoreCalculator instance = CreateContextHelper.instantiateClass(context, BattleScoreCalculator.class);
		instance.createFromContext(context);
		return instance;
	}

	/**
	 * 构造计算所需的数据
	 */
	public void createFromContext(JSONObject context)
		throws Exception
	{
		maxValue = CreateContextParser.getInt(context, KEY_MAX_VALUE);
	}

	public int getMaxValue()
	{
		return maxValue;
	}

	public HashMap<Integer, PlayerMatchResultPROTO.Builder> createResultBuilders(Map<Integer, PlayerInfo> players)
	{
		// 构造所有玩家的结果
		HashMap<Integer, PlayerMatchResultPROTO.Builder> resultBuilders = new HashMap<>();
		for (PlayerInfo player : players.values())
			resultBuilders.put(player.getRoleId(), createPlayerMatchResultBuilder(player));

		return resultBuilders;
	}

	/**
	 * 计算所有玩家的牌局最终得分,
	 * 
	 * @param players 所有玩家
	 * @param inoutResultBuilders 输出计算结果
	 */
	public void calculatePlayerScore(Map<Integer, PlayerInfo> players, HashMap<Integer, PlayerMatchResultPROTO.Builder> inoutResultBuilders)
	{
		// 收集分数计算数据
		collectScore(players, inoutResultBuilders);

		// 计算玩家总分
		inoutResultBuilders.values().forEach((result) -> {
			result.getEventsBuilderList().forEach((event) -> {

				// 计算分数
				int score = calculateScore(event, ScoreCalcluateContext.sumScore(event), maxValue);

				// 合并后的分数
				int combinedPoint = score * (event.getCombinedTimes() + 1);

				// 加/减分处理
				if (event.getAddOperation() == false)
					combinedPoint = -combinedPoint;

				// 设置合并后的分数
				event.setCombinedPoint(combinedPoint);

				// 总分
				event.setEventPoint(combinedPoint * event.getTargetsList().size());

				// 计算分数
				result.setPointInGame(result.getPointInGame() + event.getEventPoint());

				// 修改客户端显示
				modifyScoreFromClientDisplay(event);
			});

			result.setTotalPoint(result.getTotalPoint() + result.getPointInGame());
		});
	}

	/**
	 * 构建用于每局计算分数的结构
	 *
	 * 整理分数, 将分数附加于分数目标, 并合并可以合并显示的计分项
	 */
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
				boolean isZiMo = scoreData.getSourceId() == player.getRoleId();
				mainScoreProto.setType(isZiMo ? PlayType.HU_ZI_MO : PlayType.HU_DIAN_PAO);
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
				// 可能有多个，注释掉
				// Macro.AssetFalse(scoreData.getPoints().size() == 1);
				ScorePoint scorePoint = scoreData.getPoints().get(0);
				ResultScorePROTO.Builder scoreProto = ResultScorePROTO.newBuilder();
				scoreProto.setType(scorePoint.getScoreType());
				scoreProto.setCalcType(scorePoint.getCalcType());
				scoreProto.setPoint(scorePoint.getScoreValue());
				eventProto.setScore(scoreProto);

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
					if (canBeCombined(eventProto, compairEventProto) && (eventProto.getScore().getType() != PlayType.HU_ZI_MO && eventProto.getScore().getType() != PlayType.HU_DIAN_PAO))
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
	 * 比较两个HuScoreCheckContext
	 */
	public int compairScoreContext(HuScoreCheckContext l, HuScoreCheckContext r)
	{
		if (l.calculatedScore.towIndex != r.calculatedScore.towIndex)
			return l.calculatedScore.towIndex - r.calculatedScore.towIndex;
		else
			return l.calculatedScore.totalAdd - r.calculatedScore.totalAdd;
	}

	/**
	 * 计算牌局个人得分
	 *
	 * 不同玩法算分方式不同, 可以重载这个函数
	 */
	protected int calculateScore(ResultEventPROTO.Builder event, ScoreCalcluateContext context, int maxValue)
	{
		// TODO : 修改为抽象方法, 将四川的逻辑移到四川的代码中

		int score = context.totalAdd;

		// 最高番限制
		if (context.isHuScore)
			context.towIndex = maxValue == 0 ? context.towIndex : Math.min(context.towIndex, maxValue);
		// 没有翻数就不用算了, 防止多了1
		if (context.towIndex != 0)
			score += (int)(Math.pow(2, context.towIndex - 1));

		return score;
	}

	/**
	 * 计算完具体得分之后, 可以通过这个函数修改客户端用于显示的分数
	 */
	protected void modifyScoreFromClientDisplay(ResultEventPROTO.Builder event)
	{
		// TODO : 修改为抽象方法, 将四川的逻辑移到四川的代码中

		// 修改客户端显示
		event.getSubScoresBuilderList().forEach(subScoreBuilder -> {
			if (!PlayType.isHuType(subScoreBuilder.getType()))
				return;

			// 如果不是乘方运算，跳过
			if (subScoreBuilder.getCalcType() != ScoreCalculateType.TWO_INDEX.getValue())
				return;

			// 基础番数，客户端的显示修改为2^(n-1),附加番数，客户端的显示修改为2^n
			if (PlayType.isBasicFan(subScoreBuilder.getType()))
				subScoreBuilder.setPoint((int)(Math.pow(2, subScoreBuilder.getPoint() - 1)));
			else
				subScoreBuilder.setPoint((int)(Math.pow(2, subScoreBuilder.getPoint())));

		});
	}

	/**
	 * 创建并初始化PlayerMatchResultPROTO.Builder
	 */
	protected static PlayerMatchResultPROTO.Builder createPlayerMatchResultBuilder(PlayerInfo player)
	{
		PlayerMatchResultPROTO.Builder builder = PlayerMatchResultPROTO.newBuilder();
		builder.setRoleId(player.getRoleId());
		builder.setTotalPoint(player.getTotalPoint());
		builder.setPointInGame(0);

		return builder;
	}

	/**
	 * 创建并初始化ResultEventPROTO.Builder
	 */
	protected static ResultEventPROTO.Builder createResultEventBuilder()
	{
		ResultEventPROTO.Builder eventProto = ResultEventPROTO.newBuilder();
		eventProto.setAddOperation(true);
		eventProto.setCombinedTimes(0);
		eventProto.setCombinedPoint(0);
		eventProto.setEventPoint(0);

		return eventProto;
	}

	/**
	 * 比较两个event是否可以合并
	 *
	 * 具有相同计分类型,目标,和自分数的类型可以合并
	 */
	protected static boolean canBeCombined(ResultEventPROTO.Builder l, ResultEventPROTO.Builder r)
	{
		// required bool addOperation = 1;
		if (l.getAddOperation() != r.getAddOperation())
			return false;

		// required ResultScorePROTO score = 2;
		if (isSame(l.getScoreBuilder(), r.getScoreBuilder()) == false)
			return false;

		// repeated ResultScorePROTO subScores = 3;
		if (l.getSubScoresCount() != r.getSubScoresCount())
			return false;

		for (int i = 0; i < l.getSubScoresCount(); ++i)
			if (isSame(l.getSubScoresBuilder(i), r.getSubScoresBuilder(i)) == false)
				return false;

		// repeated int32 targets = 4;
		if (l.getTargetsCount() != r.getTargetsCount())
			return false;

		for (int i = 0; i < l.getTargetsCount(); ++i)
			if (r.getTargetsList().contains(l.getTargets(i)) == false)
				return false;

		return true;
	}

	/**
	 * 比较两个ResultScorePROTO是否一样
	 */
	protected static boolean isSame(ResultScorePROTO.Builder l, ResultScorePROTO.Builder r)
	{
		if (l.getType() != r.getType())
			return false;

		if (l.getCalcType() != r.getCalcType())
			return false;

		if (l.getPoint() != r.getPoint())
			return false;

		return true;
	}
}