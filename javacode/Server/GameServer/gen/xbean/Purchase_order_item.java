package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class Purchase_order_item extends limax.zdb.XBean {
    private String orderId; 
    private String channelOrderId; 
    private int playerId; 
    private String channelId; 
    private String channelUid; 
    private int areaId; 
    private String deviceType; 
    private int rmb; 
    private String itemId; 
    private int status; 
    private String sign; 
    private long createTime; 

    Purchase_order_item(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        orderId = "";
        channelOrderId = "";
        channelId = "";
        channelUid = "";
        deviceType = "";
        itemId = "";
        sign = "";
	}

	public Purchase_order_item() {
		this(null, null);
	}

	public Purchase_order_item(Purchase_order_item _o_) {
		this(_o_, null, null);
	}

	Purchase_order_item(Purchase_order_item _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.Purchase_order_item", true);
        this.orderId = _o_.orderId;
        this.channelOrderId = _o_.channelOrderId;
        this.playerId = _o_.playerId;
        this.channelId = _o_.channelId;
        this.channelUid = _o_.channelUid;
        this.areaId = _o_.areaId;
        this.deviceType = _o_.deviceType;
        this.rmb = _o_.rmb;
        this.itemId = _o_.itemId;
        this.status = _o_.status;
        this.sign = _o_.sign;
        this.createTime = _o_.createTime;
	}

	public void copyFrom(Purchase_order_item _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromPurchase_order_item", true);
		this.verifyStandaloneOrLockHeld("copyToPurchase_order_item", false);
        limax.zdb.Logs.logObject(this, "orderId");
        this.orderId = _o_.orderId;
        limax.zdb.Logs.logObject(this, "channelOrderId");
        this.channelOrderId = _o_.channelOrderId;
        limax.zdb.Logs.logObject(this, "playerId");
        this.playerId = _o_.playerId;
        limax.zdb.Logs.logObject(this, "channelId");
        this.channelId = _o_.channelId;
        limax.zdb.Logs.logObject(this, "channelUid");
        this.channelUid = _o_.channelUid;
        limax.zdb.Logs.logObject(this, "areaId");
        this.areaId = _o_.areaId;
        limax.zdb.Logs.logObject(this, "deviceType");
        this.deviceType = _o_.deviceType;
        limax.zdb.Logs.logObject(this, "rmb");
        this.rmb = _o_.rmb;
        limax.zdb.Logs.logObject(this, "itemId");
        this.itemId = _o_.itemId;
        limax.zdb.Logs.logObject(this, "status");
        this.status = _o_.status;
        limax.zdb.Logs.logObject(this, "sign");
        this.sign = _o_.sign;
        limax.zdb.Logs.logObject(this, "createTime");
        this.createTime = _o_.createTime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.orderId);
        _os_.marshal(this.channelOrderId);
        _os_.marshal(this.playerId);
        _os_.marshal(this.channelId);
        _os_.marshal(this.channelUid);
        _os_.marshal(this.areaId);
        _os_.marshal(this.deviceType);
        _os_.marshal(this.rmb);
        _os_.marshal(this.itemId);
        _os_.marshal(this.status);
        _os_.marshal(this.sign);
        _os_.marshal(this.createTime);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.orderId = _os_.unmarshal_String();
		this.channelOrderId = _os_.unmarshal_String();
		this.playerId = _os_.unmarshal_int();
		this.channelId = _os_.unmarshal_String();
		this.channelUid = _os_.unmarshal_String();
		this.areaId = _os_.unmarshal_int();
		this.deviceType = _os_.unmarshal_String();
		this.rmb = _os_.unmarshal_int();
		this.itemId = _os_.unmarshal_String();
		this.status = _os_.unmarshal_int();
		this.sign = _os_.unmarshal_String();
		this.createTime = _os_.unmarshal_long();
		return _os_;
	}

	public String getOrderId() { 
		this.verifyStandaloneOrLockHeld("getOrderId", true);
		return this.orderId;
	}

	public String getChannelOrderId() { 
		this.verifyStandaloneOrLockHeld("getChannelOrderId", true);
		return this.channelOrderId;
	}

	public int getPlayerId() { 
		this.verifyStandaloneOrLockHeld("getPlayerId", true);
		return this.playerId;
	}

	public String getChannelId() { 
		this.verifyStandaloneOrLockHeld("getChannelId", true);
		return this.channelId;
	}

	public String getChannelUid() { 
		this.verifyStandaloneOrLockHeld("getChannelUid", true);
		return this.channelUid;
	}

	public int getAreaId() { 
		this.verifyStandaloneOrLockHeld("getAreaId", true);
		return this.areaId;
	}

	public String getDeviceType() { 
		this.verifyStandaloneOrLockHeld("getDeviceType", true);
		return this.deviceType;
	}

	public int getRmb() { 
		this.verifyStandaloneOrLockHeld("getRmb", true);
		return this.rmb;
	}

	public String getItemId() { 
		this.verifyStandaloneOrLockHeld("getItemId", true);
		return this.itemId;
	}

	public int getStatus() { 
		this.verifyStandaloneOrLockHeld("getStatus", true);
		return this.status;
	}

	public String getSign() { 
		this.verifyStandaloneOrLockHeld("getSign", true);
		return this.sign;
	}

	public long getCreateTime() { 
		this.verifyStandaloneOrLockHeld("getCreateTime", true);
		return this.createTime;
	}

	public void setOrderId(String _v_) { 
		this.verifyStandaloneOrLockHeld("setOrderId", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "orderId");
		this.orderId = _v_;
	}

	public void setChannelOrderId(String _v_) { 
		this.verifyStandaloneOrLockHeld("setChannelOrderId", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "channelOrderId");
		this.channelOrderId = _v_;
	}

	public void setPlayerId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setPlayerId", false);
		limax.zdb.Logs.logObject(this, "playerId");
		this.playerId = _v_;
	}

	public void setChannelId(String _v_) { 
		this.verifyStandaloneOrLockHeld("setChannelId", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "channelId");
		this.channelId = _v_;
	}

	public void setChannelUid(String _v_) { 
		this.verifyStandaloneOrLockHeld("setChannelUid", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "channelUid");
		this.channelUid = _v_;
	}

	public void setAreaId(int _v_) { 
		this.verifyStandaloneOrLockHeld("setAreaId", false);
		limax.zdb.Logs.logObject(this, "areaId");
		this.areaId = _v_;
	}

	public void setDeviceType(String _v_) { 
		this.verifyStandaloneOrLockHeld("setDeviceType", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "deviceType");
		this.deviceType = _v_;
	}

	public void setRmb(int _v_) { 
		this.verifyStandaloneOrLockHeld("setRmb", false);
		limax.zdb.Logs.logObject(this, "rmb");
		this.rmb = _v_;
	}

	public void setItemId(String _v_) { 
		this.verifyStandaloneOrLockHeld("setItemId", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "itemId");
		this.itemId = _v_;
	}

	public void setStatus(int _v_) { 
		this.verifyStandaloneOrLockHeld("setStatus", false);
		limax.zdb.Logs.logObject(this, "status");
		this.status = _v_;
	}

	public void setSign(String _v_) { 
		this.verifyStandaloneOrLockHeld("setSign", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "sign");
		this.sign = _v_;
	}

	public void setCreateTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setCreateTime", false);
		limax.zdb.Logs.logObject(this, "createTime");
		this.createTime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		Purchase_order_item _o_ = null;
		if ( _o1_ instanceof Purchase_order_item ) _o_ = (Purchase_order_item)_o1_;
		else return false;
		if (!this.orderId.equals(_o_.orderId)) return false;
		if (!this.channelOrderId.equals(_o_.channelOrderId)) return false;
		if (this.playerId != _o_.playerId) return false;
		if (!this.channelId.equals(_o_.channelId)) return false;
		if (!this.channelUid.equals(_o_.channelUid)) return false;
		if (this.areaId != _o_.areaId) return false;
		if (!this.deviceType.equals(_o_.deviceType)) return false;
		if (this.rmb != _o_.rmb) return false;
		if (!this.itemId.equals(_o_.itemId)) return false;
		if (this.status != _o_.status) return false;
		if (!this.sign.equals(_o_.sign)) return false;
		if (this.createTime != _o_.createTime) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.orderId.hashCode();
		_h_ += _h_ * 31 + this.channelOrderId.hashCode();
		_h_ += _h_ * 31 + this.playerId;
		_h_ += _h_ * 31 + this.channelId.hashCode();
		_h_ += _h_ * 31 + this.channelUid.hashCode();
		_h_ += _h_ * 31 + this.areaId;
		_h_ += _h_ * 31 + this.deviceType.hashCode();
		_h_ += _h_ * 31 + this.rmb;
		_h_ += _h_ * 31 + this.itemId.hashCode();
		_h_ += _h_ * 31 + this.status;
		_h_ += _h_ * 31 + this.sign.hashCode();
		_h_ += _h_ * 31 + (int)(this.createTime ^ (this.createTime >>> 32));
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append("T").append(this.orderId.length()).append(",");
		_sb_.append("T").append(this.channelOrderId.length()).append(",");
		_sb_.append(this.playerId).append(",");
		_sb_.append("T").append(this.channelId.length()).append(",");
		_sb_.append("T").append(this.channelUid.length()).append(",");
		_sb_.append(this.areaId).append(",");
		_sb_.append("T").append(this.deviceType.length()).append(",");
		_sb_.append(this.rmb).append(",");
		_sb_.append("T").append(this.itemId.length()).append(",");
		_sb_.append(this.status).append(",");
		_sb_.append("T").append(this.sign.length()).append(",");
		_sb_.append(this.createTime).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
