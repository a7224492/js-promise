package com.kodgames.battleserver.service.battle.core.check;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardGroup;
import com.kodgames.battleserver.service.battle.common.xbean.CardInfo;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.BattleHelper;
import com.kodgames.battleserver.service.battle.core.MahjongHelper;
import com.kodgames.battleserver.service.battle.core.hu.CardGroupType;
import com.kodgames.battleserver.service.battle.core.hu.HuChecker;
import com.kodgames.battleserver.service.battle.core.hu.data.HuCardGroup;
import com.kodgames.battleserver.service.battle.core.hu.data.HuCheckerMemoryPool;

public class CheckHelper
{
	final static Logger logger = LoggerFactory.getLogger(CheckHelper.class);

	/**
	 * 258将牌
	 * 
	 * 必须按照从小打到排列
	 */
	public static final byte[] CARD_258_JIANG = {(byte)(CardType.WAN.Value() + 1), (byte)(CardType.WAN.Value() + 4), (byte)(CardType.WAN.Value() + 7), // 258万
		(byte)(CardType.TIAO.Value() + 1), (byte)(CardType.TIAO.Value() + 4), (byte)(CardType.TIAO.Value() + 7), // 258条
		(byte)(CardType.TONG.Value() + 1), (byte)(CardType.TONG.Value() + 4), (byte)(CardType.TONG.Value() + 7), // 258桶
	};

	/**
	 * 幺牌：即幺九牌。（一九万一九饼一九条以及字牌）
	 * 
	 * 必须按照从小打到排列
	 */
	public static final byte[] CARD_YAO = {CardType.WAN.Value(), (byte)(CardType.WAN.Value() + 8), // 一九万
		CardType.TIAO.Value(), (byte)(CardType.TIAO.Value() + 8), // 一九条
		CardType.TONG.Value(), (byte)(CardType.TONG.Value() + 8), // 一九桶
		CardType.ZI.Value(), (byte)(CardType.ZI.Value() + 1), (byte)(CardType.ZI.Value() + 2), (byte)(CardType.ZI.Value() + 3), (byte)(CardType.ZI.Value() + 4), (byte)(CardType.ZI.Value() + 5),
		(byte)(CardType.ZI.Value() + 6) // 东南西北中发白
	};

	/**
	 * 构造用于检测的数组, 只记录可以打出的牌
	 */
	public static byte[] converToCardCountArray(CardInfo cardInfo)
	{
		byte[] checkPai = new byte[CardType.TOTAL_COUNT.Value()];
		for (byte card : cardInfo.getHandCards())
			checkPai[card]++;
		return checkPai;
	}

	/**
	 * 构造用于检测的数组, 记录所有的牌
	 */
	public static byte[] converToAllCardCountArray(CardInfo cardInfo)
	{
		byte[] checkPai = converToCardCountArray(cardInfo);

		// 添加CardHeap中的牌
		cardInfo.getCardHeap().forEach((step) -> {
			if (CardGroupType.isCardGroupType(step.getPlayType()))
			{
				step.getCards().forEach((card) -> {
					checkPai[card]++;
				});
			}
		});

		return checkPai;
	}

	/**
	 * 获取玩家所有牌的信息
	 * 
	 * @param cardInfo
	 * @param includeHandCard 是否包含手牌
	 * @param includeCardHeap 是否包含碰杠吃等
	 * @param includeOutCard 是否包含打出去的牌
	 * @return
	 */
	public static byte[] convertToAllCardCountArray(CardInfo cardInfo, boolean includeHandCard, boolean includeCardHeap, boolean includeOutCard)
	{
		byte[] cardPai = new byte[CardType.TOTAL_COUNT.Value()];

		// 添加手牌
		if (includeHandCard)
		{
			for (byte card : cardInfo.getHandCards())
				cardPai[card]++;
		}

		// 添加CardHeap中的牌
		if (includeCardHeap)
		{
			cardInfo.getCardHeap().forEach((step) -> {
				if (CardGroupType.isCardGroupType(step.getPlayType()))
				{
					step.getCards().forEach((card) -> {
						cardPai[card]++;
					});
				}
			});
		}

		// 添加出牌
		if (includeOutCard)
		{
			for (byte card : cardInfo.getOutCards())
				cardPai[card]++;
		}

		return cardPai;
	}

	/**
	 * 检测门前清
	 */
	public static boolean check_MenQianQing(List<CardGroup> cardGroups)
	{
		for (CardGroup cardGroup : cardGroups)
		{
			if (CardGroupType.isMingType(cardGroup.getGroupType()))
				return false;
		}

		return true;
	}

	/**
	 * 判断当前是否为自模和
	 */
	public static boolean checkHu_ZiMo(BattleBean context, int roleId)
	{
		// 判断最后一个step是否是自己摸牌
		Step lastStep = context.getRecords().size() != 0 ? context.getRecords().get(context.getRecords().size() - 1) : null;
		if (lastStep == null)
			return false;
		return lastStep.getRoleId() == roleId && lastStep.getPlayType() == PlayType.OPERATE_DEAL;
	}

	/**
	 * 获取一向听
	 */
	public static List<Byte> getTingCards(BattleBean context, int roleId)
	{
		long checkTime = System.currentTimeMillis();
		HuCheckerMemoryPool pool = new HuCheckerMemoryPool();
		List<Byte> tingCards = new ArrayList<>();
		try
		{
			HuChecker huCheckProcessor = BattleHelper.getInstance().getHuCheckProcessor();
			huCheckProcessor.checkTing(context, roleId, (byte)0, true, pool, tingCards, null);
		}
		finally
		{
			// 释放内存池
			if (pool.relase() == false)
				logger.warn(MahjongHelper.getPlayerCardDesc(context, roleId, "内存释放错误"));

			long processTime = System.currentTimeMillis() - checkTime;
			logger.debug("getTingCards检测时间:{}", processTime);
			if (processTime > 100)
				logger.warn(MahjongHelper.getPlayerCardDesc(context, roleId, "getTingCards检测时间：" + processTime));

		}

		return tingCards.stream().distinct().collect(Collectors.toList());
	}

	/**
	 * 获取一向听, 牌型
	 */
	public static List<List<HuCardGroup>> getTingGroups(BattleBean context, int roleId)
	{
		long checkTime = System.currentTimeMillis();
		HuCheckerMemoryPool pool = new HuCheckerMemoryPool();
		List<List<HuCardGroup>> tingGroups = new ArrayList<>();
		try
		{
			HuChecker huCheckProcessor = BattleHelper.getInstance().getHuCheckProcessor();
			huCheckProcessor.checkTing(context, roleId, (byte)0, true, pool, null, tingGroups);
		}
		finally
		{
			// 释放内存池
			if (pool.relase() == false)
				logger.warn(MahjongHelper.getPlayerCardDesc(context, roleId, "内存释放错误"));

			long processTime = System.currentTimeMillis() - checkTime;
			logger.debug("getTingCards检测时间:{}", processTime);
			if (processTime > 100)
				logger.warn(MahjongHelper.getPlayerCardDesc(context, roleId, "getTingCards检测时间：" + processTime));

		}

		return tingGroups;
	}

	/**
	 * 手牌为满的情况, 检测听牌
	 */
	public static boolean checkTing(BattleBean context, int roleId)
	{
		long checkTime = System.currentTimeMillis();
		HuCheckerMemoryPool pool = new HuCheckerMemoryPool();
		try
		{
			HuChecker huCheckProcessor = BattleHelper.getInstance().getHuCheckProcessor();
			return huCheckProcessor.checkTing(context, roleId, (byte)0, true, pool, null, null);
		}
		finally
		{
			// 释放内存池
			pool.relase();

			long processTime = System.currentTimeMillis() - checkTime;
			logger.debug("checkTing检测时间:{}", processTime);
			if (processTime > 100)
				logger.warn(MahjongHelper.getPlayerCardDesc(context, roleId, "checkTing检测时间：" + processTime));
		}
	}

	/**
	 * 获取玩家当前有多少种花色的牌
	 * 
	 * 包含手牌和吃碰杠
	 */
	public static int getPlayerCardTypeCount(PlayerInfo player)
	{
		return getPlayerCardTypeCount(player, (byte)0);
	}

	public static int getPlayerCardTypeCount(PlayerInfo player, byte dealCard)
	{
		// 从手牌获取
		int[] typeCountArray = new int[CardType.getTypeCount()];
		for (byte card : player.getCards().getHandCards())
			typeCountArray[CardType.getCardType(card).getTypeIndex()]++;

		if (dealCard != 0)
			typeCountArray[CardType.getCardType(dealCard).getTypeIndex()]++;
		// 从CardHeap获取
		for (Step step : player.getCards().getCardHeap())
		{
			if (!PlayType.isChiPengGang(step.getPlayType()))
				continue;
			for (byte card : step.getCards())
				typeCountArray[CardType.getCardType(card).getTypeIndex()]++;
		}

		int typeCount = 0;
		for (int count : typeCountArray)
			if (count != 0)
				typeCount++;

		return typeCount;
	}
}
