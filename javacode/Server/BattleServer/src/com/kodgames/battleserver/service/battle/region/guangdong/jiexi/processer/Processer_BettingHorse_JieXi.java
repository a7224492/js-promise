package com.kodgames.battleserver.service.battle.region.guangdong.jiexi.processer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalculateType;
import com.kodgames.battleserver.service.battle.region.guangdong.common.processor.Processor_BettingHorse;

/**
 * 揭西奖马
 * 
 * @author 毛建伟
 */
public class Processer_BettingHorse_JieXi extends Processor_BettingHorse
{

	@Override
	public void start()
	{
		bettingHorse();

		finish();
	}

	private void bettingHorse()
	{
		// 如果不需要翻马就退出
		if (check() == false)
			return;
		// 获取翻到的所有马牌
		List<Byte> horseCards = turnBettingHorse();
		// 记录所有玩家中的马牌
		Map<Integer, List<Byte>> allPlayerBettingCards = new HashMap<>();
		// 循环所有玩家计算赢的马牌
		context.getPlayers().values().forEach(player -> {
			allPlayerBettingCards.put(player.getRoleId(), checkWinCards(horseCards, player.getRoleId()));
		});

		// 存放所有玩家的杠分（是一个copy）
		Map<Integer, List<ScoreData>> playersGangScoreDatas = new HashMap<>();
		// 循环判断玩家的杠分
		for (PlayerInfo player : context.getPlayers().values())
		{
			// 新建List
			List<ScoreData> gangDatas = new ArrayList<>();
			for (ScoreData scoreData : player.getCards().getScoreDatas())
			{
				if (PlayType.isGangOperator(scoreData.getPoints().get(0).getScoreType()))
				{
					ScoreData copyData = new ScoreData();
					copyData.copyFrom(scoreData);
					gangDatas.add(copyData);
				}
			}
			// 放入存放的内存中
			playersGangScoreDatas.put(player.getRoleId(), gangDatas);
		}

		// 循环胡牌玩家
		for (int index : context.getHuIndex())
		{
			// 获取胡牌玩家
			Step huStep = context.getRecords().get(index);
			PlayerInfo huPlayer = context.getPlayerById(huStep.getRoleId());
			// 玩家赢的马牌
			List<Byte> bettingCards = new ArrayList<>();
			{
				// 判断是否为抢杠胡
				int roleId = checkQiangGang(huPlayer);
				if (checkQiangGang(huPlayer) != -1)
				{
					// 是枪杆胡，添加除了被抢杠的人中的马牌到bettingCards中
					for (int id : allPlayerBettingCards.keySet())
					{
						if (id != roleId)
						{
							bettingCards.addAll(allPlayerBettingCards.get(id));
						}
					}
				}
				else
					bettingCards.addAll(allPlayerBettingCards.get(huPlayer.getRoleId()));
			}
			// 发送消息并存入cardHeap中
			sendMessageAndSaveCardHeap(horseCards, bettingCards, huPlayer.getRoleId());
			// 计算胡牌分数
			calculateHu(huPlayer, bettingCards);
			// 计算杠牌分数
			if (applytoGang)
			{
				calculateGang(huPlayer, allPlayerBettingCards, playersGangScoreDatas);
			}
		}
	}

	/**
	 * 检查玩家是否为抢杠胡
	 * 
	 * @param player 胡牌玩家
	 * @return -1代表不是抢杠胡，否则返回被抢杠的玩家id
	 */
	private int checkQiangGang(PlayerInfo player)
	{
		// 循环玩家分数
		for (ScoreData data : player.getCards().getScoreDatas())
		{
			// 是胡的类型
			if (PlayType.isHuType(data.getPoints().get(0).getScoreType()))
			{
				// 循环分数列表
				for (ScorePoint point : data.getPoints())
				{
					if (point.getScoreType() == PlayType.HU_QIANG_GANG_HU)
						return data.getSourceId();
				}

				return -1;
			}
		}

		return -1;
	}

	/**
	 * 检测是否可以进行翻马
	 * 
	 * @return
	 */
	protected boolean check()
	{
		int huSize = context.getHuIndex().size();
		// 黄庄不翻马
		if (huSize == 0 || (huSize > 1 && !duoXiangNeedMa))
			return false;
		return true;
	}

	/**
	 * 翻奖马
	 * 
	 * @return 翻到的所有马牌
	 */
	protected List<Byte> turnBettingHorse()
	{
		// 获取奖马的数量
		int houseCardCount = context.getCardPool().getStayCount();

		// 翻制定数量的牌
		List<Byte> houseCards = new ArrayList<>();
		while (houseCardCount > 0)
		{
			houseCards.add(context.getCardPool().getCards().remove(0));
			--houseCardCount;
		}

		return houseCards;
	}

	/**
	 * 检查赢的牌
	 * 
	 * @param horseCards 翻出来的所有马牌
	 * @param roleId 需要检查的人
	 * @return 赢的马牌（可能有重复的）
	 */
	protected List<Byte> checkWinCards(List<Byte> horseCards, int roleId)
	{
		// 判断
		List<Byte> bettingCards = new ArrayList<>();
		// 获取玩家对应的马牌
		byte[] playerBettingCards = getHouseCardTypeList(roleId);
		// 循环判断玩家中的牌
		for (byte card : horseCards)
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

		return bettingCards;
	}

	/**
	 * 向客户端发送消息并存到cardHeap中
	 * 
	 * @param horseCards 翻到的所有马牌
	 * @param bettingCards 中的马牌
	 * @param roleId 玩家id
	 */
	protected void sendMessageAndSaveCardHeap(List<Byte> horseCards, List<Byte> bettingCards, int roleId)
	{
		// 是否为一炮多响
		boolean isDuoXiang = context.getHuIndex().size() > 1;
		// 向客户端发送翻到的牌并存到cardheap中，弃牌余牌用
		{
			Step dealBettingHourseStep = new Step(roleId, PlayType.DISPLAY_DEAL_BETTING_HOUSE, horseCards);
			context.addCardHeap(dealBettingHourseStep);
			if (isDuoXiang)
				controller.addDisplayOperations(new Step(roleId, PlayType.DISPLAY_DEAL_BETTING_HOUSE_MULTI, horseCards));
			else
				controller.addDisplayOperations(dealBettingHourseStep);
		}
		// 向客户端发送中马的牌并添加中的马牌
		{
			Step bettingHouseStep = new Step(roleId, PlayType.DISPLAY_BETTING_HOUSE, bettingCards);
			context.addCardHeap(bettingHouseStep);
			if (isDuoXiang)
				controller.addDisplayOperations(new Step(roleId, PlayType.DISPLAY_BETTING_HOUSE_MULTI, bettingCards));
			else
				controller.addDisplayOperations(bettingHouseStep);
		}
		// 发送消息
		controller.sendDisplayOperations();
	}

	/**
	 * 计算胡牌分数
	 * 
	 * @param huPlayer 胡牌玩家
	 * @param bettingCards 胡牌玩家赢得马牌
	 */
	protected void calculateHu(PlayerInfo huPlayer, List<Byte> bettingCards)
	{
		// 没中马牌退出不计算
		if (bettingCards.isEmpty())
			return;
		// 循环胡牌玩家的分数
		for (ScoreData data : huPlayer.getCards().getScoreDatas())
		{
			// 找到胡牌分数
			if (PlayType.isHuType(data.getPoints().get(0).getScoreType()))
			{
				// 新建一个分数
				ScorePoint point = new ScorePoint();
				point.setCalcType(ScoreCalculateType.TOTAL_MULTI_2ND.getValue());
				point.setScoreType(PlayType.DISPLAY_BETTING_HOUSE);
				point.setScoreValue(bettingCards.size() + 1);
				// 添加到data中
				data.getPoints().add(point);
			}
		}
	}

	/**
	 * 计算玩家的杠分
	 * 
	 * @param horseCards 翻出的所有马牌
	 * @param huPlayer 胡牌玩家
	 */
	protected void calculateGang(PlayerInfo huPlayer, Map<Integer, List<Byte>> allPlayerBettingCards, Map<Integer, List<ScoreData>> playersGangScoreDatas)
	{

		// 循环玩家
		PlayerInfo player = huPlayer;
		// do while循环是为了从胡牌玩家开始
		do
		{
			// 获取该玩家买中的马牌
			List<Byte> winCards = allPlayerBettingCards.get(player.getRoleId());
			// 如果没有买中，或者玩家身上没有杠
			if (winCards.isEmpty() || playersGangScoreDatas.get(player.getRoleId()).isEmpty())
			{
				// 获取下一玩家
				player = context.getPlayerById(context.getNextRoleId(player.getRoleId()));
				continue;
			}
			// 如果是胡牌玩家
			if (player.getRoleId() == huPlayer.getRoleId())
			{
				for (int index = huPlayer.getCards().getScoreDatas().size() - 1; index >= 0; --index)
				{
					// 获取分数和类型
					ScoreData scoreData = huPlayer.getCards().getScoreDatas().get(index);
					int type = scoreData.getPoints().get(0).getScoreType();

					if (PlayType.isGangOperator(type))
					{
						// 是否为明杠
						boolean isMingGang = type == PlayType.OPERATE_GANG_A_CARD;

						// 新建一个分数
						ScorePoint point = new ScorePoint();
						point.setCalcType(ScoreCalculateType.TOTAL_MULTI_2ND.getValue());
						point.setScoreType(PlayType.DISPLAY_BETTING_HOUSE);
						point.setScoreValue(winCards.size() + 1);
						// 明杠
						if (!isMingGang)
						{
							// 复制一份
							ScoreData copyData = new ScoreData();
							// copy玩家的杠
							for (ScoreData playerData : playersGangScoreDatas.get(player.getRoleId()))
							{
								if (playerData.getSourceCard() == scoreData.getSourceCard())
									copyData.copyFrom(playerData);
							}
							// 删除其他胡牌人
							scoreData.getScoreTargetList().removeIf(roleId -> {
								for (int huIndex : context.getHuIndex())
								{
									Step huStep = context.getRecords().get(huIndex);
									if (huStep != null && huStep.getRoleId() == roleId)
									{
										return true;
									}
								}
								return false;
							});
							// 如果收分目标发生了改变，证明是多胡,并且其他胡牌人买中了
							if (scoreData.getScoreTargetList().size() != copyData.getScoreTargetList().size())
							{
								point.setScoreValue(winCards.size());
								huPlayer.getCards().getScoreDatas().add(copyData);
							}
						}
						// 添加到scoreData中
						scoreData.getPoints().add(point);
					}
				}
				// 获取下一玩家
				player = context.getPlayerById(context.getNextRoleId(player.getRoleId()));
				// 进入下一循环
				continue;
			}

			Step playerLastStep = context.getPlayerLastRecord(player.getRoleId());
			boolean playerIsHu = playerLastStep != null && playerLastStep.getPlayType() == PlayType.OPERATE_HU;

			// 循环玩家判断玩家身上是否有杠分
			for (int index = player.getCards().getScoreDatas().size() - 1; index >= 0; --index)
			{
				ScoreData scoreData = player.getCards().getScoreDatas().get(index);
				// 获取分数类型
				int type = scoreData.getPoints().get(0).getScoreType();
				// 计算杠分，一个马牌时可能消掉
				if (PlayType.isGangOperator(type))
				{
					// 新建一个copydata，存放到胡牌玩家身上
					ScoreData copyData = new ScoreData();
					// 剩余的中马牌
					int size = winCards.size() - 1;
					// copy玩家的杠
					for (ScoreData playerData : playersGangScoreDatas.get(player.getRoleId()))
					{
						if (playerData.getSourceCard() == scoreData.getSourceCard())
							copyData.copyFrom(playerData);
					}
					// 明杠收分目标包含胡牌玩家，删除这个杠
					if (type == PlayType.OPERATE_GANG_A_CARD)
					{
						// 是否为胡牌玩家点杠
						boolean isDianGang = scoreData.getScoreTargetList().contains(huPlayer.getRoleId());

						if (!isDianGang || size > 0)
						{
							// 如果是胡牌玩家点杠
							if (isDianGang)
							{
								if (playerIsHu)
									continue;
								// 清空收分目标并添加当前玩家
								copyData.getScoreTargetList().clear();
								copyData.getScoreTargetList().add(player.getRoleId());
							}
							// 新建一个分数
							ScorePoint horsePoint = new ScorePoint();
							horsePoint.setCalcType(ScoreCalculateType.TOTAL_MULTI_2ND.getValue());
							horsePoint.setScoreType(PlayType.DISPLAY_BETTING_HOUSE);
							horsePoint.setScoreValue(isDianGang ? size : winCards.size());

							// 添加马跟杠分
							copyData.getPoints().add(horsePoint);
							huPlayer.getCards().getScoreDatas().add(copyData);
						}
						if (isDianGang && !playerIsHu)
						{
							// 删除杠
							player.getCards().getScoreDatas().remove(index);
						}
					}
					else
					{
						// 删除收分目标中的胡牌玩家
						if (!playerIsHu)
							scoreData.getScoreTargetList().removeIf(roleId -> {
								if (roleId == huPlayer.getRoleId())
									return true;
								return false;
							});

						// 胡牌玩家身上的杠删除掉胡牌玩家（copy关系，所以收分目标可能还有自己）
						copyData.getScoreTargetList().removeIf(roleId -> {
							if (roleId == huPlayer.getRoleId())
								return true;
							return false;
						});
						// 收分目标为空的时候删除杠分
						if (scoreData.getScoreTargetList().isEmpty())
							player.getCards().getScoreDatas().remove(index);

						// 添加到胡牌玩家身上
						if (!playerIsHu)
							huPlayer.getCards().getScoreDatas().add(copyData);

						// 如果还有剩余中马牌
						if (size > 0)
						{
							// 判断是否已经添加过
							boolean hasAdd = false;
							for (ScoreData huPlayerData : huPlayer.getCards().getScoreDatas())
							{
								if (huPlayerData.getSourceCard() == copyData.getSourceCard() && huPlayerData.getPoints().size() > 1)
									hasAdd = true;
							}
							// 如果已经添加过
							if (hasAdd)
								continue;

							ScoreData data = new ScoreData();
							data.copyFrom(copyData);
							// 添加当前玩家到收分目标中,并且当前玩家不是胡牌玩家
							if (!playerIsHu)
								data.getScoreTargetList().add(player.getRoleId());

							// 新建一个分数
							ScorePoint horsePoint = new ScorePoint();
							horsePoint.setCalcType(ScoreCalculateType.TOTAL_MULTI_2ND.getValue());
							horsePoint.setScoreType(PlayType.DISPLAY_BETTING_HOUSE);
							horsePoint.setScoreValue(playerIsHu ? winCards.size() : size);
							// 添加马跟杠分
							data.getPoints().add(horsePoint);
							huPlayer.getCards().getScoreDatas().add(data);
						}
					}

				}
			}
			// 获取下一玩家
			player = context.getPlayerById(context.getNextRoleId(player.getRoleId()));
		} while (player.getRoleId() != huPlayer.getRoleId());
	}
}
