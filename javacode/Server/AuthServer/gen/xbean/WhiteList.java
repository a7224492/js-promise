package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class WhiteList extends limax.zdb.XBean {
    private java.util.ArrayList<Integer> list; 

    WhiteList(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        list = new java.util.ArrayList<Integer>();
	}

	public WhiteList() {
		this(null, null);
	}

	public WhiteList(WhiteList _o_) {
		this(_o_, null, null);
	}

	WhiteList(WhiteList _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.WhiteList", true);
        this.list = new java.util.ArrayList<Integer>();
        this.list.addAll(_o_.list);
	}

	public void copyFrom(WhiteList _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromWhiteList", true);
		this.verifyStandaloneOrLockHeld("copyToWhiteList", false);
        java.util.List<Integer> this_list = limax.zdb.Logs.logList(this, "list", ()->{});
        this_list.clear();
        this_list.addAll(_o_.list);
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal_size(this.list.size());
        for (Integer _v_ : this.list) {
        	_os_.marshal(_v_);
        }
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			int _v_ = _os_.unmarshal_int();
			this.list.add(_v_);
		}
		return _os_;
	}

	public java.util.List<Integer> getList() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logList(this, "list", this.verifyStandaloneOrLockHeld("getList", true)) : this.list;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		WhiteList _o_ = null;
		if ( _o1_ instanceof WhiteList ) _o_ = (WhiteList)_o1_;
		else return false;
		if (!this.list.equals(_o_.list)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.list.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.list).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
