package com.kodgames.battleserver.service.battle.common.xbean;

import java.util.ArrayList;
import java.util.List;

/**
 * Step对象，表示战斗操作数据
 */
public class Step
{
	/** 玩家Id */
	protected int roleId;
	/** 操作类型 */
	protected int playType;
	/** 操作来源玩家Id */
	protected int sourceRoleId;
	/** 优先级 */
	protected int priority;
	/** 操作卡值 */
	protected ArrayList<Byte> cards;

	public Step()
	{
		cards = new ArrayList<Byte>();
	}

	public Step(int roleId, int operateType)
	{
		this();
		this.roleId = roleId;
		this.playType = operateType;
		this.sourceRoleId = roleId;
	}

	public Step(int roleId, int operateType, Byte card)
	{
		this();
		this.roleId = roleId;
		this.playType = operateType;
		this.sourceRoleId = roleId;
		this.cards.add(card);
	}

	public Step(int roleId, int operateType, List<Byte> cardDatas)
	{
		this();
		this.roleId = roleId;
		this.playType = operateType;
		this.sourceRoleId = roleId;
		for (int i = 0; i < cardDatas.size(); ++i)
			this.cards.add(cardDatas.get(i));
	}

	public Step(Step step)
	{
		this();
		copyFrom(step);
	}

	public void copyFrom(Step step)
	{
		this.roleId = step.roleId;
		this.playType = step.playType;
		this.sourceRoleId = step.sourceRoleId;
		this.priority = step.priority;
		this.cards.clear();
		this.cards.addAll(step.cards);
	}

	public Step copy()
	{
		return new Step(this);
	}

	public int getRoleId()
	{
		return this.roleId;
	}

	public int getPlayType()
	{
		return this.playType;
	}
	
	public int getSourceRoleId()
	{
		return this.sourceRoleId;
	}

	public int getPriority()
	{
		return this.priority;
	}

	public List<Byte> getCards()
	{
		return this.cards;
	}

	public void addCard(byte card)
	{
		if (cards == null)
			cards = new ArrayList<Byte>();
		cards.add(card);
	}
	
	public void addCard(byte card, int count)
	{
		if (cards == null)
			cards = new ArrayList<Byte>();
		
		for (int index = 0; index < count; ++index)
			cards.add(card);
	}

	public void setRoleId(int roleId)
	{
		this.roleId = roleId;
	}

	public void setPlayType(int playType)
	{
		this.playType = playType;
	}
	
	public void setSourceRoleId(int sourceRoleId)
	{
		this.sourceRoleId = sourceRoleId;
	}

	public void setPriority(int priority)
	{
		this.priority = priority;
	}

	/** 设置高位优先级 */
	public void setHighPriority(int highPriority)
	{
		this.priority = (priority & 0x0000FFFF) | (highPriority << 16);
	}

	/** 设置低位优先级 */
	public void setLowPriority(int lowPriority)
	{
		priority = priority | (lowPriority & 0x0000FFFF);
	}

	@Override
	public boolean equals(Object object)
	{
		if (object instanceof Step == false)
			return false;

		Step step = (Step)object;
		if (this.roleId != step.roleId)
			return false;
		if (this.playType != step.playType)
			return false;
		if (this.sourceRoleId != step.sourceRoleId)
			return false;
		if (this.priority != step.priority)
			return false;
		if (!this.cards.equals(step.cards))
			return false;
		return true;
	}

	@Override
	public int hashCode()
	{
		int _h_ = 0;
		_h_ += _h_ * 31 + this.roleId;
		_h_ += _h_ * 31 + this.playType;
		_h_ += _h_ * 31 + this.sourceRoleId;
		_h_ += _h_ * 31 + this.priority;
		_h_ += _h_ * 31 + this.cards.hashCode();
		return _h_;
	}

	@Override
	public String toString()
	{
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.roleId).append(",");
		_sb_.append(this.playType).append(",");
		_sb_.append(this.sourceRoleId).append(",");
		_sb_.append(this.priority).append(",");
		_sb_.append(this.cards).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
