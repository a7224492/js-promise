package com.kodgames.battleserver.service.battle.common.xbean;

/** 玩家投票解散房间信息 */
public final class PlayerVoteInfo
{
	/** 玩家Id */
	private int roleId;

	/** 投票类型 RoomProtoBuf.EMVote */
	private int voteType;

	public PlayerVoteInfo()
	{
	}

	public PlayerVoteInfo(PlayerVoteInfo playerVoteInfo)
	{
		copyFrom(playerVoteInfo);
	}

	public void copyFrom(PlayerVoteInfo playerVoteInfo)
	{
		this.roleId = playerVoteInfo.roleId;
		this.voteType = playerVoteInfo.voteType;
	}

	public int getRoleId()
	{
		return roleId;
	}

	public void setRoleId(int roleId)
	{
		this.roleId = roleId;
	}

	public int getVoteType()
	{
		return voteType;
	}

	public void setVoteType(int voteType)
	{
		this.voteType = voteType;
	}

	@Override
	public final boolean equals(Object object)
	{
		if (object instanceof PlayerVoteInfo == false)
			return false;

		PlayerVoteInfo playerVoteInfo = (PlayerVoteInfo)object;
		if (this.roleId != playerVoteInfo.roleId)
			return false;
		if (this.voteType != playerVoteInfo.voteType)
			return false;
		return true;
	}

	@Override
	public int hashCode()
	{
		int _h_ = 0;
		_h_ += _h_ * 31 + this.roleId;
		_h_ += _h_ * 31 + this.voteType;
		return _h_;
	}

	@Override
	public String toString()
	{
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.roleId).append(",");
		_sb_.append(this.voteType).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}
}