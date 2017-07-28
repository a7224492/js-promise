package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class ActivityRank extends limax.zdb.XBean {
    private java.util.HashMap<Long, xbean.DateActivityRank> dateRank; 

    ActivityRank(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        dateRank = new java.util.HashMap<Long, xbean.DateActivityRank>();
	}

	public ActivityRank() {
		this(null, null);
	}

	public ActivityRank(ActivityRank _o_) {
		this(_o_, null, null);
	}

	ActivityRank(ActivityRank _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.ActivityRank", true);
        this.dateRank = new java.util.HashMap<Long, xbean.DateActivityRank>();
        _o_.dateRank.forEach((_k_, _v_) -> this.dateRank.put(_k_, new DateActivityRank(_v_, this, "dateRank")));
	}

	public void copyFrom(ActivityRank _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromActivityRank", true);
		this.verifyStandaloneOrLockHeld("copyToActivityRank", false);
        java.util.Map<Long, xbean.DateActivityRank> this_dateRank = limax.zdb.Logs.logMap(this, "dateRank", ()->{});
        this_dateRank.clear();
        _o_.dateRank.forEach((_k_, _v_) -> this_dateRank.put(_k_, new DateActivityRank(_v_)));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal_size(this.dateRank.size());
        for (java.util.Map.Entry<Long, xbean.DateActivityRank> _e_ : this.dateRank.entrySet()) {
        	_os_.marshal(_e_.getKey());
        	_os_.marshal(_e_.getValue());
        }
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			long _k_ = _os_.unmarshal_long();
			xbean.DateActivityRank _v_ = new DateActivityRank(this, "dateRank");
			_v_.unmarshal(_os_);
			this.dateRank.put(_k_, _v_);
		}
		return _os_;
	}

	public java.util.Map<Long, xbean.DateActivityRank> getDateRank() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logMap(this, "dateRank", this.verifyStandaloneOrLockHeld("getDateRank", true)) : this.dateRank;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		ActivityRank _o_ = null;
		if ( _o1_ instanceof ActivityRank ) _o_ = (ActivityRank)_o1_;
		else return false;
		if (!this.dateRank.equals(_o_.dateRank)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.dateRank.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.dateRank).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
