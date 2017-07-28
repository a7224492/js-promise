package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class NormalContact extends limax.zdb.XBean {
    private int id; 
    private int agencyId; 
    private String content; 
    private String sender; 

    NormalContact(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        content = "";
        sender = "";
	}

	public NormalContact() {
		this(null, null);
	}

	public NormalContact(NormalContact _o_) {
		this(_o_, null, null);
	}

	NormalContact(NormalContact _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.NormalContact", true);
        this.id = _o_.id;
        this.agencyId = _o_.agencyId;
        this.content = _o_.content;
        this.sender = _o_.sender;
	}

	public void copyFrom(NormalContact _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromNormalContact", true);
		this.verifyStandaloneOrLockHeld("copyToNormalContact", false);
        limax.zdb.Logs.logObject(this, "id");
        this.id = _o_.id;
        limax.zdb.Logs.logObject(this, "agencyId");
        this.agencyId = _o_.agencyId;
        limax.zdb.Logs.logObject(this, "content");
        this.content = _o_.content;
        limax.zdb.Logs.logObject(this, "sender");
        this.sender = _o_.sender;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.id);
        _os_.marshal(this.agencyId);
        _os_.marshal(this.content);
        _os_.marshal(this.sender);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.id = _os_.unmarshal_int();
		this.agencyId = _os_.unmarshal_int();
		this.content = _os_.unmarshal_String();
		this.sender = _os_.unmarshal_String();
		return _os_;
	}

	public int getId() { 
		this.verifyStandaloneOrLockHeld("getId", true);
		return this.id;
	}

	public int getAgencyId() { 
		this.verifyStandaloneOrLockHeld("getAgencyId", true);
		return this.agencyId;
	}

	public String getContent() { 
		this.verifyStandaloneOrLockHeld("getContent", true);
		return this.content;
	}

	public String getSender() { 
		this.verifyStandaloneOrLockHeld("getSender", true);
		return this.sender;
	}

	public void setId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setId", false);
		limax.zdb.Logs.logObject(this, "id");
		this.id = _v_;
	}

	public void setAgencyId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setAgencyId", false);
		limax.zdb.Logs.logObject(this, "agencyId");
		this.agencyId = _v_;
	}

	public void setContent(String _v_) { 
		this.verifyStandaloneOrLockHeld("setContent", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "content");
		this.content = _v_;
	}

	public void setSender(String _v_) { 
		this.verifyStandaloneOrLockHeld("setSender", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "sender");
		this.sender = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		NormalContact _o_ = null;
		if ( _o1_ instanceof NormalContact ) _o_ = (NormalContact)_o1_;
		else return false;
		if (this.id != _o_.id) return false;
		if (this.agencyId != _o_.agencyId) return false;
		if (!this.content.equals(_o_.content)) return false;
		if (!this.sender.equals(_o_.sender)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.id;
		_h_ += _h_ * 31 + this.agencyId;
		_h_ += _h_ * 31 + this.content.hashCode();
		_h_ += _h_ * 31 + this.sender.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.id).append(",");
		_sb_.append(this.agencyId).append(",");
		_sb_.append("T").append(this.content.length()).append(",");
		_sb_.append("T").append(this.sender.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
