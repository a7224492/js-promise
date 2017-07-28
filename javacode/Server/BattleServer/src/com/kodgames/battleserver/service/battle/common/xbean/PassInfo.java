package com.kodgames.battleserver.service.battle.common.xbean;

import java.util.ArrayList;
import java.util.List;

/** 玩家过牌信息 */
public final class PassInfo
{
	/** 过的类型，比如过胡，类型为OPERATE_CAN_HU */
	private int playType;

	/**
	 * 玩家的这个过的操作来源于哪里，比如A出牌，B可以碰，B选择过之后，产生了一个PassInfo信息，sourceIndex为A出牌这个操作在 BattleBean的Records的索引
	 */
	private int sourceIndex;

	/** 玩家选择过的这个操作发生在玩家自己的哪一圈 */
	private int playRound;

	/** 玩家选择过的牌 */
	private ArrayList<Byte> card;

	public PassInfo()
	{
		card = new ArrayList<Byte>();
	}

	public PassInfo(PassInfo passInfo)
	{
		this();
		copyFrom(passInfo);
	}

	public void copyFrom(PassInfo passInfo)
	{
		this.playType = passInfo.playType;
		this.sourceIndex = passInfo.sourceIndex;
		this.playRound = passInfo.playRound;
		this.card.clear();
		this.card.addAll(passInfo.card);
	}

	public int getPlayType()
	{
		return this.playType;
	}

	public List<Byte> getCard()
	{
		return this.card;
	}

	public int getSourceIndex()
	{
		return this.sourceIndex;
	}

	public int getPlayRound()
	{
		return this.playRound;
	}

	public void setPlayType(int playType)
	{
		this.playType = playType;
	}

	public void setSourceIndex(int sourceIndex)
	{
		this.sourceIndex = sourceIndex;
	}

	public void setPlayRound(int playRound)
	{
		this.playRound = playRound;
	}

	@Override
	public final boolean equals(Object object)
	{
		if (object instanceof PassInfo == false)
			return false;

		PassInfo passInfo = (PassInfo)object;
		if (this.playType != passInfo.playType)
			return false;
		if (!this.card.equals(passInfo.card))
			return false;
		if (this.sourceIndex != passInfo.sourceIndex)
			return false;
		if (this.playRound != passInfo.playRound)
			return false;
		return true;
	}

	@Override
	public int hashCode()
	{
		int _h_ = 0;
		_h_ += _h_ * 31 + this.playType;
		_h_ += _h_ * 31 + this.card.hashCode();
		_h_ += _h_ * 31 + this.sourceIndex;
		_h_ += _h_ * 31 + this.playRound;
		return _h_;
	}

	@Override
	public String toString()
	{
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.playType).append(",");
		_sb_.append(this.card).append(",");
		_sb_.append(this.sourceIndex).append(",");
		_sb_.append(this.playRound).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}
}