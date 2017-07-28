package com.kodgames.authserver.auth.wx;

import java.util.HashMap;

import com.kodgames.authserver.config.AuthWxConfig;
import com.kodgames.authserver.service.account.AuthCallback;
import com.kodgames.core.net.http.HttpClient;
import com.kodgames.core.net.http.HttpUri;

public class WxHelper
{
	private static HttpClient httpClient = null;

	private static HttpClient getClient()
	{
		try
		{
			if (null == httpClient)
				httpClient = new HttpClient();
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			httpClient = null;
		}

		return httpClient;
	}

	
	

	// Request :
	// https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
	// Response :
	// {
	// "access_token":"ACCESS_TOKEN",
	// "expires_in":7200,
	// "refresh_token":"REFRESH_TOKEN",
	// "openid":"OPENID",
	// "scope":"SCOPE"
	// }
	public static boolean requestAccessToken(final String code, AuthCallback callback)
	{
		HashMap<String, Object> args = new HashMap<>();
		
		args.put("appid", AuthWxConfig.getInstance().getAppid(callback.getAppCode()));
		args.put("secret", AuthWxConfig.getInstance().getSecret(callback.getAppCode()));
		args.put("code", code);
		args.put("grant_type", "authorization_code");
		HttpUri uri = new HttpUri("https://api.weixin.qq.com", new String[] {"sns", "oauth2", "access_token"}, args);

		HttpClient client = getClient();
		if (null == client)
			return false;

		client.asyncGet(uri, new WxAuthCodeCallback(callback));
		return true;
	}

	// Request :
	// https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN
	// Response :
	// {
	// "access_token":"ACCESS_TOKEN",
	// "expires_in":7200,
	// "refresh_token":"REFRESH_TOKEN",
	// "openid":"OPENID",
	// "scope":"SCOPE"
	// }
//	@SuppressWarnings("unchecked")
//	public static boolean refreshAccessToken(final String openId, final String refreshToken, AuthCallback callback)
//	{
//		HashMap<String, Object> args = new HashMap<>();
//		args.put("appid", WX_APPID);
//		args.put("grant_type", "refresh_token");
//		args.put("refresh_token", refreshToken);
//		HttpUri uri = new HttpUri("https://api.weixin.qq.com", new String[] {"sns", "oauth2", "refresh_token"}, args);
//
//		HttpClient client = getClient();
//		if (null == client)
//			return false;
//
//		client.asyncGet(uri, new WxRefreshCallback(callback));
//		return true;
//	}

	// Request :
	// https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
	// Response :
	// {
	// "openid":"OPENID",
	// "nickname":"NICKNAME",
	// "sex":1,
	// "province":"PROVINCE",
	// "city":"CITY",
	// "country":"COUNTRY",
	// "headimgurl":
	// "http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0",
	// "privilege":[
	// "PRIVILEGE1",
	// "PRIVILEGE2"
	// ],
	// "unionid": " o6_bmasdasdsad6_2sgVt7hMZOPfL"
	// }
	// or
	// {
	// "errcode":40003,"errmsg":"invalid openid"
	// }
	public static boolean requestUserInfo(final String openid, final String accessToken, AuthCallback callback, final WxAccessResult wxAuthResult)
	{
		HashMap<String, Object> args = new HashMap<>();
		args.put("access_token", accessToken);
		args.put("openid", openid);
		HttpUri uri = new HttpUri("https://api.weixin.qq.com", new String[] {"sns", "userinfo"}, args);
		
		HttpClient client = getClient();
		if (null == client)
			return false;

		client.asyncGet(uri, new WxUesrInfoCallback(callback, wxAuthResult));
		return true;
	}
}
