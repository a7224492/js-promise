package com.kodgames.corgi.core.snappy;

public class BufferUtil
{
	public static short swapShort(short value)
	{
		return Short.reverseBytes(value);
	}
	
	public static int swapInt(int value)
	{
		return Integer.reverseBytes(value);
	}
	
	public static int swapMedium(int value) {
        int swapped = value << 16 & 0xff0000 | value & 0xff00 | value >>> 16 & 0xff;
        if ((swapped & 0x800000) != 0) {
            swapped |= 0xff000000;
        }
        return swapped;
    }
}
