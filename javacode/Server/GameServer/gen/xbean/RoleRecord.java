package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class RoleRecord extends limax.zdb.XBean {
    private int role_id; 
    private java.util.ArrayList<xbean.CombatInfo> combatTimes; 
    private int agencyId; 
    private int combatTotal; 

    RoleRecord(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        combatTimes = new java.util.ArrayList<xbean.CombatInfo>();
	}

	public RoleRecord() {
		this(null, null);
	}

	public RoleRecord(RoleRecord _o_) {
		this(_o_, null, null);
	}

	RoleRecord(RoleRecord _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.RoleRecord", true);
        this.role_id = _o_.role_id;
        this.combatTimes = new java.util.ArrayList<xbean.CombatInfo>();
        _o_.combatTimes.forEach(_v_ -> this.combatTimes.add(new CombatInfo(_v_, this, "combatTimes")));
        this.agencyId = _o_.agencyId;
        this.combatTotal = _o_.combatTotal;
	}

	public void copyFrom(RoleRecord _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromRoleRecord", true);
		this.verifyStandaloneOrLockHeld("copyToRoleRecord", false);
        limax.zdb.Logs.logObject(this, "role_id");
        this.role_id = _o_.role_id;
        java.util.List<xbean.CombatInfo> this_combatTimes = limax.zdb.Logs.logList(this, "combatTimes", ()->{});
        this_combatTimes.clear();
        _o_.combatTimes.forEach(_v_ -> this_combatTimes.add(new CombatInfo(_v_)));
        limax.zdb.Logs.logObject(this, "agencyId");
        this.agencyId = _o_.agencyId;
        limax.zdb.Logs.logObject(this, "combatTotal");
        this.combatTotal = _o_.combatTotal;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.role_id);
        _os_.marshal_size(this.combatTimes.size());
        for (xbean.CombatInfo _v_ : this.combatTimes) {
        	_os_.marshal(_v_);
        }
        _os_.marshal(this.agencyId);
        _os_.marshal(this.combatTotal);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.role_id = _os_.unmarshal_int();
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			xbean.CombatInfo _v_ = new CombatInfo(this, "combatTimes");
			_v_.unmarshal(_os_);
			this.combatTimes.add(_v_);
		}
		this.agencyId = _os_.unmarshal_int();
		this.combatTotal = _os_.unmarshal_int();
		return _os_;
	}

	public int getRole_id() { 
		this.verifyStandaloneOrLockHeld("getRole_id", true);
		return this.role_id;
	}

	public java.util.List<xbean.CombatInfo> getCombatTimes() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logList(this, "combatTimes", this.verifyStandaloneOrLockHeld("getCombatTimes", true)) : this.combatTimes;
	}

	public int getAgencyId() { 
		this.verifyStandaloneOrLockHeld("getAgencyId", true);
		return this.agencyId;
	}

	public int getCombatTotal() { 
		this.verifyStandaloneOrLockHeld("getCombatTotal", true);
		return this.combatTotal;
	}

	public void setRole_id(int _v_) { 
		this.verifyStandaloneOrLockHeld("setRole_id", false);
		limax.zdb.Logs.logObject(this, "role_id");
		this.role_id = _v_;
	}

	public void setAgencyId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setAgencyId", false);
		limax.zdb.Logs.logObject(this, "agencyId");
		this.agencyId = _v_;
	}

	public void setCombatTotal(int _v_) { 
		this.verifyStandaloneOrLockHeld("setCombatTotal", false);
		limax.zdb.Logs.logObject(this, "combatTotal");
		this.combatTotal = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		RoleRecord _o_ = null;
		if ( _o1_ instanceof RoleRecord ) _o_ = (RoleRecord)_o1_;
		else return false;
		if (this.role_id != _o_.role_id) return false;
		if (!this.combatTimes.equals(_o_.combatTimes)) return false;
		if (this.agencyId != _o_.agencyId) return false;
		if (this.combatTotal != _o_.combatTotal) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.role_id;
		_h_ += _h_ * 31 + this.combatTimes.hashCode();
		_h_ += _h_ * 31 + this.agencyId;
		_h_ += _h_ * 31 + this.combatTotal;
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.role_id).append(",");
		_sb_.append(this.combatTimes).append(",");
		_sb_.append(this.agencyId).append(",");
		_sb_.append(this.combatTotal).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
