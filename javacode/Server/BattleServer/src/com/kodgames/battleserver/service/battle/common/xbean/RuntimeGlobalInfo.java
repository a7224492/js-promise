package com.kodgames.battleserver.service.battle.common.xbean;

/** 全局统计信息 */
public final class RuntimeGlobalInfo
{
	/** 房间总数 */
	private int totalRoomCount;

	/** 活跃房间总数 */
	private int positiveRoomCount;

	/** 沉默房间总数 */
	private int silentRoomCount;

	public RuntimeGlobalInfo()
	{
	}

	public RuntimeGlobalInfo(RuntimeGlobalInfo runtimeGlobalInfo)
	{
		copyFrom(runtimeGlobalInfo);
	}

	public void copyFrom(RuntimeGlobalInfo runtimeGlobalInfo)
	{
		this.totalRoomCount = runtimeGlobalInfo.totalRoomCount;
		this.positiveRoomCount = runtimeGlobalInfo.positiveRoomCount;
		this.silentRoomCount = runtimeGlobalInfo.silentRoomCount;
	}

	public int getTotalRoomCount()
	{
		return this.totalRoomCount;
	}

	public int getPositiveRoomCount()
	{
		return this.positiveRoomCount;
	}

	public int getSilentRoomCount()
	{
		return this.silentRoomCount;
	}

	public void setTotalRoomCount(int totalRoomCount)
	{
		this.totalRoomCount = totalRoomCount;
	}

	public void setPositiveRoomCount(int positiveRoomCount)
	{
		this.positiveRoomCount = positiveRoomCount;
	}

	public void setSilentRoomCount(int silentRoomCount)
	{
		this.silentRoomCount = silentRoomCount;
	}

	@Override
	public final boolean equals(Object object)
	{
		if (object instanceof RuntimeGlobalInfo == false)
			return false;

		RuntimeGlobalInfo runtimeGlobalInfo = (RuntimeGlobalInfo)object;
		if (this.totalRoomCount != runtimeGlobalInfo.totalRoomCount)
			return false;
		if (this.positiveRoomCount != runtimeGlobalInfo.positiveRoomCount)
			return false;
		if (this.silentRoomCount != runtimeGlobalInfo.silentRoomCount)
			return false;
		return true;
	}

	@Override
	public int hashCode()
	{
		int _h_ = 0;
		_h_ += _h_ * 31 + this.totalRoomCount;
		_h_ += _h_ * 31 + this.positiveRoomCount;
		_h_ += _h_ * 31 + this.silentRoomCount;
		return _h_;
	}

	@Override
	public String toString()
	{
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.totalRoomCount).append(",");
		_sb_.append(this.positiveRoomCount).append(",");
		_sb_.append(this.silentRoomCount).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}
}