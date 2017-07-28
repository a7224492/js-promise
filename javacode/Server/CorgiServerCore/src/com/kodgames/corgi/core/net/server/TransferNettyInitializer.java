package com.kodgames.corgi.core.net.server;


import java.util.concurrent.atomic.AtomicInteger;

import com.kodgames.corgi.core.net.NetParameter;
import com.kodgames.corgi.core.net.handler.AbstractMessageInitializer;
import com.kodgames.corgi.core.net.handler.netty.HeartBeatHandler;
import com.kodgames.corgi.core.net.handler.netty.MessageProcessor;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;


public class TransferNettyInitializer extends ChannelInitializer<Channel>
{
	public static final int INITIALIZER_TYPE_CLIENT = 1;
	public static final int INITIALIZER_TYPE_SERVER = 2;
	
	static AtomicInteger handlerId=new AtomicInteger(0);
	
	AbstractMessageInitializer messageInitializer;
	
	private int type;

	public TransferNettyInitializer(AbstractMessageInitializer messageInitializer, int type)
	{
		this.messageInitializer = messageInitializer;
		this.type = type;
	}

	@Override
	protected void initChannel(Channel ch) throws Exception
	{
		ChannelPipeline p = ch.pipeline();
		String nameId = "-" + handlerId.incrementAndGet();

		// length excludes the lenghFieldlength and the data decoded exclude the length field
		p.addLast("FrameDecoder" + nameId, new LengthFieldBasedFrameDecoder(204800, 0, 4, 0, 4));
		p.addLast("LengthFieldPrepender" + nameId, new LengthFieldPrepender(4, false));
		p.addLast("IdleHandler", new IdleStateHandler(NetParameter.SSHeartHalfTimeout, 0, 0));
        p.addLast("HeartBeatHandler", new HeartBeatHandler());
/*        int handlerType = TransferHandler.TRANSFER_TYPE_IS;
        if (this.type == INITIALIZER_TYPE_CLIENT)
        {
        	handlerType = TransferHandler.TRANSFER_TYPE_CI;
        }
		p.addLast("TransferHandler" + nameId, new TransferHandler(messageInitializer, handlerType));*/
		p.addLast("MessageProcessor" + nameId, new MessageProcessor(messageInitializer));
	}
}
