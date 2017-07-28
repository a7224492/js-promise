package com.kodgames.battleserver.service.battle.common.xbean;

/***
 * 继承Step类，用于发送ScoreData给客户端，ScoreData不在Step中保存
 */
public final class Step4Display extends Step
{
	private ScoreData scoreData;

	public Step4Display()
	{
		super();
		scoreData = new ScoreData();
	}

	public Step4Display(Step step)
	{
		this();
		copyFrom(step);
	}

	public void copyFrom(Step step)
	{
		this.roleId = step.roleId;
		this.playType = step.playType;
		this.priority = step.priority;
		this.cards.clear();
		this.cards.addAll(step.cards);

		if (step instanceof Step4Display)
		{
			this.scoreData.copyFrom(((Step4Display)step).scoreData);
		}
	}

	public ScoreData getScoreData()
	{
		return this.scoreData;
	}

	@Override
	public boolean equals(Object object)
	{
		if (object instanceof Step4Display == false)
			return false;

		Step4Display step = (Step4Display)object;
		if (this.roleId != step.roleId)
			return false;
		if (this.playType != step.playType)
			return false;
		if (this.priority != step.priority)
			return false;
		if (!this.cards.equals(step.cards))
			return false;
		if (!this.scoreData.equals(step.scoreData))
			return false;
		return true;
	}

	@Override
	public int hashCode()
	{
		int _h_ = 0;
		_h_ += _h_ * 31 + this.roleId;
		_h_ += _h_ * 31 + this.playType;
		_h_ += _h_ * 31 + this.priority;
		_h_ += _h_ * 31 + this.cards.hashCode();
		_h_ += _h_ * 31 + this.scoreData.hashCode();
		return _h_;
	}

	@Override
	public String toString()
	{
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.roleId).append(",");
		_sb_.append(this.playType).append(",");
		_sb_.append(this.priority).append(",");
		_sb_.append(this.cards).append(",");
		_sb_.append(this.scoreData).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
