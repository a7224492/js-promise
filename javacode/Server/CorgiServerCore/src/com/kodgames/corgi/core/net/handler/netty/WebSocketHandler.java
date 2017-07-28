package com.kodgames.corgi.core.net.handler.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class WebSocketHandler extends ChannelDuplexHandler
{

	public WebSocketHandler()
	{
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
		throws Exception
	{
		if (msg instanceof BinaryWebSocketFrame)
		{
			BinaryWebSocketFrame inBuffer = (BinaryWebSocketFrame)msg;
			ctx.fireChannelRead(inBuffer.content());
		}
		else
		{
			ctx.fireChannelRead(((WebSocketFrame)msg).content());
		}
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
		throws Exception
	{
		if (msg instanceof ByteBuf)
		{
			ByteBuf inBuf = (ByteBuf)msg;
			ctx.write(new BinaryWebSocketFrame(inBuf), promise);
		}
		else
		{
			ctx.write(msg, promise);
		}
	}
}
