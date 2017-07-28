package com.kodgames.corgi.core.net.common;

import java.math.BigInteger;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 产生新key
 * 发送更新key协议之后，启用新key加密
 * 收到更新key协议应答后，启用新key解密 
 *
 */
public class EncryptCoder
{
	private Logger logger = LoggerFactory.getLogger(EncryptCoder.class);
	
	private boolean encryptFlag = true;
	
	private byte oldKey = 0;
	public byte newKey = 0;
	
	private boolean encryptUseNew = false;
	private boolean decryptUseNew = false;
	private DHHelper dhHelper = new DHHelper();
	public void encryptUseNewKey()
	{
		this.encryptUseNew = true;
	}
	
	public void decryptUseNewKey()
	{
		this.decryptUseNew = true;
	}
	
	/*
	 * 根据DH算法计算出的K算出实际需要的K
	 */
	private int getRealKey(BigInteger key)
	{
		String keyStr = key.toString();
		int tempK = Integer.parseInt(keyStr.substring(keyStr.length() - 9, keyStr.length()));
//		logger.error("---real key without mod={}", tempK);
		return tempK % 255 + 1;
	}
	/**
	 * 每调用一次就会重新生成计算K的Q与发往客户端的Y
	 */
	public ArrayList<String> genYandQ()
	{
		if (!encryptFlag)
		{
			//未开启加密
			return null;
		}
		
		if (newKey != 0)
		{
			//保存当前key为oldKey
			oldKey = newKey;			
		}
		else
		{
			//第一次，没有旧key，所以不加密，也不解密
		}
		
		BigInteger serverY = dhHelper.generateDHResponse();
		int qIndex = dhHelper.getqIndex();
		
		//产生新key之后，加解密都要使用旧key
		this.encryptUseNew = false;
		this.decryptUseNew = false;
		
		ArrayList<String> yAndQ = new ArrayList<>();
		yAndQ.add(0,serverY.toString());
		yAndQ.add(1,qIndex+"");
		return yAndQ;
	}
	
	public boolean computeKey(String clientYStr)
	{
		BigInteger clientY;
		try
		{
			clientY = new BigInteger(clientYStr);
		}
		catch(NumberFormatException e)
		{
			return false;
		}
		
		dhHelper.setClientY(clientY);

		newKey = (byte)getRealKey(dhHelper.computeDHKey());
		this.encryptUseNew = false;
		this.decryptUseNew = false;
		return true;
	}
	
	public byte getEncryptKey()
	{
		byte key = 0;
		if (encryptUseNew)
		{
			key = newKey;
		}
		else
		{
			key = oldKey;
		}
		
		return key;
	}
	
	public boolean hasEncryptKey()
	{
		return getEncryptKey() != 0;
	}
	
	public void encryptData(byte[] data, int offset, int count)
	{
		byte key = getEncryptKey();
		if (key == 0)
		{
			logger.warn("encryptDataImpl encryptUseNew {} but key is 0", encryptUseNew);
			return;
		}
		
		for (int i = offset; i < offset + count; i++)
		{
			data[i-offset] = (byte) (data[i] ^ key);
		}
	}
	
	public byte getDecryptKey()
	{
		byte key = 0;
		if (decryptUseNew)
		{
			key = newKey;
		}
		else
		{
			key = oldKey;
		}
		
		return key;
	}
	
	public boolean hasDecryptKey()
	{
		return getDecryptKey() != 0;
	}
	
	public void decryptData(byte[] data, int offset, int count)
	{
		byte key = getDecryptKey();
		
		if (key == 0)
		{
			logger.warn("decryptDataImpl decryptUseNew {} but key is 0", decryptUseNew);
			return;
		}
		
		if (data == null || count == 0)
		{
			logger.debug("data is {} count is {} then return", data, count);
			return;
		}
		
		for (int i = offset; i < offset + count; i++)
		{
			data[i-offset] = (byte) (data[i] ^ key);
		}
	}
}
