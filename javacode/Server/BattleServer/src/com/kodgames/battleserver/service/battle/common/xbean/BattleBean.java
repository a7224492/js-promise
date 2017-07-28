package com.kodgames.battleserver.service.battle.common.xbean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;

/**
 * 每局战斗数据
 */
public final class BattleBean
{
	/**
	 * 房间id
	 */
	private int roomId;

	/**
	 * 是否正在战斗
	 */
	private boolean isRunning;

	/**
	 * 开始战斗时间
	 */
	private long startTime;

	/**
	 * 战斗结束时间
	 */
	private long endTime;

	/**
	 * 庄家id
	 */
	private int zhuang;

	/**
	 * 战斗结束后存储下一局庄家id
	 */
	private int nextZhuang;

	/**
	 * 战斗状态 对应MahjongConstant.BattleState
	 */
	private int battleState;

	/**
	 * 房间规则
	 */
	private ArrayList<Integer> gameRules;

	/**
	 * 房间内玩家id
	 */
	private ArrayList<Integer> playerIds;

	/**
	 * 房间内玩家
	 */
	private HashMap<Integer, PlayerInfo> players;

	/**
	 * 等待队列
	 */
	private WaitSquence waitSquence;

	/**
	 * 牌池信息
	 */
	private CardPool cardPool;

	/**
	 * 战斗记录，只记录有效记录 比如可以胡，可以碰等信息不回存储，只有真正进行操作的时候才会存储
	 */
	private ArrayList<Step> records;

	/**
	 * 胡牌记录对应的record的索引
	 */
	private ArrayList<Integer> huIndex;

	public BattleBean()
	{
		gameRules = new ArrayList<Integer>();
		playerIds = new ArrayList<Integer>();
		players = new HashMap<Integer, PlayerInfo>();
		waitSquence = new WaitSquence();
		cardPool = new CardPool();
		records = new ArrayList<Step>();
		huIndex = new ArrayList<Integer>();
	}

	public BattleBean(BattleBean battleBean)
	{
		this();
		copyFrom(battleBean);
	}

	public void copyFrom(BattleBean battleBean)
	{
		this.roomId = battleBean.roomId;
		this.isRunning = battleBean.isRunning;
		this.startTime = battleBean.startTime;
		this.endTime = battleBean.endTime;
		this.zhuang = battleBean.zhuang;
		this.nextZhuang = battleBean.nextZhuang;
		this.battleState = battleBean.battleState;

		this.waitSquence.copyFrom(battleBean.waitSquence);
		this.cardPool.copyFrom(battleBean.cardPool);

		this.playerIds.clear();
		this.playerIds.addAll(battleBean.playerIds);

		this.players.clear();
		battleBean.players.forEach((k, v) -> this.players.put(k, new PlayerInfo(v)));

		this.gameRules.clear();
		this.gameRules.addAll(battleBean.gameRules);

		this.records.clear();
		battleBean.records.forEach(record -> this.records.add(new Step(record)));

		this.huIndex.clear();
		this.huIndex.addAll(battleBean.huIndex);
	}

	public int getRoomId()
	{
		return this.roomId;
	}

	public WaitSquence getWaitSquence()
	{
		return this.waitSquence;
	}

	public HashMap<Integer, PlayerInfo> getPlayers()
	{
		return players;
	}

	public PlayerInfo getPlayerById(int roleId)
	{
		return this.players.get(roleId);
	}

	public List<Integer> getPlayerIds()
	{
		return this.playerIds;
	}

	public List<Integer> getGameRules()
	{
		return this.gameRules;
	}

	public CardPool getCardPool()
	{
		return this.cardPool;
	}

	public boolean getIsRunning()
	{
		return this.isRunning;
	}

	public long getEndTime()
	{
		return this.endTime;
	}

	public long getStartTime()
	{
		return this.startTime;
	}

	public int getZhuang()
	{
		return this.zhuang;
	}

	public int getNextZhuang()
	{
		return this.nextZhuang;
	}

	public List<Step> getRecords()
	{
		return this.records;
	}

	public int getBattleState()
	{
		return this.battleState;
	}

	public List<Integer> getHuIndex()
	{
		return this.huIndex;
	}

	public void setRoomId(int roomId)
	{
		this.roomId = roomId;
	}

	public void setIsRunning(boolean isRunning)
	{
		this.isRunning = isRunning;
	}

	public void setStartTime(long startTime)
	{
		this.startTime = startTime;
	}

	public void setEndTime(long endTime)
	{
		this.endTime = endTime;
	}

	public void setZhuang(int zhuang)
	{
		this.zhuang = zhuang;
	}

	public void setNextZhuang(int nextZhuang)
	{
		this.nextZhuang = nextZhuang;
	}

	public void setBattleState(int battleState)
	{
		this.battleState = battleState;
	}

	@Override
	public final boolean equals(Object object)
	{
		if (object instanceof BattleBean == false)
			return false;

		BattleBean battleBean = (BattleBean)object;
		if (this.roomId != battleBean.roomId)
			return false;
		if (!this.waitSquence.equals(battleBean.waitSquence))
			return false;
		if (!this.playerIds.equals(battleBean.playerIds))
			return false;
		if (!this.players.equals(battleBean.players))
			return false;
		if (!this.gameRules.equals(battleBean.gameRules))
			return false;
		if (!this.cardPool.equals(battleBean.cardPool))
			return false;
		if (this.isRunning != battleBean.isRunning)
			return false;
		if (this.startTime != battleBean.startTime)
			return false;
		if (this.zhuang != battleBean.zhuang)
			return false;
		if (this.nextZhuang != battleBean.nextZhuang)
			return false;
		if (!this.records.equals(battleBean.records))
			return false;
		if (this.battleState != battleBean.battleState)
			return false;
		if (!this.huIndex.equals(battleBean.huIndex))
			return false;
		return true;
	}

	@Override
	public int hashCode()
	{
		int _h_ = 0;
		_h_ += _h_ * 31 + this.roomId;
		_h_ += _h_ * 31 + this.waitSquence.hashCode();
		_h_ += _h_ * 31 + this.playerIds.hashCode();
		_h_ += _h_ * 31 + this.players.hashCode();
		_h_ += _h_ * 31 + this.gameRules.hashCode();
		_h_ += _h_ * 31 + this.cardPool.hashCode();
		_h_ += _h_ * 31 + (this.isRunning ? 1231 : 1237);
		_h_ += _h_ * 31 + (int)(this.startTime ^ (this.startTime >>> 32));
		_h_ += _h_ * 31 + this.zhuang;
		_h_ += _h_ * 31 + this.nextZhuang;
		_h_ += _h_ * 31 + this.records.hashCode();
		_h_ += _h_ * 31 + this.battleState;
		_h_ += _h_ * 31 + this.huIndex.hashCode();
		return _h_;
	}

	@Override
	public String toString()
	{
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.roomId).append(",");
		_sb_.append(this.waitSquence).append(",");
		_sb_.append(this.playerIds).append(",");
		_sb_.append(this.players).append(",");
		_sb_.append(this.gameRules).append(",");
		_sb_.append(this.cardPool).append(",");
		_sb_.append(this.isRunning).append(",");
		_sb_.append(this.startTime).append(",");
		_sb_.append(this.zhuang).append(",");
		_sb_.append(this.nextZhuang).append(",");
		_sb_.append(this.records).append(",");
		_sb_.append(this.battleState).append(",");
		_sb_.append(this.huIndex).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	/**
	 * 判定玩家是否有胡牌信息
	 */
	public boolean hasHuInfo(int roleId)
	{
		for (int recordIndex : getHuIndex())
		{
			if (getRecords().get(recordIndex).getRoleId() == roleId)
				return true;
		}

		return false;
	}

	/**
	 * 获取玩家胡牌次数
	 */
	public List<Integer> getHuRecordIndices(int roleId)
	{
		List<Integer> huRecordIndices = new ArrayList<Integer>();
		for (int recordIndex : getHuIndex())
		{
			if (getRecords().get(recordIndex).getRoleId() == roleId)
				huRecordIndices.add(recordIndex);
		}

		return huRecordIndices;
	}

	public CardInfo getCardInfo(int roleId)
	{
		return getPlayers().get(roleId).getCards();
	}

	public int getPlayerSize()
	{
		return getPlayerIds().size();
	}

	/**
	 * 保存玩家操作
	 */
	public void saveRecord(Step step)
	{
		Step record = new Step();
		record.copyFrom(step);

		// 操作保存在统一结构中, 玩家身上保存相应的编号
		int roleId = record.getRoleId();
		int recordSize = records.size();
		CardInfo cardInfo = getPlayers().get(roleId).getCards();

		records.add(record);
		cardInfo.getRecordIndices().add(recordSize);

		// 保存记录后的附加操作
		switch (step.getPlayType())
		{
			case PlayType.OPERATE_HU:
				cardInfo.setPlayRound(cardInfo.getPlayRound() + 1);
				huIndex.add(recordSize);
				break;
			case PlayType.OPERATE_PLAY_A_CARD:
			case PlayType.OPERATE_PLAY_A_CARD_HIDE:
			case PlayType.OPERATE_DEAL:
			case PlayType.OPERATE_CHI_A_CARD:
			case PlayType.OPERATE_PENG_A_CARD:
			case PlayType.OPERATE_GANG_A_CARD:
			case PlayType.OPERATE_BU_GANG_A_CARD:
			case PlayType.OPERATE_AN_GANG:
			case PlayType.OPERATE_TING_CARD:
				cardInfo.setPlayRound(cardInfo.getPlayRound() + 1);
				break;
		}
	}

	/**
	 * 倒序获取牌局的最后操作
	 */
	public Step getLastRecordStep(int lastIndex)
	{
		if (getRecords().size() <= lastIndex)
			return null;

		return getRecords().get(getRecords().size() - 1 - lastIndex);
	}

	/**
	 * 获取玩家的最后一个操作
	 */
	public Step getPlayerLastRecord(int roleId)
	{
		List<Integer> records = getPlayers().get(roleId).getCards().getRecordIndices();
		if (records.size() <= 0)
			return null;
		else
			return getRecords().get(records.get(records.size() - 1));
	}

	/**
	 * 判定是否含有这种操作记录
	 */
	public boolean hasCardHeap(int roleId, int playType)
	{
		CardInfo cardInfo = getPlayers().get(roleId).getCards();
		Iterator<Step> iterator = cardInfo.getCardHeap().iterator();
		while (iterator.hasNext())
		{
			if (iterator.next().getPlayType() == playType)
				return true;
		}

		return false;
	}

	/**
	 * 获取这种操作记录
	 */
	public Step getCardHeap(int roleId, int playType)
	{
		CardInfo cardInfo = getPlayers().get(roleId).getCards();
		Iterator<Step> iterator = cardInfo.getCardHeap().iterator();
		while (iterator.hasNext())
		{
			Step step = iterator.next();
			if (step.getPlayType() == playType)
				return step;
		}

		return null;
	}

	/**
	 * 添加一张牌到已出牌队列
	 */
	public void addOutCard(int roleId, byte card)
	{
		CardInfo cardInfo = getPlayers().get(roleId).getCards();
		cardInfo.getOutCards().add(card);
	}

	/**
	 * 添加手牌
	 */
	public void addHandCard(int roleId, byte card)
	{
		CardInfo cardInfo = getPlayers().get(roleId).getCards();
		cardInfo.getHandCards().add(0, card);
	}

	/**
	 * 移除一张手牌
	 */
	public void removeHandCard(int roleId, byte card)
	{
		removeHandCard(roleId, card, 1);
	}

	/**
	 * 移除指定张数的手牌
	 */
	public void removeHandCard(int roleId, byte card, int count)
	{
		CardInfo cardInfo = getPlayers().get(roleId).getCards();
		for (int i = 0; i < count; i++)
			cardInfo.getHandCards().remove(Byte.valueOf(card));
	}

	/**
	 * 从已出牌队列中移除一张手牌
	 */
	public void removeOutCard(int roleId, byte card)
	{
		CardInfo cardInfo = getPlayers().get(roleId).getCards();
		cardInfo.getOutCards().remove(Byte.valueOf(card));
	}

	public void addCardHeap(int roleId, int playType)
	{
		addCardHeap(roleId, playType, (byte)0, 0);
	}

	public void addCardHeap(int roleId, int playType, byte card)
	{
		addCardHeap(roleId, playType, card, 1);
	}

	/**
	 * 添加一组牌到牌堆中
	 */
	public void addCardHeap(int roleId, int playType, byte card, int count)
	{
		Step heap = new Step();
		heap.setRoleId(roleId);
		heap.setPlayType(playType);
		for (int i = 0; i < count; i++)
			heap.getCards().add(card);
		CardInfo cardInfo = getPlayers().get(roleId).getCards();
		cardInfo.getCardHeap().add(heap);
	}

	/**
	 * 添加一组牌到牌堆中
	 */
	public void addCardHeap(int roleId, int playType, List<Byte> cards)
	{
		Step heap = new Step();
		heap.setRoleId(roleId);
		heap.setPlayType(playType);
		heap.getCards().addAll(cards);
		CardInfo cardInfo = getPlayers().get(roleId).getCards();
		cardInfo.getCardHeap().add(heap);
	}

	/**
	 * 添加一组牌到牌堆中
	 */
	public void addCardHeap(Step step)
	{
		getPlayers().get(step.getRoleId()).getCards().getCardHeap().add(new Step(step));
	}

	/**
	 * 从牌堆中删除指定牌
	 */
	public void removeCardHeap(int roleId, int playType, byte card)
	{
		CardInfo cardInfo = getPlayers().get(roleId).getCards();
		Iterator<Step> iterator = cardInfo.getCardHeap().iterator();
		while (iterator.hasNext())
		{
			Step step = iterator.next();
			if (step.getPlayType() == playType && step.getCards().get(0) == card)
			{
				iterator.remove();
				break;
			}
		}
	}

	/**
	 * 获取玩家鬼牌
	 */
	public List<Byte> getPlayerMasterCards(int roleId)
	{
		return getPlayerById(roleId).getCards().getMasterCards();
	}

	/**
	 * 设置玩家鬼牌
	 */
	public void setPlayerMasterCard(int roleId, List<Byte> masterCard)
	{
		if (masterCard == null)
			return;

		CardInfo cardInfo = getPlayerById(roleId).getCards();
		for (byte card : masterCard)
			cardInfo.getMasterCards().add(card);
	}

	/**
	 * 设置玩家鬼牌
	 */
	public void setPlayerMasterCard(int roleId, byte... masterCard)
	{
		if (masterCard == null)
			return;

		CardInfo cardInfo = getPlayerById(roleId).getCards();
		for (byte card : masterCard)
			cardInfo.getMasterCards().add(card);
	}

	/**
	 * 获取scoreData
	 *
	 * @param roleId      玩家id
	 * @param recordIndex 操作索引
	 * @return
	 */
	public ScoreData getScoreData(int roleId, int recordIndex)
	{
		for (ScoreData scoreData : getPlayerById(roleId).getCards().getScoreDatas())
		{
			if (scoreData.getStepIndex() == recordIndex)
				return scoreData;
		}

		return null;
	}

	/**
	 * 获取玩家逆时针下一玩家
	 */
	public int getNextRoleId(int roleId)
	{
		List<Integer> roleIds = getPlayerIds();
		int index = roleIds.indexOf(roleId);
		if (index == roleIds.size() - 1)
			index = 0;
		else
			index += 1;

		return roleIds.get(index);
	}

	/**
	 * 获取玩家顺时针下一个玩家
	 */
	public int getPreRoleId(int roleId)
	{
		List<Integer> roleIds = getPlayerIds();
		int index = roleIds.indexOf(roleId);
		if (index == 0)
			index = roleIds.size() - 1;
		else
			index -= 1;

		return roleIds.get(index);
	}

	/**
	 * 获取玩家对家
	 */
	public int getOppositeRoleId(int roleId)
	{
		int oppositeRoleId = getPreRoleId(roleId);
		oppositeRoleId = getPreRoleId(oppositeRoleId);
		return oppositeRoleId;
	}
}