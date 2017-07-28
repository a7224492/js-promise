package com.kodgames.manageserver.service.exchangecode.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class DESUtil
{
	private static ByteMapping byteMapping = new ByteMapping();

	public static String decrypt(String decryptString, String sign)
		throws Exception
	{
		String decrytKey = "sema" + sign;
		SecretKeySpec key = new SecretKeySpec(decrytKey.getBytes(), "DES");

		Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, key);
		return new String(cipher.doFinal(byteMapping.deMapping(decryptString)));
	}

	public static byte[] encrypt(String encryptString, String encryptKey)
		throws Exception
	{
		encryptKey = "sema" + encryptKey;
		DESKeySpec dks = new DESKeySpec(encryptKey.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(encryptString.getBytes());
	}
}