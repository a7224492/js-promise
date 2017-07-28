package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class AddAllCardRecord extends limax.zdb.XBean {
    private String gmAdmin; 
    private int count; 
    private long time; 

    AddAllCardRecord(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        gmAdmin = "";
	}

	public AddAllCardRecord() {
		this(null, null);
	}

	public AddAllCardRecord(AddAllCardRecord _o_) {
		this(_o_, null, null);
	}

	AddAllCardRecord(AddAllCardRecord _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.AddAllCardRecord", true);
        this.gmAdmin = _o_.gmAdmin;
        this.count = _o_.count;
        this.time = _o_.time;
	}

	public void copyFrom(AddAllCardRecord _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromAddAllCardRecord", true);
		this.verifyStandaloneOrLockHeld("copyToAddAllCardRecord", false);
        limax.zdb.Logs.logObject(this, "gmAdmin");
        this.gmAdmin = _o_.gmAdmin;
        limax.zdb.Logs.logObject(this, "count");
        this.count = _o_.count;
        limax.zdb.Logs.logObject(this, "time");
        this.time = _o_.time;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.gmAdmin);
        _os_.marshal(this.count);
        _os_.marshal(this.time);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.gmAdmin = _os_.unmarshal_String();
		this.count = _os_.unmarshal_int();
		this.time = _os_.unmarshal_long();
		return _os_;
	}

	public String getGmAdmin() { 
		this.verifyStandaloneOrLockHeld("getGmAdmin", true);
		return this.gmAdmin;
	}

	public int getCount() { 
		this.verifyStandaloneOrLockHeld("getCount", true);
		return this.count;
	}

	public long getTime() { 
		this.verifyStandaloneOrLockHeld("getTime", true);
		return this.time;
	}

	public void setGmAdmin(String _v_) { 
		this.verifyStandaloneOrLockHeld("setGmAdmin", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "gmAdmin");
		this.gmAdmin = _v_;
	}

	public void setCount(int _v_) { 
		this.verifyStandaloneOrLockHeld("setCount", false);
		limax.zdb.Logs.logObject(this, "count");
		this.count = _v_;
	}

	public void setTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setTime", false);
		limax.zdb.Logs.logObject(this, "time");
		this.time = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		AddAllCardRecord _o_ = null;
		if ( _o1_ instanceof AddAllCardRecord ) _o_ = (AddAllCardRecord)_o1_;
		else return false;
		if (!this.gmAdmin.equals(_o_.gmAdmin)) return false;
		if (this.count != _o_.count) return false;
		if (this.time != _o_.time) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.gmAdmin.hashCode();
		_h_ += _h_ * 31 + this.count;
		_h_ += _h_ * 31 + (int)(this.time ^ (this.time >>> 32));
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append("T").append(this.gmAdmin.length()).append(",");
		_sb_.append(this.count).append(",");
		_sb_.append(this.time).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
