package com.kodgames.authserver.auth.wx;

import java.util.Map;

public class WxUserInfo
{
	private String nickname;
	private int sex; // 1为男性，2为女性
	private String headImgUrl;
	private String province;
	private String city;
	private String country;
	private String unionid;

	public WxUserInfo(final String nickname, final int sex, final String headImgUrl)
		throws Exception
	{
		init(nickname, sex, headImgUrl);
	}
	
	public WxUserInfo(Map<String, Object> map)
	{
		init((String)map.get("nickname"), (int)map.get("sex"), (String)map.get("headimgurl"));
		this.province = (String)map.get("province");
		this.city = (String)map.get("city");
		this.country = (String)map.get("country");
		this.unionid = (String)map.get("unionid");
	}
	
	private void init(String nickname, int sex, String headImgUrl)
	{
		this.nickname = nickname;
		this.sex = sex;

		if (headImgUrl.endsWith("/0"))
		{
			this.headImgUrl = headImgUrl.replace("/0", "/96");
		}
		else
		{
			this.headImgUrl = "";
		}
	}

	public String getNickname()
	{
		return nickname;
	}

	public int getSex()
	{
		return sex;
	}

	public String getHeadImgUrl()
	{
		return headImgUrl;
	}
	
	public String getProvince()
	{
		return province;
	}
	
	public String getCity()
	{
		return city;
	}
	
	public String getCountry()
	{
		return country;
	}
	
	public String getUnionid()
	{
		return unionid;
	}
}
