package com.kodgames.corgi.core.util;

public class IPUtils
{
	public static String ipToStr(int ip)
	{
		StringBuffer sb = new StringBuffer("");  
        // 直接右移24位  
        sb.append(String.valueOf((ip >>> 24)));  
        sb.append(".");  
        // 将高8位置0，然后右移16位  
        sb.append(String.valueOf((ip & 0x00FFFFFF) >>> 16));  
        sb.append(".");  
        // 将高16位置0，然后右移8位  
        sb.append(String.valueOf((ip & 0x0000FFFF) >>> 8));  
        sb.append(".");  
        // 将高24位置0  
        sb.append(String.valueOf((ip & 0x000000FF)));  
        return sb.toString();  
	}
	
	public static int ipToInt(String strIp)
	{
		 String[] ip = strIp.split("\\.");  
	        return (Integer.parseInt(ip[0]) << 24) + (Integer.parseInt(ip[1]) << 16) + (Integer.parseInt(ip[2]) << 8) + Integer.parseInt(ip[3]);  
	}
}
