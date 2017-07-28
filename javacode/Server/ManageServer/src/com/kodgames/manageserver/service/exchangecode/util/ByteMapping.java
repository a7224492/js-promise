package com.kodgames.manageserver.service.exchangecode.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sun.security.util.BitArray;

public class ByteMapping
{
	private Map<Byte, Character> byteMapping = new HashMap<Byte, Character>();
	private Map<Character, Byte> deByteMapping = new HashMap<Character, Byte>();

	public ByteMapping()
	{
		byteMapping.put((byte)0x00, 'Y');
		byteMapping.put((byte)0x01, 'X');
		byteMapping.put((byte)0x02, 'W');
		byteMapping.put((byte)0x03, 'V');
		byteMapping.put((byte)0x04, 'U');
		byteMapping.put((byte)0x05, 'T');
		byteMapping.put((byte)0x06, 'S');
		byteMapping.put((byte)0x07, 'R');

		byteMapping.put((byte)0x08, 'Q');
		byteMapping.put((byte)0x09, 'P');
		byteMapping.put((byte)0x0A, 'N');
		byteMapping.put((byte)0x0B, 'M');
		byteMapping.put((byte)0x0C, 'L');
		byteMapping.put((byte)0x0D, 'K');
		byteMapping.put((byte)0x0E, 'J');
		byteMapping.put((byte)0x0F, 'H');

		byteMapping.put((byte)0x10, 'G');
		byteMapping.put((byte)0x11, 'F');
		byteMapping.put((byte)0x12, 'E');
		byteMapping.put((byte)0x13, 'D');
		byteMapping.put((byte)0x14, 'C');
		byteMapping.put((byte)0x15, 'B');
		byteMapping.put((byte)0x16, 'A');
		byteMapping.put((byte)0x17, '9');

		byteMapping.put((byte)0x18, '8');
		byteMapping.put((byte)0x19, '7');
		byteMapping.put((byte)0x1A, '6');
		byteMapping.put((byte)0x1B, '5');
		byteMapping.put((byte)0x1C, '4');
		byteMapping.put((byte)0x1D, '3');
		byteMapping.put((byte)0x1E, '2');
		byteMapping.put((byte)0x1F, 'Z');

		deByteMapping.put('Y', (byte)0x00);
		deByteMapping.put('X', (byte)0x01);
		deByteMapping.put('W', (byte)0x02);
		deByteMapping.put('V', (byte)0x03);
		deByteMapping.put('U', (byte)0x04);
		deByteMapping.put('T', (byte)0x05);
		deByteMapping.put('S', (byte)0x06);
		deByteMapping.put('R', (byte)0x07);

		deByteMapping.put('Q', (byte)0x08);
		deByteMapping.put('P', (byte)0x09);
		deByteMapping.put('N', (byte)0x0A);
		deByteMapping.put('M', (byte)0x0B);
		deByteMapping.put('L', (byte)0x0C);
		deByteMapping.put('K', (byte)0x0D);
		deByteMapping.put('J', (byte)0x0E);
		deByteMapping.put('H', (byte)0x0F);

		deByteMapping.put('G', (byte)0x10);
		deByteMapping.put('F', (byte)0x11);
		deByteMapping.put('E', (byte)0x12);
		deByteMapping.put('D', (byte)0x13);
		deByteMapping.put('C', (byte)0x14);
		deByteMapping.put('B', (byte)0x15);
		deByteMapping.put('A', (byte)0x16);
		deByteMapping.put('9', (byte)0x17);

		deByteMapping.put('8', (byte)0x18);
		deByteMapping.put('7', (byte)0x19);
		deByteMapping.put('6', (byte)0x1A);
		deByteMapping.put('5', (byte)0x1B);
		deByteMapping.put('4', (byte)0x1C);
		deByteMapping.put('3', (byte)0x1D);
		deByteMapping.put('2', (byte)0x1E);
		deByteMapping.put('Z', (byte)0x1F);

	}

	private byte convertToByte(BitArray bits)
	{
		return bits.toByteArray()[0];
	}

	public String mapping(byte[] arrayOfBytes)
	{
		BitArray bits = new BitArray(64, arrayOfBytes);
		StringBuilder sb = new StringBuilder();
		int bitsLength = bits.length();

		for (int i = 0; i < bitsLength - 4; i += 5)
		{
			boolean[] bitGroup = new boolean[8];
			bitGroup[0] = false;
			bitGroup[1] = false;
			bitGroup[2] = false;
			bitGroup[3] = bits.get(i + 0);
			bitGroup[4] = bits.get(i + 1);
			bitGroup[5] = bits.get(i + 2);
			bitGroup[6] = bits.get(i + 3);
			bitGroup[7] = bits.get(i + 4);
			BitArray tempBits = new BitArray(bitGroup);
			byte byte5 = convertToByte(tempBits);

			if (byteMapping.containsKey(byte5))
			{
				sb.append(byteMapping.get(byte5));
			}
			else
			{
				return "-1";
			}
		}
		BitArray extendBits = new BitArray(8, new byte[] {0x00});
		extendBits.set(7, false);
		extendBits.set(6, bits.get(bitsLength - 1));
		extendBits.set(5, bits.get(bitsLength - 2));
		extendBits.set(4, bits.get(bitsLength - 3));
		extendBits.set(3, bits.get(bitsLength - 4));
		extendBits.set(2, false);
		extendBits.set(1, false);
		extendBits.set(0, false);

		byte extendByte5 = convertToByte(extendBits);
		if (byteMapping.containsKey(extendByte5))
		{
			sb.append(byteMapping.get(extendByte5));
		}
		return sb.toString();
	}

	public byte[] deMapping(String des)
	{
		byte[] b = new byte[] {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
		BitArray bits = new BitArray(64, b);

		char[] charArray = des.toCharArray();

		List<BitArray> bitArrayGroup = new ArrayList<BitArray>();

		for (int i = 0; i < charArray.length; ++i)
		{
			if (deByteMapping.containsKey(charArray[i]))
			{
				byte[] byte5 = new byte[1];
				byte5[0] = deByteMapping.get(charArray[i]);
				BitArray tempBitArray = new BitArray(8, byte5);
				bitArrayGroup.add(tempBitArray);
			}
			else
			{
				return b;
			}
		}
		int index = 0;
		for (int i = 0; i < bitArrayGroup.size() - 1; ++i)
		{
			BitArray bitArray = bitArrayGroup.get(i);
			for (int j = 0; j < 5; ++j)
			{
				bits.set(index++, bitArray.get(j + 3));
			}
		}
		BitArray extensionBitArray = bitArrayGroup.get(bitArrayGroup.size() - 1);
		for (int j = 0; j < 4; ++j)
		{
			bits.set(index++, extensionBitArray.get(j + 3));
		}
		return bits.toByteArray();
	}
}
