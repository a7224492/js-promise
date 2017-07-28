package xbean;

import limax.codec.OctetsStream;
import limax.codec.MarshalException;

public final class Marquee extends limax.zdb.XBean {
    private long id; 
    private int type; 
    private int showType; 
    private String msg; 
    private int weeklyRepeat; 
    private long absoluteDate; 
    private java.util.ArrayList<Long> hourAndMinute; 
    private int rollTimes; 
    private int intervalTime; 
    private String color; 
    private boolean active; 
    private int operateBy; 

    Marquee(limax.zdb.XBean _xp_, String _vn_) {
        super(_xp_, _vn_);
        msg = "";
        hourAndMinute = new java.util.ArrayList<Long>();
        color = "";
	}

	public Marquee() {
		this(null, null);
	}

	public Marquee(Marquee _o_) {
		this(_o_, null, null);
	}

	Marquee(Marquee _o_, limax.zdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		_o_.verifyStandaloneOrLockHeld("_o_.Marquee", true);
        this.id = _o_.id;
        this.type = _o_.type;
        this.showType = _o_.showType;
        this.msg = _o_.msg;
        this.weeklyRepeat = _o_.weeklyRepeat;
        this.absoluteDate = _o_.absoluteDate;
        this.hourAndMinute = new java.util.ArrayList<Long>();
        this.hourAndMinute.addAll(_o_.hourAndMinute);
        this.rollTimes = _o_.rollTimes;
        this.intervalTime = _o_.intervalTime;
        this.color = _o_.color;
        this.active = _o_.active;
        this.operateBy = _o_.operateBy;
	}

	public void copyFrom(Marquee _o_) {
		_o_.verifyStandaloneOrLockHeld("copyFromMarquee", true);
		this.verifyStandaloneOrLockHeld("copyToMarquee", false);
        limax.zdb.Logs.logObject(this, "id");
        this.id = _o_.id;
        limax.zdb.Logs.logObject(this, "type");
        this.type = _o_.type;
        limax.zdb.Logs.logObject(this, "showType");
        this.showType = _o_.showType;
        limax.zdb.Logs.logObject(this, "msg");
        this.msg = _o_.msg;
        limax.zdb.Logs.logObject(this, "weeklyRepeat");
        this.weeklyRepeat = _o_.weeklyRepeat;
        limax.zdb.Logs.logObject(this, "absoluteDate");
        this.absoluteDate = _o_.absoluteDate;
        java.util.List<Long> this_hourAndMinute = limax.zdb.Logs.logList(this, "hourAndMinute", ()->{});
        this_hourAndMinute.clear();
        this_hourAndMinute.addAll(_o_.hourAndMinute);
        limax.zdb.Logs.logObject(this, "rollTimes");
        this.rollTimes = _o_.rollTimes;
        limax.zdb.Logs.logObject(this, "intervalTime");
        this.intervalTime = _o_.intervalTime;
        limax.zdb.Logs.logObject(this, "color");
        this.color = _o_.color;
        limax.zdb.Logs.logObject(this, "active");
        this.active = _o_.active;
        limax.zdb.Logs.logObject(this, "operateBy");
        this.operateBy = _o_.operateBy;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
        this.verifyStandaloneOrLockHeld("marshal", true);
        _os_.marshal(this.id);
        _os_.marshal(this.type);
        _os_.marshal(this.showType);
        _os_.marshal(this.msg);
        _os_.marshal(this.weeklyRepeat);
        _os_.marshal(this.absoluteDate);
        _os_.marshal_size(this.hourAndMinute.size());
        for (Long _v_ : this.hourAndMinute) {
        	_os_.marshal(_v_);
        }
        _os_.marshal(this.rollTimes);
        _os_.marshal(this.intervalTime);
        _os_.marshal(this.color);
        _os_.marshal(this.active);
        _os_.marshal(this.operateBy);
        return _os_;
    }

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		this.verifyStandaloneOrLockHeld("unmarshal", false);
		this.id = _os_.unmarshal_long();
		this.type = _os_.unmarshal_int();
		this.showType = _os_.unmarshal_int();
		this.msg = _os_.unmarshal_String();
		this.weeklyRepeat = _os_.unmarshal_int();
		this.absoluteDate = _os_.unmarshal_long();
		for(int _i_ = _os_.unmarshal_size(); _i_ > 0; --_i_) {
			long _v_ = _os_.unmarshal_long();
			this.hourAndMinute.add(_v_);
		}
		this.rollTimes = _os_.unmarshal_int();
		this.intervalTime = _os_.unmarshal_int();
		this.color = _os_.unmarshal_String();
		this.active = _os_.unmarshal_boolean();
		this.operateBy = _os_.unmarshal_int();
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

	public int getShowType() { 
		this.verifyStandaloneOrLockHeld("getShowType", true);
		return this.showType;
	}

	public String getMsg() { 
		this.verifyStandaloneOrLockHeld("getMsg", true);
		return this.msg;
	}

	public int getWeeklyRepeat() { 
		this.verifyStandaloneOrLockHeld("getWeeklyRepeat", true);
		return this.weeklyRepeat;
	}

	public long getAbsoluteDate() { 
		this.verifyStandaloneOrLockHeld("getAbsoluteDate", true);
		return this.absoluteDate;
	}

	public java.util.List<Long> getHourAndMinute() {  
		return limax.zdb.Transaction.isActive() ? limax.zdb.Logs.logList(this, "hourAndMinute", this.verifyStandaloneOrLockHeld("getHourAndMinute", true)) : this.hourAndMinute;
	}

	public int getRollTimes() { 
		this.verifyStandaloneOrLockHeld("getRollTimes", true);
		return this.rollTimes;
	}

	public int getIntervalTime() { 
		this.verifyStandaloneOrLockHeld("getIntervalTime", true);
		return this.intervalTime;
	}

	public String getColor() { 
		this.verifyStandaloneOrLockHeld("getColor", true);
		return this.color;
	}

	public boolean getActive() { 
		this.verifyStandaloneOrLockHeld("getActive", true);
		return this.active;
	}

	public int getOperateBy() { 
		this.verifyStandaloneOrLockHeld("getOperateBy", true);
		return this.operateBy;
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

	public void setShowType(int _v_) { 
		this.verifyStandaloneOrLockHeld("setShowType", false);
		limax.zdb.Logs.logObject(this, "showType");
		this.showType = _v_;
	}

	public void setMsg(String _v_) { 
		this.verifyStandaloneOrLockHeld("setMsg", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "msg");
		this.msg = _v_;
	}

	public void setWeeklyRepeat(int _v_) { 
		this.verifyStandaloneOrLockHeld("setWeeklyRepeat", false);
		limax.zdb.Logs.logObject(this, "weeklyRepeat");
		this.weeklyRepeat = _v_;
	}

	public void setAbsoluteDate(long _v_) { 
		this.verifyStandaloneOrLockHeld("setAbsoluteDate", false);
		limax.zdb.Logs.logObject(this, "absoluteDate");
		this.absoluteDate = _v_;
	}

	public void setRollTimes(int _v_) { 
		this.verifyStandaloneOrLockHeld("setRollTimes", false);
		limax.zdb.Logs.logObject(this, "rollTimes");
		this.rollTimes = _v_;
	}

	public void setIntervalTime(int _v_) { 
		this.verifyStandaloneOrLockHeld("setIntervalTime", false);
		limax.zdb.Logs.logObject(this, "intervalTime");
		this.intervalTime = _v_;
	}

	public void setColor(String _v_) { 
		this.verifyStandaloneOrLockHeld("setColor", false);
		java.util.Objects.requireNonNull(_v_);
		limax.zdb.Logs.logObject(this, "color");
		this.color = _v_;
	}

	public void setActive(boolean _v_) { 
		this.verifyStandaloneOrLockHeld("setActive", false);
		limax.zdb.Logs.logObject(this, "active");
		this.active = _v_;
	}

	public void setOperateBy(int _v_) { 
		this.verifyStandaloneOrLockHeld("setOperateBy", false);
		limax.zdb.Logs.logObject(this, "operateBy");
		this.operateBy = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		this.verifyStandaloneOrLockHeld("equals", true);
		Marquee _o_ = null;
		if ( _o1_ instanceof Marquee ) _o_ = (Marquee)_o1_;
		else return false;
		if (this.id != _o_.id) return false;
		if (this.type != _o_.type) return false;
		if (this.showType != _o_.showType) return false;
		if (!this.msg.equals(_o_.msg)) return false;
		if (this.weeklyRepeat != _o_.weeklyRepeat) return false;
		if (this.absoluteDate != _o_.absoluteDate) return false;
		if (!this.hourAndMinute.equals(_o_.hourAndMinute)) return false;
		if (this.rollTimes != _o_.rollTimes) return false;
		if (this.intervalTime != _o_.intervalTime) return false;
		if (!this.color.equals(_o_.color)) return false;
		if (this.active != _o_.active) return false;
		if (this.operateBy != _o_.operateBy) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += _h_ * 31 + (int)(this.id ^ (this.id >>> 32));
		_h_ += _h_ * 31 + this.type;
		_h_ += _h_ * 31 + this.showType;
		_h_ += _h_ * 31 + this.msg.hashCode();
		_h_ += _h_ * 31 + this.weeklyRepeat;
		_h_ += _h_ * 31 + (int)(this.absoluteDate ^ (this.absoluteDate >>> 32));
		_h_ += _h_ * 31 + this.hourAndMinute.hashCode();
		_h_ += _h_ * 31 + this.rollTimes;
		_h_ += _h_ * 31 + this.intervalTime;
		_h_ += _h_ * 31 + this.color.hashCode();
		_h_ += _h_ * 31 + (this.active ? 1231 : 1237);
		_h_ += _h_ * 31 + this.operateBy;
		return _h_;
	}

	@Override
	public String toString() {
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.id).append(",");
		_sb_.append(this.type).append(",");
		_sb_.append(this.showType).append(",");
		_sb_.append("T").append(this.msg.length()).append(",");
		_sb_.append(this.weeklyRepeat).append(",");
		_sb_.append(this.absoluteDate).append(",");
		_sb_.append(this.hourAndMinute).append(",");
		_sb_.append(this.rollTimes).append(",");
		_sb_.append(this.intervalTime).append(",");
		_sb_.append("T").append(this.color.length()).append(",");
		_sb_.append(this.active).append(",");
		_sb_.append(this.operateBy).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}
