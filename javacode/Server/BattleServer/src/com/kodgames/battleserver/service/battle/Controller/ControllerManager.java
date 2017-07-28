package com.kodgames.battleserver.service.battle.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessage;
import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.BattleConst;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.BaseController;
import com.kodgames.battleserver.service.battle.core.MahjongHelper;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.creator.ICreateContextHandler;
import com.kodgames.battleserver.service.room.RoomService;
import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.message.proto.battle.BattleProtoBuf.BCBattlePlayerInfoSYN;
import com.kodgames.message.proto.battle.BattleProtoBuf.BCPlayStepSYN;
import com.kodgames.message.proto.battle.BattleProtoBuf.BattlePlayerInfoPROTO;
import com.kodgames.message.proto.battle.BattleProtoBuf.PlayStepPROTO;

import net.sf.json.JSONObject;

/**
 * 控制类，控制和客户端的消息交互
 */
public class ControllerManager extends BaseController implements ICreateContextHandler
{
	static Logger logger = LoggerFactory.getLogger(ControllerManager.class);

	public static final String KEY_DISPLAY_STEP_PROCESSORS = "displayStepProcessors";
	public static final String KEY_DISPLAY_STEP_PLAY_TYPE = "displayStepPlayType";

	private WaitSquenceController waitSquence;
	private PlaybackController playback;
	private Map<Integer, DisplayStepProcessor> stepProcessorMap = new HashMap<>();

	public ControllerManager()
	{
		this.waitSquence = new WaitSquenceController();
		this.playback = new PlaybackController();
	}

	public static ControllerManager create(JSONObject context)
		throws Exception
	{
		if (context.isNullObject())
		{
			return new ControllerManager();
		}
		
		ControllerManager instance = CreateContextHelper.instantiateClass(context, ControllerManager.class);
		instance.createFromContext(context);
		return instance;
	}

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		for (JSONObject stepConfig : CreateContextParser.getJSONArray(context, KEY_DISPLAY_STEP_PROCESSORS))
		{
			int playType = stepConfig.getInt(KEY_DISPLAY_STEP_PLAY_TYPE);
			DisplayStepProcessor processor = DisplayStepProcessor.create(stepConfig);
			Macro.AssetTrue(processor == null, getClass() + " : no processor for playtype -> " + playType);
			processor.setController(this);
			stepProcessorMap.put(playType, processor);
		}
	}

	public WaitSquenceController getWaitSquenceController()
	{
		return waitSquence;
	}
	
	public PlaybackController getPlayback()
	{
		return playback;
	}

	/**
	 * 响应指定玩家在等待队列中的操作, 返回可以真正执行的操作
	 * 
	 * @param roleId 玩家id
	 * @param playType 操作类型
	 * @param cards 操作参数
	 * @return 如果可以继续执行, 返回可以执行的操作列表
	 */
	public List<Step> getWaitingOperations(int roleId)
	{
		return waitSquence.getWaitingOperations(roleId);
	}

	/**
	 * 执行玩家操作
	 */
	public List<Step> correspondWaitingOperation(int roleId, int playType, byte[] cards)
	{
		return waitSquence.correspondWaitingOperation(roleId, playType, cards);
	}

	/**
	 * 将操作operators作为一个操作队列保存
	 * <p>
	 * 如果当前玩家已经在等待队列中, 则忽略这个操作 *
	 *
	 * @return 是否成功加入这个操作
	 */
	public boolean addWaitingOperations(Step... operators)
	{
		return waitSquence.addWaitingOperations(operators);
	}

	/**
	 * 添加发给指定用户的显示操作
	 */
	public void addDisplayOperations(Step... displays)
	{
		waitSquence.addDisplayOperations(displays);
	}

	public void sendDisplayOperations()
	{
		sendDisplayOperations(0, false);
	}

	public void sendDisplayOperations(boolean rejoin)
	{
		sendDisplayOperations(0, rejoin);
	}

	/**
	 * 是否可以将手牌（发牌）发送给其他玩家
	 * 
	 * @param roleId
	 * @return
	 */
	public boolean canSendHandCard2Other(int roleId)
	{
		return context.hasCardHeap(roleId, PlayType.DISPLAY_TANPAI);
	}

	/**
	 * 发送战斗消息给玩家
	 *
	 * @param onlySendId 指定需要发送的玩家， 如果为0发送给所有人
	 * @param rejoin 当前是否是由于复牌引擎的发送
	 */
	public void sendDisplayOperations(int onlySendId, boolean rejoin)
	{
		// 创建消息发送结构
		Map<Integer, List<PlayStepPROTO>> steps = new HashMap<>();
		context.getPlayers().keySet().forEach(tempRoleId -> steps.put(tempRoleId, new ArrayList<>()));

		List<Step> steps2Send = waitSquence.getDisplayOperations();
		// 回放处理
		playback.processStep(steps2Send, rejoin);
		
		for (Step disPlay : steps2Send)
		{
			// 玩家复牌时，不在对其他玩家发送操作消息
			if(rejoin && onlySendId != 0 && disPlay.getPlayType() != disPlay.getPlayType())
				continue;
			
			// 地区自定义操作处理
			DisplayStepProcessor processor = stepProcessorMap.get(disPlay.getPlayType());
			if (processor != null)
			{
				Map<Integer, List<PlayStepPROTO>> displaySteps = processor.process(context, disPlay, rejoin);
				for (Entry<Integer, List<PlayStepPROTO>> entry : displaySteps.entrySet())
				{
					List<PlayStepPROTO> roleSteps = steps.get(entry.getKey());
					if (roleSteps == null)
					{
						logger.error("{} : invalid roleId -> {} !", getClass(), entry.getKey());
						continue;
					}
					roleSteps.addAll(entry.getValue());
				}

				continue;
			}

			// 默认操作处理
			switch (disPlay.getPlayType())
			{
				case PlayType.OPERATE_DEAL_FIRST:
					// 对于发牌和复牌操作， 单独构造消息发送
					BCBattlePlayerInfoSYN.Builder sendBuilder = BCBattlePlayerInfoSYN.newBuilder();
					sendBuilder.setProtocolSeq(getProtocolSeq(context.getRoomId(), disPlay.getRoleId()));
					sendBuilder.setIsRecover(rejoin);
					sendBuilder.setEnableMutilHu(battleHelper.getBattleFinishChecker().enableMutilHu());
					sendBuilder.setTotalCardsNum(context.getCardPool().getPlayCardCount());

					for (PlayerInfo player : context.getPlayers().values())
					{
						BattlePlayerInfoPROTO.Builder infoBuilder = BattlePlayerInfoPROTO.newBuilder();
						infoBuilder.setRoleId(player.getRoleId()).setStatus(player.getStatus()).setPointInGame(player.getPointInGame()).setTotalPoint(player.getTotalPoint());

						// 设置手牌
						ByteString cards;
						List<Byte> handCards = new ArrayList<>();
						player.getCards().getHandCards().forEach(card -> {

							// 如果是玩家自己，或者该玩家的选择了摊牌，那么发送真实手牌信息给重连玩家
							if (player.getRoleId() == disPlay.getRoleId() || canSendHandCard2Other(player.getRoleId()))
								handCards.add(card);
							else
								handCards.add((byte)-1);
						});

						// 如果上一个操作是发牌，这张牌已经发到手牌中，需要从
						// 为了实现客户端的手牌效果：最后一张是最后摸到的牌
						Step dealStep = context.getPlayerLastRecord(player.getRoleId());
						if (dealStep != null && dealStep.getPlayType() == PlayType.OPERATE_DEAL)
						{
							// 删除发牌
							byte dealCard = (player.getRoleId() == disPlay.getRoleId() || canSendHandCard2Other(player.getRoleId())) ? dealStep.getCards().get(0) : -1;
							handCards.remove(Byte.valueOf(dealCard));

							// 断线重连，添加摸牌消息
							if (rejoin)
							{
								PlayStepPROTO.Builder stepBuilder = MahjongHelper.step2Proto(dealStep).toBuilder();
								stepBuilder.setCards(convert2ByteString(dealCard));
								steps.get(disPlay.getRoleId()).add(stepBuilder.build());
							}
						}
						cards = convert2ByteString(handCards);
						infoBuilder.setHandCards(cards);

						// 设置打出去的牌
						cards = convert2ByteString(player.getCards().getOutCards());
						infoBuilder.setOutCards(cards);

						// 设置操作牌
						for (Step step : player.getCards().getCardHeap())
						{
							PlayStepPROTO.Builder stepBuilder = MahjongHelper.step2Proto(step).toBuilder();

							// 修改牌值，不让其他玩家知道
							if (player.getRoleId() != disPlay.getRoleId() && battleHelper.hideCardHeapCards(step.getPlayType()))
								stepBuilder.setCards(convert2ByteString((byte)-1));
							infoBuilder.addOperateCards(stepBuilder.build());
						}
						sendBuilder.addPlayers(infoBuilder.build());
					}

					sendMessage(disPlay.getRoleId(), sendBuilder.build());
					break;

				case PlayType.DISPLAY_TANPAI:
					// 明打发送手牌给其他玩家
					steps.keySet().forEach(tempRoleId -> {
						PlayStepPROTO.Builder mingStep = MahjongHelper.step2Proto(disPlay).toBuilder();
						mingStep.setCards(convert2ByteString(context.getPlayers().get(disPlay.getRoleId()).getCards().getHandCards()));
						steps.get(tempRoleId).add(mingStep.build());
					});
					break;

				case PlayType.OPERATE_DEAL:
				case PlayType.OPERATE_CAN_PLAY_A_CARD:
				case PlayType.OPERATE_CAN_AUTO_PLAY_LAST_DEALED_CARD:
					// 上面两种操作需要对其他玩家屏蔽将真实数据
					steps.keySet().forEach(tempRoleId -> {
						PlayStepPROTO.Builder stepBuilder = MahjongHelper.step2Proto(disPlay).toBuilder();
						// 不发送真实牌值给其他玩家，除非是摊牌
						if (tempRoleId != disPlay.getRoleId() && !canSendHandCard2Other(disPlay.getRoleId()))
							stepBuilder.setCards(convert2ByteString((byte)-1));
						steps.get(tempRoleId).add(stepBuilder.build());
					});
					break;
				default:
					if (PlayType.isSend2Target(disPlay.getPlayType()))
						// 只发送到目标的消息
						steps.get(disPlay.getRoleId()).add(MahjongHelper.step2Proto(disPlay));
					else
						// 发送给所有人
						steps.keySet().forEach(tempRoleId -> {

							PlayStepPROTO.Builder stepBuilder = MahjongHelper.step2Proto(disPlay).toBuilder();
							// 修改牌值，不让其他玩家知道
							if (tempRoleId != disPlay.getRoleId() && battleHelper.hideCardHeapCards(disPlay.getPlayType()))
								stepBuilder.setCards(convert2ByteString((byte)-1));

							steps.get(tempRoleId).add(stepBuilder.build());
						});
					break;
			}
		}

		// 下发消息给玩家
		steps.entrySet().forEach(entry -> {
			if (onlySendId != 0 && entry.getKey() != onlySendId)
				return;

			if (entry.getValue().size() > 0)
			{
				entry.getValue()
					.add(PlayStepPROTO.newBuilder().setPlayType(PlayType.DISPLAY_LAST_CARD_COUNT).setRoleId(BattleConst.INVALID_ROLEID).setPointInGame(context.getCardPool().getCards().size()).build());

				BCPlayStepSYN.Builder builder = BCPlayStepSYN.newBuilder();
				builder.setProtocolSeq(getProtocolSeq(context.getRoomId(), entry.getKey()));
				builder.addAllSteps(entry.getValue());
				sendMessage(entry.getKey(), builder.build());
			}
		});
	}

	/**
	 * 对指定玩家发送协议
	 */
	public void sendMessage(int roleId, GeneratedMessage message)
	{
		logger.info("Send BatttleMessage to {}, message is {}", roleId, message);
		Connection connection = ConnectionManager.getInstance().getClientVirtualConnection(roleId);
		// 判定连接是否存在，玩家可能离线
		if (connection != null)
			connection.write(GlobalConstants.DEFAULT_CALLBACK, message);
	}

	/**
	 * 获取制定玩家当前的操作序号
	 */
	public int getProtocolSeq(int roomId, int roleId)
	{
		RoomService roomService = ServiceContainer.getInstance().getPublicService(RoomService.class);
		return roomService.getAndSetNewPlayProtocolSequence(roomId, roleId);
	}
}
