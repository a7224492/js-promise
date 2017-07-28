package com.kodgames.battleserver.service.battle.common.xbean;

import java.util.ArrayList;
import java.util.List;

/** 得分数据，当玩家杠胡时，会产生一个ScoreData对象存储在相应PlayerInfo的CardInfo中 */
public final class ScoreData
{
	/** True:正向添加， False:反向添加 */
	private boolean AddOperation;

	/** 得分来源于哪一个玩家 */
	private int sourceId;

	/** 得分来源于哪一张牌 */
	private byte sourceCard;

	/** 得分来源于SourceId的操作索引 */
	private int sourceRecordIndex;

	/** 得分玩家的Step索引 */
	private int stepIndex;

	/** 收分目标玩家 */
	private ArrayList<Integer> scoreTargetList;

	/** 子分项 */
	private ArrayList<ScorePoint> points;

	/** 胡牌时候存储胡牌类型 */
	private ArrayList<CardGroup> cardGroups;

	public ScoreData()
	{
		scoreTargetList = new ArrayList<Integer>();
		points = new ArrayList<ScorePoint>();
		cardGroups = new ArrayList<CardGroup>();
	}

	public ScoreData(ScoreData scoreData)
	{
		this();
		copyFrom(scoreData);
	}

	public void copyFrom(ScoreData scoreData)
	{
		this.AddOperation = scoreData.AddOperation;
		this.sourceId = scoreData.sourceId;
		this.sourceCard = scoreData.sourceCard;
		this.sourceRecordIndex = scoreData.sourceRecordIndex;
		this.stepIndex = scoreData.stepIndex;

		this.scoreTargetList.clear();
		this.scoreTargetList.addAll(scoreData.scoreTargetList);

		this.points.clear();
		scoreData.points.forEach(point -> this.points.add(new ScorePoint(point)));

		this.cardGroups.clear();
		scoreData.cardGroups.forEach(cardGroup -> this.cardGroups.add(new CardGroup(cardGroup)));
	}

	public boolean getAddOperation()
	{
		return this.AddOperation;
	}

	public int getSourceId()
	{
		return this.sourceId;
	}

	public byte getSourceCard()
	{
		return this.sourceCard;
	}

	public int getSourceRecrodIndex()
	{
		return this.sourceRecordIndex;
	}

	public List<Integer> getScoreTargetList()
	{
		return this.scoreTargetList;
	}

	public List<ScorePoint> getPoints()
	{
		return this.points;
	}

	public List<CardGroup> getCardGroups()
	{
		return this.cardGroups;
	}

	public void setAddOperation(boolean addOperation)
	{
		this.AddOperation = addOperation;
	}

	public void setSourceId(int sourceId)
	{
		this.sourceId = sourceId;
	}

	public void setSourceCard(byte sourceCard)
	{
		this.sourceCard = sourceCard;
	}

	public void setSourceRecrodIndex(int sourceRecordIndex)
	{
		this.sourceRecordIndex = sourceRecordIndex;
	}

	public int getStepIndex()
	{
		return stepIndex;
	}

	public void setStepIndex(int stepIndex)
	{
		this.stepIndex = stepIndex;
	}

	@Override
	public final boolean equals(Object object)
	{
		if (object instanceof ScoreData == false)
			return false;

		ScoreData scoreData = (ScoreData)object;
		if (this.AddOperation != scoreData.AddOperation)
			return false;
		if (this.sourceId != scoreData.sourceId)
			return false;
		if (this.sourceCard != scoreData.sourceCard)
			return false;
		if (this.sourceRecordIndex != scoreData.sourceRecordIndex)
			return false;
		if (!this.scoreTargetList.equals(scoreData.scoreTargetList))
			return false;
		if (!this.points.equals(scoreData.points))
			return false;
		if (!this.cardGroups.equals(scoreData.cardGroups))
			return false;
		return true;
	}

	@Override
	public int hashCode()
	{
		int _h_ = 0;
		_h_ += _h_ * 31 + (this.AddOperation ? 1231 : 1237);
		_h_ += _h_ * 31 + this.sourceId;
		_h_ += _h_ * 31 + (int)this.sourceCard;
		_h_ += _h_ * 31 + this.sourceRecordIndex;
		_h_ += _h_ * 31 + this.scoreTargetList.hashCode();
		_h_ += _h_ * 31 + this.points.hashCode();
		_h_ += _h_ * 31 + this.cardGroups.hashCode();
		return _h_;
	}

	@Override
	public String toString()
	{
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.AddOperation).append(",");
		_sb_.append(this.sourceId).append(",");
		_sb_.append(this.sourceCard).append(",");
		_sb_.append(this.sourceRecordIndex).append(",");
		_sb_.append(this.scoreTargetList).append(",");
		_sb_.append(this.points).append(",");
		_sb_.append(this.cardGroups).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}
}