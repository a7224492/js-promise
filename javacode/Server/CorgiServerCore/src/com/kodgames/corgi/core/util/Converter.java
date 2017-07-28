package com.kodgames.corgi.core.util;

import java.util.ArrayList;
import java.util.List;

import com.google.protobuf.ByteString;

public class Converter
{
	public static ByteString byteListToByteString(final List<Byte> byteList)
	{
		if (null == byteList || byteList.isEmpty())
		{
			return ByteString.EMPTY;
		}

		byte[] bytes = new byte[byteList.size()];
		for (int index = 0; index < bytes.length; ++index)
		{
			bytes[index] = byteList.get(index);
		}

		return ByteString.copyFrom(bytes);
	}

	public static List<Byte> byteStringToByteList(ByteString byteString)
	{
		List<Byte> byteList = new ArrayList<>();
		for (Byte val : byteString.toByteArray())
		{
			byteList.add(val);
		}
		return byteList;
	}

	public static byte[] byteListToArray(List<Byte> byteList)
	{
		if (byteList == null || byteList.size() == 0)
			return new byte[] {};
		byte[] byteArray = new byte[byteList.size()];
		for (int i = 0; i < byteList.size(); i++)
		{
			byteArray[i] = byteList.get(i);
		}
		return byteArray;
	}
}
