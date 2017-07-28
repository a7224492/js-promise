package com.kodgames.battleserver.service.battle.region.guangdong.common.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.processer.Processer;

import net.sf.json.JSONObject;

/**
 * 生成鬼牌操作(广东)
 */
public class Processor_CheckMasterCard extends Processer
{
	public static final String KEY_DEFAULT_MASTER_CARD = "defaultMasterCard";
	public static final String KEY_GENERATE_MASTER_CARD_COUNT = "generateMasterCardCount";

	/**
	 * 默认的万能牌
	 */
	private byte defaultMasterCard;

	/**
	 * 需要生成的万能牌数量
	 */
	private byte generateMasterCardCount;

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		if (CreateContextParser.containsKey(context, KEY_DEFAULT_MASTER_CARD))
			defaultMasterCard = (byte)CreateContextParser.getInt(context, KEY_DEFAULT_MASTER_CARD);
		if (CreateContextParser.containsKey(context, KEY_GENERATE_MASTER_CARD_COUNT))
			generateMasterCardCount = (byte)CreateContextParser.getInt(context, KEY_GENERATE_MASTER_CARD_COUNT);
	}

	@Override
	public void start()
	{
		// 下发庄家信息
		checkMasterCard();

		finish();
	}

	private void checkMasterCard()
	{
		List<Byte> generatedMasterCards = new ArrayList<>();
		if (defaultMasterCard != 0)
			generatedMasterCards.add(defaultMasterCard);

		// 如果需要翻牌，增加翻牌行为
		if (generateMasterCardCount > 0)
		{
			// 抽一张牌
			byte dealCard = context.getCardPool().getCards().remove(0);
			// 算出鬼牌
			byte masterCard = 0;
			MahjongConstant.CardType cardType = null;

			// 循环遍历，防止翻出花牌
			while (true)
			{
				masterCard = dealCard;
				cardType = MahjongConstant.CardType.getCardType(dealCard);
				// 翻出的不是花牌，退出循环
				if (CardType.HUA != cardType)
					break;

				// 把花牌放入牌堆的随机位置
				Random random = new Random(System.currentTimeMillis());
				List<Byte> cardPools = context.getCardPool().getCards();
				int pos = random.nextInt(cardPools.size());
				cardPools.add(pos, masterCard);

				// 发送翻出的牌
				// TODO : 优化内容，下次更新后上线
				// Step step = new Step();
				// step.setRoleId(context.getZhuang());
				// step.setPlayType(PlayType.DISPLAY_DEAL_MASTER_CARD);
				// step.getCards().add(dealCard);
				// controller.addDisplayOperations(step);

				// 再翻一张
				dealCard = context.getCardPool().getCards().remove(0);
			}

			while (generateMasterCardCount > 0)
			{
				// 超出返回回到本type的第一张
				masterCard++;
				if (masterCard >= cardType.MaxValue())
					masterCard = cardType.Value();

				generatedMasterCards.add(masterCard);
				generateMasterCardCount--;
			}

			// 发送翻出的牌
			Step step = new Step();
			step.setRoleId(context.getZhuang());
			step.setPlayType(PlayType.DISPLAY_DEAL_MASTER_CARD);
			step.getCards().add(dealCard);
			step.getCards().addAll(generatedMasterCards);
			controller.addDisplayOperations(step);
		}

		// 保存鬼牌, 每个人都要记录
		for (int playerId : context.getPlayerIds())
		{
			context.addCardHeap(playerId, PlayType.DISPLAY_SHOW_MASTER_CARD, generatedMasterCards);
			context.setPlayerMasterCard(playerId, generatedMasterCards);
		}

		// 发送鬼牌
		for (Integer roleId : context.getPlayerIds())
		{
			Step op = new Step(roleId, MahjongConstant.PlayType.DISPLAY_SHOW_MASTER_CARD, generatedMasterCards);
			controller.addDisplayOperations(op);
		}
		controller.sendDisplayOperations();
	}
}
