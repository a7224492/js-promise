package com.kodgames.battleserver.service.battle.region.guangdong.common.score.battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalcluateContext;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalculateType;
import com.kodgames.battleserver.service.battle.core.score.battle.BattleScoreCalculator;
import com.kodgames.message.proto.battle.BattleProtoBuf.PlayerMatchResultPROTO;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultEventPROTO;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultScorePROTO;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultScorePROTO.Builder;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultScorePROTOOrBuilder;

public class BattleScoreCalculator_GuangDong extends BattleScoreCalculator
{
	public static final String KEY_BUY_MA_TYPES = "key_buyMaTypes";
	public static final String KEY_INGORE_TYPES = "Key_ingoreTypes";
	public static final String KEY_HUTYPSE = "key_huTypes";
	public static final String KEY_OTHER_MULTI_TYPE = "key_otherMultiType";
	public static final String KEY_OTHER_ADD_TYPE = "key_otherAddType";

	/**
	 * 胡的类型
	 */
	private List<Integer> huTypes = new ArrayList<Integer>();
	private List<Integer> ingoreTypes = new ArrayList<Integer>();
	protected List<Integer> buyMaFenType = new ArrayList<Integer>();
	protected List<Integer> otherMultiTypes = new ArrayList<Integer>();
	protected List<Integer> otherAddTypes = new ArrayList<Integer>();

	@Override
	public void createFromContext(net.sf.json.JSONObject context)
		throws Exception
	{
		super.createFromContext(context);

		if (context.containsKey(KEY_HUTYPSE))
		{
			for (int i = 0; i < context.getJSONArray(KEY_HUTYPSE).size(); ++i)
				huTypes.add((Integer)context.getJSONArray(KEY_HUTYPSE).get(i));
		}

		if (context.containsKey(KEY_INGORE_TYPES))
		{
			for (int i = 0; i < CreateContextParser.getArraySize(context, KEY_INGORE_TYPES); ++i)
			{
				ingoreTypes.add(CreateContextParser.getIntInArray(context, KEY_INGORE_TYPES, i));
			}
		}

		if (context.containsKey(KEY_BUY_MA_TYPES))
		{
			for (int i = 0; i < CreateContextParser.getArraySize(context, KEY_BUY_MA_TYPES); ++i)
			{
				buyMaFenType.add(CreateContextParser.getIntInArray(context, KEY_BUY_MA_TYPES, i));
			}
		}
		if (context.containsKey(KEY_OTHER_MULTI_TYPE))
		{
			for (int i = 0; i < CreateContextParser.getArraySize(context, KEY_OTHER_MULTI_TYPE); ++i)
			{
				otherMultiTypes.add(CreateContextParser.getIntInArray(context, KEY_OTHER_MULTI_TYPE, i));
			}
		}
		if (context.containsKey(KEY_OTHER_ADD_TYPE))
		{
			for (int i = 0; i < CreateContextParser.getArraySize(context, KEY_OTHER_ADD_TYPE); ++i)
			{
				otherAddTypes.add(CreateContextParser.getIntInArray(context, KEY_OTHER_ADD_TYPE, i));
			}
		}
	};

	/**
	 * 计算牌局个人得分
	 * 
	 * 不同玩法算分方式不同, 可以重载这个函数
	 */
	@Override
	protected int calculateScore(ResultEventPROTO.Builder event, ScoreCalcluateContext context, int maxValue)
	{
		// 广东翻数不是连乘
		Macro.AssetFalse(context.towIndex == 0);

		int score = context.totalAdd;
		// 分数类型判定
		int type = event.getSubScoresOrBuilderList().get(0).getType();

		// 最高番限制
		if (context.isHuScore || buyMaFenType.contains(type))
		{
			int multi = 1;
			int otherAdd = 0;
			// 循环所有分数
			for (ResultScorePROTOOrBuilder subScore : event.getSubScoresOrBuilderList())
			{
				// 其他的乘分类型
				if (otherMultiTypes.contains(subScore.getType()))
					multi *= subScore.getPoint();
				// 其他的加分类型
				if (otherAddTypes.contains(subScore.getType()))
					otherAdd += subScore.getPoint();
			}
			score = (((score - otherAdd) * context.totalMulti / multi) + otherAdd) * multi;
			// 是否有分数限制
			if (maxValue != 0)
			{
				score = Math.min(score, maxValue);
			}

			score *= context.totalMulti2nd;
		}

		// 是否为杠分，杠分需要算奖马的分
		if (PlayType.isGangOperator(type))
			score *= context.totalMulti2nd;

		return score;
	}

	/**
	 * 修改买马的显示
	 */
	protected void modifyScoreFromClientDisplay(ResultEventPROTO.Builder event)
	{
		// 如若是买马和罚马的得分
		if (buyMaFenType.contains(event.getSubScoresOrBuilderList().get(0).getType()))
		{
			int point = 0;
			// 修改客户端买马罚马的显示效果，分值经过合并，所以直接设置为合并次数+1再乘以原来的值
			for (Builder subScore : event.getSubScoresBuilderList())
			{
				if (subScore.getType() == PlayType.DISPLAY_BUY_HORSE || subScore.getType() == PlayType.DISPLAY_PUNISH_HORSE)
				{
					subScore.setPoint((event.getCombinedTimes() + 1) * subScore.getPoint());
					point = subScore.getPoint();
				}
			}

			// 删除掉除了ingoreTypes里面的分数项
			for (int index = event.getSubScoresCount() - 1; index >= 0; --index)
			{
				if (ingoreTypes.contains(event.getSubScoresList().get(index).getType()) == false)
					event.removeSubScores(index);
			}

			Macro.AssetFalse(point >= 0, "分数错误，没有买马罚马分数");
			// 设置胡牌得分为总分除以奖马的分值
			event.getSubScoresBuilderList().get(0).setPoint(event.getCombinedPoint() / point);
			// 设置合并次数为0
			event.setCombinedTimes(0);
		}
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
				boolean isZiMo = scoreData.getSourceId() == player.getRoleId();
				mainScoreProto.setType(checkHuType(scoreData) != 0 ? checkHuType(scoreData) : isZiMo ? PlayType.HU_ZI_MO : PlayType.HU_DIAN_PAO);
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
	 * 检查需要发送给客户端什么分数类型
	 * 
	 * @param scoreData
	 * @return
	 */
	private int checkHuType(ScoreData scoreData)
	{
		Macro.AssetFalse(PlayType.isHuType(scoreData.getPoints().get(0).getScoreType()), "传了错误的类型,应该传胡牌的分值");
		int type = 0;
		for (ScorePoint point : scoreData.getPoints())
		{
			if (huTypes.contains(point.getScoreType()))
				type = point.getScoreType();
		}

		return type;
	}
}