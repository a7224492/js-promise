package com.mycode.mybatis;

import com.mysql.jdbc.Driver;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by jiangzhen on 2017/8/16
 */
public class DbUtil
{
	public static class TableAnBean
	{
		public String tableName;
		public String beanName;
	}

	public static List<TableAnBean> getBeanName(String url, String username, String password) throws Exception
	{
		Class<?> driverClz = null;
		try
		{
			driverClz = Class.forName("com.mysql.jdbc.Driver");
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}

		Driver driver = (Driver) driverClz.newInstance();
		Properties prop = new Properties();
		prop.setProperty("user", username);
		prop.setProperty("password", password);
		java.sql.Connection con = driver.connect(url, prop);

		Statement smt = con.createStatement();
		ResultSet rs = smt.executeQuery("show tables");

		List<TableAnBean> retList = new ArrayList<TableAnBean>();
		while (rs.next())
		{
			String tableName = rs.getString(1);
			String clzName = "";
			for (String str : tableName.split("_"))
			{
				if (!str.equals("t") && !str.equals("game") && !str.equals("pub"))
					clzName = clzName + str.substring(0, 1).toUpperCase() + str.substring(1, str.length());
			}

			clzName += "Bean";


			TableAnBean tab = new TableAnBean();
			tab.beanName = clzName;
			tab.tableName = tableName;
			retList.add(tab);
		}

		return retList;
	}
}
