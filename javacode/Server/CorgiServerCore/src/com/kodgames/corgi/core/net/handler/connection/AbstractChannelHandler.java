package com.kodgames.corgi.core.net.handler.connection;

import com.kodgames.corgi.core.net.common.NettyNode;

import io.netty.channel.Channel;

public abstract class AbstractChannelHandler
{
	/**
	 * 在ChannelActive时，附加数据到Channel中
	 * @param node
	 */
	public abstract NettyNode attachChannel(Channel channel, int srcIp);
	
	public abstract void handleChannelActive(NettyNode node);

	public abstract void handleChannelInactive(NettyNode node);

	public Object getMessageKey(NettyNode node)
	{
		return node.getKey();
	}
}