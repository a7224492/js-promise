package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class ClubRooms extends limax.zdb.XBean {
    private java.util.HashMap<Integer, xbean.ClubRoomInfo> rooms; 

    ClubRooms(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        rooms = new java.util.HashMap<Integer, xbean.ClubRoomInfo>();
	}

	public ClubRooms() {
		this(null, null);
	}

	public ClubRooms(ClubRooms _o_) {
		this(_o_, null, null);
	}

	ClubRooms(ClubRooms _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.ClubRooms", true);
        this.rooms = new java.util.HashMap<Integer, xbean.ClubRoomInfo>();
        _o_.rooms.forEach((_k_, _v_) -> this.rooms.put(_k_, new ClubRoomInfo(_v_, this, "rooms")));
	}

	public void copyFrom(ClubRooms _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromClubRooms", true);
		this.verifyStandaloneOrLockHeld("copyToClubRooms", false);
        java.util.Map<Integer, xbean.ClubRoomInfo> this_rooms = limax.zdb.Logs.logMap(this, "rooms", ()->{});
        this_rooms.clear();
        _o_.rooms.forEach((_k_, _v_) -> this_rooms.put(_k_, new ClubRoomInfo(_v_)));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal_size(this.rooms.size());
        for (java.util.Map.Entry<Integer, xbean.ClubRoomInfo> _e_ : this.rooms.entrySet()) {
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
			xbean.ClubRoomInfo _v_ = new ClubRoomInfo(this, "rooms");
			_v_.unmarshal(_os_);
			this.rooms.put(_k_, _v_);
		}
		return _os_;
	}

	public java.util.Map<Integer, xbean.ClubRoomInfo> getRooms() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logMap(this, "rooms", this.verifyStandaloneOrLockHeld("getRooms", true)) : this.rooms;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		ClubRooms _o_ = null;
		if ( _o1_ instanceof ClubRooms ) _o_ = (ClubRooms)_o1_;
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
