package com.mycode.mybatis;

/**
 * Created by jiangzhen on 2017/7/26
 */
public class User
{
	private int id;
	private String username;
	private int age;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public int getAge()
	{
		return age;
	}

	public void setAge(int age)
	{
		this.age = age;
	}

	public String toString()
	{
		return "id="+id+", username="+username+", age="+age;
	}
}
