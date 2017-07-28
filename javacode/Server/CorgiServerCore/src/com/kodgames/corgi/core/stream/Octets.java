package com.kodgames.corgi.core.stream;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * @author cuichao
 */
public class Octets implements Serializable, Cloneable, Comparable<Object> 
{
	private static final long serialVersionUID = 3878982824086926389L;
	private static final int DEFAULT_SIZE = 128;
	private static volatile String DEFAULT_CHARSET = "ISO-8859-1";
	private volatile byte[] buffer = null;
	private volatile int count = 0;
	
	public Octets()
	{
		reserve(DEFAULT_SIZE);
	}
	public Octets(int size)
	{
		reserve(size);
	}
	public Octets(Octets rhs)
	{
		replace(rhs);
	}
	public Octets(byte[] rhs)
	{
		replace(rhs);
	}
	public Octets(byte[] rhs, int pos, int size)
	{
		replace(rhs, pos, size);
	}
	public Octets(Octets rhs, int pos, int size)
	{
		replace(rhs, pos, size);
	}
	
	@Override
	public int compareTo(Object o)
    {
		return compareTo((Octets)o);
	}
	public int compareTo(Octets rhs)
	{
		int c = count - rhs.count;
		if(c != 0)
			return c;
		
		byte[] v1 = buffer;
		byte[] v2 = rhs.buffer;
		for(int i = 0; i < count; i++)
		{
			int v = v1[i] - v2[i];
			if(v != 0)
				return v;
		}
		return 0;
	}
	
	public static Octets wrap(byte[] bytes, int length)
	{
		return new Octets(bytes, length);
	}
	public static Octets wrap(byte[] bytes)
	{
		return wrap(bytes, bytes.length);
	}
	public static Octets wrap(String str, String encoding)
	{
		try
		{
			return wrap(str.getBytes(encoding));
		}
		catch(java.io.UnsupportedEncodingException ex)
		{
			throw new RuntimeException(ex);
		}
	}

	public void reserve(int size)
	{
		if(buffer == null)
			buffer = roundup(size);
		else if(size > buffer.length)
		{
			byte[] temp = roundup(size);
			System.arraycopy(buffer, 0, temp, 0, count);
			buffer = temp;
		}
	}
	
	public Octets replace(byte[] data, int pos, int size)
	{
		reserve(size);
		System.arraycopy(data, pos, buffer, 0, size);
		count = size;
		return this;
	}
	public Octets replace(Octets data, int pos, int size)
	{
		return replace(data.buffer, pos, size);
	}
	public Octets replace(byte[] data)
	{
		return replace(data, 0, data.length);
	}
	public Octets replace(Octets data)
	{
		return replace(data.buffer, 0, data.count);
	}

	public Octets resize(int size)
	{
		reserve(size);
		count = size;
		return this;
	}
	
	public int size()	
	{
		return count;
	}
	
	public int capacity()
	{ 
		return buffer.length;
	}
	
	public Octets clear()
	{
		count = 0;
		return this;
	}
	
	public Octets swap(Octets rhs)
	{
		int size = count;
		count = rhs.count;
		rhs.count = size;
		
		byte[] temp = rhs.buffer;
		rhs.buffer = buffer;
		buffer = temp;
		
		return this;
	}
	
	public Octets pushBack(byte data)
	{
		reserve(count + 1);
		buffer[count++] = data;
		return this;
	}
	
	//前闭后开原则
	public Octets erase(int from, int to)
	{
		if(to < from)
			return null;
		
		System.arraycopy(buffer, to, buffer, from, count - to);
		count -= to - from;
		return this;
	}
	
	public Octets insert (int from, byte[] data, int pos, int size)
	{
		reserve(count + size);
		System.arraycopy(buffer, from, buffer, from + size, count - from);
		System.arraycopy(data, pos, buffer, from, size);
		count += size;
		return this;
	}
	public Octets insert(int from, Octets data, int pos, int size)
	{
		return insert(from, data.buffer, pos, size);
	}
	public Octets insert(int from, byte[] data)
	{
		return insert(from, data, 0, data.length);
	}
	public Octets insert(int from, Octets data)
	{
		return insert(from, data.buffer, 0, data.size());
	}
	
	@Override
    public Object clone()
	{
		return new Octets(this);
	}
	
	@Override
    public boolean equals(Object o)
	{
		if(this == o)
			return true;
		return compareTo(o) == 0;
	}
	
	@Override
    public int hashCode()
	{
		if(buffer == null)
			return 0;
		
		int ret = 1;
		for(int i = 0; i < count; i++)
			ret = 31 * ret + buffer[i];
		
		return ret;
	}
	
	@Override
    public String toString()
	{
		return "octets.size = " + count;
	}
	
	public byte[] getByte()
	{
		byte[] temp = new byte[count];
		System.arraycopy(buffer, 0, temp, 0, count);
		return temp;
	}
	
	public byte[] array()
	{
		return buffer;
	}

	public byte getByte(int pos)
	{
		return buffer[pos];
	}

	public void setByte(int pos, byte b)
	{
		buffer[pos] = b;
	}
	
	public ByteBuffer getByteBuffer(int off, int size)
	{
		return ByteBuffer.wrap(buffer, off, size);
	}
	public ByteBuffer getByteBuffer(int off)
	{
		return ByteBuffer.wrap(buffer, off, count - off);
	}
	public ByteBuffer getByteBuffer()
	{
		return ByteBuffer.wrap(buffer, 0, count);
	}
	
	public String getString() throws Exception
	{
		return new String(buffer, 0, count, DEFAULT_CHARSET);
	}
	public String getString(String encoding)
	{
		try
		{
			return new String(buffer, 0, count, encoding);
		}
		catch(java.io.UnsupportedEncodingException ex)
		{
			throw new RuntimeException(ex);
		}
	}

	public void setString(String str) throws Exception
	{
		buffer = str.getBytes(DEFAULT_CHARSET);
		count = buffer.length;
	}
	
	public void dump()
	{
		for(int i = 0; i < count; i++)
			System.out.printf("%02x ", buffer[i]);
		
		System.out.println();
	}
	
	public static void setDefaultCharset(String name)
	{
		DEFAULT_CHARSET = name;
	}
	
	private Octets(byte[] bytes, int length)
	{
		this.buffer = bytes;
		this.count = length;
	}
	
	private byte[] roundup(int size)
	{
		int capacity = 16;
		while(size > capacity)
			capacity <<= 1;
		
		return new byte[capacity];
	}
}
