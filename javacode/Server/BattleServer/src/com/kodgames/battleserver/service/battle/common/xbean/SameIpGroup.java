package com.kodgames.battleserver.service.battle.common.xbean;

import java.util.HashSet;
import java.util.Set;

/** 相同IP的玩家组 */
public final class SameIpGroup
{
	/** 玩家Id */
	private Set<Integer> players;

	public SameIpGroup()
	{
		players = new HashSet<>();
	}

	public SameIpGroup(SameIpGroup sameIpGroup)
	{
		this();
		copyFrom(sameIpGroup);
	}

	public void copyFrom(SameIpGroup sameIpGroup)
	{
		this.players.clear();
		this.players.addAll(sameIpGroup.players);
	}

	public java.util.Set<Integer> getPlayers()
	{
		return this.players;
	}

	@Override
	public final boolean equals(Object object)
	{
		if (object instanceof SameIpGroup == false)
			return false;

		SameIpGroup sameIpGroup = (SameIpGroup)object;
		if (!this.players.equals(sameIpGroup.players))
			return false;
		return true;
	}

	@Override
	public int hashCode()
	{
		int _h_ = 0;
		_h_ += _h_ * 31 + this.players.hashCode();
		return _h_;
	}

	@Override
	public String toString()
	{
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.players).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}
}