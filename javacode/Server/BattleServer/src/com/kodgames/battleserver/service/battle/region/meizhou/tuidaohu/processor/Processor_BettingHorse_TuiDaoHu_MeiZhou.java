package com.kodgames.battleserver.service.battle.region.meizhou.tuidaohu.processor;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalculateType;
import com.kodgames.battleserver.service.battle.core.score.filter.ScoreTargetFilter;
import com.kodgames.battleserver.service.battle.region.meizhou.common.processor.Processor_JiangMa;

/**
 * 梅州推倒胡玩法的奖马
 */
public class Processor_BettingHorse_TuiDaoHu_MeiZhou extends Processor_JiangMa
{
	public static final String KEY_HORSE_SCORE_TARGET_FILTERS = "horseScoreTargetFilters";

	public static final String KEY_QIANG_GANG_QUAN_BAO = "qiangGangQuanBao";

	public static final String KEY_MA_GEN_PAI = "maGenPai";

	/**
	 * 收取马牌分数目标过滤器
	 */
	private List<ScoreTargetFilter> horseScoreTargetFilters = new ArrayList<>();

	/**
	 * 抢杠胡收分目标的配置，为true时收取一家的分
	 */
	private boolean qiangGangQuanBao;

	/**
	 * 马跟牌的配置，为true时是马跟牌
	 */
	private boolean maGenPai;

	@Override public void createFromContext(JSONObject context)
		throws Exception
	{
		for (JSONObject json : CreateContextParser.getJSONArray(context, KEY_HORSE_SCORE_TARGET_FILTERS))
			horseScoreTargetFilters.add(ScoreTargetFilter.create(json));
		qiangGangQuanBao = CreateContextParser.getBoolean(context, KEY_QIANG_GANG_QUAN_BAO);
		maGenPai = CreateContextParser.getBoolean(context, KEY_MA_GEN_PAI);
	}

	@Override public void start()
	{
		// 下发庄家信息
		bettingHorse();

		finish();
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
			// 一炮多响的情况，是放炮的人拿马
			Step step = context.getRecords().get(context.getHuIndex().get(0));
			bettingPlayer = context.getPlayers().get(context.getScoreData(step.getRoleId(), context.getHuIndex().get(0)).getSourceId());
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

		// 向客户端发送翻到的牌
		controller.addDisplayOperations(new Step(bettingPlayer.getRoleId(), PlayType.DISPLAY_DEAL_BETTING_HOUSE, houseCards));

		// 判断翻到了几个奖马
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

		// 将翻倍参数加到每个胡牌人身上
		for (int huIndex : context.getHuIndex())
		{
			// 这里获取到当前的胡牌玩家，是为了一炮多响时的操作
			Step huStep = context.getRecords().get(huIndex);
			ScoreData huScoreData = context.getScoreData(huStep.getRoleId(), huIndex);
			PlayerInfo huPlayer = context.getPlayers().get(huStep.getRoleId());

			// 添加一个scoreData
			ScoreData horseScoreData = new ScoreData();
			horseScoreData.setAddOperation(true);
			horseScoreData.setSourceId(huScoreData.getSourceId());
			horseScoreData.setSourceRecrodIndex(context.getRecords().size() - 1);

			// 为用户添加马牌分的scorePoint
			{
				// 马牌数量的point，为了在房间统计的时候获取奖马数量
				ScorePoint point = new ScorePoint();
				point.setScoreType(MahjongConstant.PlayType.DISPLAY_BETTING_HOUSE);
				point.setCalcType(ScoreCalculateType.TOTAL_ADD.getValue());
				point.setScoreValue(bettingCards.size());

				/**
				 * 收取分数目标
				 */
				for (PlayerInfo targetPlayer : context.getPlayers().values())
				{
					// 自己不收自己分
					if (huPlayer.getRoleId() == targetPlayer.getRoleId())
						continue;

					// 所有过滤器都通过才满足目标
					boolean meetCondition = true;

					for (ScoreTargetFilter filter : horseScoreTargetFilters)
					{
						if (filter.filter(context, huPlayer, context.getPlayers().get(horseScoreData.getSourceId()), targetPlayer))
							continue;

						meetCondition = false;
						break;
					}

					if (meetCondition)
						horseScoreData.getScoreTargetList().add(targetPlayer.getRoleId());
				}

				// 添加point到scoredata中
				horseScoreData.getPoints().add(point);
			}

			// 设置奖马的基础分
			{
				boolean isQiangGangHu = false;

				ScorePoint point = new ScorePoint();
				point.setScoreType(MahjongConstant.PlayType.DISPLAY_BETTING_HOUSE);
				point.setCalcType(ScoreCalculateType.TOTAL_MULTI_2ND.getValue());

				// 设置奖马的基础分值，如果马跟牌和牌型分一样
				for (ScoreData scoreData : huPlayer.getCards().getScoreDatas())
				{
					if (PlayType.isHuType(scoreData.getPoints().get(0).getScoreType()))
					{
						// 记录最大番
						int maxScoreValue = 0;

						// 找到最大番和判断是否为抢杠胡
						for (ScorePoint scorePoint : scoreData.getPoints())
						{
							if (maxScoreValue < scorePoint.getScoreValue() && scorePoint.getScoreType() != PlayType.HU_QIANG_GANG_HU)
								maxScoreValue = scorePoint.getScoreValue();

							if (scorePoint.getScoreType() == PlayType.HU_QIANG_GANG_HU)
								isQiangGangHu = true;
						}

						// 计算结束后maxScoreValue不应该是0，最少是2
						Macro.AssetTrue(maxScoreValue == 0);

						// 设置point的分和最大番的分一样，*3是三家的，全包一家交三家的
						if (qiangGangQuanBao && isQiangGangHu)
							point.setScoreValue((maGenPai ? maxScoreValue : 2) * 3);
						else
							point.setScoreValue(maGenPai ? maxScoreValue : 2);
					}
				}

				horseScoreData.getPoints().add(point);
			}

			// 添加到玩家分数中
			huPlayer.getCards().getScoreDatas().add(horseScoreData);
		}
	}
}
