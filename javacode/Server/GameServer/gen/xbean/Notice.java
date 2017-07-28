package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class Notice extends limax.zdb.XBean {
    private long id; 
    private String content; 
    private String imgUrl; 
    private long startTime; 
    private long endTime; 
    private boolean isCancel; 
    private String noticeName; 
    private int popTimes; 

    Notice(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        content = "";
        imgUrl = "";
        noticeName = "";
	}

	public Notice() {
		this(null, null);
	}

	public Notice(Notice _o_) {
		this(_o_, null, null);
	}

	Notice(Notice _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.Notice", true);
        this.id = _o_.id;
        this.content = _o_.content;
        this.imgUrl = _o_.imgUrl;
        this.startTime = _o_.startTime;
        this.endTime = _o_.endTime;
        this.isCancel = _o_.isCancel;
        this.noticeName = _o_.noticeName;
        this.popTimes = _o_.popTimes;
	}

	public void copyFrom(Notice _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromNotice", true);
		this.verifyStandaloneOrLockHeld("copyToNotice", false);
        limax.zdb.Logs.logObject(this, "id");
        this.id = _o_.id;
        limax.zdb.Logs.logObject(this, "content");
        this.content = _o_.content;
        limax.zdb.Logs.logObject(this, "imgUrl");
        this.imgUrl = _o_.imgUrl;
        limax.zdb.Logs.logObject(this, "startTime");
        this.startTime = _o_.startTime;
        limax.zdb.Logs.logObject(this, "endTime");
        this.endTime = _o_.endTime;
        limax.zdb.Logs.logObject(this, "isCancel");
        this.isCancel = _o_.isCancel;
        limax.zdb.Logs.logObject(this, "noticeName");
        this.noticeName = _o_.noticeName;
        limax.zdb.Logs.logObject(this, "popTimes");
        this.popTimes = _o_.popTimes;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.id);
        _os_.marshal(this.content);
        _os_.marshal(this.imgUrl);
        _os_.marshal(this.startTime);
        _os_.marshal(this.endTime);
        _os_.marshal(this.isCancel);
        _os_.marshal(this.noticeName);
        _os_.marshal(this.popTimes);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.id = _os_.unmarshal_long();
		this.content = _os_.unmarshal_String();
		this.imgUrl = _os_.unmarshal_String();
		this.startTime = _os_.unmarshal_long();
		this.endTime = _os_.unmarshal_long();
		this.isCancel = _os_.unmarshal_boolean();
		this.noticeName = _os_.unmarshal_String();
		this.popTimes = _os_.unmarshal_int();
		return _os_;
	}

	public long getId() { 
		this.verifyStandaloneOrLockHeld("getId", true);
		return this.id;
	}

	public String getContent() { 
		this.verifyStandaloneOrLockHeld("getContent", true);
		return this.content;
	}

	public String getImgUrl() { 
		this.verifyStandaloneOrLockHeld("getImgUrl", true);
		return this.imgUrl;
	}

	public long getStartTime() { 
		this.verifyStandaloneOrLockHeld("getStartTime", true);
		return this.startTime;
	}

	public long getEndTime() { 
		this.verifyStandaloneOrLockHeld("getEndTime", true);
		return this.endTime;
	}

	public boolean getIsCancel() { 
		this.verifyStandaloneOrLockHeld("getIsCancel", true);
		return this.isCancel;
	}

	public String getNoticeName() { 
		this.verifyStandaloneOrLockHeld("getNoticeName", true);
		return this.noticeName;
	}

	public int getPopTimes() { 
		this.verifyStandaloneOrLockHeld("getPopTimes", true);
		return this.popTimes;
	}

	public void setId(long _v_) { 
		this.verifyStandaloneOrLockHeld("setId", false);
		limax.zdb.Logs.logObject(this, "id");
		this.id = _v_;
	}

	public void setContent(String _v_) { 
		this.verifyStandaloneOrLockHeld("setContent", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "content");
		this.content = _v_;
	}

	public void setImgUrl(String _v_) { 
		this.verifyStandaloneOrLockHeld("setImgUrl", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "imgUrl");
		this.imgUrl = _v_;
	}

	public void setStartTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setStartTime", false);
		limax.zdb.Logs.logObject(this, "startTime");
		this.startTime = _v_;
	}

	public void setEndTime(long _v_) { 
		this.verifyStandaloneOrLockHeld("setEndTime", false);
		limax.zdb.Logs.logObject(this, "endTime");
		this.endTime = _v_;
	}

	public void setIsCancel(boolean _v_) { 
		this.verifyStandaloneOrLockHeld("setIsCancel", false);
		limax.zdb.Logs.logObject(this, "isCancel");
		this.isCancel = _v_;
	}

	public void setNoticeName(String _v_) { 
		this.verifyStandaloneOrLockHeld("setNoticeName", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "noticeName");
		this.noticeName = _v_;
	}

	public void setPopTimes(int _v_) { 
		this.verifyStandaloneOrLockHeld("setPopTimes", false);
		limax.zdb.Logs.logObject(this, "popTimes");
		this.popTimes = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		Notice _o_ = null;
		if ( _o1_ instanceof Notice ) _o_ = (Notice)_o1_;
		else return false;
		if (this.id != _o_.id) return false;
		if (!this.content.equals(_o_.content)) return false;
		if (!this.imgUrl.equals(_o_.imgUrl)) return false;
		if (this.startTime != _o_.startTime) return false;
		if (this.endTime != _o_.endTime) return false;
		if (this.isCancel != _o_.isCancel) return false;
		if (!this.noticeName.equals(_o_.noticeName)) return false;
		if (this.popTimes != _o_.popTimes) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + (int)(this.id ^ (this.id >>> 32));
		_h_ += _h_ * 31 + this.content.hashCode();
		_h_ += _h_ * 31 + this.imgUrl.hashCode();
		_h_ += _h_ * 31 + (int)(this.startTime ^ (this.startTime >>> 32));
		_h_ += _h_ * 31 + (int)(this.endTime ^ (this.endTime >>> 32));
		_h_ += _h_ * 31 + (this.isCancel ? 1231 : 1237);
		_h_ += _h_ * 31 + this.noticeName.hashCode();
		_h_ += _h_ * 31 + this.popTimes;
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.id).append(",");
		_sb_.append("T").append(this.content.length()).append(",");
		_sb_.append("T").append(this.imgUrl.length()).append(",");
		_sb_.append(this.startTime).append(",");
		_sb_.append(this.endTime).append(",");
		_sb_.append(this.isCancel).append(",");
		_sb_.append("T").append(this.noticeName.length()).append(",");
		_sb_.append(this.popTimes).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
