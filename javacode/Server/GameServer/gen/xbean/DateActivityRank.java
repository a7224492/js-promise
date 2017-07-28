package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class DateActivityRank extends limax.zdb.XBean {
    private java.util.ArrayList<xbean.RoleRank> roleRanks; 

    DateActivityRank(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        roleRanks = new java.util.ArrayList<xbean.RoleRank>();
	}

	public DateActivityRank() {
		this(null, null);
	}

	public DateActivityRank(DateActivityRank _o_) {
		this(_o_, null, null);
	}

	DateActivityRank(DateActivityRank _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.DateActivityRank", true);
        this.roleRanks = new java.util.ArrayList<xbean.RoleRank>();
        _o_.roleRanks.forEach(_v_ -> this.roleRanks.add(new RoleRank(_v_, this, "roleRanks")));
	}

	public void copyFrom(DateActivityRank _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromDateActivityRank", true);
		this.verifyStandaloneOrLockHeld("copyToDateActivityRank", false);
        java.util.List<xbean.RoleRank> this_roleRanks = limax.zdb.Logs.logList(this, "roleRanks", ()->{});
        this_roleRanks.clear();
        _o_.roleRanks.forEach(_v_ -> this_roleRanks.add(new RoleRank(_v_)));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal_size(this.roleRanks.size());
        for (xbean.RoleRank _v_ : this.roleRanks) {
        	_os_.marshal(_v_);
        }
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			xbean.RoleRank _v_ = new RoleRank(this, "roleRanks");
			_v_.unmarshal(_os_);
			this.roleRanks.add(_v_);
		}
		return _os_;
	}

	public java.util.List<xbean.RoleRank> getRoleRanks() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logList(this, "roleRanks", this.verifyStandaloneOrLockHeld("getRoleRanks", true)) : this.roleRanks;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		DateActivityRank _o_ = null;
		if ( _o1_ instanceof DateActivityRank ) _o_ = (DateActivityRank)_o1_;
		else return false;
		if (!this.roleRanks.equals(_o_.roleRanks)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.roleRanks.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.roleRanks).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
