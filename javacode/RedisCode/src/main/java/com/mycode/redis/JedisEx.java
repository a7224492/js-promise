package com.mycode.redis;

import redis.clients.jedis.Jedis;

/**
 * Created by jiangzhen on 2017/7/26
 */
public class JedisEx
{
	public static void main(String[] args)
	{
		Jedis jedis;

		//连接redis服务器，192.168.0.100:6379
		jedis = new Jedis("127.0.0.1", 6379);

		//权限认证
		jedis.auth("loho@2017");

		jedis.hset("1000", "name", "jiangzhen");
		jedis.hset("1000", "age", "23");
		jedis.hset("1000", "province", "guizhou");

		try
		{
			Thread.sleep(2000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		String name = jedis.hget("1000", "name");
	}
}
