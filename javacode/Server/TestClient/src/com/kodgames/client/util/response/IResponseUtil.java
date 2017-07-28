package com.kodgames.client.util.response;

/**
 * 测试响应时间的工具需要实现的接口
 * @author jiangzhen
 *
 * @param <T> 测试的实例，比如打牌的玩家roleId
 */
public interface IResponseUtil<T>
{
	/**
	 * 设置发送时的时间
	 */
	public void setSendTime(T instance, long time);
	
	/**
	 * 设置接收时的时间
	 */
	public void setRecvTime(T instance, long time);
	
	/**
	 * 得到响应时间
	 */
	public long getResponseTime(T instance);
}
