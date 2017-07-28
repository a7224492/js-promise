package com.kodgames.battleserver.service.battle.region.guangdong.shanwei.processer;

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
import com.kodgames.battleserver.service.battle.region.guangdong.common.processor.Processor_BettingHorse;

/**
 * 汕尾奖马
 */
public class Processer_BettingHorse_ShanWei extends Processor_BettingHorse
{
	public static final String KEY_JIANG_MA_JIA_FAN = "Key_jiangMaJiaFan";

	public static final String KEY_JIANG_MA_FEN = "Key_jiangMaFen";

	/**
	 * 奖马加番（默认奖马加倍）
	 */
	private boolean jiangMaJiaFan = false;

	/**
	 * 奖马加番时的奖马分值
	 */
	private int jiangMaFen = 0;

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		super.createFromContext(context);
		jiangMaJiaFan = CreateContextParser.getBoolean(context, KEY_JIANG_MA_JIA_FAN);
		// 如果有奖马加番，在进行不配置奖马分值
		if (jiangMaJiaFan)
			jiangMaFen = CreateContextParser.getInt(context, KEY_JIANG_MA_FEN);
	}

	@Override
	public void start()
	{
		// 奖马判断
		bettingHorse();
		finish();
	}

	/**
	 * 奖马判断
	 */
	protected void bettingHorse()
	{
		// 没有人胡或者一炮多响， 不需要翻马
		if (context.getHuIndex().size() == 0 || context.getHuIndex().size() > 1)
			return;

		// 获取需要判断拿马的人
		PlayerInfo bettingPlayer = null;
		// 胡牌的人拿马
		Step step = context.getRecords().get(context.getHuIndex().get(0));
		bettingPlayer = context.getPlayers().get(step.getRoleId());

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

		// 判断玩家翻中了几个马
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

		// 向客户端发送中马的牌并存到cardheap中，弃牌余牌用
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

			if (jiangMaJiaFan)
			{
				// 创建一个奖马的scoreData
				ScoreData data = new ScoreData();
				for (ScoreData scoreData : player.getCards().getScoreDatas())
				{
					// 如果是胡牌分，就copy一份赋给奖马分的scoreData
					if (PlayType.isHuType(scoreData.getPoints().get(0).getScoreType()))
					{
						data.copyFrom(scoreData);
					}
				}

				// 删除除了抢杠胡之外的分
				data.getPoints().removeIf(point -> {
					if (point.getScoreType() == PlayType.HU_QIANG_GANG_HU)
						return false;

					return true;
				});

				data.getCardGroups().clear();

				{
					// 奖马分值
					ScorePoint valuePoint = new ScorePoint();
					valuePoint.setScoreType(MahjongConstant.PlayType.DISPLAY_BETTING_HOUSE_VALUE);
					valuePoint.setCalcType(ScoreCalculateType.TOTAL_MULTI_2ND.getValue());
					valuePoint.setScoreValue(jiangMaFen);
					// 添加到data中
					data.getPoints().add(valuePoint);
				}

				{
					// 奖马个数
					ScorePoint point = new ScorePoint();
					point.setScoreType(MahjongConstant.PlayType.DISPLAY_BETTING_HOUSE);
					point.setCalcType(ScoreCalculateType.TOTAL_ADD.getValue());
					point.setScoreValue(bettingCards.size());
					// 放到第一个位置
					data.getPoints().add(0, point);
				}

				// 添加到玩家身上
				player.getCards().getScoreDatas().add(data);
			}
			else
			{
				// 积分加倍
				ScorePoint point = new ScorePoint();
				point.setScoreType(MahjongConstant.PlayType.DISPLAY_BETTING_HOUSE);

				// 奖马加倍
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
