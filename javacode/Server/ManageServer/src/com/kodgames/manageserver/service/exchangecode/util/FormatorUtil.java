package com.kodgames.manageserver.service.exchangecode.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatorUtil
{

	private static final Pattern valid8aplhaDigitPattern = Pattern.compile("[0-9A-Za-z]{8}");

	private static final Pattern digit8Pattern = Pattern.compile("[0-9]{8}");

	private static final Pattern digitAndLetter17Pattern =
		Pattern.compile("[A-Z0-9]{4}[ABCDEFGHJKLMNPQRSTUVWXYZ23456789]{13}");

	public static boolean isLegal8alphaDigit(String context)
	{
		Matcher matcher = valid8aplhaDigitPattern.matcher(context);
		return matcher.matches();
	}

	public static boolean isLegal8Digit(String context)
	{
		Matcher matcher = digit8Pattern.matcher(context);
		return matcher.matches();
	}

	public static boolean isLegal17DigitAndLetter(String context)
	{
		Matcher matcher = digitAndLetter17Pattern.matcher(context);
		return matcher.matches();
	}
}
