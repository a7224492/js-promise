package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class UnionidAccountidBean extends limax.zdb.XBean {
    private int accountId; 
    private long lastLoginTime; 
    private java.util.ArrayList<Integer> mergeList; 

    UnionidAccountidBean(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        mergeList = new java.util.ArrayList<Integer>();
	}

	public UnionidAccountidBean() {
		this(null, null);
	}

	public UnionidAccountidBean(UnionidAccountidBean _o_) {
		this(_o_, null, null);
	}

	UnionidAccountidBean(UnionidAccountidBean _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.UnionidAccountidBean", true);
        this.accountId = _o_.accountId;
        this.lastLoginTime = _o_.lastLoginTime;
        this.mergeList = new java.util.ArrayList<Integer>();
        this.mergeList.addAll(_o_.mergeList);
	}

	public void copyFrom(UnionidAccountidBean _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromUnionidAccountidBean", true);
		this.verifyStandaloneOrLockHeld("copyToUnionidAccountidBean", false);
        limax.zdb.Logs.logObject(this, "accountId");
        this.accountId = _o_.accountId;
        limax.zdb.Logs.logObject(this, "lastLoginTime");
        this.lastLoginTime = _o_.lastLoginTime;
        java.util.List<Integer> this_mergeList = limax.zdb.Logs.logList(this, "mergeList", ()->{});
        this_mergeList.clear();
        this_mergeList.addAll(_o_.mergeList);
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.accountId);
        _os_.marshal(this.lastLoginTime);
        _os_.marshal_size(this.mergeList.size());
        for (Integer _v_ : this.mergeList) {
        	_os_.marshal(_v_);
        }
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.accountId = _os_.unmarshal_int();
		this.lastLoginTime = _os_.unmarshal_long();
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			int _v_ = _os_.unmarshal_int();
			this.mergeList.add(_v_);
		}
		return _os_;
	}

	public int getAccountId() { 
		this.verifyStandaloneOrLockHeld("getAccountId", true);
		return this.accountId;
	}

	public long getLastLoginTime() { 
		this.verifyStandaloneOrLockHeld("getLastLoginTime", true);
		return this.lastLoginTime;
	}

	public java.util.List<Integer> getMergeList() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logList(this, "mergeList", this.verifyStandaloneOrLockHeld("getMergeList", true)) : this.mergeList;
	}

	public void setAccountId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setAccountId", false);
		limax.zdb.Logs.logObject(this, "accountId");
		this.accountId = _v_;
	}

	public void setLastLoginTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setLastLoginTime", false);
		limax.zdb.Logs.logObject(this, "lastLoginTime");
		this.lastLoginTime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		UnionidAccountidBean _o_ = null;
		if ( _o1_ instanceof UnionidAccountidBean ) _o_ = (UnionidAccountidBean)_o1_;
		else return false;
		if (this.accountId != _o_.accountId) return false;
		if (this.lastLoginTime != _o_.lastLoginTime) return false;
		if (!this.mergeList.equals(_o_.mergeList)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.accountId;
		_h_ += _h_ * 31 + (int)(this.lastLoginTime ^ (this.lastLoginTime >>> 32));
		_h_ += _h_ * 31 + this.mergeList.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.accountId).append(",");
		_sb_.append(this.lastLoginTime).append(",");
		_sb_.append(this.mergeList).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
