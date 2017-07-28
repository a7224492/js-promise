package com.kodgames.battleserver.service.battle.region.meizhou.huizhou.processor;

import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalculateType;
import com.kodgames.battleserver.service.battle.processer.Processer;

import net.sf.json.JSONObject;

/**
 * 马跟分积分翻倍<br>
 * 不选马跟分积分增加<br>
 * 马跟杠杠分翻倍
 */
public class Processor_JiangMa_HuiZhou extends Processer
{
	/**
	 * 玩家对应的奖马信息数组
	 */
	private static final byte[][] HOUSE_CARD_TYPE_LIST = {

		// 庄家：1、5、9 东 中
		{CardType.WAN.Value(), (byte)(CardType.WAN.Value() + 4), (byte)(CardType.WAN.Value() + 8), CardType.TIAO.Value(), (byte)(CardType.TIAO.Value() + 4), (byte)(CardType.TIAO.Value() + 8),
			CardType.TONG.Value(), (byte)(CardType.TONG.Value() + 4), (byte)(CardType.TONG.Value() + 8), CardType.ZI.Value(), (byte)(CardType.ZI.Value() + 4)},

		// 庄家的下家（逆时针）：2、6 南、发
		{(byte)(CardType.WAN.Value() + 1), (byte)(CardType.WAN.Value() + 5), (byte)(CardType.TIAO.Value() + 1), (byte)(CardType.TIAO.Value() + 5), (byte)(CardType.TONG.Value() + 1),
			(byte)(CardType.TONG.Value() + 5), (byte)(CardType.ZI.Value() + 1), (byte)(CardType.ZI.Value() + 5)},

		// 再下家：3、7 西、白
		{(byte)(CardType.WAN.Value() + 2), (byte)(CardType.WAN.Value() + 6), (byte)(CardType.TIAO.Value() + 2), (byte)(CardType.TIAO.Value() + 6), (byte)(CardType.TONG.Value() + 2),
			(byte)(CardType.TONG.Value() + 6), (byte)(CardType.ZI.Value() + 2), (byte)(CardType.ZI.Value() + 6)},

		// 再下家：4、8 北
		{(byte)(CardType.WAN.Value() + 3), (byte)(CardType.WAN.Value() + 7), (byte)(CardType.TIAO.Value() + 3), (byte)(CardType.TIAO.Value() + 7), (byte)(CardType.TONG.Value() + 3),
			(byte)(CardType.TONG.Value() + 7), (byte)(CardType.ZI.Value() + 3)}};

	public static final String KEY_MA_GEN_GANG = "maGenGang";
	public static final String KEY_MA_GEN_FEN = "maGenFen";

	/** 马跟分 */
	protected boolean maGenFen;

	/** 马跟杠 */
	protected boolean maGenGang;

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		maGenFen = CreateContextParser.getBoolean(context, KEY_MA_GEN_FEN);
		maGenGang = CreateContextParser.getBoolean(context, KEY_MA_GEN_GANG);
	}

	@Override
	public void start()
	{
		// 下发庄家信息
		bettingHorse();
		finish();
	}

	/**
	 * 过去指定玩家应该奖马的牌
	 */
	protected byte[] getHouseCardTypeList(int roleId)
	{
		int zhuangPlayerId = context.getZhuang();
		if (roleId == zhuangPlayerId)
			return HOUSE_CARD_TYPE_LIST[0];
		else if (roleId == context.getNextRoleId(zhuangPlayerId))
			return HOUSE_CARD_TYPE_LIST[1];
		else if (roleId == context.getOppositeRoleId(zhuangPlayerId))
			return HOUSE_CARD_TYPE_LIST[2];
		else
			return HOUSE_CARD_TYPE_LIST[3];
	}

	/**
	 * 奖马判断
	 */
	private void bettingHorse()
	{
		// 没有人胡， 不需要翻马
		if (context.getHuIndex().size() == 0)
			return;

		// 获取需要判断翻马的人
		PlayerInfo bettingPlayer = null;
		if (context.getHuIndex().size() == 1)
		{
			// 胡牌的人翻马
			Step step = context.getRecords().get(context.getHuIndex().get(0));
			bettingPlayer = context.getPlayers().get(step.getRoleId());
		}
		else
		{
			// 一杠多响的情况，是被抢杠的人翻马
			Step step = context.getRecords().get(context.getHuIndex().get(0));
			bettingPlayer = context.getPlayers().get(context.getScoreData(step.getRoleId(), context.getHuIndex().get(0)).getSourceId());
		}

		// 获取将马的数量
		int houseCardCount = context.getCardPool().getStayCount();

		// 翻指定数量的牌
		List<Byte> houseCards = new ArrayList<>();
		while (houseCardCount > 0)
		{
			houseCards.add(context.getCardPool().getCards().remove(0));
			--houseCardCount;
		}

		// 向客户端发送翻到的牌
		controller.addDisplayOperations(new Step(bettingPlayer.getRoleId(), PlayType.DISPLAY_DEAL_BETTING_HOUSE, houseCards));

		// 判断
		List<Byte> bettingCards = new ArrayList<>();
		byte[] playerBettingCards = getHouseCardTypeList(bettingPlayer.getRoleId());
		for (byte card : houseCards)
		{
			for (byte playerBettingCard : playerBettingCards)
			{
				if (playerBettingCard == card)
				{
					bettingCards.add(card);
					break;
				}
			}
		}

		// 向客户端发送中马的牌
		controller.addDisplayOperations(new Step(bettingPlayer.getRoleId(), PlayType.DISPLAY_BETTING_HOUSE, bettingCards));
		controller.sendDisplayOperations();

		context.getHuIndex().forEach(huIndex -> {
			// 这里获取到当前的胡牌玩家，是为了一炮多响时的操作
			Step huStep = context.getRecords().get(huIndex);
			PlayerInfo player = context.getPlayers().get(huStep.getRoleId());

			// 添加翻出的马牌到玩家的step中，发送马牌使用
			player.getCards().getCardHeap().add(new Step(player.getRoleId(), PlayType.DISPLAY_BETTING_HOUSE, houseCards));
		});

		// 什么都没有抽中，不用算翻
		if (bettingCards.size() == 0)
			return;

		// 将翻倍参数加到每个胡牌人身上, 由于马与杠上花等属于连乘的关系, 将马的分数计算类型与之分开
		context.getHuIndex().forEach(huIndex -> {

			// 这里获取到当前的胡牌玩家，是为了一炮多响时的操作
			Step huStep = context.getRecords().get(huIndex);
			PlayerInfo player = context.getPlayers().get(huStep.getRoleId());

			ScorePoint point = new ScorePoint();
			if (maGenFen)
			{
				// 马跟分积分翻倍
				point.setScoreType(MahjongConstant.PlayType.DISPLAY_BETTING_HOUSE);
				point.setCalcType(ScoreCalculateType.TOTAL_MULTI.getValue());
				point.setScoreValue(bettingCards.size() + 1);
			}
			else
			{
				// 积分增加
				point.setScoreType(MahjongConstant.PlayType.DISPLAY_BETTING_HOUSE);
				point.setCalcType(ScoreCalculateType.TOTAL_ADD.getValue());
				point.setScoreValue(bettingCards.size() * 2);
			}

			// 添加到胡牌分数
			for (ScoreData scoreData : player.getCards().getScoreDatas())
			{
				// 胡牌分数中奖马直接放在scoreData中就会在结算的时候进行计算
				if (PlayType.isHuType(scoreData.getPoints().get(0).getScoreType()))
				{
					scoreData.getPoints().add(new ScorePoint(point));
					break;
				}
			}
			if (maGenGang && maGenFen)
			{
				// 添加到杠牌分数
				for (ScoreData scoreData : player.getCards().getScoreDatas())
				{
					if (PlayType.isGangOperator(scoreData.getPoints().get(0).getScoreType()))
					{
						// 积分加倍
						ScorePoint gangPoint = new ScorePoint();
						gangPoint.copyFrom(point);

						// 让客户端可以在结算消息中获得中的马
						scoreData.getPoints().add(gangPoint);
					}
				}
			}
		});
	}
}
