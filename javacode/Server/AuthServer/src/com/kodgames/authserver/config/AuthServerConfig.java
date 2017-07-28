package com.kodgames.authserver.config;

public class AuthServerConfig
{
	public static final String ERROR_TAG = "error";
	public static final String RESULT_TAG = "result";

	public static final String PLATFORM_TEST = "test";
	public static final String PLATFORM_IOS = "ios";
	public static final String PLATFORM_ANDROID = "android";
	public static final String PLATFORM_WEB = "web";

	public static final String CHANNEL_WX = "wx";
	public static final String CHANNEL_TEST = "test";

	public static final int TABLE_ACCOUNTID_SEED_KEY = 0;

	public static final int TABLE_CLIENT_VERSION_KEY = 0;

	// 30天
	public static final long WX_REFRESHTOKEN_TIMEOUT = 1000 * 60 * 60 * 24 * 30L;

	// AccountID的初始值
	public static final int ACCOUNT_ID_INIT_SEED = 50000;
	
	// 线上版本
	public static final int APP_CODE_DEFAULT = 0;
	// 企业证书版本
	public static final int APP_CODE_QIYE = 1001;
	// 苹果充值版本
	public static final int APP_CODE_PURCHASE = 1002;
	
	public static final int APP_CODE_3 = 1003;
	public static final int APP_CODE_4 = 1004;
	public static final int APP_CODE_5 = 1005;
	public static final int APP_CODE_6 = 1006;
	public static final int APP_CODE_7 = 1007;
	public static final int APP_CODE_8 = 1008;
	public static final int APP_CODE_9 = 1009;
	public static final int APP_CODE_10 = 1010;
}
