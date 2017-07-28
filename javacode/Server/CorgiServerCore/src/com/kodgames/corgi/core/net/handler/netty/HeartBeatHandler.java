package com.kodgames.corgi.core.net.handler.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

//服务器可能处于一直发送消息，但是不需要客户端回复的状态，比如加载战斗
//所以单纯的依靠write_idle已经不可靠了，在read_idle时，不能直接断开，需要有缓冲
public class HeartBeatHandler extends ChannelDuplexHandler 
{
	private static final Logger logger = LoggerFactory.getLogger(HeartBeatHandler.class);
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception
	{
		if (evt instanceof IdleStateEvent)
		{
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state().equals(IdleState.READER_IDLE))
			{
				if ( event.isFirst() )
				{
					ByteBuf outBuf = ctx.alloc().buffer(2);
					outBuf.writeByte(MessageProcessor.MESSAGE_TYPE_HEARTBEAT);
					//Secret key
					outBuf.writeByte(0);
					ctx.writeAndFlush(outBuf);
				}
				else
				{
					ctx.channel().close();
				}
				//ctx.fireChannelInactive();
/*				SocketAddress client = ctx.channel().remoteAddress();
				Integer count = clientMaps.get(client);
				if (count == null)
				{
					//第一次idle不断开
					count = 1;
					clientMaps.put(client, count);
//					logger.debug("recv {} reader_idle then send ping cmd", ctx.channel().remoteAddress());
					ByteBuf outBuf = ctx.alloc().buffer(1);
	    			outBuf.writeByte(1);
	    			ctx.writeAndFlush(outBuf);
				}
				else
				{
					//第二次reader_idle断开连接
//					logger.warn("recv reader_idle {} then close channel", ctx.channel().remoteAddress());
					// do nothing 等待逻辑超时
					ctx.channel().close();
					clientMaps.remove(client);
				}*/


			}
			else if (event.state().equals(IdleState.WRITER_IDLE))
			{
				logger.warn("HeartBeat WRITER_IDLE but !!!not set!!! to {} local {}", ctx.channel().remoteAddress(), ctx.channel().localAddress());
			}
			else if (event.state().equals(IdleState.ALL_IDLE))
			{
				logger.warn("HeartBeat ALL_IDLE but !!!not set!!! to {} local {}", ctx.channel().remoteAddress(), ctx.channel().localAddress());
			}
		}
		
		//ctx.fireUserEventTriggered(evt);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		if (msg instanceof ByteBuf && ((ByteBuf)msg).readableBytes() >1)
		{
			ByteBuf inBuffer = (ByteBuf)msg;
			byte dataType = inBuffer.getByte(0);
			byte secretKey = inBuffer.getByte(1);
			//DO nothing
			if (dataType == MessageProcessor.MESSAGE_TYPE_HEARTBEAT)
			{
				ReferenceCountUtil.release(inBuffer);
				if (secretKey != MessageProcessor.MESSAGE_SECRET_KEY)
				{
					ctx.channel().close();
				}
			}
			else
			{
				ctx.fireChannelRead(msg);
			}
		}
		else
		{
			ctx.fireChannelRead(msg);
		}
	}

}
