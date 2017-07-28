package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class RankActivityVersion extends limax.zdb.XBean {
    private int version; 

    RankActivityVersion(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
	}

	public RankActivityVersion() {
		this(null, null);
	}

	public RankActivityVersion(RankActivityVersion _o_) {
		this(_o_, null, null);
	}

	RankActivityVersion(RankActivityVersion _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.RankActivityVersion", true);
        this.version = _o_.version;
	}

	public void copyFrom(RankActivityVersion _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromRankActivityVersion", true);
		this.verifyStandaloneOrLockHeld("copyToRankActivityVersion", false);
        limax.zdb.Logs.logObject(this, "version");
        this.version = _o_.version;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.version);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.version = _os_.unmarshal_int();
		return _os_;
	}

	public int getVersion() { 
		this.verifyStandaloneOrLockHeld("getVersion", true);
		return this.version;
	}

	public void setVersion(int _v_) { 
		this.verifyStandaloneOrLockHeld("setVersion", false);
		limax.zdb.Logs.logObject(this, "version");
		this.version = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		RankActivityVersion _o_ = null;
		if ( _o1_ instanceof RankActivityVersion ) _o_ = (RankActivityVersion)_o1_;
		else return false;
		if (this.version != _o_.version) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.version;
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.version).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
