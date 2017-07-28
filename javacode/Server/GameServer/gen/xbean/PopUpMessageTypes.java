package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class PopUpMessageTypes extends limax.zdb.XBean {
    private int tab; 
    private int style; 
    private java.util.ArrayList<String> content; 

    PopUpMessageTypes(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        content = new java.util.ArrayList<String>();
	}

	public PopUpMessageTypes() {
		this(null, null);
	}

	public PopUpMessageTypes(PopUpMessageTypes _o_) {
		this(_o_, null, null);
	}

	PopUpMessageTypes(PopUpMessageTypes _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.PopUpMessageTypes", true);
        this.tab = _o_.tab;
        this.style = _o_.style;
        this.content = new java.util.ArrayList<String>();
        this.content.addAll(_o_.content);
	}

	public void copyFrom(PopUpMessageTypes _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromPopUpMessageTypes", true);
		this.verifyStandaloneOrLockHeld("copyToPopUpMessageTypes", false);
        limax.zdb.Logs.logObject(this, "tab");
        this.tab = _o_.tab;
        limax.zdb.Logs.logObject(this, "style");
        this.style = _o_.style;
        java.util.List<String> this_content = limax.zdb.Logs.logList(this, "content", ()->{});
        this_content.clear();
        this_content.addAll(_o_.content);
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.tab);
        _os_.marshal(this.style);
        _os_.marshal_size(this.content.size());
        for (String _v_ : this.content) {
        	_os_.marshal(_v_);
        }
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.tab = _os_.unmarshal_int();
		this.style = _os_.unmarshal_int();
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			String _v_ = _os_.unmarshal_String();
			this.content.add(_v_);
		}
		return _os_;
	}

	public int getTab() { 
		this.verifyStandaloneOrLockHeld("getTab", true);
		return this.tab;
	}

	public int getStyle() { 
		this.verifyStandaloneOrLockHeld("getStyle", true);
		return this.style;
	}

	public java.util.List<String> getContent() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logList(this, "content", this.verifyStandaloneOrLockHeld("getContent", true)) : this.content;
	}

	public void setTab(int _v_) { 
		this.verifyStandaloneOrLockHeld("setTab", false);
		limax.zdb.Logs.logObject(this, "tab");
		this.tab = _v_;
	}

	public void setStyle(int _v_) { 
		this.verifyStandaloneOrLockHeld("setStyle", false);
		limax.zdb.Logs.logObject(this, "style");
		this.style = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		PopUpMessageTypes _o_ = null;
		if ( _o1_ instanceof PopUpMessageTypes ) _o_ = (PopUpMessageTypes)_o1_;
		else return false;
		if (this.tab != _o_.tab) return false;
		if (this.style != _o_.style) return false;
		if (!this.content.equals(_o_.content)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.tab;
		_h_ += _h_ * 31 + this.style;
		_h_ += _h_ * 31 + this.content.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.tab).append(",");
		_sb_.append(this.style).append(",");
		_sb_.append(this.content).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
