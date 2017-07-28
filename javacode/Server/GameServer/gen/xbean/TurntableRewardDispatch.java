package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class TurntableRewardDispatch extends limax.zdb.XBean {
    private java.util.ArrayList<xbean.TurntableRewardDispatchBean> bean; 

    TurntableRewardDispatch(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        bean = new java.util.ArrayList<xbean.TurntableRewardDispatchBean>();
	}

	public TurntableRewardDispatch() {
		this(null, null);
	}

	public TurntableRewardDispatch(TurntableRewardDispatch _o_) {
		this(_o_, null, null);
	}

	TurntableRewardDispatch(TurntableRewardDispatch _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.TurntableRewardDispatch", true);
        this.bean = new java.util.ArrayList<xbean.TurntableRewardDispatchBean>();
        _o_.bean.forEach(_v_ -> this.bean.add(new TurntableRewardDispatchBean(_v_, this, "bean")));
	}

	public void copyFrom(TurntableRewardDispatch _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromTurntableRewardDispatch", true);
		this.verifyStandaloneOrLockHeld("copyToTurntableRewardDispatch", false);
        java.util.List<xbean.TurntableRewardDispatchBean> this_bean = limax.zdb.Logs.logList(this, "bean", ()->{});
        this_bean.clear();
        _o_.bean.forEach(_v_ -> this_bean.add(new TurntableRewardDispatchBean(_v_)));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal_size(this.bean.size());
        for (xbean.TurntableRewardDispatchBean _v_ : this.bean) {
        	_os_.marshal(_v_);
        }
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			xbean.TurntableRewardDispatchBean _v_ = new TurntableRewardDispatchBean(this, "bean");
			_v_.unmarshal(_os_);
			this.bean.add(_v_);
		}
		return _os_;
	}

	public java.util.List<xbean.TurntableRewardDispatchBean> getBean() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logList(this, "bean", this.verifyStandaloneOrLockHeld("getBean", true)) : this.bean;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		TurntableRewardDispatch _o_ = null;
		if ( _o1_ instanceof TurntableRewardDispatch ) _o_ = (TurntableRewardDispatch)_o1_;
		else return false;
		if (!this.bean.equals(_o_.bean)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.bean.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.bean).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
