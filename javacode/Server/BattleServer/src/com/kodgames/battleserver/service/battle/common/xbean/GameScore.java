package com.kodgames.battleserver.service.battle.common.xbean;

/** 玩家总统计信息 */
public final class GameScore
{
	/** 统计类型 */
	private int scoreType;

	/** 正向统计 */
	private boolean addOperation;

	/** 计分 */
	private boolean calculateScorePoint;

	/** 次数 */
	private int times;

	public GameScore()
	{
	}

	public GameScore(GameScore gameScore)
	{
		copyFrom(gameScore);
	}

	public void copyFrom(GameScore gameScore)
	{
		this.scoreType = gameScore.scoreType;
		this.addOperation = gameScore.addOperation;
		this.calculateScorePoint = gameScore.calculateScorePoint;
		this.times = gameScore.times;
	}

	public int getScoreType()
	{
		return this.scoreType;
	}

	public boolean getAddOperation()
	{
		return this.addOperation;
	}

	public boolean getCalculateScorePoint()
	{
		return this.calculateScorePoint;
	}

	public int getTimes()
	{
		return this.times;
	}

	public void setScoreType(int _v_)
	{
		this.scoreType = _v_;
	}

	public void setAddOperation(boolean _v_)
	{
		this.addOperation = _v_;
	}

	public void setCalculateScorePoint(boolean _v_)
	{
		this.calculateScorePoint = _v_;
	}

	public void setTimes(int _v_)
	{
		this.times = _v_;
	}

	@Override
	public final boolean equals(Object object)
	{
		if (object instanceof GameScore == false)
			return false;

		GameScore gameScore = (GameScore)object;
		if (this.scoreType != gameScore.scoreType)
			return false;
		if (this.addOperation != gameScore.addOperation)
			return false;
		if (this.calculateScorePoint != gameScore.calculateScorePoint)
			return false;
		if (this.times != gameScore.times)
			return false;
		return true;
	}

	@Override
	public int hashCode()
	{
		int _h_ = 0;
		_h_ += _h_ * 31 + this.scoreType;
		_h_ += _h_ * 31 + (this.addOperation ? 1231 : 1237);
		_h_ += _h_ * 31 + (this.calculateScorePoint ? 1231 : 1237);
		_h_ += _h_ * 31 + this.times;
		return _h_;
	}

	@Override
	public String toString()
	{
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.scoreType).append(",");
		_sb_.append(this.addOperation).append(",");
		_sb_.append(this.calculateScorePoint).append(",");
		_sb_.append(this.times).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}
}