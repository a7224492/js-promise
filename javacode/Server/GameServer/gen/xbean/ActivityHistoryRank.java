package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class ActivityHistoryRank extends limax.zdb.XBean {
    private java.util.HashMap<Long, xbean.HistoryRank> ranks; 

    ActivityHistoryRank(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        ranks = new java.util.HashMap<Long, xbean.HistoryRank>();
	}

	public ActivityHistoryRank() {
		this(null, null);
	}

	public ActivityHistoryRank(ActivityHistoryRank _o_) {
		this(_o_, null, null);
	}

	ActivityHistoryRank(ActivityHistoryRank _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.ActivityHistoryRank", true);
        this.ranks = new java.util.HashMap<Long, xbean.HistoryRank>();
        _o_.ranks.forEach((_k_, _v_) -> this.ranks.put(_k_, new HistoryRank(_v_, this, "ranks")));
	}

	public void copyFrom(ActivityHistoryRank _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromActivityHistoryRank", true);
		this.verifyStandaloneOrLockHeld("copyToActivityHistoryRank", false);
        java.util.Map<Long, xbean.HistoryRank> this_ranks = limax.zdb.Logs.logMap(this, "ranks", ()->{});
        this_ranks.clear();
        _o_.ranks.forEach((_k_, _v_) -> this_ranks.put(_k_, new HistoryRank(_v_)));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal_size(this.ranks.size());
        for (java.util.Map.Entry<Long, xbean.HistoryRank> _e_ : this.ranks.entrySet()) {
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
			xbean.HistoryRank _v_ = new HistoryRank(this, "ranks");
			_v_.unmarshal(_os_);
			this.ranks.put(_k_, _v_);
		}
		return _os_;
	}

	public java.util.Map<Long, xbean.HistoryRank> getRanks() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logMap(this, "ranks", this.verifyStandaloneOrLockHeld("getRanks", true)) : this.ranks;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		ActivityHistoryRank _o_ = null;
		if ( _o1_ instanceof ActivityHistoryRank ) _o_ = (ActivityHistoryRank)_o1_;
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
