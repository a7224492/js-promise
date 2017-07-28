package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class Mail extends limax.zdb.XBean {
    private long id; 
    private int type; 
    private String msg; 
    private long time; 
    private int sender; 

    Mail(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        msg = "";
	}

	public Mail() {
		this(null, null);
	}

	public Mail(Mail _o_) {
		this(_o_, null, null);
	}

	Mail(Mail _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.Mail", true);
        this.id = _o_.id;
        this.type = _o_.type;
        this.msg = _o_.msg;
        this.time = _o_.time;
        this.sender = _o_.sender;
	}

	public void copyFrom(Mail _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromMail", true);
		this.verifyStandaloneOrLockHeld("copyToMail", false);
        limax.zdb.Logs.logObject(this, "id");
        this.id = _o_.id;
        limax.zdb.Logs.logObject(this, "type");
        this.type = _o_.type;
        limax.zdb.Logs.logObject(this, "msg");
        this.msg = _o_.msg;
        limax.zdb.Logs.logObject(this, "time");
        this.time = _o_.time;
        limax.zdb.Logs.logObject(this, "sender");
        this.sender = _o_.sender;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.id);
        _os_.marshal(this.type);
        _os_.marshal(this.msg);
        _os_.marshal(this.time);
        _os_.marshal(this.sender);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.id = _os_.unmarshal_long();
		this.type = _os_.unmarshal_int();
		this.msg = _os_.unmarshal_String();
		this.time = _os_.unmarshal_long();
		this.sender = _os_.unmarshal_int();
		return _os_;
	}

	public long getId() { 
		this.verifyStandaloneOrLockHeld("getId", true);
		return this.id;
	}

	public int getType() { 
		this.verifyStandaloneOrLockHeld("getType", true);
		return this.type;
	}

	public String getMsg() { 
		this.verifyStandaloneOrLockHeld("getMsg", true);
		return this.msg;
	}

	public long getTime() { 
		this.verifyStandaloneOrLockHeld("getTime", true);
		return this.time;
	}

	public int getSender() { 
		this.verifyStandaloneOrLockHeld("getSender", true);
		return this.sender;
	}

	public void setId(long _v_) { 
		this.verifyStandaloneOrLockHeld("setId", false);
		limax.zdb.Logs.logObject(this, "id");
		this.id = _v_;
	}

	public void setType(int _v_) { 
		this.verifyStandaloneOrLockHeld("setType", false);
		limax.zdb.Logs.logObject(this, "type");
		this.type = _v_;
	}

	public void setMsg(String _v_) { 
		this.verifyStandaloneOrLockHeld("setMsg", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "msg");
		this.msg = _v_;
	}

	public void setTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setTime", false);
		limax.zdb.Logs.logObject(this, "time");
		this.time = _v_;
	}

	public void setSender(int _v_) { 
		this.verifyStandaloneOrLockHeld("setSender", false);
		limax.zdb.Logs.logObject(this, "sender");
		this.sender = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		Mail _o_ = null;
		if ( _o1_ instanceof Mail ) _o_ = (Mail)_o1_;
		else return false;
		if (this.id != _o_.id) return false;
		if (this.type != _o_.type) return false;
		if (!this.msg.equals(_o_.msg)) return false;
		if (this.time != _o_.time) return false;
		if (this.sender != _o_.sender) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + (int)(this.id ^ (this.id >>> 32));
		_h_ += _h_ * 31 + this.type;
		_h_ += _h_ * 31 + this.msg.hashCode();
		_h_ += _h_ * 31 + (int)(this.time ^ (this.time >>> 32));
		_h_ += _h_ * 31 + this.sender;
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.id).append(",");
		_sb_.append(this.type).append(",");
		_sb_.append("T").append(this.msg.length()).append(",");
		_sb_.append(this.time).append(",");
		_sb_.append(this.sender).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
