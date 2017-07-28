package com.kodgames.corgi.core.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;





import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.net.www.protocol.file.FileURLConnection;
import sun.net.www.protocol.jar.JarURLConnection;

public class PackageScaner
{
	private static final Logger logger = LoggerFactory.getLogger(PackageScaner.class);
	
	@SuppressWarnings("rawtypes")
	public static List<Class> getClasses(String namespace, String fileExt, boolean isRecursion) throws ClassNotFoundException
	{
		ArrayList<Class> classes = new ArrayList<Class>();
		String[] files = scanNamespaceFiles(namespace, fileExt, isRecursion);
		if(files == null)
			return classes;
		for(String fileName : files)
		{
			String classPath = namespace + "." + fileName.subSequence(0, fileName.length() - (fileExt.length()));
			classes.add(Class.forName(classPath));
			
		}
		return classes;
	}

	public static String[] scanNamespaceFiles(String namespace, String fileExt, boolean isRecursion)
	{
		String respath = namespace.replace('.', '/');
		respath = respath.replace('.', '/');

		List<String> tmpNameList = new ArrayList<String>();
		try
		{
			URL url = null;
			if (!respath.startsWith("/"))
				url = PackageScaner.class.getResource("/" + respath);
			else
				url = PackageScaner.class.getResource(respath);

			URLConnection tmpURLConnection = url.openConnection();
			String tmpItemName;
			if (tmpURLConnection instanceof JarURLConnection)
			{
				JarURLConnection tmpJarURLConnection = (JarURLConnection) tmpURLConnection;
				int tmpPos;
				String tmpPath;
				JarFile jarFile = tmpJarURLConnection.getJarFile();
				Enumeration<JarEntry> entrys = jarFile.entries();				
				while (entrys.hasMoreElements())
				{
					JarEntry tmpJarEntry = entrys.nextElement();
					if (!tmpJarEntry.isDirectory())
					{
						tmpItemName = tmpJarEntry.getName();
						if (tmpItemName.indexOf('$') < 0
								&& (fileExt == null || tmpItemName.endsWith(fileExt)))
						{
							tmpPos = tmpItemName.lastIndexOf('/');
							if (tmpPos > 0)
							{
								tmpPath = tmpItemName.substring(0, tmpPos);
								if(isRecursion){
									if (tmpPath.startsWith(respath))
									{
										
										String r = tmpItemName.substring(respath.length()+1).replaceAll("/", ".");
										tmpNameList.add(r);
									}	
								}else{
									if (respath.equals(tmpPath))
									{
										tmpNameList.add(tmpItemName.substring(tmpPos + 1));
									}
								}
								
							}
						}
					}
				}
			}
			else if (tmpURLConnection instanceof FileURLConnection)
			{
				File file = new File(url.getFile());
				if (file.exists() && file.isDirectory())
				{
					File[] fileArray = file.listFiles();
					for (File f : fileArray)
					{
						if(f.isDirectory() && f.getName().indexOf(".")!=-1)
							continue;
						
						tmpItemName = f.getName();
						if(f.isDirectory() && isRecursion){
							String[] inner = scanNamespaceFiles(namespace+"."+tmpItemName, fileExt, isRecursion);
							if(inner == null){
								continue;
							}
							for(String i : inner){
								if(i!=null){
									tmpNameList.add(tmpItemName+"."+i);
								}
							}
						}else if(fileExt == null || tmpItemName.endsWith(fileExt) )
						{
							tmpNameList.add(tmpItemName);
						}else{
							continue;// 明确一下，不符合要求就跳过
						}
					}
				}
				else
				{
					logger.error("scaning stop,invalid package path:" + url.getFile());
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (tmpNameList.size() > 0)
		{
			String[] r = new String[tmpNameList.size()];
			tmpNameList.toArray(r);
			tmpNameList.clear();
			return r;
		}
		return null;
	}

	public static void main(String[] args) throws IOException
	{
//		JarPathLoader.getNewJarLoader("/dist/server-core-1.0.0.jar");
//		URLClassLoader c = JarClassLoader.getClassLoad("D:/t4game/workspace3/GameServerCore/dist", true);
//		
//		System.out.println(c.getResource("com/t4game/test/classloader/test.txt"));
//		String[] files = scanNamespaceFiles("com.t4game.test", "txt",false);
//		for (int i = 0; files != null && i < files.length; i++)
//		{
//			System.out.println(files[i]);
//		}
//		System.out.println("**********************");
//		files = scanNamespaceFiles("com.kodgames.game.action.activity", ".class");
//		for (int i = 0; files != null && i < files.length; i++)
//		{
//			System.out.println(files[i]);
//		}
		
		JarFile jarFile = new JarFile("dist/server-core-1.0.0.jar");
		Enumeration<JarEntry> entrys=jarFile.entries();    
		
	    while(entrys.hasMoreElements())   
	    {    
              JarEntry  entry = (JarEntry)entrys.nextElement();    
              if   (entry.isDirectory())  
            	  continue;    
              System.out.println(entry.getName());
	    }
	    
	    jarFile.close();
	}
}
