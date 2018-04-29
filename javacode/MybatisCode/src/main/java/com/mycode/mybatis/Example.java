package com.mycode.mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.VerboseProgressCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangzhen on 2017/7/26
 */
public class Example
{
	public static void main(String[] args) throws Exception
	{
//		String resource = "mybatis-config.xml";
//		InputStream inputStream = null;
//		try
//		{
//			inputStream = Resources.getResourceAsStream(resource);
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
//		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
//
//		SqlSession session = sqlSessionFactory.openSession();
//		try {
//			UserMapper userMapper = (UserMapper) session.getMapper(UserMapper.class);
//			User user = userMapper.selectOneUser(1001);
//			System.out.println(user);
//		} finally {
//			session.close();
//		}

		List<String> warnings = new ArrayList<String>();
		boolean overwrite = true;
		InputStream input = Object.class.getResourceAsStream("/generatorConfig.xml");
		ConfigurationParser cp = new ConfigurationParser(warnings);
		Configuration config = cp.parseConfiguration(input);
		DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
		ProgressCallback progressCallback = new VerboseProgressCallback();
		myBatisGenerator.generate(progressCallback);
	}
}