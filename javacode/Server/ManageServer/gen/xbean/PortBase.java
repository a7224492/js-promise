package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class PortBase extends limax.zdb.XBean {
    private String ip; 
    private int serverType; 
    private int portType; 
    private int area; 
    private int port; 

    PortBase(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        ip = "";
	}

	public PortBase() {
		this(null, null);
	}

	public PortBase(PortBase _o_) {
		this(_o_, null, null);
	}

	PortBase(PortBase _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.PortBase", true);
        this.ip = _o_.ip;
        this.serverType = _o_.serverType;
        this.portType = _o_.portType;
        this.area = _o_.area;
        this.port = _o_.port;
	}

	public void copyFrom(PortBase _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromPortBase", true);
		this.verifyStandaloneOrLockHeld("copyToPortBase", false);
        limax.zdb.Logs.logObject(this, "ip");
        this.ip = _o_.ip;
        limax.zdb.Logs.logObject(this, "serverType");
        this.serverType = _o_.serverType;
        limax.zdb.Logs.logObject(this, "portType");
        this.portType = _o_.portType;
        limax.zdb.Logs.logObject(this, "area");
        this.area = _o_.area;
        limax.zdb.Logs.logObject(this, "port");
        this.port = _o_.port;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.ip);
        _os_.marshal(this.serverType);
        _os_.marshal(this.portType);
        _os_.marshal(this.area);
        _os_.marshal(this.port);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.ip = _os_.unmarshal_String();
		this.serverType = _os_.unmarshal_int();
		this.portType = _os_.unmarshal_int();
		this.area = _os_.unmarshal_int();
		this.port = _os_.unmarshal_int();
		return _os_;
	}

	public String getIp() { 
		this.verifyStandaloneOrLockHeld("getIp", true);
		return this.ip;
	}

	public int getServerType() { 
		this.verifyStandaloneOrLockHeld("getServerType", true);
		return this.serverType;
	}

	public int getPortType() { 
		this.verifyStandaloneOrLockHeld("getPortType", true);
		return this.portType;
	}

	public int getArea() { 
		this.verifyStandaloneOrLockHeld("getArea", true);
		return this.area;
	}

	public int getPort() { 
		this.verifyStandaloneOrLockHeld("getPort", true);
		return this.port;
	}

	public void setIp(String _v_) { 
		this.verifyStandaloneOrLockHeld("setIp", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "ip");
		this.ip = _v_;
	}

	public void setServerType(int _v_) { 
		this.verifyStandaloneOrLockHeld("setServerType", false);
		limax.zdb.Logs.logObject(this, "serverType");
		this.serverType = _v_;
	}

	public void setPortType(int _v_) { 
		this.verifyStandaloneOrLockHeld("setPortType", false);
		limax.zdb.Logs.logObject(this, "portType");
		this.portType = _v_;
	}

	public void setArea(int _v_) { 
		this.verifyStandaloneOrLockHeld("setArea", false);
		limax.zdb.Logs.logObject(this, "area");
		this.area = _v_;
	}

	public void setPort(int _v_) { 
		this.verifyStandaloneOrLockHeld("setPort", false);
		limax.zdb.Logs.logObject(this, "port");
		this.port = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		PortBase _o_ = null;
		if ( _o1_ instanceof PortBase ) _o_ = (PortBase)_o1_;
		else return false;
		if (!this.ip.equals(_o_.ip)) return false;
		if (this.serverType != _o_.serverType) return false;
		if (this.portType != _o_.portType) return false;
		if (this.area != _o_.area) return false;
		if (this.port != _o_.port) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.ip.hashCode();
		_h_ += _h_ * 31 + this.serverType;
		_h_ += _h_ * 31 + this.portType;
		_h_ += _h_ * 31 + this.area;
		_h_ += _h_ * 31 + this.port;
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append("T").append(this.ip.length()).append(",");
		_sb_.append(this.serverType).append(",");
		_sb_.append(this.portType).append(",");
		_sb_.append(this.area).append(",");
		_sb_.append(this.port).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
