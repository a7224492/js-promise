package com.kodgames.client.util.response;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 测试响应时间工具
 * @author jiangzhen
 *
 */
class ResponseTimeUtilAdapter<T> implements IResponseUtil<T>
{
	/**
	 * 用来测试响应时间的一个结构体
	 * @author kod
	 *
	 */
	private static class ResponseStruct
	{
		public long sendTime = 0;
		public long recvTime = 0;
		
		/**
		 * 表示实例处于什么样的状态之中(默认状态，已经发送消息且没有收到消息，发送消息后收到消息)
		 */
		public StateEnum state;
			
		public ResponseStruct(long sendTime, long recvTime)
		{
			this.sendTime = sendTime;
			this.recvTime = recvTime;
			this.state = StateEnum.DEFAULT;
		}
	}
	private static enum StateEnum {DEFAULT, SEND_AND_WAIT_RECV, SEND_AND_RECV};
	
	/**
	 * 用来保存实例的响应时间
	 * 测试的玩家数量有可能是10个，100个等等
	 */
	private Map<T, ResponseStruct> map = new ConcurrentHashMap<T, ResponseStruct>();

	public ResponseTimeUtilAdapter()
	{

	}

	@Override
	public void setSendTime(T instance, long time)
	{
		if (instance == null)
		{
			throw new NullPointerException("instance is null");
		}
		if (time < 0)
		{
			throw new IllegalArgumentException("time is negative");
		}
		ResponseStruct rs = new ResponseStruct(time, 0);
		rs.state = StateEnum.SEND_AND_WAIT_RECV;
		map.put(instance, rs);
	}

	@Override
	public void setRecvTime(T instance, long time)
	{
		if (instance == null)
		{
			throw new NullPointerException("instance is null");
		}
		ResponseStruct rs = map.get(instance);
		if (rs == null)
		{
			throw new IllegalStateException("instance is not SEND_AND_WAIT_RECV");
		}
		if (time < 0)
		{
			throw new IllegalArgumentException("time is negative");
		}
		rs.recvTime = time;
		rs.state = StateEnum.SEND_AND_RECV;
		map.put(instance, rs);
	}

	@Override
	public long getResponseTime(T instance)
	{
		if (instance == null)
		{
			throw new NullPointerException("instance is null");
		}
		ResponseStruct rs = map.get(instance);
		if (rs == null)
		{
			throw new NullPointerException("instance in map is null");
		}
		if (rs.state != StateEnum.SEND_AND_RECV)
		{
			throw new IllegalStateException("state is not SEND_AND_RECV");
		}
		return rs.recvTime-rs.sendTime;
	}
	
	private StateEnum getInstanceState(T instance)
	{
		if (instance == null)
		{
			throw new NullPointerException("instance is null");
		}
		ResponseStruct rs = this.map.get(instance); 
		if (rs == null)
		{
			return StateEnum.DEFAULT;
		}
		
		return rs.state;
	}
	
	public boolean isDeafaultState(T instance)
	{
		return this.getInstanceState(instance) == StateEnum.DEFAULT;
	}
	public boolean isSendAndWaitRecvState(T instance)
	{
		return this.getInstanceState(instance) == StateEnum.SEND_AND_WAIT_RECV;
	}
	public boolean isSendAndRecvState(T instance)
	{
		return this.getInstanceState(instance) == StateEnum.SEND_AND_RECV;
	}
}
