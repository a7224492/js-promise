package com.kodgames.authserver.auth.wx;

public class WxParam
{
	private String appid = null;
	private String secret = null;

	public WxParam()
	{
	}

	public WxParam(String appid, String secret)
	{
		this.appid = appid;
		this.secret = secret;
	}

	public void setAppid(String appid)
	{
		this.appid = appid;
	}

	public void setSecret(String secret)
	{
		this.secret = secret;
	}

	public String getAppid()
	{
		return this.appid;
	}

	public String getSecret()
	{
		return this.secret;
	}
}
