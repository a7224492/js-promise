package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class ClubManager extends limax.zdb.XBean {
    private int firstClubId; 

    ClubManager(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
	}

	public ClubManager() {
		this(null, null);
	}

	public ClubManager(ClubManager _o_) {
		this(_o_, null, null);
	}

	ClubManager(ClubManager _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.ClubManager", true);
        this.firstClubId = _o_.firstClubId;
	}

	public void copyFrom(ClubManager _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromClubManager", true);
		this.verifyStandaloneOrLockHeld("copyToClubManager", false);
        limax.zdb.Logs.logObject(this, "firstClubId");
        this.firstClubId = _o_.firstClubId;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.firstClubId);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.firstClubId = _os_.unmarshal_int();
		return _os_;
	}

	public int getFirstClubId() { 
		this.verifyStandaloneOrLockHeld("getFirstClubId", true);
		return this.firstClubId;
	}

	public void setFirstClubId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setFirstClubId", false);
		limax.zdb.Logs.logObject(this, "firstClubId");
		this.firstClubId = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		ClubManager _o_ = null;
		if ( _o1_ instanceof ClubManager ) _o_ = (ClubManager)_o1_;
		else return false;
		if (this.firstClubId != _o_.firstClubId) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.firstClubId;
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.firstClubId).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
