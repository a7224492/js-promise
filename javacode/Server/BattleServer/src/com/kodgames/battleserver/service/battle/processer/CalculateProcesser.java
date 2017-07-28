package com.kodgames.battleserver.service.battle.processer;

import java.util.*;
import java.util.stream.Collectors;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.battleserver.service.battle.BattleService;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.BattleRoom;
import com.kodgames.battleserver.service.battle.common.xbean.CardInfo;
import com.kodgames.battleserver.service.battle.common.xbean.CardPool;
import com.kodgames.battleserver.service.battle.common.xbean.GameScore;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.BattleState;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayerStatus;
import com.kodgames.battleserver.service.battle.core.BattleHelper;
import com.kodgames.battleserver.service.battle.core.MahjongHelper;
import com.kodgames.battleserver.service.battle.core.check.CheckHelper;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.score.battle.BattleScoreProcessor;
import com.kodgames.battleserver.service.battle.core.score.game.GameScoreCalculator;
import com.kodgames.battleserver.service.room.RoomService;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.core.util.Converter;
import com.kodgames.message.proto.battle.BattleProtoBuf.BCFinalMatchResultSYN;
import com.kodgames.message.proto.battle.BattleProtoBuf.BCMatchResultSYN;
import com.kodgames.message.proto.battle.BattleProtoBuf.PlayerMatchResultPROTO;

/**
 * 牌局结算显示的分数
 */
public class CalculateProcesser extends Processer
{
	protected static final Logger logger = LoggerFactory.getLogger(CalculateProcesser.class);

	public static final String KEY_BATTLE_SCORE_PROCESSOR = "battleScoreProcessor";
	public static final String KEY_GAME_SCORE_CALCULATOR = "gameScoreCalculator";
	public static final String KEY_SHOW_HU_CARDS = "shouHuCards";

	protected BattleScoreProcessor battleScoreProcessor = new BattleScoreProcessor();
	protected GameScoreCalculator gameScoreCaculator = null;
	protected boolean showHuCards = false;

	protected BCMatchResultSYN.Builder calcBuilder = BCMatchResultSYN.newBuilder();

	private Set<Integer> readyPlayerIds = new HashSet<>();
	
	@Override
	public void start()
	{
		try
		{
			checkAllCards();
			checkPlayerCards();
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}

		calculateScore();
	}

	public void createFromContext(JSONObject context)
		throws Exception
	{
		battleScoreProcessor.createFromContext(CreateContextParser.getJSONObject(context, KEY_BATTLE_SCORE_PROCESSOR));
		gameScoreCaculator = GameScoreCalculator.create(CreateContextParser.getJSONObject(context, KEY_GAME_SCORE_CALCULATOR));
		if (context.containsKey(KEY_SHOW_HU_CARDS))
			showHuCards = CreateContextParser.getBoolean(context, KEY_SHOW_HU_CARDS);
	}

	@Override
	public void rejoin(int roleId)
	{
		RoomService roomService = ServiceContainer.getInstance().getPublicService(RoomService.class);
		BattleRoom roomInfo = battleHelper.getRoomInfo();
		// 获取玩家
		PlayerInfo playerInfo = roomInfo.getPlayerById(roleId);
		// 玩家是否为准备状态
		boolean isReady = 0 != (playerInfo.getStatus() & PlayerStatus.READY);
		// 如果玩家已经准备了，添加到已准备列表，并判断是否所有人都准备了，
		if (isReady)
		{
			logger.info("player rejoin, roleId is: {}", roleId);
			readyPlayerIds.add(roleId);
			// 所有人都准备了就销毁房间
			if (readyPlayerIds.size() == roomInfo.getMaxMemberCount())
				endBattle();
		}
		else
		{
			logger.info("player update status, roleId is: {}", roleId);
			// 玩家不是准备状态证明是断线重连加入，所以需要下发房间结算消息
			this.calcBuilder.setIsRejoin(true);
			controller.sendMessage(roleId, calcBuilder.build());
		}
	}

	protected void calculateScore()
	{
		BattleRoom roomInfo = battleHelper.getRoomInfo();
		int roomId = roomInfo.getRoomId();

		RoomService roomService = ServiceContainer.getInstance().getPublicService(RoomService.class);
		// 战斗结算数据
		battleScoreProcessor.process(context);
		HashMap<Integer, PlayerMatchResultPROTO.Builder> resultPROTOs = calculatePlayerMatchResult(context.getPlayers(), gameScoreCaculator);

		// 发送单局结算消息给玩家
		BCMatchResultSYN.Builder calcBuilder = BCMatchResultSYN.newBuilder();
		calcBuilder.setIsHuang(context.getBattleState() == BattleState.HUANGZHUANG);
		// 设置剩余牌
		calcBuilder.setLastCards(MahjongHelper.convert2ByteString(context.getCardPool().getCards()));
		calcBuilder.setIsRejoin(false);
		resultPROTOs.values().forEach(result -> {
			calcBuilder.addMatchResults(result.build());
		});

		// 发送
		context.getPlayerIds().stream().forEach(roleId -> {
			calcBuilder.setProtocolSeq(roomService.getAndSetNewPlayProtocolSequence(roomId, roleId));
			controller.sendMessage(roleId, calcBuilder.build());
		});
		// 添加到context中，复牌用
		this.calcBuilder = calcBuilder;
		// 计算下一庄家
		battleHelper.getZhuangCalculator().calculateNextZhuang(roomInfo);
		int zhuang = context.getZhuang();
		int nextZhuang = context.getNextZhuang();

		// 增加圈数或局数，并下发消息
		ServiceContainer.getInstance().getPublicService(BattleService.class).roundFinish(roomId, zhuang, nextZhuang, calcBuilder);

		// 如果房间不存在了，（在房间结束的时候就删除了）
		if (roomService.isRoomExist(roomInfo.getRoomId()) == false)
			endBattle();
	}

	protected HashMap<Integer, PlayerMatchResultPROTO.Builder> calculatePlayerMatchResult(Map<Integer, PlayerInfo> players, GameScoreCalculator gameScoreCaculator)
	{
		// 构造所有玩家的结果
		HashMap<Integer, PlayerMatchResultPROTO.Builder> resultBuilders = BattleHelper.getInstance().getBattleScoreCalculator().createResultBuilders(players);

		/*
		 * 计算个人得分
		 */
		BattleHelper.getInstance().getBattleScoreCalculator().calculatePlayerScore(players, resultBuilders);

		/*
		 * 统计牌局得分
		 */
		players.values().forEach((player) -> {
			gameScoreCaculator.calculate(player, resultBuilders.get(player.getRoleId()));
		});

		// 更新玩家得分
		players.values().forEach((player) -> {
			player.setPointInGame(resultBuilders.get(player.getRoleId()).getPointInGame());
			player.setTotalPoint(resultBuilders.get(player.getRoleId()).getTotalPoint());
			// 设置玩家本局状态
			resultBuilders.get(player.getRoleId()).setStatus(player.getStatus());
			// 设置打出的牌
			resultBuilders.get(player.getRoleId()).setOutCards(MahjongHelper.convert2ByteString(player.getCards().getOutCards()));
			// 设置可胡的牌
			resultBuilders.get(player.getRoleId()).setHuCards(MahjongHelper.convert2ByteString(showHuCards ? CheckHelper.getTingCards(context, player.getRoleId()) : new ArrayList<Byte>()));
		});

		// 牌局得分放到roomPlayer身上（这步转换是由于在计算玩家得分的时候可能需要获取玩家身上的cardHeap）
		BattleHelper.getInstance().getRoomInfo().forEachPlayers(roomPlayer -> {
			roomPlayer.getCards().getGameScore().clear();
			roomPlayer.setTotalPoint(players.get(roomPlayer.getRoleId()).getTotalPoint());
			roomPlayer.setPointInGame(players.get(roomPlayer.getRoleId()).getPointInGame());
			players.get(roomPlayer.getRoleId()).getCards().getGameScore().forEach(gameScore -> {
				GameScore score = new GameScore(gameScore);
				roomPlayer.getCards().getGameScore().add(score);
			});
		});



		// 构造返回结果
		resultBuilders.values().forEach((resultBuilder) -> {

			CardInfo cardInfo = players.get(resultBuilder.getRoleId()).getCards();
			// 手牌
			resultBuilder.setHandCards(Converter.byteListToByteString(cardInfo.getHandCards().stream().collect(Collectors.toList())));
			// 操作牌
			for (Step step : cardInfo.getCardHeap())
				resultBuilder.addOperateCards(MahjongHelper.step2Proto(step));
		});

		return resultBuilders;
	}

	/**
	 * 检测所有牌的数量是否合法
	 */
	protected void checkAllCards()
	{
		// 统计所有牌的数量
		byte[] allCardCount = new byte[CardType.TOTAL_COUNT.Value()];
		BattleBean context = getBattleBean();

		Map<Integer, Byte> stepIndexToHuCard = new HashMap<>();

		// 统计玩家的牌数量
		context.getPlayers().values().forEach(player -> {
			CardInfo cardInfo = player.getCards();

			// 手牌
			cardInfo.getHandCards().forEach(card -> ++allCardCount[card]);

			// 出牌
			cardInfo.getOutCards().forEach(card -> ++allCardCount[card]);

			// 吃碰杠
			cardInfo.getCardHeap().forEach(step -> {
				int playType = step.getPlayType();
				if (PlayType.isChiPengGang(playType))
				{
					step.getCards().forEach(card -> ++allCardCount[card]);
				}
			});

			// 收集胡的牌
			cardInfo.getScoreDatas().forEach(scoreData -> {
				List<ScorePoint> points = scoreData.getPoints();
				ScorePoint point = points.get(0);
				byte sourceCard = scoreData.getSourceCard();
				if (PlayType.isHuType(point.getScoreType()) && sourceCard != CardType.INVALID.Value())
				{
					int stepIndex = scoreData.getSourceRecrodIndex();
					stepIndexToHuCard.put(stepIndex, sourceCard);
				}
			});
		});

		// 统计胡的牌的数量
		stepIndexToHuCard.values().forEach(card -> ++allCardCount[card]);

		// 统计牌池剩余牌的数量
		CardPool pool = context.getCardPool();
		pool.getCards().forEach(card -> ++allCardCount[card]);

		// 检测牌的数量
		RoomService roomService = ServiceContainer.getInstance().getPublicService(RoomService.class);
		for (byte card = 0; card < allCardCount.length; ++card)
		{
			// 判断牌的类型是否支持
			CardType type = CardType.getCardType(card);
			boolean supported = pool.getSupportedCardTypes().contains(type.Value());

			// 数量检查
			byte cardCount = allCardCount[card];
			if ((supported == false && cardCount != 0) || (supported == true && cardCount > type.oneCardMax()))
			{
				int roomId = context.getRoomId();
				BattleRoom room = roomService.getRoomInfo(roomId);
				int gameIndex = room.getGames().size() - 1;
				logger.error("Invalid cardCount : roomId={}, gameIndex={}, card={}, count={} !", roomId, gameIndex, card, cardCount);
			}
		}
	}

	/**
	 * 检查玩家牌型中牌的数量是否正确
	 * 
	 * 包括手牌、吃碰杠、胡的牌的数量
	 */
	protected void checkPlayerCards()
	{
		// 统计所有牌的数量
		BattleBean context = getBattleBean();

		// 统计玩家的牌数量
		for (PlayerInfo player : context.getPlayers().values())
		{
			// 手牌数量
			CardInfo cardInfo = player.getCards();
			int cardCount = cardInfo.getHandCards().size();

			// 吃碰杠的牌数量
			for (Step step : cardInfo.getCardHeap())
			{
				int playType = step.getPlayType();
				if (PlayType.isChiPengGang(playType))
				{
					cardCount += 3;
				}
			}

			// 检测牌型数量是否合法
			if (cardCount != context.getCardPool().getPlayCardCount())
			{
				BattleRoom room = BattleHelper.getInstance().getRoomInfo();
				int gameIndex = room.getGames().size() - 1;
				logger.error("Invalid player card count : roomId={}, gameIndex={}, roleId={}, cardCount={}", room.getRoomId(), gameIndex, player.getRoleId(), cardCount);
			}
		}
	}

	/**
	 * 结束房间
	 */
	protected void endBattle()
	{
		// 设置房间状态为非运行状态
		context.setIsRunning(false);
		// 获取房间信息
		BattleRoom roomInfo = battleHelper.getRoomInfo();

		// 结束牌局
		ServiceContainer.getInstance().getPublicService(BattleService.class).removeBattle(roomInfo.getRoomId());
	}

}
