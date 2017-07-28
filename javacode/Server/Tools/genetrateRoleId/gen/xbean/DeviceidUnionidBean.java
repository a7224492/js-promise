package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class DeviceidUnionidBean extends limax.zdb.XBean {
    private java.util.ArrayList<String> UnionidList; 

    DeviceidUnionidBean(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        UnionidList = new java.util.ArrayList<String>();
	}

	public DeviceidUnionidBean() {
		this(null, null);
	}

	public DeviceidUnionidBean(DeviceidUnionidBean _o_) {
		this(_o_, null, null);
	}

	DeviceidUnionidBean(DeviceidUnionidBean _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.DeviceidUnionidBean", true);
        this.UnionidList = new java.util.ArrayList<String>();
        this.UnionidList.addAll(_o_.UnionidList);
	}

	public void copyFrom(DeviceidUnionidBean _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromDeviceidUnionidBean", true);
		this.verifyStandaloneOrLockHeld("copyToDeviceidUnionidBean", false);
        java.util.List<String> this_UnionidList = limax.zdb.Logs.logList(this, "UnionidList", ()->{});
        this_UnionidList.clear();
        this_UnionidList.addAll(_o_.UnionidList);
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal_size(this.UnionidList.size());
        for (String _v_ : this.UnionidList) {
        	_os_.marshal(_v_);
        }
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			String _v_ = _os_.unmarshal_String();
			this.UnionidList.add(_v_);
		}
		return _os_;
	}

	public java.util.List<String> getUnionidList() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logList(this, "UnionidList", this.verifyStandaloneOrLockHeld("getUnionidList", true)) : this.UnionidList;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		DeviceidUnionidBean _o_ = null;
		if ( _o1_ instanceof DeviceidUnionidBean ) _o_ = (DeviceidUnionidBean)_o1_;
		else return false;
		if (!this.UnionidList.equals(_o_.UnionidList)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.UnionidList.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.UnionidList).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
