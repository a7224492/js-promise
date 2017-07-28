package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class ProVersion extends limax.zdb.XBean {
    private String version; 
    private String description; 

    ProVersion(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        version = "";
        description = "";
	}

	public ProVersion() {
		this(null, null);
	}

	public ProVersion(ProVersion _o_) {
		this(_o_, null, null);
	}

	ProVersion(ProVersion _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.ProVersion", true);
        this.version = _o_.version;
        this.description = _o_.description;
	}

	public void copyFrom(ProVersion _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromProVersion", true);
		this.verifyStandaloneOrLockHeld("copyToProVersion", false);
        limax.zdb.Logs.logObject(this, "version");
        this.version = _o_.version;
        limax.zdb.Logs.logObject(this, "description");
        this.description = _o_.description;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.version);
        _os_.marshal(this.description);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.version = _os_.unmarshal_String();
		this.description = _os_.unmarshal_String();
		return _os_;
	}

	public String getVersion() { 
		this.verifyStandaloneOrLockHeld("getVersion", true);
		return this.version;
	}

	public String getDescription() { 
		this.verifyStandaloneOrLockHeld("getDescription", true);
		return this.description;
	}

	public void setVersion(String _v_) { 
		this.verifyStandaloneOrLockHeld("setVersion", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "version");
		this.version = _v_;
	}

	public void setDescription(String _v_) { 
		this.verifyStandaloneOrLockHeld("setDescription", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "description");
		this.description = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		ProVersion _o_ = null;
		if ( _o1_ instanceof ProVersion ) _o_ = (ProVersion)_o1_;
		else return false;
		if (!this.version.equals(_o_.version)) return false;
		if (!this.description.equals(_o_.description)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.version.hashCode();
		_h_ += _h_ * 31 + this.description.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append("T").append(this.version.length()).append(",");
		_sb_.append("T").append(this.description.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
