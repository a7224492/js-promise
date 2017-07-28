package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class MainNotice extends limax.zdb.XBean {
    private String type; 
    private long id; 
    private String title; 
    private String content; 
    private long startTime; 
    private long endTime; 
    private boolean isCancel; 

    MainNotice(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        type = "";
        title = "";
        content = "";
	}

	public MainNotice() {
		this(null, null);
	}

	public MainNotice(MainNotice _o_) {
		this(_o_, null, null);
	}

	MainNotice(MainNotice _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.MainNotice", true);
        this.type = _o_.type;
        this.id = _o_.id;
        this.title = _o_.title;
        this.content = _o_.content;
        this.startTime = _o_.startTime;
        this.endTime = _o_.endTime;
        this.isCancel = _o_.isCancel;
	}

	public void copyFrom(MainNotice _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromMainNotice", true);
		this.verifyStandaloneOrLockHeld("copyToMainNotice", false);
        limax.zdb.Logs.logObject(this, "type");
        this.type = _o_.type;
        limax.zdb.Logs.logObject(this, "id");
        this.id = _o_.id;
        limax.zdb.Logs.logObject(this, "title");
        this.title = _o_.title;
        limax.zdb.Logs.logObject(this, "content");
        this.content = _o_.content;
        limax.zdb.Logs.logObject(this, "startTime");
        this.startTime = _o_.startTime;
        limax.zdb.Logs.logObject(this, "endTime");
        this.endTime = _o_.endTime;
        limax.zdb.Logs.logObject(this, "isCancel");
        this.isCancel = _o_.isCancel;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.type);
        _os_.marshal(this.id);
        _os_.marshal(this.title);
        _os_.marshal(this.content);
        _os_.marshal(this.startTime);
        _os_.marshal(this.endTime);
        _os_.marshal(this.isCancel);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.type = _os_.unmarshal_String();
		this.id = _os_.unmarshal_long();
		this.title = _os_.unmarshal_String();
		this.content = _os_.unmarshal_String();
		this.startTime = _os_.unmarshal_long();
		this.endTime = _os_.unmarshal_long();
		this.isCancel = _os_.unmarshal_boolean();
		return _os_;
	}

	public String getType() { 
		this.verifyStandaloneOrLockHeld("getType", true);
		return this.type;
	}

	public long getId() { 
		this.verifyStandaloneOrLockHeld("getId", true);
		return this.id;
	}

	public String getTitle() { 
		this.verifyStandaloneOrLockHeld("getTitle", true);
		return this.title;
	}

	public String getContent() { 
		this.verifyStandaloneOrLockHeld("getContent", true);
		return this.content;
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

	public void setType(String _v_) { 
		this.verifyStandaloneOrLockHeld("setType", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "type");
		this.type = _v_;
	}

	public void setId(long _v_) { 
		this.verifyStandaloneOrLockHeld("setId", false);
		limax.zdb.Logs.logObject(this, "id");
		this.id = _v_;
	}

	public void setTitle(String _v_) { 
		this.verifyStandaloneOrLockHeld("setTitle", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "title");
		this.title = _v_;
	}

	public void setContent(String _v_) { 
		this.verifyStandaloneOrLockHeld("setContent", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "content");
		this.content = _v_;
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

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		MainNotice _o_ = null;
		if ( _o1_ instanceof MainNotice ) _o_ = (MainNotice)_o1_;
		else return false;
		if (!this.type.equals(_o_.type)) return false;
		if (this.id != _o_.id) return false;
		if (!this.title.equals(_o_.title)) return false;
		if (!this.content.equals(_o_.content)) return false;
		if (this.startTime != _o_.startTime) return false;
		if (this.endTime != _o_.endTime) return false;
		if (this.isCancel != _o_.isCancel) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.type.hashCode();
		_h_ += _h_ * 31 + (int)(this.id ^ (this.id >>> 32));
		_h_ += _h_ * 31 + this.title.hashCode();
		_h_ += _h_ * 31 + this.content.hashCode();
		_h_ += _h_ * 31 + (int)(this.startTime ^ (this.startTime >>> 32));
		_h_ += _h_ * 31 + (int)(this.endTime ^ (this.endTime >>> 32));
		_h_ += _h_ * 31 + (this.isCancel ? 1231 : 1237);
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append("T").append(this.type.length()).append(",");
		_sb_.append(this.id).append(",");
		_sb_.append("T").append(this.title.length()).append(",");
		_sb_.append("T").append(this.content.length()).append(",");
		_sb_.append(this.startTime).append(",");
		_sb_.append(this.endTime).append(",");
		_sb_.append(this.isCancel).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
