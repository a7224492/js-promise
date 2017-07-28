package com.kodgames.battleserver.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Macro
{
	final static Logger logger = LoggerFactory.getLogger(Macro.class);

	/**
	 * 不应该是true, 如果是true就报错
	 * 
	 * @param tf
	 * @return 返回tf
	 */
	public static boolean AssetTrue(boolean tf)
	{
		LogAssert(!tf);
		return tf;
	}

	public static boolean AssetTrue(boolean tf, String message)
	{
		LogAssert(!tf, message);
		return tf;
	}

	/**
	 * 不应该是false，如果是false报错
	 * 
	 * @param tf
	 * @return 返回tf
	 */
	public static boolean AssetFalse(boolean tf)
	{
		LogAssert(tf);
		return tf;
	}

	public static boolean AssetFalse(boolean tf, String message)
	{
		LogAssert(tf, message);
		return tf;
	}

	public static void LogAssert(boolean tf)
	{
		if (tf)
			return;

		StringBuilder sb = new StringBuilder();
		for (StackTraceElement stackTrace : Thread.currentThread().getStackTrace())
		{
			sb.append(stackTrace);
			sb.append("\n");
		}

		logger.error("Asset failed!\n{}", sb);
	}

	public static void LogAssert(boolean tf, String message)
	{
		if (tf)
			return;

		StringBuilder sb = new StringBuilder();
		for (StackTraceElement stackTrace : Thread.currentThread().getStackTrace())
		{
			sb.append(stackTrace);
			sb.append("\n");
		}

		logger.error("Asset failed!, message:{}\n{}", message, sb);
	}
}