package com.kodgames.corgi.core.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 对象的扩展需求
 * @author cuichao
 */
public class ObjectExtension implements Serializable
{
	private static final long serialVersionUID = 4252316775529085549L;

	public Object copy(Object object)
	{
		try
		{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(object);
			ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bis);
			return ois.readObject();
		}
		catch (IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public Object copy()
	{
		return copy(this);
	}
}
