package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class AccountIDSeed extends limax.zdb.XBean {
    private int seed; 

    AccountIDSeed(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
	}

	public AccountIDSeed() {
		this(null, null);
	}

	public AccountIDSeed(AccountIDSeed _o_) {
		this(_o_, null, null);
	}

	AccountIDSeed(AccountIDSeed _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.AccountIDSeed", true);
        this.seed = _o_.seed;
	}

	public void copyFrom(AccountIDSeed _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromAccountIDSeed", true);
		this.verifyStandaloneOrLockHeld("copyToAccountIDSeed", false);
        limax.zdb.Logs.logObject(this, "seed");
        this.seed = _o_.seed;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.seed);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.seed = _os_.unmarshal_int();
		return _os_;
	}

	public int getSeed() { 
		this.verifyStandaloneOrLockHeld("getSeed", true);
		return this.seed;
	}

	public void setSeed(int _v_) { 
		this.verifyStandaloneOrLockHeld("setSeed", false);
		limax.zdb.Logs.logObject(this, "seed");
		this.seed = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		AccountIDSeed _o_ = null;
		if ( _o1_ instanceof AccountIDSeed ) _o_ = (AccountIDSeed)_o1_;
		else return false;
		if (this.seed != _o_.seed) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.seed;
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.seed).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
