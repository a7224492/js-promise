package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class RoleInfo extends limax.zdb.XBean {
    private java.util.ArrayList<cbean.GlobalRoomId> historyRooms; 

    RoleInfo(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        historyRooms = new java.util.ArrayList<cbean.GlobalRoomId>();
	}

	public RoleInfo() {
		this(null, null);
	}

	public RoleInfo(RoleInfo _o_) {
		this(_o_, null, null);
	}

	RoleInfo(RoleInfo _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.RoleInfo", true);
        this.historyRooms = new java.util.ArrayList<cbean.GlobalRoomId>();
        this.historyRooms.addAll(_o_.historyRooms);
	}

	public void copyFrom(RoleInfo _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromRoleInfo", true);
		this.verifyStandaloneOrLockHeld("copyToRoleInfo", false);
        java.util.List<cbean.GlobalRoomId> this_historyRooms = limax.zdb.Logs.logList(this, "historyRooms", ()->{});
        this_historyRooms.clear();
        this_historyRooms.addAll(_o_.historyRooms);
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal_size(this.historyRooms.size());
        for (cbean.GlobalRoomId _v_ : this.historyRooms) {
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
			this.historyRooms.add(_v_);
		}
		return _os_;
	}

	public java.util.List<cbean.GlobalRoomId> getHistoryRooms() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logList(this, "historyRooms", this.verifyStandaloneOrLockHeld("getHistoryRooms", true)) : this.historyRooms;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		RoleInfo _o_ = null;
		if ( _o1_ instanceof RoleInfo ) _o_ = (RoleInfo)_o1_;
		else return false;
		if (!this.historyRooms.equals(_o_.historyRooms)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.historyRooms.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.historyRooms).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
