package com.kodgames.battleserver.service.battle.region.meizhou.heyuan.score.battle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalculateType;
import com.kodgames.battleserver.service.battle.core.score.battle.BattleScoreCalculator;
import com.kodgames.message.proto.battle.BattleProtoBuf.PlayerMatchResultPROTO;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultEventPROTO;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultScorePROTO;
import net.sf.json.JSONObject;

/**
 * 河源分数计算器
 */
public class BattleScoreCalculator_HeYuan extends BattleScoreCalculator
{
	public static final String KEY_HU_EVENT_ORDER = "huEventOrder";
	public static final String KEY_HU_PLAY_TYPE = "huPlayType";

	private static Map<Integer, Integer> huTypeToOrder = new HashMap<>();

	private static final Comparator<ResultScorePROTO> huProtoComparator = new Comparator<ResultScorePROTO>()
	{

		@Override
		public int compare(ResultScorePROTO p1, ResultScorePROTO p2)
		{
			Macro.AssetTrue(huTypeToOrder.get(p1.getType()) == null, "unordered score type : " + p1.getType());
			Macro.AssetTrue(huTypeToOrder.get(p2.getType()) == null, "unordered score type : " + p2.getType());
			return Integer.compare(huTypeToOrder.get(p1.getType()), huTypeToOrder.get(p2.getType()));
		}

	};

	private static final Comparator<ScorePoint> huPointComparator = new Comparator<ScorePoint>()
	{

		@Override
		public int compare(ScorePoint p1, ScorePoint p2)
		{
			Macro.AssetTrue(huTypeToOrder.get(p1.getScoreType()) == null, "unordered score type : " + p1.getScoreType());
			Macro.AssetTrue(huTypeToOrder.get(p2.getScoreType()) == null, "unordered score type : " + p2.getScoreType());
			return Integer.compare(huTypeToOrder.get(p1.getScoreType()), huTypeToOrder.get(p2.getScoreType()));
		}

	};

	public static Comparator<ScorePoint> getHuComparator()
	{
		return huPointComparator;
	}

	/**
	 * 构造计算所需的数据
	 */
	public void createFromContext(JSONObject context)
		throws Exception
	{
		List<JSONObject> events = CreateContextParser.getJSONArray(context, KEY_HU_EVENT_ORDER);
		for (int index = 0; index < events.size(); ++index)
		{
			JSONObject obj = events.get(index);
			
			huTypeToOrder.put(obj.getInt(KEY_HU_PLAY_TYPE), index);
		}
	}

	/**
	 * 计算所有玩家的牌局最终得分
	 * 
	 * @param players 所有玩家
	 * @param inoutResultBuilders 输出计算结果
	 */
	public void calculatePlayerScore(Map<Integer, PlayerInfo> players, HashMap<Integer, PlayerMatchResultPROTO.Builder> inoutResultBuilders)
	{
		// 收集分数计算数据
		collectScore(players, inoutResultBuilders);

		// 计算事件总分
		inoutResultBuilders.values().forEach((result) -> {
			result.getEventsBuilderList().forEach((event) -> {

				// 合并后的分数
				int combinedPoint = event.getEventPoint() * (event.getCombinedTimes() + 1);

				// 设置合并后的分数
				event.setCombinedPoint(combinedPoint);

				// 设置事件总分
				event.setEventPoint(combinedPoint * event.getTargetsList().size());

				// 计算单局总分
				result.setPointInGame(result.getPointInGame() + event.getEventPoint());
			});

			// 计算房间总分
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
		Macro.AssetTrue(score <= 0, "calculateScore : invalid score -> " + score);

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
				List<ResultEventPROTO.Builder> huEvents = new ArrayList<>();
				huEvents.add(normalBuilder);

				huEvents.forEach(event -> {
					// 对胡牌事件排序
					List<ResultScorePROTO> subScoreList = new ArrayList<>(event.getSubScoresList());
					Collections.sort(subScoreList, huProtoComparator);
					event.clearSubScores();
					event.addAllSubScores(subScoreList);

					// 计算单人总分
					calculateScore(event);

					// 添加计分
					inoutResultBuilders.get(roleId).addEvents(event);

					// 反向构建目标者的分数
					calcTargetScore(roleId, event, inoutResultBuilders);
				});
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
}
