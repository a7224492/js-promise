package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class ServerInfo extends limax.zdb.XBean {
    private int id; 
    private int type; 
    private String ip; 
    private int port4server; 
    private int port4webSocketClient; 
    private int port4SocketClient; 
    private String ip4Client; 

    ServerInfo(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        ip = "";
        ip4Client = "";
	}

	public ServerInfo() {
		this(null, null);
	}

	public ServerInfo(ServerInfo _o_) {
		this(_o_, null, null);
	}

	ServerInfo(ServerInfo _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.ServerInfo", true);
        this.id = _o_.id;
        this.type = _o_.type;
        this.ip = _o_.ip;
        this.port4server = _o_.port4server;
        this.port4webSocketClient = _o_.port4webSocketClient;
        this.port4SocketClient = _o_.port4SocketClient;
        this.ip4Client = _o_.ip4Client;
	}

	public void copyFrom(ServerInfo _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromServerInfo", true);
		this.verifyStandaloneOrLockHeld("copyToServerInfo", false);
        limax.zdb.Logs.logObject(this, "id");
        this.id = _o_.id;
        limax.zdb.Logs.logObject(this, "type");
        this.type = _o_.type;
        limax.zdb.Logs.logObject(this, "ip");
        this.ip = _o_.ip;
        limax.zdb.Logs.logObject(this, "port4server");
        this.port4server = _o_.port4server;
        limax.zdb.Logs.logObject(this, "port4webSocketClient");
        this.port4webSocketClient = _o_.port4webSocketClient;
        limax.zdb.Logs.logObject(this, "port4SocketClient");
        this.port4SocketClient = _o_.port4SocketClient;
        limax.zdb.Logs.logObject(this, "ip4Client");
        this.ip4Client = _o_.ip4Client;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.id);
        _os_.marshal(this.type);
        _os_.marshal(this.ip);
        _os_.marshal(this.port4server);
        _os_.marshal(this.port4webSocketClient);
        _os_.marshal(this.port4SocketClient);
        _os_.marshal(this.ip4Client);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.id = _os_.unmarshal_int();
		this.type = _os_.unmarshal_int();
		this.ip = _os_.unmarshal_String();
		this.port4server = _os_.unmarshal_int();
		this.port4webSocketClient = _os_.unmarshal_int();
		this.port4SocketClient = _os_.unmarshal_int();
		this.ip4Client = _os_.unmarshal_String();
		return _os_;
	}

	public int getId() { 
		this.verifyStandaloneOrLockHeld("getId", true);
		return this.id;
	}

	public int getType() { 
		this.verifyStandaloneOrLockHeld("getType", true);
		return this.type;
	}

	public String getIp() { 
		this.verifyStandaloneOrLockHeld("getIp", true);
		return this.ip;
	}

	public int getPort4server() { 
		this.verifyStandaloneOrLockHeld("getPort4server", true);
		return this.port4server;
	}

	public int getPort4webSocketClient() { 
		this.verifyStandaloneOrLockHeld("getPort4webSocketClient", true);
		return this.port4webSocketClient;
	}

	public int getPort4SocketClient() { 
		this.verifyStandaloneOrLockHeld("getPort4SocketClient", true);
		return this.port4SocketClient;
	}

	public String getIp4Client() { 
		this.verifyStandaloneOrLockHeld("getIp4Client", true);
		return this.ip4Client;
	}

	public void setId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setId", false);
		limax.zdb.Logs.logObject(this, "id");
		this.id = _v_;
	}

	public void setType(int _v_) { 
		this.verifyStandaloneOrLockHeld("setType", false);
		limax.zdb.Logs.logObject(this, "type");
		this.type = _v_;
	}

	public void setIp(String _v_) { 
		this.verifyStandaloneOrLockHeld("setIp", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "ip");
		this.ip = _v_;
	}

	public void setPort4server(int _v_) { 
		this.verifyStandaloneOrLockHeld("setPort4server", false);
		limax.zdb.Logs.logObject(this, "port4server");
		this.port4server = _v_;
	}

	public void setPort4webSocketClient(int _v_) { 
		this.verifyStandaloneOrLockHeld("setPort4webSocketClient", false);
		limax.zdb.Logs.logObject(this, "port4webSocketClient");
		this.port4webSocketClient = _v_;
	}

	public void setPort4SocketClient(int _v_) { 
		this.verifyStandaloneOrLockHeld("setPort4SocketClient", false);
		limax.zdb.Logs.logObject(this, "port4SocketClient");
		this.port4SocketClient = _v_;
	}

	public void setIp4Client(String _v_) { 
		this.verifyStandaloneOrLockHeld("setIp4Client", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "ip4Client");
		this.ip4Client = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		ServerInfo _o_ = null;
		if ( _o1_ instanceof ServerInfo ) _o_ = (ServerInfo)_o1_;
		else return false;
		if (this.id != _o_.id) return false;
		if (this.type != _o_.type) return false;
		if (!this.ip.equals(_o_.ip)) return false;
		if (this.port4server != _o_.port4server) return false;
		if (this.port4webSocketClient != _o_.port4webSocketClient) return false;
		if (this.port4SocketClient != _o_.port4SocketClient) return false;
		if (!this.ip4Client.equals(_o_.ip4Client)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.id;
		_h_ += _h_ * 31 + this.type;
		_h_ += _h_ * 31 + this.ip.hashCode();
		_h_ += _h_ * 31 + this.port4server;
		_h_ += _h_ * 31 + this.port4webSocketClient;
		_h_ += _h_ * 31 + this.port4SocketClient;
		_h_ += _h_ * 31 + this.ip4Client.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.id).append(",");
		_sb_.append(this.type).append(",");
		_sb_.append("T").append(this.ip.length()).append(",");
		_sb_.append(this.port4server).append(",");
		_sb_.append(this.port4webSocketClient).append(",");
		_sb_.append(this.port4SocketClient).append(",");
		_sb_.append("T").append(this.ip4Client.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
