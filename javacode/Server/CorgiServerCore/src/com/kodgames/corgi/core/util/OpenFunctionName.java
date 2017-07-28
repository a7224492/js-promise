package com.kodgames.corgi.core.util;

/**
 * 功能开关名称
 */
public class OpenFunctionName
{
	// 激活码
	public static final String ActiveCode = "ActiveCode";
	// 兑换码
	public static final String ExchangeCode = "ExchangeCode";
	
	public static final String[] functionList = new String[]{ActiveCode, ExchangeCode};

	public static String[] getFunctionlist()
	{
		return functionList;
	}
}
