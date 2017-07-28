package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class ButtonTableMap extends limax.zdb.XBean {
    private java.util.HashMap<Integer, xbean.ButtonBean> buttonMap; 

    ButtonTableMap(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        buttonMap = new java.util.HashMap<Integer, xbean.ButtonBean>();
	}

	public ButtonTableMap() {
		this(null, null);
	}

	public ButtonTableMap(ButtonTableMap _o_) {
		this(_o_, null, null);
	}

	ButtonTableMap(ButtonTableMap _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.ButtonTableMap", true);
        this.buttonMap = new java.util.HashMap<Integer, xbean.ButtonBean>();
        _o_.buttonMap.forEach((_k_, _v_) -> this.buttonMap.put(_k_, new ButtonBean(_v_, this, "buttonMap")));
	}

	public void copyFrom(ButtonTableMap _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromButtonTableMap", true);
		this.verifyStandaloneOrLockHeld("copyToButtonTableMap", false);
        java.util.Map<Integer, xbean.ButtonBean> this_buttonMap = limax.zdb.Logs.logMap(this, "buttonMap", ()->{});
        this_buttonMap.clear();
        _o_.buttonMap.forEach((_k_, _v_) -> this_buttonMap.put(_k_, new ButtonBean(_v_)));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal_size(this.buttonMap.size());
        for (java.util.Map.Entry<Integer, xbean.ButtonBean> _e_ : this.buttonMap.entrySet()) {
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
			xbean.ButtonBean _v_ = new ButtonBean(this, "buttonMap");
			_v_.unmarshal(_os_);
			this.buttonMap.put(_k_, _v_);
		}
		return _os_;
	}

	public java.util.Map<Integer, xbean.ButtonBean> getButtonMap() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logMap(this, "buttonMap", this.verifyStandaloneOrLockHeld("getButtonMap", true)) : this.buttonMap;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		ButtonTableMap _o_ = null;
		if ( _o1_ instanceof ButtonTableMap ) _o_ = (ButtonTableMap)_o1_;
		else return false;
		if (!this.buttonMap.equals(_o_.buttonMap)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.buttonMap.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.buttonMap).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
