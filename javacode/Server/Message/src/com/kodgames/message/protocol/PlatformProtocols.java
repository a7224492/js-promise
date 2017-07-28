/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kodgames.message.protocol;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.kodgames.message.generaor.ProtocolConfigAnnotation;

/**
 * 
 * @author mrui
 */
public class PlatformProtocols
{
	// ServerType 0xxxxx0000
	final public static int SERVER_TYPE_MANAGE = 0x00010000;
	final public static int SERVER_TYPE_AUTH = 0x00020000;
	final public static int SERVER_TYPE_GAME = 0x00030000;
	final public static int SERVER_TYPE_BATTLE = 0x00040000;

	// Protocol Type 0xxxxx0000
	final public static int PROTOCOL_TYPE_MANAGE = 0x00010000;
	final public static int PROTOCOL_TYPE_AUTH = 0x00020000;
	final public static int PROTOCOL_TYPE_GAME = 0x00030000;
	final public static int PROTOCOL_TYPE_BATTLE = 0x00040000;

	public static Map<Integer, ProtocolConfigAnnotation> messageInfoMap =
		new HashMap<Integer, ProtocolConfigAnnotation>();

	public static int type(Integer id)
	{
		return 0xffff0000 & id;
	}

	public static int idWithoutType(Integer id)
	{
		return 0x0000ffff & id;
	}

	static
	{
		for (Field f : PlatformProtocolsConfig.class.getFields())
		{
			ProtocolConfigAnnotation annotation = f.getAnnotation(ProtocolConfigAnnotation.class);
			if (annotation != null)
				try
				{
					messageInfoMap.put(f.getInt(null), annotation);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
		}
	}

	public static ProtocolConfigAnnotation getProtocolConfigInfo(int protocolId)
	{
		return messageInfoMap.get(protocolId);
	}
}
