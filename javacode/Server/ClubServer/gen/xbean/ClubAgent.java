package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class ClubAgent extends limax.zdb.XBean {
    private java.util.ArrayList<Integer> clubs; 

    ClubAgent(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        clubs = new java.util.ArrayList<Integer>();
	}

	public ClubAgent() {
		this(null, null);
	}

	public ClubAgent(ClubAgent _o_) {
		this(_o_, null, null);
	}

	ClubAgent(ClubAgent _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.ClubAgent", true);
        this.clubs = new java.util.ArrayList<Integer>();
        this.clubs.addAll(_o_.clubs);
	}

	public void copyFrom(ClubAgent _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromClubAgent", true);
		this.verifyStandaloneOrLockHeld("copyToClubAgent", false);
        java.util.List<Integer> this_clubs = limax.zdb.Logs.logList(this, "clubs", ()->{});
        this_clubs.clear();
        this_clubs.addAll(_o_.clubs);
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal_size(this.clubs.size());
        for (Integer _v_ : this.clubs) {
        	_os_.marshal(_v_);
        }
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			int _v_ = _os_.unmarshal_int();
			this.clubs.add(_v_);
		}
		return _os_;
	}

	public java.util.List<Integer> getClubs() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logList(this, "clubs", this.verifyStandaloneOrLockHeld("getClubs", true)) : this.clubs;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		ClubAgent _o_ = null;
		if ( _o1_ instanceof ClubAgent ) _o_ = (ClubAgent)_o1_;
		else return false;
		if (!this.clubs.equals(_o_.clubs)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.clubs.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.clubs).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
