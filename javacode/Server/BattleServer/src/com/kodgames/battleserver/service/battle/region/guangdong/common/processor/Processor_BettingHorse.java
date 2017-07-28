package com.kodgames.battleserver.service.battle.region.guangdong.common.processor;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalculateType;
import com.kodgames.battleserver.service.battle.processer.Processer;

/**
 * 广东奖马
 */
public class Processor_BettingHorse extends Processer
{
	public static final String KEY_IS_THREE_PLAYER = "Key_isThreePlayer";
	public static final String KEY_APPLY_TO_GANG = "applytoGang";
	public static final String KEY_YI_PAO_DUO_XIANG_NEED_MA = "key_yiPaoDuoXiangNeedMa";

	/**
	 * 马跟杠
	 */
	protected boolean applytoGang;

	/**
	 * 三人玩法
	 */
	protected boolean isThreePlayer = false;

	/**
	 * 一炮多响时是否翻马
	 */
	protected boolean duoXiangNeedMa = true;

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		applytoGang = CreateContextParser.getBoolean(context, KEY_APPLY_TO_GANG);
		isThreePlayer = CreateContextParser.getBoolean(context, KEY_IS_THREE_PLAYER);
		if (context.containsKey(KEY_YI_PAO_DUO_XIANG_NEED_MA))
			duoXiangNeedMa = CreateContextParser.getBoolean(context, KEY_YI_PAO_DUO_XIANG_NEED_MA);
	}

	@Override
	public void start()
	{
		// 下发庄家信息
		bettingHorse();
		finish();
	}

	/**
	 * 获取指定玩家应该奖马的牌
	 */
	protected byte[] getHouseCardTypeList(int roleId)
	{
		return BettingHorseHelper.getHouseCardTypeList(context, roleId, context.getZhuang(), isThreePlayer);
	}

	/**
	 * 奖马判断
	 */
	private void bettingHorse()
	{
		// 没有人胡， 不需要翻马
		if (context.getHuIndex().size() == 0)
			return;

		// 获取需要判断拿马的人
		PlayerInfo bettingPlayer = null;
		if (context.getHuIndex().size() == 1)
		{
			// 胡牌的人拿马
			Step step = context.getRecords().get(context.getHuIndex().get(0));
			bettingPlayer = context.getPlayers().get(step.getRoleId());
		}
		else
		{
			// 一炮多响需要翻马
			if (duoXiangNeedMa)
			{
				// 一炮多响的情况，是放炮的人拿马
				Step step = context.getRecords().get(context.getHuIndex().get(0));
				bettingPlayer = context.getPlayers().get(context.getScoreData(step.getRoleId(), context.getHuIndex().get(0)).getSourceId());
			}
			else
				return;
		}

		// 获取赌马的数量
		int houseCardCount = context.getCardPool().getStayCount();

		// 翻制定数量的牌
		List<Byte> houseCards = new ArrayList<>();
		while (houseCardCount > 0)
		{
			houseCards.add(context.getCardPool().getCards().remove(0));
			--houseCardCount;
		}

		// 向客户端发送翻到的牌并存到cardheap中，弃牌余牌用
		{
			Step dealBettingHourseStep = new Step(bettingPlayer.getRoleId(), PlayType.DISPLAY_DEAL_BETTING_HOUSE, houseCards);
			context.addCardHeap(dealBettingHourseStep);
			controller.addDisplayOperations(dealBettingHourseStep);
		}

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

		// 向客户端发送中马的牌并添加中的马牌
		{
			Step bettingHouseStep = new Step(bettingPlayer.getRoleId(), PlayType.DISPLAY_BETTING_HOUSE, bettingCards);
			context.addCardHeap(bettingHouseStep);
			controller.addDisplayOperations(bettingHouseStep);
		}
		controller.sendDisplayOperations();

		// 什么都没有抽中，不用算翻
		if (bettingCards.size() == 0)
			return;

		// 将翻倍参数加到每个胡牌人身上, 由于马与杠上花等属于连乘的关系, 将马的分数计算类型与之分开
		context.getHuIndex().forEach(huIndex -> {

			// 这里获取到当前的胡牌玩家，是为了一炮多响时的操作
			Step huStep = context.getRecords().get(huIndex);
			PlayerInfo player = context.getPlayers().get(huStep.getRoleId());

			// 积分加倍
			ScorePoint point = new ScorePoint();
			point.setScoreType(MahjongConstant.PlayType.DISPLAY_BETTING_HOUSE);
			point.setCalcType(ScoreCalculateType.TOTAL_MULTI_2ND.getValue());
			point.setScoreValue(bettingCards.size() + 1);

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
			if (applytoGang)
			{
				// 添加到杠牌分数
				for (ScoreData scoreData : player.getCards().getScoreDatas())
				{
					if (PlayType.isGangOperator(scoreData.getPoints().get(0).getScoreType()))
					{
						// 积分加倍
						ScorePoint gangPoint = new ScorePoint();
						gangPoint.setScoreType(MahjongConstant.PlayType.DISPLAY_BETTING_HOUSE);
						gangPoint.setCalcType(ScoreCalculateType.TOTAL_MULTI_2ND.getValue());
						gangPoint.setScoreValue(bettingCards.size() + 1);

						// 让客户端可以在结算消息中获得中的马
						scoreData.getPoints().add(gangPoint);
					}
				}
			}
		});
	}
}
