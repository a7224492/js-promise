package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class DeviceidAccountidBean extends limax.zdb.XBean {
    private java.util.ArrayList<Integer> accountIdList; 

    DeviceidAccountidBean(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        accountIdList = new java.util.ArrayList<Integer>();
	}

	public DeviceidAccountidBean() {
		this(null, null);
	}

	public DeviceidAccountidBean(DeviceidAccountidBean _o_) {
		this(_o_, null, null);
	}

	DeviceidAccountidBean(DeviceidAccountidBean _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.DeviceidAccountidBean", true);
        this.accountIdList = new java.util.ArrayList<Integer>();
        this.accountIdList.addAll(_o_.accountIdList);
	}

	public void copyFrom(DeviceidAccountidBean _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromDeviceidAccountidBean", true);
		this.verifyStandaloneOrLockHeld("copyToDeviceidAccountidBean", false);
        java.util.List<Integer> this_accountIdList = limax.zdb.Logs.logList(this, "accountIdList", ()->{});
        this_accountIdList.clear();
        this_accountIdList.addAll(_o_.accountIdList);
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal_size(this.accountIdList.size());
        for (Integer _v_ : this.accountIdList) {
        	_os_.marshal(_v_);
        }
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			int _v_ = _os_.unmarshal_int();
			this.accountIdList.add(_v_);
		}
		return _os_;
	}

	public java.util.List<Integer> getAccountIdList() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logList(this, "accountIdList", this.verifyStandaloneOrLockHeld("getAccountIdList", true)) : this.accountIdList;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		DeviceidAccountidBean _o_ = null;
		if ( _o1_ instanceof DeviceidAccountidBean ) _o_ = (DeviceidAccountidBean)_o1_;
		else return false;
		if (!this.accountIdList.equals(_o_.accountIdList)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.accountIdList.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.accountIdList).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
