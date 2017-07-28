package com.kodgames.game.util;

import java.security.MessageDigest;

/**
 * MD5工具类
 */
public class Md5Util
{
	private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

	private static String toHexString(byte[] b)
	{
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++)
		{
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	// 返回的是小写的md5串
	public static String createMd5(String SourceString)
		throws Exception
	{
		MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
		digest.update(SourceString.getBytes());
		byte messageDigest[] = digest.digest();
		return toHexString(messageDigest).toLowerCase();
	}
}
