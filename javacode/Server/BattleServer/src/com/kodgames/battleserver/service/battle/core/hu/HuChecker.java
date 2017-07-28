package com.kodgames.battleserver.service.battle.core.hu;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.check.CheckHelper;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.creator.ICreateContextHandler;
import com.kodgames.battleserver.service.battle.core.hu.data.HuCardGroup;
import com.kodgames.battleserver.service.battle.core.hu.data.HuCheckerMemoryPool;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationResultFilter;

import net.sf.json.JSONObject;

/**
 * 检测是否可以和牌
 */
public class HuChecker implements ICreateContextHandler
{
	public static final String KEY_FILTERS = "operationResultFilters";
	public static final String KEY_CHECKERS = "checkers";

	protected List<OperationResultFilter> operationResultFilters = new ArrayList<>();
	protected List<HuCheckerBase> checkers = new ArrayList<>();

	public static HuChecker create(JSONObject context)
		throws Exception
	{
		HuChecker instance = CreateContextHelper.instantiateClass(context, HuChecker.class);
		instance.createFromContext(context);
		return instance;
	}

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		// 构造内容

		// Key_filters可以不配置
		if (CreateContextParser.containsKey(context, KEY_FILTERS))
			for (JSONObject subContext : CreateContextParser.getJSONArray(context, KEY_FILTERS))
				operationResultFilters.add(OperationResultFilter.create(subContext));

		// checker必须配置
		for (JSONObject subContext : CreateContextParser.getJSONArray(context, KEY_CHECKERS))
			checkers.add(HuCheckerBase.create(subContext));
	}

	/**
	 * 获取checker
	 * 
	 * @param scoreType
	 * @return
	 */
	public HuCheckerBase getCheckerByScoreType(int scoreType)
	{
		for (HuCheckerBase checkerBase : checkers)
		{
			if (checkerBase.hasScoreType(scoreType))
				return checkerBase;
		}

		return null;
	}

	/**
	 * 检测和牌
	 * 
	 * @param context 上下文
	 * @param roleId 要检测的玩家Id
	 * @param card 要检测的牌
	 * @param phaseDeal 当前是否为抓牌阶段, 在抓牌阶段card已经加入到玩家手牌
	 * @param pool 内存池
	 * @return 是否可以和牌
	 */
	public boolean check(BattleBean context, int roleId, byte card, boolean phaseDeal, HuCheckerMemoryPool pool)
	{
		// 构造和牌检测操作, 过去和牌行为
		Step operation = new Step(roleId, PlayType.OPERATE_CAN_HU, card);
		for (OperationResultFilter filter : operationResultFilters)
			if (filter.filter(context, operation, card, phaseDeal) == false)
				return false;

		// 检测每个check是否可以和牌
		byte[] cardCountArray = CheckHelper.converToCardCountArray(context.getPlayers().get(roleId).getCards());

		// 检查有多少张万能牌,并移除手牌中的万能牌
		List<Byte> masterCards = context.getPlayerMasterCards(roleId);
		int masterCardCount = 0;
		if (masterCards != null && masterCards.size() != 0)
		{
			for (byte i = 0; i < cardCountArray.length; ++i)
			{
				if (cardCountArray[i] == 0)
					continue;

				if (masterCards.contains(i) == false)
					continue;

				// 如果是万能牌， 从卡牌数量队列中删除，添加到万能牌计数中
				masterCardCount += cardCountArray[i];
				cardCountArray[i] = 0;
			}
		}

		// 检查有多少已经固定的牌形
		int fixedTileCount = 0;
		for (Step step : context.getPlayers().get(roleId).getCards().getCardHeap())
			if (PlayType.isChiPengGang(step.getPlayType()))
				fixedTileCount++;

		// 防止打出的鬼牌做鬼，鬼牌只有在手牌中才有用
		if (!phaseDeal)
			cardCountArray[card]++;

		for (HuCheckerBase checker : checkers)
			if (checker.check(context, roleId, cardCountArray, fixedTileCount, masterCardCount, context.getCardPool().getSupportedCardTypes(), pool, null, false, 1))
				return true;

		return false;
	}

	/**
	 * 检测停牌, 支持获取一向听
	 * 
	 * @param context 上下文
	 * @param roleId 要检测的玩家Id
	 * @param card 要检测的牌
	 * @param phaseDeal 当前是否为抓牌阶段, 在抓牌阶段card已经加入到玩家手牌
	 * @param pool 内存池
	 * @param tingCards 如果不为null, 获取一向听
	 * @return 是否可以听牌
	 */
	public boolean checkTing(BattleBean context, int roleId, byte card, boolean phaseDeal, HuCheckerMemoryPool pool, List<Byte> tingCards, List<List<HuCardGroup>> outHuCardGroupCollector)
	{
		// 构造和牌检测操作, 过去和牌行为
		Step operation = new Step(roleId, PlayType.OPERATE_CAN_HU, card);
		for (OperationResultFilter filter : operationResultFilters)
			if (filter.filter(context, operation, card, phaseDeal) == false)
				return false;

		// 检测每个check是否可以和牌
		byte[] cardCountArray = CheckHelper.converToCardCountArray(context.getPlayers().get(roleId).getCards());

		// 检查有多少张万能牌,并移除手牌中的万能牌
		List<Byte> masterCards = context.getPlayerMasterCards(roleId);
		int masterCardCount = 0;
		if (masterCards != null && masterCards.size() != 0)
		{
			for (byte i = 0; i < cardCountArray.length; ++i)
			{
				if (cardCountArray[i] == 0)
					continue;

				if (masterCards.contains(i) == false)
					continue;

				// 如果是万能牌， 从卡牌数量队列中删除，添加到万能牌计数中
				masterCardCount += cardCountArray[i];
				cardCountArray[i] = 0;
			}
		}

		// 检查有多少已经固定的牌形
		int fixedTileCount = 0;
		for (Step step : context.getPlayers().get(roleId).getCards().getCardHeap())
			if (PlayType.isChiPengGang(step.getPlayType()))
				fixedTileCount++;

		// 防止打出的鬼牌做鬼，鬼牌只有在手牌中才有用
		if (!phaseDeal)
			cardCountArray[card]++;

		if (tingCards == null && outHuCardGroupCollector == null)
		{
			// 不用获取具体可以胡的牌
			for (HuCheckerBase checker : checkers)
				if (checker.check(context, roleId, cardCountArray, fixedTileCount, masterCardCount, context.getCardPool().getSupportedCardTypes(), pool, null, false, 1))
					return true;

			return false;
		}
		else
		{
			for (HuCheckerBase checker : checkers)
			{
				// 获取一向听
				List<List<HuCardGroup>> huCardGroupCollector = pool.allocHuCardGroupListList();
				try
				{
					checker.check(context, roleId, cardCountArray, fixedTileCount, masterCardCount, context.getCardPool().getSupportedCardTypes(), pool, huCardGroupCollector, tingCards != null, 0);

					if (huCardGroupCollector.size() == 0)
						continue;

					if (outHuCardGroupCollector != null)
					{
						// 拷贝牌型组
						for (List<HuCardGroup> cardGroups : huCardGroupCollector)
						{
							List<HuCardGroup> newCardGroups = new ArrayList<>();
							outHuCardGroupCollector.add(newCardGroups);
							for (HuCardGroup cardGroup : cardGroups)
							{
								newCardGroups.add(new HuCardGroup(cardGroup));
							}
						}
					}

					if (tingCards != null)
					{
						for (List<HuCardGroup> cardGroups : huCardGroupCollector)
						{
							// 检测是否有空位置
							for (HuCardGroup cardGroup : cardGroups)
							{
								int emptyIndex = 0;
								for (; emptyIndex < cardGroup.cardList.length; ++emptyIndex)
									if (cardGroup.cardList[emptyIndex] == 0)
										break;

								if (emptyIndex == cardGroup.cardList.length)
									continue;

								// 进行组的填充
								checker.fillTingGroups(cardGroups, cardGroup, emptyIndex, tingCards);

								// 一向听, 不用再算同一牌形了
								break;
							}
						}
					}
				}
				finally
				{
					// 回收内存
					pool.deallocListList(huCardGroupCollector);
					huCardGroupCollector = null;
				}
			}
			
			// 过滤重复听牌
			if(tingCards != null)
				tingCards = new ArrayList<Byte>(new HashSet<Byte>(tingCards));
			
			return true;
		}
	}
}