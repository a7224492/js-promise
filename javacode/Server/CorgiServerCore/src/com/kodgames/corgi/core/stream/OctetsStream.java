package com.kodgames.corgi.core.stream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author cuichao
 */
public class OctetsStream extends Octets 
{
	private static final long serialVersionUID = 2001312831600002845L;
	private static final int MAX_SPACE = 16384;
	private static volatile boolean isCompact = false;
	private volatile int pos = 0;
	private volatile int transpos = 0;

	public OctetsStream() 
	{
	}

	public OctetsStream(int size)
	{
		super(size);
	}

	public OctetsStream(Octets o)
	{
		super(o);
	}

	public static OctetsStream wrap(Octets o) 
	{
		OctetsStream os = new OctetsStream();
		os.swap(o);
		return os;
	}

	@Override
    public Object clone() 
	{
		OctetsStream os = (OctetsStream) super.clone();
		os.pos = pos;
		os.transpos = pos;
		return os;
	}

	public boolean eos() 
	{
		return pos == size();
	}

	public void setPosition(int pos)
	{
		this.pos = pos;
	}

	public int getPosition()
	{
		return pos;
	}

	public int remain() 
	{
		return size() - pos;
	}

	public OctetsStream marshal(byte x) 
	{
		pushBack(x);
		return this;
	}

	public OctetsStream marshal(boolean b)
	{
		pushBack((byte) (b ? 1 : 0));
		return this;
	}

	public OctetsStream marshal(short x) 
	{
		return marshal((byte) (x >> 8)).marshal((byte) x);
	}

	public OctetsStream marshal(char x) 
	{
		return marshal((byte) (x >> 8)).marshal((byte) x);
	}

	public OctetsStream marshal(int x) 
	{
		return compactUint32(x);
	}

	public OctetsStream marshal(long x)
	{
		return compactUint64(x);
	}

	public OctetsStream marshal(float x) 
	{
		return marshal(Float.floatToRawIntBits(x));
	}

	public OctetsStream marshal(double x)
	{
		return marshal(Double.doubleToRawLongBits(x));
	}

	public OctetsStream marshal(Octets o) 
	{
		marshal(o.size());
		insert(size(), o);
		return this;
	}

	public OctetsStream marshal(IMarshal m)
	{
		return m.marshal(this);
	}

	public OctetsStream marshal(String str) 
	{
		return marshal(str, "UTF-8");
	}

	public OctetsStream marshal(String str, String charset) 
	{
		try 
		{
			marshal((charset == null) ? str.getBytes() : str.getBytes(charset));
		} 
		catch (Exception ex) 
		{
			throw new RuntimeException(ex);
		}
		return this;
	}

	public OctetsStream marshal(byte[] bytes) 
	{
		marshal(bytes.length);
		insert(size(), bytes);
		return this;
	}

	public <T> OctetsStream marshal(List<T> list)
	{
		// if(list.size() < 0)
		// throw new RuntimeException("list's size is invalid.");

		marshal(list.size());
		for (T t : list)
			marshal(t);

		return this;
	}

	public <T> OctetsStream marshal(Set<T> set)
	{
		// if(set.size() < 0)
		// throw new RuntimeException("set's size is invalid.");

		marshal(set.size());
		for (T t : set)
			marshal(t);

		return this;
	}

	public <K, V> OctetsStream marshal(Map<K, V> map) 
	{
		// if(map.size() < 0)
		// throw new RuntimeException("map's size is invalid.");

		marshal(map.size());
		for (Map.Entry<K, V> entry : map.entrySet()) 
			marshal(entry.getKey()).marshal(entry.getValue());

		return this;
	}

	public <T> OctetsStream marshal(T t)
	{
		if (t instanceof Byte)
			marshal((Byte)t);
		else if (t instanceof Boolean)
			marshal((Boolean) t);
		else if (t instanceof CharSequence)
			marshal((CharSequence) t);
		else if (t instanceof Short)
			marshal((Short) t);
		else if (t instanceof Integer)
			marshal((Integer) t);
		else if (t instanceof Long)
			marshal((Long) t);
		else if (t instanceof Float)
			marshal((Float) t);
		else if (t instanceof Double)
			marshal((Double) t);
		else if (t instanceof String)
			marshal((String) t);
		else if (t instanceof Octets)
			marshal((Octets) t);
		else if (t instanceof IMarshal)
			marshal((IMarshal) t);
		else
			throw new RuntimeException("can't recognition type.");

		return this;
	}

	public OctetsStream begin() 
	{
		transpos = pos;
		return this;
	}

	public OctetsStream rollback()
	{
		pos = transpos;
		return this;
	}

	public OctetsStream commit()
	{
		if (pos >= MAX_SPACE) 
		{
			erase(0, pos);
			pos = 0;
		}
		return this;
	}

	// unmarshal
	public byte unmarshalByte()
	{
		try
		{
			if (pos + 1 > size())
				throw new MarshalException();
			return getByte(pos++);
		}
		catch (com.kodgames.corgi.core.stream.MarshalException e)
		{
			e.printStackTrace();
		}

		return 0;
	}

	public boolean unmarshalBool() 
	{
		return unmarshalByte() == 1;
	}

	public short unmarshalShort()  
	{
		try
		{
			if (pos + 2 > size())
				throw new MarshalException();
			byte b0 = getByte(pos++);
			byte b1 = getByte(pos++);
			return (short) ((b0 << 8) | (b1 & 0xFF));
		}
		catch (com.kodgames.corgi.core.stream.MarshalException e)
		{
			e.printStackTrace();
		}

		return 0;
	}

//	public char unmarshalChar() 
//	{
//		try
//		{
//			if (pos + 2 > size())
//				throw new MarshalException();
//			byte b0 = getByte(pos++);
//			byte b1 = getByte(pos++);
//			return (char) ((b0 << 8) | (b1 & 0xFF));
//		}
//		catch (com.kodgames.corgi.core.stream.MarshalException e)
//		{
//			e.printStackTrace();
//		}
//
//		return '';		
//	}

	public int unmarshalInt()  
	{
		return uncompactUint32();
	}

	public long unmarshalLong() 
	{
		return uncompactUint64();
	}

	public float unmarshalFloat() 
	{
		return Float.intBitsToFloat(unmarshalInt());
	}

	public double unmarshalDouble()
	{
		return Double.longBitsToDouble(unmarshalLong());
	}

	public Octets unmarshalOctets() throws MarshalException
	{
		int size = unmarshalInt();
		if (pos + size > size())
			throw new MarshalException();

		Octets o = new Octets(this, pos, size);
		pos += size;
		return o;
	}

	public OctetsStream unmarshal(Octets os) throws MarshalException
	{
		int size = unmarshalInt();
		if (pos + size > size())
			throw new MarshalException();

		os.replace(this, pos, size);
		pos += size;
		return this;
	}

	public byte[] unmarshalBytes() throws MarshalException
	{
		int size = unmarshalInt();
		if (pos + size > size())
			throw new MarshalException();

		byte[] copy = new byte[size];
		System.arraycopy(array(), pos, copy, 0, size);
		pos += size;
		return copy;
	}

	public String unmarshalString()
	{
		return unmarshalString("UTF-8");
	}

	public String unmarshalString(String charset)
	{
		try 
		{
			int size = unmarshalInt();
			if (pos + size > size())
				throw new MarshalException();
			int cur = pos;
			pos += size;
			return (charset == null) ? new String(array(), cur, size) : new String(array(), cur, size, charset);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return "";
	}

	public <T> List<T> unmarshalList(Class<T> type) throws MarshalException 
	{
		try 
		{
			int count = unmarshalInt();
			if (count < 0)
				throw new MarshalException();

			List<T> list = new ArrayList<T>();
			for (int i = 0; i < count; i++)
				list.add(unmarshal(type));

			return list;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			throw new MarshalException();
		}
	}

	public <T> Set<T> unmarshalSet(Class<T> type) throws MarshalException
	{
		try
		{
			int count = unmarshalInt();
			if (count < 0)
				throw new MarshalException();

			Set<T> set = new HashSet<T>();
			for (int i = 0; i < count; i++)
				set.add(unmarshal(type));

			return set;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new MarshalException();
		}
	}

	public <K, V> Map<K, V> unmarshalMap(Class<K> keyType, Class<V> valueType) throws MarshalException 
	{
		try 
		{
			int count = unmarshalInt();
			if (count < 0)
				throw new MarshalException();

			Map<K, V> map = new HashMap<K, V>();
			for (int i = 0; i < count; i++)
				map.put(unmarshal(keyType), unmarshal(valueType));

			return map;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			throw new MarshalException();
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T unmarshal(Class<T> type) throws MarshalException
	{
		try 
		{
			if (type.getSimpleName().equals("Byte"))
				return (T) new Byte(unmarshalByte());
			else if (type.getSimpleName().equals("Boolean"))
				return (T) new Boolean(unmarshalBool());
//			else if (type.getSimpleName().equals("CharSequence"))
//				return (T) new Character(unmarshalChar());
			else if (type.getSimpleName().equals("Short"))
				return (T) new Short(unmarshalShort());
			else if (type.getSimpleName().equals("Integer"))
				return (T) new Integer(unmarshalInt());
			else if (type.getSimpleName().equals("Long"))
				return (T) new Long(unmarshalLong());
			else if (type.getSimpleName().equals("Float"))
				return (T) new Float(unmarshalFloat());
			else if (type.getSimpleName().equals("Double"))
				return (T) new Double(unmarshalDouble());
			else if (type.getSimpleName().equals("String"))
				return (T) new String(unmarshalString());
			else if (type.getSimpleName().equals("Octets"))
				return (T) new Octets(unmarshalOctets());
			else if (type.getSuperclass().equals("IMarshal"))
				return (T) unmarshalIMarshal((IMarshal) (type.newInstance()));
			else
			{
				Class<?> temp = type.getSuperclass();
				do 
				{
					if (temp != null && temp.getSimpleName().equals("IMarshal"))
						return (T) unmarshalIMarshal((IMarshal) (type.newInstance()));
				} while (temp != null && (temp = temp.getSuperclass()) != null);

				throw new RuntimeException("can't recognition type. type is " + type.getSimpleName());
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			throw new MarshalException();
		}
	}

	public OctetsStream unmarshal(IMarshal m) throws MarshalException 
	{
		return m.unmarshal(this);
	}

	public IMarshal unmarshalIMarshal(IMarshal m) throws MarshalException 
	{
		m.unmarshal(this);
		return m;
	}

	public static boolean isCompact()
	{
		return isCompact;
	}

	public static void setCompact(boolean isCompact)
	{
		OctetsStream.isCompact = isCompact;
	}

	/**
	 * Java��֧��unsigned int
	 * @param x
	 * @return
	 */
	private OctetsStream compactInt32(int x) 
	{
		if (!isCompact)
			return marshal((byte) (x >> 24)).marshal((byte) (x >> 16)).marshal((byte) (x >> 8)).marshal((byte) x);

		return compactInt64(x);
	}

	private OctetsStream compactUint32(int x)
	{
		return compactInt32(x);
	}

	private int uncompactInt32() 
	{
		try
		{
			if (!isCompact) 
			{
				if (pos + 4 > size())
					throw new MarshalException();

				byte b0 = getByte(pos++);
				byte b1 = getByte(pos++);
				byte b2 = getByte(pos++);
				byte b3 = getByte(pos++);

				return (int) (((b0 & 0xFF) << 24) | ((b1 & 0xFF) << 16) | ((b2 & 0xFF) << 8) | ((b3 & 0xFF) << 0));
			}

			return (int) uncompactInt64();
		}
		catch (com.kodgames.corgi.core.stream.MarshalException e)
		{
			e.printStackTrace();
		}

		return 0;
	}

	private int uncompactUint32() 
	{
		return uncompactInt32();
	}

	private OctetsStream compactInt64(long x) 
	{
		if (!isCompact)
			return marshal((byte) (x >> 56)).marshal((byte) (x >> 48)).marshal((byte) (x >> 40)).marshal((byte) (x >> 32))
					.marshal((byte) (x >> 24)).marshal((byte) (x >> 16)).marshal((byte) (x >> 8)).marshal((byte) x);

		while (x > 0x7F) 
		{
			marshal((byte) ((x & 0x7F) | 0x80));
			x >>= 7;
		}
		marshal((byte) (x & 0x7F));

		return this;
	}

	private OctetsStream compactUint64(long x) 
	{
		return compactInt64(x);
	}

	private long uncompactInt64() 
	{
		try
		{
			if (!isCompact)
			{
				if (pos + 8 > size())
					throw new MarshalException();

				byte b0 = getByte(pos++);
				byte b1 = getByte(pos++);
				byte b2 = getByte(pos++);
				byte b3 = getByte(pos++);
				byte b4 = getByte(pos++);
				byte b5 = getByte(pos++);
				byte b6 = getByte(pos++);
				byte b7 = getByte(pos++);

				return ((((long) b0 & 0xFF) << 56) | (((long) b1 & 0xFF) << 48) | (((long) b2 & 0xFF) << 40) | (((long) b3 & 0xFF) << 32)
						| (((long) b4 & 0xFF) << 24) | (((long) b5 & 0xFF) << 16) | (((long) b6 & 0xFF) << 8) | (((long) b7 & 0xFF) << 0));
			}

			byte b = unmarshalByte();
			int index = 0;
			long ret = 0;
			do 
			{
				ret |= ((long) (b & 0x7F) << (7 * index++));
			} while (((b & 0x80) != 0) && ((b = unmarshalByte()) != 0));

			return ret;
		}
		catch (com.kodgames.corgi.core.stream.MarshalException e)
		{
			e.printStackTrace();
		}

		return 0;
	}

	private long uncompactUint64()
	{
		return uncompactInt64();
	}

	public static void main(String[] args) throws Exception 
	{
		{
			float f = 123.45F;
			int i = Float.floatToIntBits(f);
			System.out.println("f = " + i);
		}
		{
			double d = 234.56;
			long l = Double.doubleToLongBits(d);
			System.out.println("d = " + l);
		}
		OctetsStream.setCompact(true);
		OctetsStream os = new OctetsStream();

		os.marshal('a');
		os.marshal((short) 1);
		os.marshal(127);
		os.marshal(128);
		os.marshal(10034234L);
		os.marshal(12.3455F);
		os.marshal(123.434D);
		os.marshal("abcdefg");

		// marshal list
		List<Integer> list = new ArrayList<Integer>();
		list.add(100);
		list.add(200);
		list.add(300);
		os.marshal(list);

		// marshal set
		Set<Integer> set = new HashSet<Integer>();
		set.add(100);
		set.add(200);
		set.add(300);
		os.marshal(set);

		// marshal map
		Map<Integer, Float> map = new HashMap<Integer, Float>();
		map.put(1, 1.1F);
		map.put(2, 2.2F);
		map.put(3, 3.3F);
		os.marshal(map);

		// unmarshal
		//System.out.println(os.unmarshalChar());
		System.out.println(os.unmarshalShort());
		System.out.println(os.unmarshalInt());
		System.out.println(os.unmarshalInt());
		System.out.println(os.unmarshalLong());
		System.out.println(os.unmarshalFloat());
		System.out.println(os.unmarshalDouble());
		System.out.println(os.unmarshalString());

		// unmarshal list
		System.out.print("list is [");
		for (Integer i : os.unmarshalList(Integer.class))
			System.out.print(i + ", ");
		System.out.println("]");

		// unmarshal set
		System.out.print("set is [");
		for (Integer i : os.unmarshalSet(Integer.class))
			System.out.print(i + ", ");
		System.out.println("]");

		// unmarshal map
		System.out.print("map is [");
		for (Map.Entry<Integer, Float> entry : os.unmarshalMap(Integer.class,
				Float.class).entrySet())
			System.out.print("<key = " + entry.getKey() + ", " + "value = "
					+ entry.getValue() + ">, ");
		System.out.println("]");

		// ѹ������
		//�����ͣ�100w�Σ�
		//type			compact		no compact		protobuf
		//char: 			90			90				410	
		//short:			80			80				160	 	
		//int(1byte):		90			120				200
		//int(2byte):		110			120				220
		//long:				170			190				220
		//float:			160			120				210
		//double:			220			190				240
		//string:			420			360				370
		final int count = 100 * 10000;
		OctetsStream.setCompact(true);
		long begin = System.currentTimeMillis();
//		OctetsStream os1 = new OctetsStream();
		for (int i = 0; i < count; i++)
		{
			OctetsStream os1 = new OctetsStream();

			os1.marshal('b');
			os1.marshal((short) 100);
			os1.marshal(127);
			os1.marshal(128);
			os1.marshal(99999999123L);
			os1.marshal(123.456F);
			os1.marshal(234.567D);
//			os1.marshal("abcedefw");

//			os1.unmarshalChar();
			os1.unmarshalShort();
			os1.unmarshalInt();
			os1.unmarshalInt();
			os1.unmarshalLong();
			os1.unmarshalFloat();
			os1.unmarshalDouble();
//			os1.unmarshalString();
		}
		System.out.println("compact test. time is " + (System.currentTimeMillis() - begin));

		OctetsStream.setCompact(false);
		begin = System.currentTimeMillis();
//		OctetsStream os2 = new OctetsStream();
		for (int i = 0; i < count; i++) 
		{
			OctetsStream os2 = new OctetsStream();
			os2.marshal('b');
			os2.marshal((short) 100);
			os2.marshal(127);
			os2.marshal(128);
			os2.marshal(99999999123L);
			os2.marshal(123.456F);
			os2.marshal(234.567D);
//			os2.marshal("abcedefw");

//			os2.unmarshalChar();
			os2.unmarshalShort();
			os2.unmarshalInt();
			os2.unmarshalInt();
			os2.unmarshalLong();
			os2.unmarshalFloat();
			os2.unmarshalDouble();
//			os2.unmarshalString();
		}
		System.out.println("no compact test. time is " + (System.currentTimeMillis() - begin));

	}
}
