package com.kodgames.battleserver.service.battle.region.meizhou.heyuan.score.hu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardInfo;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.BattleHelper;
import com.kodgames.battleserver.service.battle.core.MahjongHelper;
import com.kodgames.battleserver.service.battle.core.check.CheckHelper;
import com.kodgames.battleserver.service.battle.core.hu.data.HuCheckerMemoryPool;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalcluateContext;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalculateType;
import com.kodgames.battleserver.service.battle.core.score.battle.BattleScoreCalculator;
import com.kodgames.battleserver.service.battle.core.score.filter.ScoreSourceFilter;
import com.kodgames.battleserver.service.battle.core.score.filter.ScoreTargetFilter;
import com.kodgames.battleserver.service.battle.core.score.hu.HuMainScoreChecker;
import com.kodgames.battleserver.service.battle.core.score.hu.HuScoreChecker;
import com.kodgames.battleserver.service.battle.core.score.hu.HuScoreProcessor;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;
import com.kodgames.battleserver.service.battle.region.meizhou.heyuan.score.battle.BattleScoreCalculator_HeYuan;

/**
 * 河源胡牌分数处理器
 */
public class HuScoreProcessor_HeYuan extends HuScoreProcessor

{

	/**
	 * 计算当前胡牌分数, 产生一个ScoreData保存在PlayerInfo中
	 * 
	 * @param context 牌局上下文
	 * @param roleId 检测胡牌的玩家Id
	 * @param huCard 要胡的Card
	 * @param isSelfOperation 当前胡牌行为是否
	 * @param checkTing 当前是检测听牌
	 * @return 如果有分数返回分数数据, 如果没有返回null
	 */
	public ScoreData process(BattleBean context, int roleId, byte huCard, boolean checkTing)
	{
		PlayerInfo playerInfo = context.getPlayers().get(roleId);

		/*
		 * 获取分数来源者
		 */
		PlayerInfo sourcePlayer = null;
		for (ScoreSourceFilter filter : sourcePlayerFilters)
		{
			for (PlayerInfo player : context.getPlayers().values())
			{
				if (filter.filter(context, player) == false)
					continue;

				sourcePlayer = player;
				break;
			}

			if (sourcePlayer != null)
				break;
		}

		if (Macro.AssetTrue(sourcePlayer == null, "Missing source player"))
			return null;

		/*
		 * 计算收取分数的目标
		 */
		List<Integer> targetList = new ArrayList<>();
		for (PlayerInfo player : context.getPlayers().values())
		{
			// 所有过滤器都通过才满足目标
			boolean meetCondition = true;
			for (ScoreTargetFilter filter : scoreTargetFilters)
			{
				if (filter.filter(context, playerInfo, sourcePlayer, player))
					continue;

				meetCondition = false;
				break;
			}

			if (meetCondition)
				targetList.add(player.getRoleId());
		}

		/*
		 * 检测基本类型, 基本类型不需要关心分数来源和目标
		 */
		byte[] cardCountList = CheckHelper.converToCardCountArray(playerInfo.getCards());
		int masterCardCount = getMasterCount(context, roleId, cardCountList);

		// 防止在算分的时候出现打出去的鬼牌做鬼
		if (huCard > 0)
			cardCountList[huCard]++;

		/**
		 * 胡牌分数检测
		 */
		long checkTime = System.currentTimeMillis();

		// 构造用于算分的内存池
		HuCheckerMemoryPool pool = new HuCheckerMemoryPool();
		// 胡牌结果
		HuScoreCheckContext returnHuData = null;
		try
		{
			HuScoreCheckContext maxScoreHuDataInAllChecker = null;
			// 获取当前战斗的BattleScoreCalculator的实例
			BattleScoreCalculator scoreCalculator = BattleHelper.getInstance().getBattleScoreCalculator();
			// 计算各种胡牌牌形分数
			for (HuMainScoreChecker mainChecker : mainScoreCheckers)
			{
				if (checkTing && mainChecker.skipCheckTing())
					continue;

				// 先检测基本牌形
				long subCheckTime = System.currentTimeMillis();
				List<HuScoreCheckContext> huDataContextList = new ArrayList<>();
				HuScoreCheckContext maxScoreHuDataInOneChecker = null;
				try
				{
					if (mainChecker.calculate(context, roleId, cardCountList, masterCardCount, context.getCardPool().getSupportedCardTypes(), pool, huDataContextList) == false)
						continue;

					// 计算所有牌形分数
					for (HuScoreCheckContext checkContext : huDataContextList)
					{
						// 设置基本数据
						checkContext.scoreData.setAddOperation(true);
						checkContext.scoreData.setSourceId(sourcePlayer.getRoleId());
						checkContext.scoreData.getScoreTargetList().addAll(targetList);
						checkContext.scoreData.setSourceCard(huCard);
						checkContext.scoreData.setSourceRecrodIndex(context.getRecords().size() - 1); // sourceId 的操作索引

						// 获取附加牌形分数
						for (HuSubScoreChecker subChecker : additionalScoreCheckers)
						{
							if (checkTing && subChecker.skipCheckTing())
								continue;

							subChecker.calculate(context, playerInfo.getRoleId(), checkContext);
						}

						// 互斥剔除分数
						for (HuScoreChecker checker : allScoreCheckers.values())
						{
							// 检查得分中是否有此类型分数
							boolean hasScore = false;
							for (ScorePoint point : checkContext.scoreData.getPoints())
							{
								if (point.getScoreType() == checker.getScoreType())
								{
									hasScore = true;
									break;
								}
							}

							if (hasScore == false)
								continue;

							// 删除与这个类型互斥的分数
							for (int mutexScoreType : checker.getMutexScoreTypes())
							{
								// 查找是否有这个分数
								for (int i = 0; i < checkContext.scoreData.getPoints().size(); ++i)
								{
									ScorePoint point = checkContext.scoreData.getPoints().get(i);
									if (point.getScoreType() == mutexScoreType)
									{
										// 删除
										checkContext.scoreData.getPoints().remove(i);
										break;
									}
								}
							}
						}

						// 计算总分
						checkContext.calculatedScore = sumScore(checkContext.scoreData);

						// 使用最大分数的胡数据
						if (maxScoreHuDataInOneChecker == null || scoreCalculator.compairScoreContext(maxScoreHuDataInOneChecker, checkContext) < 0)
							maxScoreHuDataInOneChecker = checkContext;
					}
				}
				finally
				{
					long processTime = System.currentTimeMillis() - subCheckTime;
					logger.debug("HuMainScoreChecker检测时间:{}", processTime);
					if (processTime > 100)
						logger.warn(MahjongHelper.getPlayerCardDesc(context, roleId, "HuMainScoreChecker检测时间：" + processTime));
				}

				// 释放内存
				for (HuScoreCheckContext checkContext : huDataContextList)
					// 先不释放最高分数, 后面需要进行比较
					if (maxScoreHuDataInOneChecker != checkContext)
						pool.deallocHuScoreCheckContext(checkContext);
				huDataContextList.clear();

				// 与之前他的MainChecker比较
				if (maxScoreHuDataInOneChecker != null)
				{
					if (maxScoreHuDataInAllChecker == null || scoreCalculator.compairScoreContext(maxScoreHuDataInAllChecker, maxScoreHuDataInOneChecker) < 0)
					{
						// 释放之前的分数
						pool.deallocHuScoreCheckContext(maxScoreHuDataInAllChecker);
						// 保存新的最高分
						maxScoreHuDataInAllChecker = maxScoreHuDataInOneChecker;
					}
					else
					{
						// 不是最高分, 直接释放掉
						pool.deallocHuScoreCheckContext(maxScoreHuDataInOneChecker);
					}
				}
			}

			if (maxScoreHuDataInAllChecker != null)
			{
				// 复制一份最高分, 用于返回, 现有的释放到pool
				returnHuData = new HuScoreCheckContext();
				returnHuData.copyFrom(maxScoreHuDataInAllChecker);
				pool.deallocHuScoreCheckContext(maxScoreHuDataInAllChecker);
			}
		}
		finally
		{
			// 释放内存池
			pool.relase();

			long processTime = System.currentTimeMillis() - checkTime;
			logger.debug("HuScoreProcessor检测时间:{}", processTime);
			if (processTime > 100)
				logger.warn(MahjongHelper.getPlayerCardDesc(context, roleId, "HuScoreProcessor检测时间：" + processTime));
		}

		if (Macro.AssetTrue(returnHuData == null))
		{
			CardInfo cardInfo = playerInfo.getCards();
			byte[] allCards = CheckHelper.converToAllCardCountArray(cardInfo);
			logger.error("HuData null : allCards={}", allCards);
			return null;
		}
		if (Macro.AssetTrue(returnHuData.scoreData.getPoints().size() == 0))
			return null;
		return returnHuData.scoreData;
	}

	private ScoreCalcluateContext sumScore(ScoreData scoreData)
	{
		ScoreCalcluateContext calcContext = new ScoreCalcluateContext();
		calcContext.isHuScore = PlayType.isHuType(scoreData.getPoints().get(0).getScoreType());
		if (calcContext.isHuScore)
		{
			Collections.sort(scoreData.getPoints(), BattleScoreCalculator_HeYuan.getHuComparator());
		}

		scoreData.getPoints().forEach((scorePoint) -> {
			ScoreCalculateType type = ScoreCalculateType.getType(scorePoint.getCalcType());
			switch (type)
			{
				case TOTAL_ADD:
					calcContext.totalAdd += scorePoint.getScoreValue();
					break;
				case TOTAL_MULTI:
					calcContext.totalAdd *= scorePoint.getScoreValue();
					break;
				default:
					Macro.AssetTrue(true, "calculateScore : unsupported type -> " + type);
			}
		});

		return calcContext;
	}

}
