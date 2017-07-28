package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class CombatInfo extends limax.zdb.XBean {
    private long time; 
    private int count; 

    CombatInfo(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
	}

	public CombatInfo() {
		this(null, null);
	}

	public CombatInfo(CombatInfo _o_) {
		this(_o_, null, null);
	}

	CombatInfo(CombatInfo _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.CombatInfo", true);
        this.time = _o_.time;
        this.count = _o_.count;
	}

	public void copyFrom(CombatInfo _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromCombatInfo", true);
		this.verifyStandaloneOrLockHeld("copyToCombatInfo", false);
        limax.zdb.Logs.logObject(this, "time");
        this.time = _o_.time;
        limax.zdb.Logs.logObject(this, "count");
        this.count = _o_.count;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.time);
        _os_.marshal(this.count);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.time = _os_.unmarshal_long();
		this.count = _os_.unmarshal_int();
		return _os_;
	}

	public long getTime() { 
		this.verifyStandaloneOrLockHeld("getTime", true);
		return this.time;
	}

	public int getCount() { 
		this.verifyStandaloneOrLockHeld("getCount", true);
		return this.count;
	}

	public void setTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setTime", false);
		limax.zdb.Logs.logObject(this, "time");
		this.time = _v_;
	}

	public void setCount(int _v_) { 
		this.verifyStandaloneOrLockHeld("setCount", false);
		limax.zdb.Logs.logObject(this, "count");
		this.count = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		CombatInfo _o_ = null;
		if ( _o1_ instanceof CombatInfo ) _o_ = (CombatInfo)_o1_;
		else return false;
		if (this.time != _o_.time) return false;
		if (this.count != _o_.count) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + (int)(this.time ^ (this.time >>> 32));
		_h_ += _h_ * 31 + this.count;
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.time).append(",");
		_sb_.append(this.count).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
