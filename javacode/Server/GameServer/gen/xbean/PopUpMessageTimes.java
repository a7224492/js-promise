package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class PopUpMessageTimes extends limax.zdb.XBean {
    private String start; 
    private String end; 

    PopUpMessageTimes(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        start = "";
        end = "";
	}

	public PopUpMessageTimes() {
		this(null, null);
	}

	public PopUpMessageTimes(PopUpMessageTimes _o_) {
		this(_o_, null, null);
	}

	PopUpMessageTimes(PopUpMessageTimes _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.PopUpMessageTimes", true);
        this.start = _o_.start;
        this.end = _o_.end;
	}

	public void copyFrom(PopUpMessageTimes _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromPopUpMessageTimes", true);
		this.verifyStandaloneOrLockHeld("copyToPopUpMessageTimes", false);
        limax.zdb.Logs.logObject(this, "start");
        this.start = _o_.start;
        limax.zdb.Logs.logObject(this, "end");
        this.end = _o_.end;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.start);
        _os_.marshal(this.end);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.start = _os_.unmarshal_String();
		this.end = _os_.unmarshal_String();
		return _os_;
	}

	public String getStart() { 
		this.verifyStandaloneOrLockHeld("getStart", true);
		return this.start;
	}

	public String getEnd() { 
		this.verifyStandaloneOrLockHeld("getEnd", true);
		return this.end;
	}

	public void setStart(String _v_) { 
		this.verifyStandaloneOrLockHeld("setStart", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "start");
		this.start = _v_;
	}

	public void setEnd(String _v_) { 
		this.verifyStandaloneOrLockHeld("setEnd", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "end");
		this.end = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		PopUpMessageTimes _o_ = null;
		if ( _o1_ instanceof PopUpMessageTimes ) _o_ = (PopUpMessageTimes)_o1_;
		else return false;
		if (!this.start.equals(_o_.start)) return false;
		if (!this.end.equals(_o_.end)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + this.start.hashCode();
		_h_ += _h_ * 31 + this.end.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append("T").append(this.start.length()).append(",");
		_sb_.append("T").append(this.end.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
