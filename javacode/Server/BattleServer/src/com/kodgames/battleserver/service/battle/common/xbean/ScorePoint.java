package com.kodgames.battleserver.service.battle.common.xbean;

import com.kodgames.message.proto.battle.BattleProtoBuf.ResultScorePROTO;

/** 具体的分值 */
public final class ScorePoint
{
	/** 计分类型 */
	private int scoreType;
	/** 分值 */
	private int scoreValue;
	/** 计算类型，加/乘/乘方 */
	private int calcType;

	public ScorePoint()
	{
	}

	public ScorePoint(ScorePoint scorePoint)
	{
		copyFrom(scorePoint);
	}

	public void copyFrom(ScorePoint scorePoint)
	{
		this.scoreType = scorePoint.scoreType;
		this.scoreValue = scorePoint.scoreValue;
		this.calcType = scorePoint.calcType;
	}

	public int getScoreType()
	{
		return this.scoreType;
	}

	public int getScoreValue()
	{
		return this.scoreValue;
	}

	public int getCalcType()
	{
		return this.calcType;
	}

	public void setScoreType(int scoreType)
	{
		this.scoreType = scoreType;
	}

	public void setScoreValue(int scoreValue)
	{
		this.scoreValue = scoreValue;
	}

	public void setCalcType(int calcType)
	{
		this.calcType = calcType;
	}

	@Override
	public final boolean equals(Object object)
	{
		if (object instanceof ScorePoint == false)
			return false;

		ScorePoint scorePoint = (ScorePoint)object;
		if (this.scoreType != scorePoint.scoreType)
			return false;
		if (this.scoreValue != scorePoint.scoreValue)
			return false;
		if (this.calcType != scorePoint.calcType)
			return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		int _h_ = 0;
		_h_ += _h_ * 31 + this.scoreType;
		_h_ += _h_ * 31 + this.scoreValue;
		_h_ += _h_ * 31 + this.calcType;
		return _h_;
	}

	@Override
	public String toString()
	{
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.scoreType).append(",");
		_sb_.append(this.scoreValue).append(",");
		_sb_.append(this.calcType).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public ResultScorePROTO toResultScoreProto()
	{
		ResultScorePROTO.Builder scoreProto = ResultScorePROTO.newBuilder();
		scoreProto.setType(this.getScoreType());
		scoreProto.setCalcType(this.getCalcType());
		scoreProto.setPoint(this.getScoreValue());

		return scoreProto.build();
	}
}