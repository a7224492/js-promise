package com.kodgames.battleserver.service.battle.region.guangdong.common.processor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.sf.json.JSONObject;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.BattleHelper;
import com.kodgames.battleserver.service.battle.core.check.CheckHelper;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalcluateContext;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalculateType;
import com.kodgames.battleserver.service.battle.core.score.hu.HuScoreProcessor;
import com.kodgames.battleserver.service.battle.processer.Processer;

/**
 * 买马罚马的结算（需要放置在奖马前）
 */
public class Processor_CalculateBuyHorse extends Processer
{
	/**
	 * 配置是否为买马
	 */
	public static final String KEY_IS_BUY = "key_isBuy";

	/**
	 * 配置忽略的胡牌类型（需要删除掉）
	 */
	public static final String KEY_INGORE_TYPE = "key_ingoreType";

	/**
	 * 配置是否为三人玩法
	 */
	public static final String KEY_IS_THREE_PLAYER = "Key_IsThreePlayer";

	/**
	 * 分数限制收分目标
	 */
	public static final String KEY_SCORE_LIMIT_VALUE = "Key_scoreLimitValue";

	/**
	 * 分数收分限制分数类型
	 */
	public static final String KEY_SCORE_LIMIT_CALCTYPE = "Key_scoreLimitCalcType";

	/**
	 * 马分的type
	 */
	private int maFenType = 0;

	/**
	 * 忽略的胡牌类型（需要删除掉）
	 */
	private List<Integer> ingoreTypes = new ArrayList<>();

	/**
	 * 马分的type
	 */
	private int huMaFenType = 0;

	/**
	 * 三人玩法
	 */
	private boolean isThreePlayer = false;

	/**
	 * 是否为买马
	 */
	private boolean isBuy = false;

	/**
	 * 被动的马分type
	 */
	private int huBeMaFenType = 0;

	private int scoreLimitValue = 0;

	private ScoreCalculateType scoreLimitCalc = ScoreCalculateType.NONE;

	private Set<Integer> scoreLimitPlayer = new HashSet<Integer>();

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		isBuy = CreateContextParser.getBoolean(context, KEY_IS_BUY);
		maFenType = isBuy ? PlayType.DISPLAY_BUY_HORSE : PlayType.DISPLAY_PUNISH_HORSE;
		huMaFenType = isBuy ? PlayType.DISPLAY_HU_BUY_HORSE_SCORE : PlayType.DISPLAY_HU_PUNISH_HORSE_SCORE;
		isThreePlayer = CreateContextParser.getBoolean(context, KEY_IS_THREE_PLAYER);
		huBeMaFenType = isBuy ? PlayType.DISPLAY_BE_HU_BUY_HORSE_SCORE : PlayType.DISPLAY_BE_HU_PUNISH_HORSE_SCORE;
		if (context.containsKey(KEY_INGORE_TYPE))
		{
			for (int i = 0; i < CreateContextParser.getArraySize(context, KEY_INGORE_TYPE); ++i)
				ingoreTypes.add(CreateContextParser.getIntInArray(context, KEY_INGORE_TYPE, i));
		}

		if (context.containsKey(KEY_SCORE_LIMIT_VALUE))
		{
			scoreLimitValue = CreateContextParser.getInt(context, KEY_SCORE_LIMIT_VALUE);
			scoreLimitCalc = CreateContextParser.getScoreCalculateType(context, KEY_SCORE_LIMIT_CALCTYPE);
		}
	}

	@Override
	public void start()
	{
		calculate();

		finish();
	}

	/**
	 * 买马罚马的结算和播放动画
	 */
	private void calculate()
	{
		// 如果没有人胡牌就返回
		if (context.getHuIndex().isEmpty())
			return;

		// 胡牌玩家的list
		List<Integer> huRoleIds = new ArrayList<>();
		// 输的玩家
		List<Integer> loseRoleIds = new ArrayList<>();

		// 循环胡牌玩家
		for (int huIndex : context.getHuIndex())
		{
			// 找到胡牌玩家的roleId
			Step huStep = context.getRecords().get(huIndex);
			huRoleIds.add(huStep.getRoleId());
			PlayerInfo huPlayer = context.getPlayerById(huStep.getRoleId());
			// 判断是否为自摸或点炮，并且设置输牌玩家
			for (ScoreData data : huPlayer.getCards().getScoreDatas())
			{
				// 是否为胡牌类型
				if (PlayType.isHuType(data.getPoints().get(0).getScoreType()))
				{
					// 自摸
					if (data.getSourceId() == huPlayer.getRoleId())
					{
						// 添加进输牌玩家中
						loseRoleIds.addAll(context.getPlayerIds());
						// 删除掉胡牌玩家的id
						loseRoleIds.removeIf(roleId -> {
							if (roleId == huPlayer.getRoleId())
								return true;
							return false;
						});
					}
					// 点炮，点炮只能有一个输牌玩家
					else
					{
						loseRoleIds.add(data.getSourceId());
					}
				}
			}
		}
		// 去重
		huRoleIds = huRoleIds.stream().distinct().collect(Collectors.toList());
		loseRoleIds = loseRoleIds.stream().distinct().collect(Collectors.toList());

		// 十倍不计分的玩家
		if (scoreLimitValue != 0)
		{
			for (int roleId : loseRoleIds)
			{
				// 检测听牌玩家的听牌最高分数
				List<Byte> tingCards = CheckHelper.getTingCards(context, roleId);
				if (tingCards.size() == 0)
					continue;

				HuScoreProcessor huScoreProcessor = BattleHelper.getInstance().getHuScoreProcessor();
				for (byte tingCard : tingCards)
				{
					// 计算分数
					ScoreData scoreData = huScoreProcessor.process(context, roleId, tingCard, true);
					if (scoreData == null)
						continue;

					// 计算总翻数
					ScoreCalcluateContext scoreCacluateContext = ScoreCalcluateContext.sumScore(scoreData);
					// 判断是否已经添加
					boolean hasAdd = false;

					switch (scoreLimitCalc)
					{
						case TWO_INDEX:
							if (scoreCacluateContext.towIndex >= scoreLimitValue)
							{
								scoreLimitPlayer.add(roleId);
								hasAdd = true;
							}
							break;

						case TOTAL_ADD:
							if (scoreCacluateContext.totalAdd >= scoreLimitValue)
							{
								scoreLimitPlayer.add(roleId);
								hasAdd = true;
							}
							break;

						case TOTAL_MULTI:
							if (scoreCacluateContext.totalMulti >= scoreLimitValue)
							{
								scoreLimitPlayer.add(roleId);
								hasAdd = true;
							}
							break;

						default:
							Macro.AssetFalse(false, "Invalid calcType : " + scoreLimitCalc);
					}
					// 如果已经添加就退出这个循环
					if (hasAdd)
						break;
				}
			}
		}

		// 遍历所有玩家
		for (PlayerInfo player : context.getPlayers().values())
		{
			// 获取这个玩家的马牌step
			Step horseStep = context.getCardHeap(player.getRoleId(), maFenType);
			// 没有马牌step，进入下一循环
			if (horseStep == null)
				continue;
			// 添加马牌信息到发送列表并存入cardHeap中，查看弃牌余牌用
			{
				Step step = new Step(player.getRoleId(), PlayType.DISPLAY_HORSE_CARD, horseStep.getCards());
				context.addCardHeap(step);
				controller.addDisplayOperations(step);
			}
			// 这个玩家赢的牌
			List<Byte> winCards = new ArrayList<>();
			// 这个玩家输的牌
			List<Byte> loseCards = new ArrayList<>();
			// 循环玩家的马牌
			for (byte horseCard : horseStep.getCards())
			{
				// 找到玩家对应的马牌
				int horseRoleId = BettingHorseHelper.getRoleIdByCard(context, huRoleIds.size() > 1 ? loseRoleIds.get(0) : context.getZhuang(), horseCard, isThreePlayer);
				// 设置玩家输赢分
				setScoreData(player, huRoleIds.contains(horseRoleId), loseRoleIds.contains(horseRoleId), horseRoleId, huRoleIds, loseRoleIds, horseCard);
				// 买中了赢的人
				if (huRoleIds.contains(horseRoleId))
					winCards.add(horseCard);
				// 买中了输的人
				if (loseRoleIds.contains(horseRoleId))
					loseCards.add(horseCard);
			}
			// 添加到发送列表
			{
				// 赢的
				Step step = new Step(player.getRoleId(), PlayType.DISPLAY_WIN_HORSE_CARD, winCards);
				context.addCardHeap(step);
				controller.addDisplayOperations(step);
			}
			{
				// 输的
				Step step = new Step(player.getRoleId(), PlayType.DISPLAY_LOSE_HORSE_CARD, loseCards);
				context.addCardHeap(step);
				controller.addDisplayOperations(step);
			}
			// 发送消息
			controller.sendDisplayOperations();
		}

		// 添加买马结束消息
		controller.addDisplayOperations(new Step(context.getZhuang(), PlayType.DISPLAY_HORSE_END));
		// 发送消息
		controller.sendDisplayOperations();
	}

	/**
	 * 设置玩家得分
	 * 
	 * @param player 当前检测的玩家
	 * @param isWin 是否买中胡牌玩家的马
	 * @param horseRoleId 买马中的玩家id
	 * @param huRoleIds 胡牌玩家id列表
	 * @param loseRoleIds 输牌晚间id列表
	 * @param horseCard 中的马牌
	 */
	private void setScoreData(PlayerInfo player, boolean isWin, boolean isLose, int horseRoleId, List<Integer> huRoleIds, List<Integer> loseRoleIds, byte horseCard)
	{
		// 如果没有买中输的人也没有买中赢的人就退出
		if (!isWin && !isLose)
			return;
		// 如果赢的人买中了输的人的马
		if ((isLose && huRoleIds.contains(player.getRoleId())))
			return;
		// 循环胡牌玩家
		for (int huRoleId : huRoleIds)
		{
			// 如果是买中了赢的人的马并且huRoleId不是买中的人的id
			if (isWin && horseRoleId != huRoleId)
				continue;
			// 获取胡牌玩家信息
			PlayerInfo huPlayer = context.getPlayerById(huRoleId);
			// 构造scoreData
			ScoreData horseData = new ScoreData();
			// 是否为自摸
			boolean isZiMo = false;
			// copy玩家的胡牌得分
			for (ScoreData data : huPlayer.getCards().getScoreDatas())
			{
				if (PlayType.isHuType(data.getPoints().get(0).getScoreType()))
				{
					isZiMo = data.getSourceId() == huRoleId;
					horseData.copyFrom(data);
				}
			}

			// 输的人买中了赢的人的马，并且是点炮
			if (isWin && !isZiMo && loseRoleIds.contains(player.getRoleId()))
				continue;

			// 删除掉忽略的分数
			horseData.getPoints().removeIf(point -> {
				if (ingoreTypes.contains(point.getScoreType()))
					return true;
				return false;
			});
			// 设置来源牌
			horseData.setSourceCard(horseCard);
			// 清空收分目标（和胡牌玩家的收分目标不一样）
			horseData.getScoreTargetList().clear();

			{
				// 添加一个马牌分
				ScorePoint horsePoint = new ScorePoint();
				horsePoint.setCalcType(ScoreCalculateType.TOTAL_MULTI_2ND.getValue());
				horsePoint.setScoreType(maFenType);
				horsePoint.setScoreValue(1);
				horseData.getPoints().add(horsePoint);
			}
			// 买中赢钱的人并且不是输的人或者是自摸
			if (isWin)
			{
				{
					// 添加一个胡牌分（分值暂时不设定，会在结算的时候添加进去）
					ScorePoint huHorsePoint = new ScorePoint();
					huHorsePoint.setCalcType(ScoreCalculateType.TOTAL_ADD.getValue());
					huHorsePoint.setScoreType(huMaFenType);
					// 添加到第一个位置，做个标记
					horseData.getPoints().add(0, huHorsePoint);
				}

				// 收分目标为所有输分的人，但要排除自己
				horseData.getScoreTargetList().addAll(loseRoleIds);
				// 删除掉自己和十倍不计分玩家
				horseData.getScoreTargetList().removeIf(roleId -> {
					if (roleId == player.getRoleId() || scoreLimitPlayer.contains(roleId))
						return true;
					return false;
				});
				horseData.setAddOperation(true);
				// 添加到玩家的分数中
				player.getCards().getScoreDatas().add(horseData);
			}
			// 买中输的人并且不是胡牌玩家并且中的买马玩家不是十倍不计分玩家
			else if (!huRoleIds.contains(player.getRoleId()) && !scoreLimitPlayer.contains(horseRoleId))
			{
				{
					// 添加一个被动胡牌分（分值暂时不设定，会在结算的时候添加进去）
					ScorePoint huHorsePoint = new ScorePoint();
					huHorsePoint.setCalcType(ScoreCalculateType.TOTAL_ADD.getValue());
					huHorsePoint.setScoreType(huBeMaFenType);
					// 添加到第一个位置，做个标记
					horseData.getPoints().add(0, huHorsePoint);
				}
				// 设置收分目标为自己
				horseData.getScoreTargetList().add(player.getRoleId());
				// 添加到胡牌的人身上
				huPlayer.getCards().getScoreDatas().add(horseData);
			}
		}
	}
}
