package com.kodgames.manageserver.service.exchangecode.util;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DesDecryptor
{
	private static final Logger logger = LoggerFactory.getLogger(DesDecryptor.class);
	private static ByteMapping digitMapping = new ByteMapping();

	public String createActiveCode(String plainText)
	{
		String sign = plainText.substring(0, 4);
		byte[] byteCode = null;
		try
		{
			byteCode = DESUtil.encrypt(plainText, sign);
		}
		catch (Exception e)
		{
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		if (byteCode.length != 8)
		{
			return "-1";
		}
		else
		{
			String activeCode = sign + digitMapping.mapping(byteCode);
			return activeCode;
		}
	}

	//	public List<String> createMassActiveCode(String sign, int rangeFrom, int rangeTo)
	//	{
	//		List<String> activeCodes = new ArrayList<String>();
	//		for (int hitNumber = rangeFrom; hitNumber < rangeTo; ++hitNumber)
	//		{
	//			String activeCode = createActiveCode(sign, hitNumber);
	//			activeCodes.add(activeCode);
	//		}
	//		return activeCodes;
	//	}

	public String deCreateExchangeCode(String encryptString, String key)
	{
		try
		{
			return DESUtil.decrypt(encryptString, key);
		}
		catch (Exception e)
		{
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return "";
	}

	//	public static void main(String... args)
	//	{
	//		DesDecryptor decryptor = new DesDecryptor();
	//		String plainText1 = decryptor.deCreateExchangeCode("V472C6ULLYMJQ", "CA30");
	//		String plainText2 = decryptor.deCreateExchangeCode("V472C6ULLYMJP", "CA30");
	//
	//		System.out.println(plainText1);
	//		System.out.println(plainText2);
	//
	//		String ssss = _62_10_Transformer.convertBase62ToDecimal(plainText1.substring(4));
	//
	//		System.out.println(ssss);
	//
	//		String activityCode = decryptor.createActiveCode(plainText1);
	//		System.out.println(activityCode);
	//	}
}
