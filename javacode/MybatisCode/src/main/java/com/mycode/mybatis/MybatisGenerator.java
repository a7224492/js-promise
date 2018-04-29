package com.mycode.mybatis;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.VerboseProgressCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangzhen on 2017/8/16
 */
public class MybatisGenerator
{
	public static void main(String[] args) throws Exception
	{
		// 读取数据库连接标签
		SAXReader reader = new SAXReader();
		String resource = System.getProperty("user.dir")+"/src/main/resources";
		Document document = reader.read(resource+"/generatorConfig.xml");
		Element contextElem = document.getRootElement().element("context");
		Element connectionElem = contextElem.element("jdbcConnection");

		// 得到数据库账号密码
		String url = connectionElem.attributeValue("connectionURL");
		String username = connectionElem.attributeValue("userId");
		String password = connectionElem.attributeValue("password");

		// 得到表名和bean名
		List<DbUtil.TableAnBean> tableAndBeanList = DbUtil.getBeanName(url, username, password);

		// 添加table标签
		contextElem.elements("table").clear();
		for (int i = 0; i < tableAndBeanList.size(); ++i)
		{
			DbUtil.TableAnBean tab = tableAndBeanList.get(i);
			String tableName = tab.tableName;
			String beanName = tab.beanName;

			//  添加一个table标签
			Element tableElem = contextElem.addElement("table");
			tableElem.addAttribute("tableName", tableName);
			tableElem.addAttribute("domainObjectName", beanName);
			tableElem.addAttribute("enableCountByExample", "false");
			tableElem.addAttribute("enableUpdateByExample", "false");
			tableElem.addAttribute("enableDeleteByExample", "false");
			tableElem.addAttribute("enableSelectByExample", "false");
			tableElem.addAttribute("selectByExampleQueryId", "false");
		}

		// 新生成的xml写入文件
		XMLWriter writer = new XMLWriter(new FileOutputStream(resource+"/generatorConfig.xml"), OutputFormat.createPrettyPrint());
		writer.write(document);

		// 生成对应的java类
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
