package com.kodgames.corgi.core.net.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.SocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.handler.AbstractMessageInitializer;
import com.kodgames.corgi.core.net.message.AbstractCustomizeMessage;

public class SimpleClient
{
	private static final Logger logger = LoggerFactory.getLogger(SimpleClient.class);
	Bootstrap clientBootstrap;
	ChannelInitializer<Channel> channelInitializer;
	AbstractMessageInitializer messageInitializer;
	Channel channel;

	public void initialize(ChannelInitializer<Channel> channelInitializer, AbstractMessageInitializer messageInitializer)
	{
		this.channelInitializer = channelInitializer;
		this.messageInitializer = messageInitializer;
		initializeNetty();
	};

	protected void initializeNetty()
	{
		EventLoopGroup workerGroupClient = new NioEventLoopGroup();
		clientBootstrap = new Bootstrap();
		clientBootstrap.group(workerGroupClient).channel(NioSocketChannel.class).handler(channelInitializer);
		clientBootstrap.option(ChannelOption.SO_KEEPALIVE, true);
		clientBootstrap.option(ChannelOption.TCP_NODELAY, true);
		clientBootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000); // 10s
	};

	public boolean connectTo(SocketAddress remoteAddress, int retryTimes)
	{
		if (clientBootstrap == null)
		{
			throw new NullPointerException("clientBootStrap");
		}

		ChannelFuture future = null;
		boolean result = false;

		for (int connectNum = 0; connectNum <= retryTimes; connectNum++)
		{
			if (connectNum > 0)
			{
				try
				{
					Thread.sleep(1000 * 2);
				}
				catch (InterruptedException e)
				{
					logger.error("exception:", e);
				}
				logger.warn("Retry to connect to {}, {} times", remoteAddress.toString(), connectNum);
			}

			try
			{
				future = clientBootstrap.connect(remoteAddress).sync();
			}
			catch (Exception e1)
			{
				logger.warn("Failed to connect to {} {}", remoteAddress, e1.getMessage());
				continue;
			}

			if (future.isSuccess())
			{
				result = true;
				channel = future.channel();
				logger.info("Connect to {} success", remoteAddress.toString());
				break;
			}
		}

		if (result == false)
		{
			logger.error("After retry {} times, still can't connect to {}!!!", retryTimes, remoteAddress.toString());
		}

		return result;
	}

	public boolean sendMessage(AbstractCustomizeMessage msg)
	{
		if (channel == null)
		{
			logger.error("client channel not init");
			return false;
		}

		if (!channel.isActive())
		{
			logger.error("client channel is unconnected");
			return false;
		}

		this.channel.writeAndFlush(msg);
		return true;
	}

	public boolean isConnected()
	{
		if (channel == null)
			return false;
		return channel.isActive();
	}
	
	public void close()
	{
		channel.close().awaitUninterruptibly();
	}
}
