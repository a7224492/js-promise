package com.kodgames.battleserver.service.battle.common.xbean;

import java.util.List;

public class WaitStep
{
	/** 玩家Id */
	private int roleId;
	/** 玩家可以选择的操作,Step的操作类型为Can类型 */
	private java.util.ArrayList<Step> steps;

	public WaitStep()
	{
		this.steps = new java.util.ArrayList<Step>();
	}

	public WaitStep(WaitStep waitStep)
	{
		this();
		copyFrom(waitStep);
	}

	public void copyFrom(WaitStep waitStep)
	{
		this.roleId = waitStep.roleId;
		this.steps.clear();
		waitStep.getSteps().forEach(step -> steps.add(new Step(step)));
	}

	public int getRoleId()
	{
		return this.roleId;
	}

	public List<Step> getSteps()
	{
		return this.steps;
	}

	public void setRoleId(int roleId)
	{
		this.roleId = roleId;
	}

	@Override
	public final boolean equals(Object object)
	{
		if (object instanceof WaitStep == false)
			return false;

		WaitStep waitStep = (WaitStep)object;
		if (this.roleId != waitStep.roleId)
			return false;
		if (!this.steps.equals(waitStep.steps))
			return false;
		return true;
	}

	@Override
	public int hashCode()
	{
		int _h_ = 0;
		_h_ += _h_ * 31 + this.roleId;
		_h_ += _h_ * 31 + this.steps.hashCode();
		return _h_;
	}

	@Override
	public String toString()
	{
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.roleId).append(",");
		_sb_.append(this.steps).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
