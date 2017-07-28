package com.kodgames.message.generaor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author XianYue
 * 将ProtocolDesc.xml中的要放到alert_message表中的数据提取出来，并生成配置sql文件到resource/output.sql中
 *
 */
public class AlertMsgCreator
{
	class AlertMsg {
		private String id;
		private String description;
		public String getId()
		{
			return id;
		}
		public void setId(String id)
		{
			this.id = id;
		}
		public String getDescription()
		{
			return description;
		}
		public void setDescription(String description)
		{
			this.description = description;
		} 
		public AlertMsg(String id,String description) {
			this.id = id;
			this.description = description;
		}
	}
	public void compileDescAll() throws Exception{
		compileDesc(null);
	}
	
	public void compileDesc(String[] keys) throws Exception{
		StringBuilder compileHead = new StringBuilder();
		if(keys != null) {
			for (int i = 0; i < keys.length; i++) {
				if(i == keys.length - 1) {
					compileHead.append(keys[i]);
				}else {
					compileHead.append(keys[i] + "|");
				}
			}
		}
		String rule = "";
		if(compileHead.length() <= 0) {
			rule = "\\w+";
		}
		else {
			rule = compileHead.toString();
		}
		String regex = "((?:" + rule + ")(?:_\\w+)+)(?:\"\\s*isSuccess=\"true)*(?:\"\\s*>)([\u4e00-\u9fa5\ufe30-\uffa0A-Z]*)";
		
		
		File file4 = new File(System.getProperty("user.dir") + "/resource/ProtocolDesc.xml"); 
		
//		InputStream stream = AlertMsgCreator.class.getResourceAsStream("/resource/ProtocolDesc.xml");
		InputStream stream = new FileInputStream(file4);
		Pattern pattern = Pattern.compile(regex);
		StringBuilder builder = new StringBuilder();
		byte[] tmp = new byte[1024];
		int len = 0;
		while((len = stream.read(tmp)) != -1) {
			builder.append(new String(tmp, 0, len));
		}
		if(stream != null) {
			stream.close();
		}
		
		Matcher matcher = pattern.matcher(builder.toString());
		
//		HashMap<String, String> map = new HashMap<>();		//id  decription
		List<AlertMsg> msgs = new ArrayList<>();
		while(matcher.find()) {
			if(matcher.group(2) == null || "".equals(matcher.group(1))) {
//				map.put(matcher.group(1),"");
				msgs.add(new AlertMsg(matcher.group(1),""));
			}
			else {
//				map.put(matcher.group(1), matcher.group(2));
				msgs.add(new AlertMsg(matcher.group(1),matcher.group(2)));
			}
		}
//		System.out.println("---------> : " + map.size());
		StringBuilder builder2 = new StringBuilder(); 
//		for (Map.Entry<String, String> entry : map.entrySet()) {
		for (AlertMsg alertMsg: msgs)
        {
			builder2.append("replace into alert_message (id, description) values('");
			builder2.append(alertMsg.getId() + "','" + alertMsg.getDescription());
			builder2.append("');\r\n");
		}
//		System.out.println(builder2.toString());
		
		
		//写文件
		File file = new File(System.getProperty("user.dir") + "/resource/output.sql");
		if(!file.exists()) {
			boolean flag = file.createNewFile();
			if(!flag) {
				System.out.println("----error: create file error" );
			}
		}
		BufferedWriter bufwrt = new BufferedWriter(new FileWriter(file));
		bufwrt.write(builder2.toString());
		bufwrt.close();
	}

	public static void main(String[] args)
	{
		AlertMsgCreator creator = new AlertMsgCreator();
		try
        {
	        creator.compileDescAll();
        }
        catch (Exception e)
        {
	        e.printStackTrace();
        }

	}

}
