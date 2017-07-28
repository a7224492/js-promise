package com.kodgames.battleserver.service.battle.common.xbean;

import java.util.ArrayList;
import java.util.List;

/** 牌池信息 */
public final class CardPool
{
	/** 牌池中的可用牌 */
	private ArrayList<Byte> cards;

	/** 支持的牌类型：万条筒花字 */
	private ArrayList<Byte> supportedCardTypes;

	/** 鬼牌 */
	private ArrayList<Byte> masterCards;

	/** 玩家手牌数量 */
	private int playCardCount;

	/** 牌池中需要保留牌的数量 */
	private int stayCount;

	public CardPool()
	{
		cards = new ArrayList<Byte>();
		supportedCardTypes = new ArrayList<Byte>();
		masterCards = new ArrayList<Byte>();
	}

	public CardPool(CardPool cardPool)
	{
		this();
		copyFrom(cardPool);
	}

	public void copyFrom(CardPool cardPool)
	{
		this.cards.clear();
		this.cards.addAll(cardPool.cards);
		this.stayCount = cardPool.stayCount;

		this.supportedCardTypes.clear();
		this.supportedCardTypes.addAll(cardPool.supportedCardTypes);

		this.masterCards.clear();
		this.masterCards.addAll(cardPool.masterCards);
	}

	public List<Byte> getCards()
	{
		return this.cards;
	}

	public int getStayCount()
	{
		return this.stayCount;
	}

	public List<Byte> getSupportedCardTypes()
	{
		return this.supportedCardTypes;
	}

	public List<Byte> getMasterCards()
	{
		return this.masterCards;
	}

	public void setStayCount(int stayCount)
	{
		this.stayCount = stayCount;
	}

	public int getPlayCardCount()
	{
		return playCardCount;
	}

	public void setPlayCardCount(int playCardCount)
	{
		this.playCardCount = playCardCount;
	}

	@Override
	public final boolean equals(Object object)
	{
		if (object instanceof CardPool == false)
			return false;

		CardPool cardPool = (CardPool)object;
		if (!this.cards.equals(cardPool.cards))
			return false;
		if (this.stayCount != cardPool.stayCount)
			return false;
		if (!this.supportedCardTypes.equals(cardPool.supportedCardTypes))
			return false;
		if (!this.masterCards.equals(cardPool.masterCards))
			return false;
		return true;
	}

	@Override
	public int hashCode()
	{
		int _h_ = 0;
		_h_ += _h_ * 31 + this.cards.hashCode();
		_h_ += _h_ * 31 + this.stayCount;
		_h_ += _h_ * 31 + this.supportedCardTypes.hashCode();
		_h_ += _h_ * 31 + this.masterCards.hashCode();
		return _h_;
	}

	@Override
	public String toString()
	{
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.cards).append(",");
		_sb_.append(this.stayCount).append(",");
		_sb_.append(this.supportedCardTypes).append(",");
		_sb_.append(this.masterCards).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}
}