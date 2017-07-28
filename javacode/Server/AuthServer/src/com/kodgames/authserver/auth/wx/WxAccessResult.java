package com.kodgames.authserver.auth.wx;

public class WxAccessResult
{
	private String openid;
	private String accessToken;
	private String refreshToken;
	private String unionid;

	public WxAccessResult(final String openid, final String accessToken, final String refreshToken, String unionid)
		throws Exception
	{
		this.openid = openid;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.unionid = unionid;
	}

	public String getOpenId()
	{
		return openid;
	}

	public String getAccessToken()
	{
		return accessToken;
	}

	public String getRefreshToken()
	{
		return refreshToken;
	}
	
	public String getUnionid()
	{
		return unionid;
	}
}
