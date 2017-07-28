package com.kodgames.corgi.core.util.protostuff;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by jiangzhen on 2017/7/26
 */
public class ProtostuffUtil
{
	private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();
	private static Objenesis objenesis = new ObjenesisStd(true);

	@SuppressWarnings("unchecked")
	public static <T> byte[] serialize(T obj) {
		Class<T> cls = (Class<T>) obj.getClass();
		LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
		try {
			Schema<T> schema = getSchema(cls);
			return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		} finally {
			buffer.clear();
		}
	}

	private static <T> Schema<T> getSchema(Class<T> cls) {
		Schema<T> schema = (Schema<T>) cachedSchema.get(cls);
		if (schema == null) {
			schema = RuntimeSchema.createFrom(cls);
			if (schema != null) {
				cachedSchema.put(cls, schema);
			}
		}

		return schema;
	}

	public static <T> T deserialize(byte[] data, Class<T> cls)
	{
		try
		{
			T message = objenesis.newInstance(cls);
			Schema<T> schema = getSchema(cls);
			ProtostuffIOUtil.mergeFrom(data, message, schema);
			return message;
		}
		catch (Exception e)
		{
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public static void main(String[] args)
	{
		StudentHelper sh = new StudentHelper();
		sh.addStudent(new Student("1001", "jiangzhen", 23));
		sh.addStudent(new Student("1001", "chuangwang", 23));
		sh.addStudent(new Student("1001", "shafuzi", 23));

		byte[] data = ProtostuffUtil.serialize(sh);
		StudentHelper list2 = ProtostuffUtil.deserialize(data, StudentHelper.class);
		System.out.println(list2);

//		Student stu = new Student("1001", "jiangzhen", 23);
//		byte[] data = ProtostuffUtil.serialize(stu);
//		Student stu2 = ProtostuffUtil.deserialize(data, Student.class);
	}
}

class StudentHelper
{
	private List<Student> list = new ArrayList<Student>();

	public void addStudent(Student stu)
	{
		list.add(stu);
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (Student stu : list)
		{
			sb.append(stu.toString()+"\n");
		}

		return sb.toString();
	}
}

class Student
{
	private String id;
	private String name;
	private int age;

	public Student(String id, String name, int age)
	{
		this.id = id;
		this.name = name;
		this.age = age;
	}

	public String toString()
	{
		return "id="+id+", name="+name+", age="+age;
	}
}
