package com.kodgames.corgi.core.util;

/**
 */
public class HexUtil
{
	public static int hexToDec(String sn)
	{
		if (null == sn || 0 == sn.trim().length())
			throw new IllegalArgumentException("接收的参数 “" + sn + "” 不是十六进制表示形式！");

		final String P = "0x";
		if (sn.toLowerCase().startsWith(P))
		{
			char[] cs = sn.substring(2).toUpperCase().toCharArray();
			char c;
			int i = 0;
			for (int j = 0; j < cs.length; j++)
			{
				c = cs[j];
				i += toDec(c, (cs.length - j - 1));
			}

			return i;
		}

		return 0;
	}

	private static int toDec(char c, int i)
	{
		final double N = 16.0;
		int n = getNum(c);
		int r = (int)(n * Math.pow(N, (double)i));
		return r;
	}

	private static int getNum(char c)
	{
		// 字符F转换成为数字为70
		if ((int)c > 70)
			return 0;

		int i = 0;
		switch (c)
		{
			case 'A':
			case 'a':
				i = 10;
				break;
			case 'B':
			case 'b':
				i = 11;
				break;
			case 'C':
			case 'c':
				i = 12;
				break;
			case 'D':
			case 'd':
				i = 13;
				break;
			case 'E':
			case 'e':
				i = 14;
				break;
			case 'F':
			case 'f':
				i = 15;
				break;
		}

		if (0 == i)
		{
			// 字符0转换成为数字为48
			return (int)c - 48;
		}
		else
			return i;
	}
}
