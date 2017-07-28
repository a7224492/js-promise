package com.kodgames.corgi.core.net.common;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import java.net.InetSocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.message.InternalMessage;

/**
 * 在Interface存在的情况下RemoteNode并不能与Client一一对应
 * Client的唯一标识转移到{@link Connection}对象
 * 断线重连逻辑需要修改
 */
public class NettyNode
{
	private static final Logger logger = LoggerFactory.getLogger(NettyNode.class);

	private Channel channel;
	private InetSocketAddress remoteAddress;
	
	private long recvProtoAmount = 0;
	private long sendProtoAmount = 0;
	
	public NettyNode(Channel channel)
	{
		this.channel = channel;
	}
	
	public Channel getChannel()
	{
		return channel;
	}
	
	public InetSocketAddress getAddress()
	{
		//如果没有得到玩家真实的IP地址，则从socket channel中返回玩家IP地址
		if (remoteAddress == null){
			return (InetSocketAddress)channel.remoteAddress();	
		}
		return remoteAddress;
	}

	
	public void setAddress(InetSocketAddress remoteAddress)
	{
		this.remoteAddress = remoteAddress;		//根据HAProxy协议的解析结果设置玩家真实的IP地址
	}
	
	public Object getKey()
	{
		return channel.remoteAddress();
	}
	
	public void setChannel(Channel channel)
	{
		this.channel = channel;
	}
	
	public void incRecvAmount()
	{
		this.recvProtoAmount += 1;
		
		//TODO:断线重连
//		if(session != null)
//			session.setUpSequenceId(recvProtoAmount);
	}
	
	public long getRecvAmount()
	{
		return this.recvProtoAmount;
	}
	
	public void incSendAmount()
	{
		this.sendProtoAmount += 1;
		//TODO:断线重连
//		if(session != null)
//			session.setDownSequenceId(sendProtoAmount);
	}
	
	public long getSendAmount()
	{
		return this.sendProtoAmount;
	}
	
	public void clearProtocolAmount()
	{
		this.sendProtoAmount = 0;
		this.recvProtoAmount = 0;
//		session.setUpSequenceId(0);
//		session.setDownSequenceId(0);
	}

	public void writeAndFlush(InternalMessage msg)
	{
 		channel.writeAndFlush(msg);
	}

	public void writeAndClose(InternalMessage msg)
	{
		ChannelFuture f = channel.writeAndFlush(msg);
		f.addListener(futureListner);
	}
		
	private static ChannelFutureListener futureListner = new ChannelFutureListener()
	{
		@Override
		public void operationComplete(ChannelFuture future)
		{
			Channel ch = future.channel();
			ch.close().addListener(ChannelFutureListener.CLOSE);
		}
	};

	public void close()
	{
		logger.debug("RemoteNode{}.close be called.", this.channel.remoteAddress());
		this.channel.close();
	}
}
