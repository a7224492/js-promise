package com.kodgames.battleserver.service.battle.common.xbean;

import java.util.ArrayList;
import java.util.List;

/** 房间投票解散信息 */
public final class RoomVoteInfo
{
	/** 发起投票的玩家id */
	private int applicant;

	/** 开始投票的时间 */
	private long startTime;

	/** 玩家投票信息 */
	private ArrayList<PlayerVoteInfo> playerVoteList;

	public RoomVoteInfo()
	{
		playerVoteList = new ArrayList<PlayerVoteInfo>();
	}

	public RoomVoteInfo(RoomVoteInfo roomVoteInfo)
	{
		this();
		copyFrom(roomVoteInfo);
	}

	public void copyFrom(RoomVoteInfo roomVoteInfo)
	{
		this.applicant = roomVoteInfo.applicant;
		this.startTime = roomVoteInfo.startTime;
		this.playerVoteList.clear();
		roomVoteInfo.playerVoteList.forEach(playerVote -> this.playerVoteList.add(new PlayerVoteInfo(playerVote)));
	}

	public int getApplicant()
	{
		return this.applicant;
	}

	public long getStartTime()
	{
		return this.startTime;
	}

	public List<PlayerVoteInfo> getPlayerVoteList()
	{
		return this.playerVoteList;
	}

	public void setApplicant(int applicant)
	{
		this.applicant = applicant;
	}

	public void setStartTime(long startTime)
	{
		this.startTime = startTime;
	}

	@Override
	public final boolean equals(Object object)
	{
		if (object instanceof RoomVoteInfo == false)
			return false;

		RoomVoteInfo roomVoteInfo = (RoomVoteInfo)object;
		if (this.applicant != roomVoteInfo.applicant)
			return false;
		if (this.startTime != roomVoteInfo.startTime)
			return false;
		if (!this.playerVoteList.equals(roomVoteInfo.playerVoteList))
			return false;
		return true;
	}

	@Override
	public int hashCode()
	{
		int _h_ = 0;
		_h_ += _h_ * 31 + this.applicant;
		_h_ += _h_ * 31 + (int)(this.startTime ^ (this.startTime >>> 32));
		_h_ += _h_ * 31 + this.playerVoteList.hashCode();
		return _h_;
	}

	@Override
	public String toString()
	{
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.applicant).append(",");
		_sb_.append(this.startTime).append(",");
		_sb_.append(this.playerVoteList).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}
}