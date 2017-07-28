package com.kodgames.corgi.core.net.handler.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.snappy.Snappy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;
@Sharable
public class SnappyFramedCodec extends ChannelDuplexHandler{
	private static final Logger logger = LoggerFactory.getLogger(SnappyFramedCodec.class);
	final static byte UNCOMPRESSED = 0;
	final static byte COMPRESSED = 1;
	private int minCompressibleLength;

	private static long appSendSize = 0;
	private static long appReceiveSize = 0;
		
	public SnappyFramedCodec(int minCompressibleLength)
	{
		this.minCompressibleLength = minCompressibleLength;
	}
	
	public SnappyFramedCodec()
	{
		this(500);
	}

	@Override	
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		if (msg instanceof ByteBuf)
		{
			ByteBuf inBuffer = (ByteBuf)msg;

			SnappyFramedCodec.appReceiveSize += inBuffer.readableBytes();
			byte dataType = inBuffer.readByte();
			if (dataType == UNCOMPRESSED)
			{
				ctx.fireChannelRead(msg);
			}
			else if (dataType == COMPRESSED)
			{
				ByteBuf outBuffer = ctx.alloc().buffer();
				byte[] inData;
				if (inBuffer.hasArray())
				{
					inData = inBuffer.array();
				}
				else
				{
					inData = new byte[inBuffer.readableBytes()];
					inBuffer.getBytes(inBuffer.readerIndex(), inData);
				}

				byte[] outData = Snappy.uncompress(inData, 0, inData.length);
				outBuffer.writeBytes(outData);

				ctx.fireChannelRead(outBuffer);
				ReferenceCountUtil.release(msg);
			}
			else
			{
				logger.error("SnappyFrame.channelRead found Illegal message format: error dataType {}", dataType);
				return;
			}
		}
		else
		{
			logger.error("SnappyFrame.channelRead found Illegal message format: expected byteBuf but {}", msg.getClass());
		}
	}
	
	@Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
	{
		if (msg instanceof ByteBuf)
		{
			ByteBuf inBuf = (ByteBuf)msg;
			ByteBuf outBuffer = ctx.alloc().buffer();
			
			if (inBuf.readableBytes() > minCompressibleLength )
			{
				outBuffer.writeByte(COMPRESSED);
				byte[] inData;
				if (inBuf.hasArray())
				{
        			inData = inBuf.array();
        		}
        		else
        		{
        			inData = new byte[inBuf.readableBytes()];
        			inBuf.getBytes(inBuf.readerIndex(), inData);
        		}
				
				byte[] outData = Snappy.compress(inData);
				outBuffer.writeBytes(outData);
			}
			else 
			{
				outBuffer.writeByte(UNCOMPRESSED);
				outBuffer.writeBytes(inBuf, inBuf.readableBytes());
			}
			if (logger.isTraceEnabled())
			{
				String log = String.format("data : ");
				byte[] data;
				if (outBuffer.hasArray())
				{
					data = outBuffer.array();
				}
				else
				{
					data = new byte[outBuffer.readableBytes()];
					outBuffer.getBytes(0, data);
				}
				for (int i = 0; i < outBuffer.readableBytes(); i++)
				{
					log += String.format(" %02x", data[i]);
				}
				logger.trace(log);
			}
			
			ctx.write(outBuffer,promise);
			SnappyFramedCodec.appSendSize += outBuffer.readableBytes();
			ReferenceCountUtil.release(inBuf);
		}
		else
		{
			ctx.write(msg, promise);
        }
    }
	
	public static long getAppReceiveSize()
	{
		return SnappyFramedCodec.appReceiveSize;
	}

	public static long getAppSendSize()
	{
		return SnappyFramedCodec.appSendSize;
	}
}
