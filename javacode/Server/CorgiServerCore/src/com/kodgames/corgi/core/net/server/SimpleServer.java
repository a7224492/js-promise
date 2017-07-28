package com.kodgames.corgi.core.net.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.handler.AbstractMessageInitializer;

public class SimpleServer
{
	private static final Logger logger = LoggerFactory.getLogger(SimpleServer.class);
	ServerBootstrap serverBootstrap;

	ChannelInitializer<Channel> channelInitializer;
	AbstractMessageInitializer messageInitializer;
	private Map<Integer, ChannelFuture> portMap = new HashMap<Integer, ChannelFuture>();

	public void initialize(ChannelInitializer<Channel> channelInitializer, AbstractMessageInitializer messageInitializer)
	{
		this.channelInitializer = channelInitializer;
		this.messageInitializer = messageInitializer;
		initializeNetty();
		logger.info("Success to initialize SimpleServer!");
	};

	protected void initializeNetty()
	{
		EventLoopGroup bossGroup = new NioEventLoopGroup(2);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(bossGroup, workerGroup);
		serverBootstrap.channel(NioServerSocketChannel.class);
		// serverBootstrap.handler(new LoggingHandler(LogLevel.DEBUG));
		serverBootstrap.childHandler(channelInitializer);

		serverBootstrap.option(ChannelOption.SO_KEEPALIVE, true);
		serverBootstrap.option(ChannelOption.TCP_NODELAY, true);
		serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);
		//serverBootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10 * 000); // 10s
		serverBootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 24 * 3600 * 1000); // 24h
	};

	public boolean openPort(InetSocketAddress localAddress)
	{
		if (serverBootstrap == null)
			throw new NullPointerException("serverBootstap");

		boolean result = false;
		try
		{
			ChannelFuture future = serverBootstrap.bind(localAddress);
			portMap.put(localAddress.getPort(), future);
			result = future.sync().isSuccess();
		}
		catch (InterruptedException e)
		{
			result = false;
			logger.error("Failed to bind local address:" + localAddress, e);
		}

		return result;
	}
	
	public int closePort(InetSocketAddress localAddress)
	{
		if(!portMap.containsKey(localAddress.getPort()))
			return -1;
		
		ChannelFuture future = portMap.get(localAddress.getPort());
		future.channel().close();
		future.channel().closeFuture();
		return 0;
	}
}
