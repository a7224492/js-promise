package com.kodgames.authserver.config;

public class Wxapp {

	int appcode;
	String wxappid;
    String wxsecret;

    public int getAppcode() {
        return appcode;
    }

    public void setAppcode(int appcode) {
        this.appcode = appcode;
    }

    
    public String getWxappid() {
        return wxappid;
    }

    public void setWxappid(String wxappid) {
        this.wxappid = wxappid;
    }

    public String getWxsecret() {
        return wxsecret;
    }

    public void setWxsecret(String wxsecret) {
        this.wxsecret = wxsecret;
    }

    @Override
    public String toString() {
        return "Wxapp{" +
                "wxappid='" + wxappid + '\'' +
                ", wxsecret='" + wxsecret + '\'' +
                '}';
    }
    
}
