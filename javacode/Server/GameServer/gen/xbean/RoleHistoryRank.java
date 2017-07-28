package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class RoleHistoryRank extends limax.zdb.XBean {
    private java.util.HashMap<Integer, xbean.ActivityHistoryRank> ranks; 

    RoleHistoryRank(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        ranks = new java.util.HashMap<Integer, xbean.ActivityHistoryRank>();
	}

	public RoleHistoryRank() {
		this(null, null);
	}

	public RoleHistoryRank(RoleHistoryRank _o_) {
		this(_o_, null, null);
	}

	RoleHistoryRank(RoleHistoryRank _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.RoleHistoryRank", true);
        this.ranks = new java.util.HashMap<Integer, xbean.ActivityHistoryRank>();
        _o_.ranks.forEach((_k_, _v_) -> this.ranks.put(_k_, new ActivityHistoryRank(_v_, this, "ranks")));
	}

	public void copyFrom(RoleHistoryRank _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromRoleHistoryRank", true);
		this.verifyStandaloneOrLockHeld("copyToRoleHistoryRank", false);
        java.util.Map<Integer, xbean.ActivityHistoryRank> this_ranks = limax.zdb.Logs.logMap(this, "ranks", ()->{});
        this_ranks.clear();
        _o_.ranks.forEach((_k_, _v_) -> this_ranks.put(_k_, new ActivityHistoryRank(_v_)));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal_size(this.ranks.size());
        for (java.util.Map.Entry<Integer, xbean.ActivityHistoryRank> _e_ : this.ranks.entrySet()) {
        	_os_.marshal(_e_.getKey());
        	_os_.marshal(_e_.getValue());
        }
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			int _k_ = _os_.unmarshal_int();
			xbean.ActivityHistoryRank _v_ = new ActivityHistoryRank(this, "ranks");
			_v_.unmarshal(_os_);
			this.ranks.put(_k_, _v_);
		}
		return _os_;
	}

	public java.util.Map<Integer, xbean.ActivityHistoryRank> getRanks() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logMap(this, "ranks", this.verifyStandaloneOrLockHeld("getRanks", true)) : this.ranks;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		RoleHistoryRank _o_ = null;
		if ( _o1_ instanceof RoleHistoryRank ) _o_ = (RoleHistoryRank)_o1_;
		else return false;
		if (!this.ranks.equals(_o_.ranks)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.ranks.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.ranks).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
