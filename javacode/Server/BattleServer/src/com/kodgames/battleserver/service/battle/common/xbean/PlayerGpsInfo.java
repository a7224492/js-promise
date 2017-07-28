package com.kodgames.battleserver.service.battle.common.xbean;

public final class PlayerGpsInfo
{
	private boolean isOpen;
	private double latitude;
	private double longitude;

	public PlayerGpsInfo()
	{
	}

	PlayerGpsInfo(PlayerGpsInfo _o_)
	{
		this.isOpen = _o_.isOpen;
		this.latitude = _o_.latitude;
		this.longitude = _o_.longitude;
	}

	public void copyFrom(PlayerGpsInfo _o_)
	{
		this.isOpen = _o_.isOpen;
		this.latitude = _o_.latitude;
		this.longitude = _o_.longitude;
	}

	public boolean getIsOpen()
	{
		return this.isOpen;
	}

	public double getLatitude()
	{
		return this.latitude;
	}

	public double getLongitude()
	{
		return this.longitude;
	}

	public void setIsOpen(boolean _v_)
	{
		this.isOpen = _v_;
	}

	public void setLatitude(double _v_)
	{
		this.latitude = _v_;
	}

	public void setLongitude(double _v_)
	{
		this.longitude = _v_;
	}

	@Override
	public final boolean equals(Object _o1_)
	{
		PlayerGpsInfo _o_ = null;
		if (_o1_ instanceof PlayerGpsInfo)
			_o_ = (PlayerGpsInfo)_o1_;
		else
			return false;
		if (this.isOpen != _o_.isOpen)
			return false;
		if (this.latitude != _o_.latitude)
			return false;
		if (this.longitude != _o_.longitude)
			return false;
		return true;
	}

	@Override
	public int hashCode()
	{
		int _h_ = 0;
		_h_ += _h_ * 31 + (this.isOpen ? 1231 : 1237);
		_h_ += _h_ * 31 + Double.valueOf(this.latitude).hashCode();
		_h_ += _h_ * 31 + Double.valueOf(this.longitude).hashCode();
		return _h_;
	}

	@Override
	public String toString()
	{
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.isOpen).append(",");
		_sb_.append(this.latitude).append(",");
		_sb_.append(this.longitude).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
