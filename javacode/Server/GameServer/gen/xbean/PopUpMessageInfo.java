package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class PopUpMessageInfo extends limax.zdb.XBean {
    private long version; 
    private long create; 
    private long update; 
    private int pop; 
    private int mode; 
    private java.util.ArrayList<xbean.PopUpMessageTimes> times; 
    private java.util.ArrayList<xbean.PopUpMessageTypes> types; 

    PopUpMessageInfo(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        times = new java.util.ArrayList<xbean.PopUpMessageTimes>();
        types = new java.util.ArrayList<xbean.PopUpMessageTypes>();
	}

	public PopUpMessageInfo() {
		this(null, null);
	}

	public PopUpMessageInfo(PopUpMessageInfo _o_) {
		this(_o_, null, null);
	}

	PopUpMessageInfo(PopUpMessageInfo _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.PopUpMessageInfo", true);
        this.version = _o_.version;
        this.create = _o_.create;
        this.update = _o_.update;
        this.pop = _o_.pop;
        this.mode = _o_.mode;
        this.times = new java.util.ArrayList<xbean.PopUpMessageTimes>();
        _o_.times.forEach(_v_ -> this.times.add(new PopUpMessageTimes(_v_, this, "times")));
        this.types = new java.util.ArrayList<xbean.PopUpMessageTypes>();
        _o_.types.forEach(_v_ -> this.types.add(new PopUpMessageTypes(_v_, this, "types")));
	}

	public void copyFrom(PopUpMessageInfo _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromPopUpMessageInfo", true);
		this.verifyStandaloneOrLockHeld("copyToPopUpMessageInfo", false);
        limax.zdb.Logs.logObject(this, "version");
        this.version = _o_.version;
        limax.zdb.Logs.logObject(this, "create");
        this.create = _o_.create;
        limax.zdb.Logs.logObject(this, "update");
        this.update = _o_.update;
        limax.zdb.Logs.logObject(this, "pop");
        this.pop = _o_.pop;
        limax.zdb.Logs.logObject(this, "mode");
        this.mode = _o_.mode;
        java.util.List<xbean.PopUpMessageTimes> this_times = limax.zdb.Logs.logList(this, "times", ()->{});
        this_times.clear();
        _o_.times.forEach(_v_ -> this_times.add(new PopUpMessageTimes(_v_)));
        java.util.List<xbean.PopUpMessageTypes> this_types = limax.zdb.Logs.logList(this, "types", ()->{});
        this_types.clear();
        _o_.types.forEach(_v_ -> this_types.add(new PopUpMessageTypes(_v_)));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.version);
        _os_.marshal(this.create);
        _os_.marshal(this.update);
        _os_.marshal(this.pop);
        _os_.marshal(this.mode);
        _os_.marshal_size(this.times.size());
        for (xbean.PopUpMessageTimes _v_ : this.times) {
        	_os_.marshal(_v_);
        }
        _os_.marshal_size(this.types.size());
        for (xbean.PopUpMessageTypes _v_ : this.types) {
        	_os_.marshal(_v_);
        }
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.version = _os_.unmarshal_long();
		this.create = _os_.unmarshal_long();
		this.update = _os_.unmarshal_long();
		this.pop = _os_.unmarshal_int();
		this.mode = _os_.unmarshal_int();
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			xbean.PopUpMessageTimes _v_ = new PopUpMessageTimes(this, "times");
			_v_.unmarshal(_os_);
			this.times.add(_v_);
		}
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			xbean.PopUpMessageTypes _v_ = new PopUpMessageTypes(this, "types");
			_v_.unmarshal(_os_);
			this.types.add(_v_);
		}
		return _os_;
	}

	public long getVersion() { 
		this.verifyStandaloneOrLockHeld("getVersion", true);
		return this.version;
	}

	public long getCreate() { 
		this.verifyStandaloneOrLockHeld("getCreate", true);
		return this.create;
	}

	public long getUpdate() { 
		this.verifyStandaloneOrLockHeld("getUpdate", true);
		return this.update;
	}

	public int getPop() { 
		this.verifyStandaloneOrLockHeld("getPop", true);
		return this.pop;
	}

	public int getMode() { 
		this.verifyStandaloneOrLockHeld("getMode", true);
		return this.mode;
	}

	public java.util.List<xbean.PopUpMessageTimes> getTimes() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logList(this, "times", this.verifyStandaloneOrLockHeld("getTimes", true)) : this.times;
	}

	public java.util.List<xbean.PopUpMessageTypes> getTypes() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logList(this, "types", this.verifyStandaloneOrLockHeld("getTypes", true)) : this.types;
	}

	public void setVersion(long _v_) { 
		this.verifyStandaloneOrLockHeld("setVersion", false);
		limax.zdb.Logs.logObject(this, "version");
		this.version = _v_;
	}

	public void setCreate(long _v_) { 
		this.verifyStandaloneOrLockHeld("setCreate", false);
		limax.zdb.Logs.logObject(this, "create");
		this.create = _v_;
	}

	public void setUpdate(long _v_) { 
		this.verifyStandaloneOrLockHeld("setUpdate", false);
		limax.zdb.Logs.logObject(this, "update");
		this.update = _v_;
	}

	public void setPop(int _v_) { 
		this.verifyStandaloneOrLockHeld("setPop", false);
		limax.zdb.Logs.logObject(this, "pop");
		this.pop = _v_;
	}

	public void setMode(int _v_) { 
		this.verifyStandaloneOrLockHeld("setMode", false);
		limax.zdb.Logs.logObject(this, "mode");
		this.mode = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		PopUpMessageInfo _o_ = null;
		if ( _o1_ instanceof PopUpMessageInfo ) _o_ = (PopUpMessageInfo)_o1_;
		else return false;
		if (this.version != _o_.version) return false;
		if (this.create != _o_.create) return false;
		if (this.update != _o_.update) return false;
		if (this.pop != _o_.pop) return false;
		if (this.mode != _o_.mode) return false;
		if (!this.times.equals(_o_.times)) return false;
		if (!this.types.equals(_o_.types)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + (int)(this.version ^ (this.version >>> 32));
		_h_ += _h_ * 31 + (int)(this.create ^ (this.create >>> 32));
		_h_ += _h_ * 31 + (int)(this.update ^ (this.update >>> 32));
		_h_ += _h_ * 31 + this.pop;
		_h_ += _h_ * 31 + this.mode;
		_h_ += _h_ * 31 + this.times.hashCode();
		_h_ += _h_ * 31 + this.types.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.version).append(",");
		_sb_.append(this.create).append(",");
		_sb_.append(this.update).append(",");
		_sb_.append(this.pop).append(",");
		_sb_.append(this.mode).append(",");
		_sb_.append(this.times).append(",");
		_sb_.append(this.types).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
