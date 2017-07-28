package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class ClientVersion extends limax.zdb.XBean {
    private xbean.ProVersion proVersion; 
    private java.util.HashMap<String, xbean.LibVersion> libVersions; 

    ClientVersion(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        proVersion = new ProVersion(this, "proVersion");
        libVersions = new java.util.HashMap<String, xbean.LibVersion>();
	}

	public ClientVersion() {
		this(null, null);
	}

	public ClientVersion(ClientVersion _o_) {
		this(_o_, null, null);
	}

	ClientVersion(ClientVersion _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.ClientVersion", true);
        this.proVersion = new ProVersion(_o_.proVersion, this, "proVersion");
        this.libVersions = new java.util.HashMap<String, xbean.LibVersion>();
        _o_.libVersions.forEach((_k_, _v_) -> this.libVersions.put(_k_, new LibVersion(_v_, this, "libVersions")));
	}

	public void copyFrom(ClientVersion _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromClientVersion", true);
		this.verifyStandaloneOrLockHeld("copyToClientVersion", false);
        this.proVersion.copyFrom(_o_.proVersion);
        java.util.Map<String, xbean.LibVersion> this_libVersions = limax.zdb.Logs.logMap(this, "libVersions", ()->{});
        this_libVersions.clear();
        _o_.libVersions.forEach((_k_, _v_) -> this_libVersions.put(_k_, new LibVersion(_v_)));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.proVersion);
        _os_.marshal_size(this.libVersions.size());
        for (java.util.Map.Entry<String, xbean.LibVersion> _e_ : this.libVersions.entrySet()) {
        	_os_.marshal(_e_.getKey());
        	_os_.marshal(_e_.getValue());
        }
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.proVersion.unmarshal(_os_);
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			String _k_ = _os_.unmarshal_String();
			xbean.LibVersion _v_ = new LibVersion(this, "libVersions");
			_v_.unmarshal(_os_);
			this.libVersions.put(_k_, _v_);
		}
		return _os_;
	}

	public xbean.ProVersion getProVersion() { 
		this.verifyStandaloneOrLockHeld("getProVersion", true);
		return this.proVersion;
	}

	public java.util.Map<String, xbean.LibVersion> getLibVersions() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logMap(this, "libVersions", this.verifyStandaloneOrLockHeld("getLibVersions", true)) : this.libVersions;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		ClientVersion _o_ = null;
		if ( _o1_ instanceof ClientVersion ) _o_ = (ClientVersion)_o1_;
		else return false;
		if (!this.proVersion.equals(_o_.proVersion)) return false;
		if (!this.libVersions.equals(_o_.libVersions)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.proVersion.hashCode();
		_h_ += _h_ * 31 + this.libVersions.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.proVersion).append(",");
		_sb_.append(this.libVersions).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
