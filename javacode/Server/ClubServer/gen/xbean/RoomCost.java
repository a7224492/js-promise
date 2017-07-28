package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class RoomCost extends limax.zdb.XBean {
    private int cost; 
    private int payType; 

    RoomCost(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
	}

	public RoomCost() {
		this(null, null);
	}

	public RoomCost(RoomCost _o_) {
		this(_o_, null, null);
	}

	RoomCost(RoomCost _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.RoomCost", true);
        this.cost = _o_.cost;
        this.payType = _o_.payType;
	}

	public void copyFrom(RoomCost _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromRoomCost", true);
		this.verifyStandaloneOrLockHeld("copyToRoomCost", false);
        limax.zdb.Logs.logObject(this, "cost");
        this.cost = _o_.cost;
        limax.zdb.Logs.logObject(this, "payType");
        this.payType = _o_.payType;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.cost);
        _os_.marshal(this.payType);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.cost = _os_.unmarshal_int();
		this.payType = _os_.unmarshal_int();
		return _os_;
	}

	public int getCost() { 
		this.verifyStandaloneOrLockHeld("getCost", true);
		return this.cost;
	}

	public int getPayType() { 
		this.verifyStandaloneOrLockHeld("getPayType", true);
		return this.payType;
	}

	public void setCost(int _v_) { 
		this.verifyStandaloneOrLockHeld("setCost", false);
		limax.zdb.Logs.logObject(this, "cost");
		this.cost = _v_;
	}

	public void setPayType(int _v_) { 
		this.verifyStandaloneOrLockHeld("setPayType", false);
		limax.zdb.Logs.logObject(this, "payType");
		this.payType = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		RoomCost _o_ = null;
		if ( _o1_ instanceof RoomCost ) _o_ = (RoomCost)_o1_;
		else return false;
		if (this.cost != _o_.cost) return false;
		if (this.payType != _o_.payType) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.cost;
		_h_ += _h_ * 31 + this.payType;
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.cost).append(",");
		_sb_.append(this.payType).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
