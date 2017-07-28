package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class AddCard extends limax.zdb.XBean {
    private int agencyId; 
    private int count; 
    private long time; 

    AddCard(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
	}

	public AddCard() {
		this(null, null);
	}

	public AddCard(AddCard _o_) {
		this(_o_, null, null);
	}

	AddCard(AddCard _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.AddCard", true);
        this.agencyId = _o_.agencyId;
        this.count = _o_.count;
        this.time = _o_.time;
	}

	public void copyFrom(AddCard _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromAddCard", true);
		this.verifyStandaloneOrLockHeld("copyToAddCard", false);
        limax.zdb.Logs.logObject(this, "agencyId");
        this.agencyId = _o_.agencyId;
        limax.zdb.Logs.logObject(this, "count");
        this.count = _o_.count;
        limax.zdb.Logs.logObject(this, "time");
        this.time = _o_.time;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.agencyId);
        _os_.marshal(this.count);
        _os_.marshal(this.time);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.agencyId = _os_.unmarshal_int();
		this.count = _os_.unmarshal_int();
		this.time = _os_.unmarshal_long();
		return _os_;
	}

	public int getAgencyId() { 
		this.verifyStandaloneOrLockHeld("getAgencyId", true);
		return this.agencyId;
	}

	public int getCount() { 
		this.verifyStandaloneOrLockHeld("getCount", true);
		return this.count;
	}

	public long getTime() { 
		this.verifyStandaloneOrLockHeld("getTime", true);
		return this.time;
	}

	public void setAgencyId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setAgencyId", false);
		limax.zdb.Logs.logObject(this, "agencyId");
		this.agencyId = _v_;
	}

	public void setCount(int _v_) { 
		this.verifyStandaloneOrLockHeld("setCount", false);
		limax.zdb.Logs.logObject(this, "count");
		this.count = _v_;
	}

	public void setTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setTime", false);
		limax.zdb.Logs.logObject(this, "time");
		this.time = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		AddCard _o_ = null;
		if ( _o1_ instanceof AddCard ) _o_ = (AddCard)_o1_;
		else return false;
		if (this.agencyId != _o_.agencyId) return false;
		if (this.count != _o_.count) return false;
		if (this.time != _o_.time) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.agencyId;
		_h_ += _h_ * 31 + this.count;
		_h_ += _h_ * 31 + (int)(this.time ^ (this.time >>> 32));
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.agencyId).append(",");
		_sb_.append(this.count).append(",");
		_sb_.append(this.time).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
