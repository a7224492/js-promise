package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class LibVersion extends limax.zdb.XBean {
    private String version; 
    private String description; 
    private String url; 
    private boolean forceUpdate; 

    LibVersion(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        version = "";
        description = "";
        url = "";
	}

	public LibVersion() {
		this(null, null);
	}

	public LibVersion(LibVersion _o_) {
		this(_o_, null, null);
	}

	LibVersion(LibVersion _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.LibVersion", true);
        this.version = _o_.version;
        this.description = _o_.description;
        this.url = _o_.url;
        this.forceUpdate = _o_.forceUpdate;
	}

	public void copyFrom(LibVersion _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromLibVersion", true);
		this.verifyStandaloneOrLockHeld("copyToLibVersion", false);
        limax.zdb.Logs.logObject(this, "version");
        this.version = _o_.version;
        limax.zdb.Logs.logObject(this, "description");
        this.description = _o_.description;
        limax.zdb.Logs.logObject(this, "url");
        this.url = _o_.url;
        limax.zdb.Logs.logObject(this, "forceUpdate");
        this.forceUpdate = _o_.forceUpdate;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.version);
        _os_.marshal(this.description);
        _os_.marshal(this.url);
        _os_.marshal(this.forceUpdate);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.version = _os_.unmarshal_String();
		this.description = _os_.unmarshal_String();
		this.url = _os_.unmarshal_String();
		this.forceUpdate = _os_.unmarshal_boolean();
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

	public String getUrl() { 
		this.verifyStandaloneOrLockHeld("getUrl", true);
		return this.url;
	}

	public boolean getForceUpdate() { 
		this.verifyStandaloneOrLockHeld("getForceUpdate", true);
		return this.forceUpdate;
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

	public void setUrl(String _v_) { 
		this.verifyStandaloneOrLockHeld("setUrl", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "url");
		this.url = _v_;
	}

	public void setForceUpdate(boolean _v_) { 
		this.verifyStandaloneOrLockHeld("setForceUpdate", false);
		limax.zdb.Logs.logObject(this, "forceUpdate");
		this.forceUpdate = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		LibVersion _o_ = null;
		if ( _o1_ instanceof LibVersion ) _o_ = (LibVersion)_o1_;
		else return false;
		if (!this.version.equals(_o_.version)) return false;
		if (!this.description.equals(_o_.description)) return false;
		if (!this.url.equals(_o_.url)) return false;
		if (this.forceUpdate != _o_.forceUpdate) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.version.hashCode();
		_h_ += _h_ * 31 + this.description.hashCode();
		_h_ += _h_ * 31 + this.url.hashCode();
		_h_ += _h_ * 31 + (this.forceUpdate ? 1231 : 1237);
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append("T").append(this.version.length()).append(",");
		_sb_.append("T").append(this.description.length()).append(",");
		_sb_.append("T").append(this.url.length()).append(",");
		_sb_.append(this.forceUpdate).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
