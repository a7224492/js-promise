package com.kodgames.corgi.core.net.handler.netty;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

/**
 * 加密handler，利用异或进行最简单的加密
 * 此处无法判断协议ID，需要在MessageProcessor中激活新的key
 */
public class EncryptHandler extends ChannelDuplexHandler
{

	/**
	 * 判断协议类型，如果是更新key协议，在协议发送完毕之后，激活新的加密Key
	 */
	@Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
    {
/*		Connection connection = ctx.channel().attr(Connection.CONNECTIONINFO).get();
		if ((connection != null) && (msg instanceof ByteBuf) && connection.getEncryptCoder().hasEncryptKey())
		{
//			logger.error("----EncryptHandler write using key={}-", remoteNode.getEncryptCoder().getEncryptKey());
			//加密数据
			ByteBuf buf = (ByteBuf)msg;
			int length = buf.readableBytes();
			byte[] data = new byte[length];
			buf.getBytes(buf.readerIndex(), data, 0, length);
			
			connection.getEncryptCoder().encryptData(data, 0, data.length);
			ByteBuf outBuf = ctx.alloc().buffer();
			outBuf.writeBytes(data);
			
			ctx.write(outBuf, promise);
		}
		else
		{		
			ctx.write(msg, promise);
		}*/
    }

	/**
	 * 判断协议类型，如果是更新key应答协议，处理完毕协议后，激活新的解密key
	 */
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
/*		Connection connection = ctx.channel().attr(Connection.CONNECTIONINFO).get();
		if ((connection != null) && (msg instanceof ByteBuf) && connection.getEncryptCoder().hasDecryptKey())
		{
			//解密数据
			ByteBuf buf = (ByteBuf)msg;
			int length = buf.readableBytes();
			byte[] data = new byte[length];
			buf.getBytes(buf.readerIndex(), data, 0, length);
			
			connection.getEncryptCoder().decryptData(data, 0, data.length);
			ByteBuf outBuf = ctx.alloc().buffer();
			outBuf.writeBytes(data);
			
			ctx.fireChannelRead(outBuf);
		}
		else
		{		
			ctx.fireChannelRead(msg);
		}*/
    }
	
}
