package com.kodgames.battleserver.service.battle.common.xbean;

import java.util.List;
import java.util.Map;

/** 
 * 等待队列中的数据 
 */
public final class WaitSquence
{
	/** 已经完成的操作 */
	private java.util.ArrayList<Step> finishTypes;
	/** 玩家优先级队列，记录每一个玩家需要操作的优先级高低 */
	private java.util.HashMap<Integer, Integer> rolePriority;
	/** 未完成的操作 */
	private java.util.ArrayList<WaitStep> operators;
	/** 等待下发给客户端的消息 */
	private java.util.ArrayList<Step> displays;

	public WaitSquence()
	{
		finishTypes = new java.util.ArrayList<Step>();
		rolePriority = new java.util.HashMap<Integer, Integer>();
		operators = new java.util.ArrayList<WaitStep>();
		displays = new java.util.ArrayList<Step>();
	}

	public WaitSquence(WaitSquence waitSquence)
	{
		this();
		copyFrom(waitSquence);
	}

	public void copyFrom(WaitSquence waitSquence)
	{
		this.finishTypes.clear();
		this.rolePriority.clear();
		this.operators.clear();
		this.displays.clear();

		waitSquence.getFinishTypes().forEach(finishType -> this.finishTypes.add(new Step(finishType)));
		waitSquence.getOperators().forEach(operator -> this.operators.add(new WaitStep(operator)));
		waitSquence.getRolePriority().forEach((k, v) -> this.rolePriority.put(k, v));
		waitSquence.getDisplays().forEach(display -> this.displays.add(new Step(display)));
	}

	public List<Step> getFinishTypes()
	{
		return this.finishTypes;
	}

	public Map<Integer, Integer> getRolePriority()
	{
		return this.rolePriority;
	}

	public List<WaitStep> getOperators()
	{
		return this.operators;
	}

	public List<Step> getDisplays()
	{
		return this.displays;
	}

	@Override
	public final boolean equals(Object object)
	{
		if (object instanceof WaitSquence == false)
			return false;

		WaitSquence waitSquence = (WaitSquence)object;
		if (!this.finishTypes.equals(waitSquence.finishTypes))
			return false;
		if (!this.rolePriority.equals(waitSquence.rolePriority))
			return false;
		if (!this.operators.equals(waitSquence.operators))
			return false;
		if (!this.displays.equals(waitSquence.displays))
			return false;
		return true;
	}

	@Override
	public int hashCode()
	{
		int _h_ = 0;
		_h_ += _h_ * 31 + this.finishTypes.hashCode();
		_h_ += _h_ * 31 + this.rolePriority.hashCode();
		_h_ += _h_ * 31 + this.operators.hashCode();
		_h_ += _h_ * 31 + this.displays.hashCode();
		return _h_;
	}

	@Override
	public String toString()
	{
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.finishTypes).append(",");
		_sb_.append(this.rolePriority).append(",");
		_sb_.append(this.operators).append(",");
		_sb_.append(this.displays).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
