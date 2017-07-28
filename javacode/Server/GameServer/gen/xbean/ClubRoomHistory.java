package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class ClubRoomHistory extends limax.zdb.XBean {
    private java.util.ArrayList<cbean.GlobalRoomId> rooms; 

    ClubRoomHistory(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        rooms = new java.util.ArrayList<cbean.GlobalRoomId>();
	}

	public ClubRoomHistory() {
		this(null, null);
	}

	public ClubRoomHistory(ClubRoomHistory _o_) {
		this(_o_, null, null);
	}

	ClubRoomHistory(ClubRoomHistory _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.ClubRoomHistory", true);
        this.rooms = new java.util.ArrayList<cbean.GlobalRoomId>();
        this.rooms.addAll(_o_.rooms);
	}

	public void copyFrom(ClubRoomHistory _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromClubRoomHistory", true);
		this.verifyStandaloneOrLockHeld("copyToClubRoomHistory", false);
        java.util.List<cbean.GlobalRoomId> this_rooms = limax.zdb.Logs.logList(this, "rooms", ()->{});
        this_rooms.clear();
        this_rooms.addAll(_o_.rooms);
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal_size(this.rooms.size());
        for (cbean.GlobalRoomId _v_ : this.rooms) {
        	_os_.marshal(_v_);
        }
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			cbean.GlobalRoomId _v_ = new cbean.GlobalRoomId();
			_v_.unmarshal(_os_);
			this.rooms.add(_v_);
		}
		return _os_;
	}

	public java.util.List<cbean.GlobalRoomId> getRooms() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logList(this, "rooms", this.verifyStandaloneOrLockHeld("getRooms", true)) : this.rooms;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		ClubRoomHistory _o_ = null;
		if ( _o1_ instanceof ClubRoomHistory ) _o_ = (ClubRoomHistory)_o1_;
		else return false;
		if (!this.rooms.equals(_o_.rooms)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.rooms.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.rooms).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
