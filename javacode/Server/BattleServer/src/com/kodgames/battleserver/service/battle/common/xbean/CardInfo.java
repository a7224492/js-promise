package com.kodgames.battleserver.service.battle.common.xbean;

import java.util.ArrayList;
import java.util.List;

/** 玩家牌的信息 */
public final class CardInfo
{
	/** 手牌 */
	private ArrayList<Byte> handCards;

	/** 额外牌（花牌） */
	private ArrayList<Byte> exCards;

	/** 出牌 */
	private ArrayList<Byte> outCards;

	/** 玩家鬼牌 */
	private ArrayList<Byte> masterCards;

	/** 特殊操作，比如吃碰杠 */
	private ArrayList<Step> cardHeap;

	/** BattleBean的records中这个玩家操作的索引记录 */
	private ArrayList<Integer> recordIndices;

	/** 得分数据，玩家杠牌胡牌之后产生的分数 */
	private ArrayList<ScoreData> scoreDatas;

	/** 玩家的总统计，比如自摸次数，胡牌次数等等 */
	private ArrayList<GameScore> gameScore;

	/** 玩家过牌信息 */
	private ArrayList<PassInfo> passInfos;

	/** 玩家自己的圈数 */
	private int playRound;

	public CardInfo()
	{
		handCards = new ArrayList<Byte>();
		exCards = new ArrayList<Byte>();
		outCards = new ArrayList<Byte>();
		masterCards = new ArrayList<Byte>();
		cardHeap = new ArrayList<Step>();
		recordIndices = new ArrayList<Integer>();
		scoreDatas = new ArrayList<ScoreData>();
		gameScore = new ArrayList<GameScore>();
		passInfos = new ArrayList<PassInfo>();
	}

	public CardInfo(CardInfo cardInfo)
	{
		this();
		copyFrom(cardInfo);
	}

	public void copyFrom(CardInfo cardInfo)
	{
		this.handCards.clear();
		this.handCards.addAll(cardInfo.handCards);

		this.exCards.clear();
		this.exCards.addAll(cardInfo.exCards);

		this.outCards.clear();
		this.outCards.addAll(cardInfo.outCards);

		this.masterCards.clear();
		this.masterCards.addAll(cardInfo.masterCards);

		this.cardHeap.clear();
		cardInfo.cardHeap.forEach(_v_ -> this.cardHeap.add(new Step(_v_)));

		this.recordIndices.clear();
		this.recordIndices.addAll(cardInfo.recordIndices);

		this.scoreDatas.clear();
		cardInfo.scoreDatas.forEach(_v_ -> this.scoreDatas.add(new ScoreData(_v_)));

		this.gameScore.clear();
		cardInfo.gameScore.forEach(_v_ -> this.gameScore.add(new GameScore(_v_)));

		this.passInfos.clear();
		cardInfo.passInfos.forEach(_v_ -> this.passInfos.add(new PassInfo(_v_)));

		this.playRound = cardInfo.playRound;
	}

	public List<Byte> getHandCards()
	{
		return this.handCards;
	}

	public List<Byte> getExCards()
	{
		return this.exCards;
	}

	public List<Byte> getOutCards()
	{
		return this.outCards;
	}

	public List<Byte> getMasterCards()
	{
		return this.masterCards;
	}

	public List<Step> getCardHeap()
	{
		return this.cardHeap;
	}

	public List<Integer> getRecordIndices()
	{
		return this.recordIndices;
	}

	public List<ScoreData> getScoreDatas()
	{
		return this.scoreDatas;
	}

	public List<GameScore> getGameScore()
	{
		return this.gameScore;
	}

	public List<PassInfo> getPassInfos()
	{
		return this.passInfos;
	}

	public int getPlayRound()
	{
		return this.playRound;
	}

	public void setPlayRound(int playSound)
	{
		this.playRound = playSound;
	}

	@Override

	public final boolean equals(Object object)
	{
		if (object instanceof CardInfo == false)
			return false;

		CardInfo cardInfo = (CardInfo)object;
		if (!this.handCards.equals(cardInfo.handCards))
			return false;
		if (!this.exCards.equals(cardInfo.exCards))
			return false;
		if (!this.outCards.equals(cardInfo.outCards))
			return false;
		if (!this.masterCards.equals(cardInfo.masterCards))
			return false;
		if (!this.cardHeap.equals(cardInfo.cardHeap))
			return false;
		if (!this.recordIndices.equals(cardInfo.recordIndices))
			return false;
		if (!this.scoreDatas.equals(cardInfo.scoreDatas))
			return false;
		if (!this.gameScore.equals(cardInfo.gameScore))
			return false;
		if (!this.passInfos.equals(cardInfo.passInfos))
			return false;
		if (this.playRound != cardInfo.playRound)
			return false;
		return true;
	}

	@Override
	public int hashCode()
	{
		int _h_ = 0;
		_h_ += _h_ * 31 + this.handCards.hashCode();
		_h_ += _h_ * 31 + this.exCards.hashCode();
		_h_ += _h_ * 31 + this.outCards.hashCode();
		_h_ += _h_ * 31 + this.masterCards.hashCode();
		_h_ += _h_ * 31 + this.cardHeap.hashCode();
		_h_ += _h_ * 31 + this.recordIndices.hashCode();
		_h_ += _h_ * 31 + this.scoreDatas.hashCode();
		_h_ += _h_ * 31 + this.gameScore.hashCode();
		_h_ += _h_ * 31 + this.passInfos.hashCode();
		_h_ += _h_ * 31 + this.playRound;
		return _h_;
	}

	@Override
	public String toString()
	{
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.handCards).append(",");
		_sb_.append(this.exCards).append(",");
		_sb_.append(this.outCards).append(",");
		_sb_.append(this.masterCards).append(",");
		_sb_.append(this.cardHeap).append(",");
		_sb_.append(this.recordIndices).append(",");
		_sb_.append(this.scoreDatas).append(",");
		_sb_.append(this.gameScore).append(",");
		_sb_.append(this.passInfos).append(",");
		_sb_.append(this.playRound).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}
}