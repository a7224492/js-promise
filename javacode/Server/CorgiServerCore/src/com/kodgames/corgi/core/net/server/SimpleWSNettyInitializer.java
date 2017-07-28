package com.kodgames.corgi.core.net.server;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.kodgames.corgi.core.net.handler.AbstractMessageInitializer;
import com.kodgames.corgi.core.net.handler.netty.HeartBeatHandler;
import com.kodgames.corgi.core.net.handler.netty.MessageProcessor;
//import com.kodgames.corgi.core.net.handler.netty.TransferHandler;
import com.kodgames.corgi.core.net.handler.netty.WebSocketHandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import com.kodgames.corgi.core.net.haproxy.HAProxyDecoder;

public class SimpleWSNettyInitializer extends ChannelInitializer<Channel>
{

	static AtomicInteger handlerId = new AtomicInteger(0);

	AbstractMessageInitializer messageInitializer;
	MessageProcessor messageProcessor;
	
	public SimpleWSNettyInitializer(AbstractMessageInitializer messageInitializer)
	{

		
		this.messageInitializer = messageInitializer;
		this.messageProcessor = new MessageProcessor(this.messageInitializer);
	}

	@Override
	protected void initChannel(Channel ch)
		throws Exception
	{
		ChannelPipeline p = ch.pipeline();
		String nameId = "-" + handlerId.incrementAndGet();
		// p.addFirst("HAProxyMessageDecoder" + nameId, haProxyMessageDecoder);
		p.addLast("HAProxyDecoder" + nameId, new HAProxyDecoder());
		p.addLast("http-codec" + nameId, new HttpServerCodec());
		p.addLast("http-chunked" + nameId, new ChunkedWriteHandler());
		p.addLast("aggregator" + nameId, new HttpObjectAggregator(65536));

		p.addLast("wsprotocol" + nameId, new WebSocketServerProtocolHandler(""));

		p.addLast("websocketHandler" + nameId, new WebSocketHandler());
		p.addLast("IdleStateHandler" + nameId, new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
		p.addLast("heartBeatHanlder" + nameId, new HeartBeatHandler());
		// p.addLast("TransferHandler" + nameId, new TransferHandler(messageInitializer,
		// TransferHandler.TRANSFER_TYPE_CI));
		p.addLast("messageProcessor" + nameId, messageProcessor);
	}
}
