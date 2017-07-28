package com.kodgames.battleserver.service.battle.processer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.CardPool;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.creator.ICreateContextHandler;
import com.kodgames.corgi.core.util.Converter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/*
 *  牌池处理
 */
public class InitCardPoolProcesser extends Processer implements ICreateContextHandler
{
	public static final String KEY_SUPPORT_CARD_TYPES = "supportCardTypes";
	public static final String KEY_SUPPORT_CARDS = "supportCards";
	public static final String KEY_SUPPORT_CARDS_TYPE = "supportCards_type";
	public static final String KEY_SUPPORT_CARDS_VALUES = "supportCards_values";
	public static final String KEY_PLAY_CARD_COUNT = "playCardCount";

	// 配牌工具支持
	private static boolean cheat = false;
	private static int CUSTOM_PLAYER_COUNT = 0;
	private static byte[] CUSTOM_HAND_CARDS = null;
	private static byte[] CUSTOM_CARDS = null;

	private Map<CardType, List<Integer>> supportCardTypes = new HashMap<>();
	private int playCardCount = 13;

	public void createFromContext(JSONObject context)
		throws Exception
	{
		// 设置支持的牌型
		if (context.containsKey(KEY_SUPPORT_CARD_TYPES))
		{
			int typeCount = CreateContextParser.getArraySize(context, KEY_SUPPORT_CARD_TYPES);
			for (int i = 0; i < typeCount; ++i)
				supportCardTypes.put(CardType.valueOf(CreateContextParser.getStringInArray(context, KEY_SUPPORT_CARD_TYPES, i)), new ArrayList<>());
		}

		// 设置支持牌型所支持的卡
		if (context.containsKey(KEY_SUPPORT_CARDS))
		{
			JSONArray jsons = context.getJSONArray(KEY_SUPPORT_CARDS);
			for (int i = 0; i < jsons.size(); i++)
			{
				JSONObject object = jsons.getJSONObject(i);
				CardType cardType = CardType.valueOf(object.getString(KEY_SUPPORT_CARDS_TYPE));
				supportCardTypes.put(cardType, new ArrayList<>());
				
				for (int j = 0; j < CreateContextParser.getArraySize(object, KEY_SUPPORT_CARDS_VALUES); ++j)
					supportCardTypes.get(cardType).add(CreateContextParser.getIntInArray(object, KEY_SUPPORT_CARDS_VALUES, j));
			}
		}

		// 设置玩家手牌数量(加1胡牌)
		if (context.containsKey(KEY_PLAY_CARD_COUNT))
			playCardCount = context.getInt(KEY_PLAY_CARD_COUNT);
	}

	@Override
	public void start()
	{
		// 初始化牌池
		initCard();

		// 完成牌池初始化，进入下一个步骤
		finish();
	}

	private void initCard()
	{
		// 初始化牌池
		initRandomCards();

		// 如果开启了配牌，使用配牌工具配置的牌池
		initCheatCards();
	}

	/*
	 * 初始化牌池
	 */
	private void initRandomCards()
	{
		List<Byte> cardPools = context.getCardPool().getCards();

		supportCardTypes.entrySet().forEach(keySet -> {

			CardType cardType = keySet.getKey();

			// 在上下文中添加支持的牌
			context.getCardPool().getSupportedCardTypes().add(cardType.Value());

			// 将支持的牌加入牌池
			if (keySet.getValue().isEmpty())
			{
				putCardByType(cardType, cardPools);
			}
			else
			{
				// 使用配置的牌加入
				keySet.getValue().forEach(card -> {
					for (int j = 0; j < cardType.oneCardMax(); j++)
						cardPools.add((byte)card.intValue());
				});
			}
		});

		// 设置玩家牌的数量
		context.getCardPool().setPlayCardCount(playCardCount);

		// 随机打乱牌池中牌的顺序
		Random random = new Random(System.currentTimeMillis());
		for (int i = 0; i < cardPools.size(); i++)
		{
			int pos = random.nextInt(cardPools.size());
			byte temp = cardPools.get(i);
			cardPools.set(i, cardPools.get(pos));
			cardPools.set(pos, temp);
		}
	}

	// 生成牌色对应牌值
	private void putCardByType(CardType cardType, List<Byte> cardPools)
	{
		for (byte i = cardType.Value(); i < cardType.MaxValue(); i++)
			for (int j = 0; j < cardType.oneCardMax(); j++)
				cardPools.add(i);
	}

	// 工具配牌
	private void initCheatCards()
	{
		if (!cheat)
			return;

		CardPool cards = context.getCardPool();
		for (int i = 0; i < CUSTOM_HAND_CARDS.length; i++)
		{
			cards.getCards().remove(i);
			cards.getCards().add(i, CUSTOM_HAND_CARDS[i]);
		}

		int j = (13 * CUSTOM_PLAYER_COUNT) + 1;
		for (int i = 0; i < CUSTOM_CARDS.length; i++)
		{
			cards.getCards().remove(j);
			cards.getCards().add(j, CUSTOM_CARDS[i]);
			j++;
		}
	}

	static
	{
		try
		{
			// 配牌配置读取
			InputStream stream = Class.class.getResourceAsStream("/cheat_card.properties");
			if (stream != null)
			{
				BufferedReader br = new BufferedReader(new InputStreamReader(stream));
				String str = null;
				while ((str = br.readLine()) != null)
				{
					if (str.startsWith("#"))
						continue;
					String[] kv = str.split("=");
					switch (kv[0])
					{
						case "CheatFlag":
							cheat = Integer.parseInt(kv[1]) == 1;
							break;
						case "CheatCardInfo":
							List<Byte> temp = new ArrayList<>();
							String[] allCards = kv[1].split(";");
							for (int i = 0; i < allCards.length; i++)
							{
								String[] cards = allCards[i].split(",");
								for (String s : cards)
								{
									String[] skv = s.split(":");
									for (int j = 0; j < Integer.parseInt(skv[1]); j++)
									{
										temp.add(Byte.parseByte(skv[0]));
									}
								}
							}
							CUSTOM_PLAYER_COUNT = allCards.length;
							CUSTOM_HAND_CARDS = Converter.byteListToArray(temp);
							break;
						case "DealOneCard":
							String[] cards = kv[1].split(",");
							CUSTOM_CARDS = new byte[cards.length];
							for (int i = 0; i < cards.length; i++)
							{
								CUSTOM_CARDS[i] = Byte.parseByte(cards[i]);
							}
							break;
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		if (cheat)
		{
			// 检测配牌合法性
			for (byte card : CUSTOM_HAND_CARDS)
				if (Macro.AssetTrue(CardType.getCardType(card) == CardType.INVALID, String.format("无效的配牌:%d", card)))
					System.exit(-1);
			for (byte card : CUSTOM_CARDS)
				if (Macro.AssetTrue(CardType.getCardType(card) == CardType.INVALID, String.format("无效的配牌:%d", card)))
					System.exit(-1);
		}
	}
}
