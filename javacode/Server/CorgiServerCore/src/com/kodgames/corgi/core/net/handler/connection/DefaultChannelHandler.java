package com.kodgames.corgi.core.net.handler.connection;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.NettyNode;
import com.kodgames.corgi.core.net.handler.AbstractMessageInitializer;
import com.kodgames.corgi.core.net.handler.message.MessageDispatcher;

import io.netty.channel.Channel;

/**
 * 没有转发节点的连接，Channel等同于Conection
 */
public class DefaultChannelHandler extends AbstractChannelHandler
{
	AbstractMessageInitializer messageInitializer;
	private int connectionType;
	
	public DefaultChannelHandler(AbstractMessageInitializer messageInitializer, int connectionType)
	{
		this.messageInitializer = messageInitializer;
		this.connectionType = connectionType;
	}
	
	@Override
	public NettyNode attachChannel(Channel channel, int srcIP)
	{
		NettyNode remoteNode = new NettyNode(channel);
		
		Connection connection = new Connection(srcIP, remoteNode, connectionType);
	
		//此时将connection绑定到Channel
		channel.attr(Connection.CONNECTIONINFO).set(connection);
		
		return remoteNode;
	}
	
	@Override
	public void handleChannelActive(NettyNode node)
	{
		Connection connection = node.getChannel().attr(Connection.CONNECTIONINFO).get();
		MessageDispatcher.getInstance().connectionStatusDispatch(messageInitializer, connection, true);
	}

	@Override
	public void handleChannelInactive(NettyNode node)
	{
		Connection connection = node.getChannel().attr(Connection.CONNECTIONINFO).get();
		MessageDispatcher.getInstance().connectionStatusDispatch(messageInitializer, connection, false);		
	}
}
