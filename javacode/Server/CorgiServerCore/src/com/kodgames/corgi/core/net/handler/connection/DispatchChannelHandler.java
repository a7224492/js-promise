package com.kodgames.corgi.core.net.handler.connection;

import com.kodgames.corgi.core.net.common.NettyNode;

import io.netty.channel.Channel;

/**
 * 有转发的ChannelHander，只实现AttachChannel
 * 在Active和Inactive时，不创建对应的Connection，但需要维护NettyNode
 * 
 */
public class DispatchChannelHandler extends AbstractChannelHandler
{
	@Override
	public NettyNode attachChannel(Channel channel, int srcIp)
	{
		NettyNode remoteNode = new NettyNode(channel);
		return remoteNode;
	}

	@Override
	public void handleChannelActive(NettyNode node)
	{		
	}

	@Override
	public void handleChannelInactive(NettyNode node)
	{
	}

}
