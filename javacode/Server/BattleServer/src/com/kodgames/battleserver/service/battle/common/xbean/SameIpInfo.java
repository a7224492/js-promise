package com.kodgames.battleserver.service.battle.common.xbean;

import java.util.HashMap;
import java.util.Map;

/** 相同IP检测 */
public final class SameIpInfo
{
	/** 是否需要报警 */
	private boolean isNeedAlert;

	/** <IP, 同IP玩家Id> */
	private HashMap<String, SameIpGroup> sameGroups;

	public SameIpInfo()
	{
		sameGroups = new HashMap<String, SameIpGroup>();
	}

	public SameIpInfo(SameIpInfo sameIpInfo)
	{
		this();
		copyFrom(sameIpInfo);
	}

	public void copyFrom(SameIpInfo sameIpInfo)
	{
		this.isNeedAlert = sameIpInfo.isNeedAlert;
		this.sameGroups.clear();
		sameIpInfo.sameGroups.forEach((k, v) -> this.sameGroups.put(k, new SameIpGroup(v)));
	}

	public boolean getIsNeedAlert()
	{
		return this.isNeedAlert;
	}

	public Map<String, SameIpGroup> getSameGroups()
	{
		return this.sameGroups;
	}

	public void setIsNeedAlert(boolean isNeedAlert)
	{
		this.isNeedAlert = isNeedAlert;
	}

	@Override
	public final boolean equals(Object object)
	{
		if (object instanceof SameIpInfo == false)
			return false;

		SameIpInfo sameIpInfo = (SameIpInfo)object;
		if (this.isNeedAlert != sameIpInfo.isNeedAlert)
			return false;
		if (!this.sameGroups.equals(sameIpInfo.sameGroups))
			return false;
		return true;
	}

	@Override
	public int hashCode()
	{
		int _h_ = 0;
		_h_ += _h_ * 31 + (this.isNeedAlert ? 1231 : 1237);
		_h_ += _h_ * 31 + this.sameGroups.hashCode();
		return _h_;
	}

	@Override
	public String toString()
	{
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.isNeedAlert).append(",");
		_sb_.append(this.sameGroups).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}
}