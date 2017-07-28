package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class VersionUpdateBean extends limax.zdb.XBean {
    private String channel; 
    private String subchannel; 
    private String libVersion; 
    private String lastLibVersion; 
    private String LibUrl; 
    private String proVersion; 
    private boolean proForceUpdate; 
    private String proUrl; 
    private String reviewVersion; 
    private String reviewUrl; 

    VersionUpdateBean(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        channel = "";
        subchannel = "";
        libVersion = "";
        lastLibVersion = "";
        LibUrl = "";
        proVersion = "";
        proUrl = "";
        reviewVersion = "";
        reviewUrl = "";
	}

	public VersionUpdateBean() {
		this(null, null);
	}

	public VersionUpdateBean(VersionUpdateBean _o_) {
		this(_o_, null, null);
	}

	VersionUpdateBean(VersionUpdateBean _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.VersionUpdateBean", true);
        this.channel = _o_.channel;
        this.subchannel = _o_.subchannel;
        this.libVersion = _o_.libVersion;
        this.lastLibVersion = _o_.lastLibVersion;
        this.LibUrl = _o_.LibUrl;
        this.proVersion = _o_.proVersion;
        this.proForceUpdate = _o_.proForceUpdate;
        this.proUrl = _o_.proUrl;
        this.reviewVersion = _o_.reviewVersion;
        this.reviewUrl = _o_.reviewUrl;
	}

	public void copyFrom(VersionUpdateBean _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromVersionUpdateBean", true);
		this.verifyStandaloneOrLockHeld("copyToVersionUpdateBean", false);
        limax.zdb.Logs.logObject(this, "channel");
        this.channel = _o_.channel;
        limax.zdb.Logs.logObject(this, "subchannel");
        this.subchannel = _o_.subchannel;
        limax.zdb.Logs.logObject(this, "libVersion");
        this.libVersion = _o_.libVersion;
        limax.zdb.Logs.logObject(this, "lastLibVersion");
        this.lastLibVersion = _o_.lastLibVersion;
        limax.zdb.Logs.logObject(this, "LibUrl");
        this.LibUrl = _o_.LibUrl;
        limax.zdb.Logs.logObject(this, "proVersion");
        this.proVersion = _o_.proVersion;
        limax.zdb.Logs.logObject(this, "proForceUpdate");
        this.proForceUpdate = _o_.proForceUpdate;
        limax.zdb.Logs.logObject(this, "proUrl");
        this.proUrl = _o_.proUrl;
        limax.zdb.Logs.logObject(this, "reviewVersion");
        this.reviewVersion = _o_.reviewVersion;
        limax.zdb.Logs.logObject(this, "reviewUrl");
        this.reviewUrl = _o_.reviewUrl;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.channel);
        _os_.marshal(this.subchannel);
        _os_.marshal(this.libVersion);
        _os_.marshal(this.lastLibVersion);
        _os_.marshal(this.LibUrl);
        _os_.marshal(this.proVersion);
        _os_.marshal(this.proForceUpdate);
        _os_.marshal(this.proUrl);
        _os_.marshal(this.reviewVersion);
        _os_.marshal(this.reviewUrl);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.channel = _os_.unmarshal_String();
		this.subchannel = _os_.unmarshal_String();
		this.libVersion = _os_.unmarshal_String();
		this.lastLibVersion = _os_.unmarshal_String();
		this.LibUrl = _os_.unmarshal_String();
		this.proVersion = _os_.unmarshal_String();
		this.proForceUpdate = _os_.unmarshal_boolean();
		this.proUrl = _os_.unmarshal_String();
		this.reviewVersion = _os_.unmarshal_String();
		this.reviewUrl = _os_.unmarshal_String();
		return _os_;
	}

	public String getChannel() { 
		this.verifyStandaloneOrLockHeld("getChannel", true);
		return this.channel;
	}

	public String getSubchannel() { 
		this.verifyStandaloneOrLockHeld("getSubchannel", true);
		return this.subchannel;
	}

	public String getLibVersion() { 
		this.verifyStandaloneOrLockHeld("getLibVersion", true);
		return this.libVersion;
	}

	public String getLastLibVersion() { 
		this.verifyStandaloneOrLockHeld("getLastLibVersion", true);
		return this.lastLibVersion;
	}

	public String getLibUrl() { 
		this.verifyStandaloneOrLockHeld("getLibUrl", true);
		return this.LibUrl;
	}

	public String getProVersion() { 
		this.verifyStandaloneOrLockHeld("getProVersion", true);
		return this.proVersion;
	}

	public boolean getProForceUpdate() { 
		this.verifyStandaloneOrLockHeld("getProForceUpdate", true);
		return this.proForceUpdate;
	}

	public String getProUrl() { 
		this.verifyStandaloneOrLockHeld("getProUrl", true);
		return this.proUrl;
	}

	public String getReviewVersion() { 
		this.verifyStandaloneOrLockHeld("getReviewVersion", true);
		return this.reviewVersion;
	}

	public String getReviewUrl() { 
		this.verifyStandaloneOrLockHeld("getReviewUrl", true);
		return this.reviewUrl;
	}

	public void setChannel(String _v_) { 
		this.verifyStandaloneOrLockHeld("setChannel", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "channel");
		this.channel = _v_;
	}

	public void setSubchannel(String _v_) { 
		this.verifyStandaloneOrLockHeld("setSubchannel", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "subchannel");
		this.subchannel = _v_;
	}

	public void setLibVersion(String _v_) { 
		this.verifyStandaloneOrLockHeld("setLibVersion", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "libVersion");
		this.libVersion = _v_;
	}

	public void setLastLibVersion(String _v_) { 
		this.verifyStandaloneOrLockHeld("setLastLibVersion", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "lastLibVersion");
		this.lastLibVersion = _v_;
	}

	public void setLibUrl(String _v_) { 
		this.verifyStandaloneOrLockHeld("setLibUrl", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "LibUrl");
		this.LibUrl = _v_;
	}

	public void setProVersion(String _v_) { 
		this.verifyStandaloneOrLockHeld("setProVersion", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "proVersion");
		this.proVersion = _v_;
	}

	public void setProForceUpdate(boolean _v_) { 
		this.verifyStandaloneOrLockHeld("setProForceUpdate", false);
		limax.zdb.Logs.logObject(this, "proForceUpdate");
		this.proForceUpdate = _v_;
	}

	public void setProUrl(String _v_) { 
		this.verifyStandaloneOrLockHeld("setProUrl", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "proUrl");
		this.proUrl = _v_;
	}

	public void setReviewVersion(String _v_) { 
		this.verifyStandaloneOrLockHeld("setReviewVersion", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "reviewVersion");
		this.reviewVersion = _v_;
	}

	public void setReviewUrl(String _v_) { 
		this.verifyStandaloneOrLockHeld("setReviewUrl", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "reviewUrl");
		this.reviewUrl = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		VersionUpdateBean _o_ = null;
		if ( _o1_ instanceof VersionUpdateBean ) _o_ = (VersionUpdateBean)_o1_;
		else return false;
		if (!this.channel.equals(_o_.channel)) return false;
		if (!this.subchannel.equals(_o_.subchannel)) return false;
		if (!this.libVersion.equals(_o_.libVersion)) return false;
		if (!this.lastLibVersion.equals(_o_.lastLibVersion)) return false;
		if (!this.LibUrl.equals(_o_.LibUrl)) return false;
		if (!this.proVersion.equals(_o_.proVersion)) return false;
		if (this.proForceUpdate != _o_.proForceUpdate) return false;
		if (!this.proUrl.equals(_o_.proUrl)) return false;
		if (!this.reviewVersion.equals(_o_.reviewVersion)) return false;
		if (!this.reviewUrl.equals(_o_.reviewUrl)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.channel.hashCode();
		_h_ += _h_ * 31 + this.subchannel.hashCode();
		_h_ += _h_ * 31 + this.libVersion.hashCode();
		_h_ += _h_ * 31 + this.lastLibVersion.hashCode();
		_h_ += _h_ * 31 + this.LibUrl.hashCode();
		_h_ += _h_ * 31 + this.proVersion.hashCode();
		_h_ += _h_ * 31 + (this.proForceUpdate ? 1231 : 1237);
		_h_ += _h_ * 31 + this.proUrl.hashCode();
		_h_ += _h_ * 31 + this.reviewVersion.hashCode();
		_h_ += _h_ * 31 + this.reviewUrl.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append("T").append(this.channel.length()).append(",");
		_sb_.append("T").append(this.subchannel.length()).append(",");
		_sb_.append("T").append(this.libVersion.length()).append(",");
		_sb_.append("T").append(this.lastLibVersion.length()).append(",");
		_sb_.append("T").append(this.LibUrl.length()).append(",");
		_sb_.append("T").append(this.proVersion.length()).append(",");
		_sb_.append(this.proForceUpdate).append(",");
		_sb_.append("T").append(this.proUrl.length()).append(",");
		_sb_.append("T").append(this.reviewVersion.length()).append(",");
		_sb_.append("T").append(this.reviewUrl.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
