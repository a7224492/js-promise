package com.kodgames.corgi.core.net.handler.netty;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.GeneratedMessage;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.NettyNode;
import com.kodgames.corgi.core.net.handler.AbstractMessageInitializer;
import com.kodgames.corgi.core.net.handler.message.AbstractByteArrayHandler;
import com.kodgames.corgi.core.net.handler.message.AbstractCustomizeMessageHandler;
import com.kodgames.corgi.core.net.handler.message.AbstractProtobufMessageHandler;
import com.kodgames.corgi.core.net.handler.message.BaseMessageHandler;
import com.kodgames.corgi.core.net.handler.message.MessageDispatcher;
import com.kodgames.corgi.core.net.message.AbstractCustomizeMessage;
import com.kodgames.corgi.core.net.message.InternalMessage;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.corgi.core.util.IPUtils;

@Sharable
public class MessageProcessor extends ChannelDuplexHandler
{
	private static final Logger logger = LoggerFactory.getLogger(MessageProcessor.class);

	public static final byte MESSAGE_TYPE_CLIENT = 0x01;
	public static final byte MESSAGE_TYPE_SERVER = 0x02;
	public static final byte MESSAGE_TYPE_I2S = 0x03;
	public static final byte MESSAGE_TYPE_S2I = 0x04;
	public static final byte MESSAGE_TYPE_HEARTBEAT = 0x05;
	public static final byte CODEC_TYPE_NONE = 0x00;
	//public static final byte CODEC_TYPE_SNAPPY = 0x10;
	public static final byte MESSAGE_SECRET_KEY = 0x4a;

	
	public static final int MESSAGE_SENDBUFFER_DEFAULTSIZE = 256;
	AbstractMessageInitializer messageInitializer;

	public MessageProcessor(AbstractMessageInitializer messageInitializer)
	{
		this.messageInitializer = messageInitializer;
	}

	private byte[] ByteBuf2ByteArray(ByteBuf buf)
	{
		byte[] array;
		final int length = buf.readableBytes();
		array = new byte[length];
		buf.getBytes(buf.readerIndex(), array, 0, length);
		return array;
	}	

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		if (!ByteBuf.class.isAssignableFrom(msg.getClass()))
		{
			ctx.fireChannelRead(msg);
			return;
		}
		
		try
		{
			//C2I  MESSAGE_TYPE_CLIENT
			//messageType(1byte)+secretKey(1byte)+dstServerID(4byte)+protocolID(4byte)+callback(4byte)+data
			//MESSAGE_TYPE_I2S
			//messageType(1byte)+clientID(4bytes)+connectionIDInIS(4byte)+clientIP(4bytes)+protocolID(4byte)+callback(4byte)+data
			//MESSAGE_TYPE_S2I
			//messageType(1byte)+connectionIDInIS(4byte)+protocolID(4byte)+callback(4byte)+data
			//S2S  MESSAGE_TYPE_SERVER
			//messageType(1byte)+protocolID(4byte)+callback(4byte)+data
			ByteBuf buf = (ByteBuf)msg;
			int len = buf.readableBytes();

			if (len < 1+4+4)	//messageType+protocolID+callback
			{
				logger.error("MessageProcessor.channelRead found error msg len {}", len);
				return;
			}
		
			Connection connection = ctx.channel().attr(Connection.CONNECTIONINFO).get();
			NettyNode remoteNode = connection.getNettyNode();
			
			byte messageType = buf.readByte();
			InternalMessage corgiMsg = new InternalMessage();

			corgiMsg.setConnection(connection);
			corgiMsg.setMessageType(messageType);
			if((messageType & 0x0F) == MESSAGE_TYPE_CLIENT)
			{
				byte secretKey = buf.readByte();
				if (secretKey != MESSAGE_SECRET_KEY)
				{
					ctx.channel().close();
				}
				else
				{
					int dstPeerID = buf.readInt();
					corgiMsg.setDstPeerID(dstPeerID);
				}
			}
			else if ((messageType & 0x0f) == MESSAGE_TYPE_S2I)
			{
				int clientConnectionID = buf.readInt();
				corgiMsg.setClientConnectionID(clientConnectionID);
			}
			else if ((messageType & 0x0f) == MESSAGE_TYPE_I2S)
			{
				int clientID = buf.readInt();
				int clientConnectionID = buf.readInt();
				int clientIP = buf.readInt();

				if (clientID == 0) {
					logger.error("invalid connection. clientID == 0 ip: {}, clientConnectionID: {}", clientIP, clientConnectionID);
					return;
				}

				corgiMsg.setClientID(clientID);
				corgiMsg.setClientIP(clientIP);

				Connection clientVirtualConnection = ConnectionManager.getInstance().getClientVirtualConnection(clientID);

				if (clientVirtualConnection == null || connection != clientVirtualConnection.getTransferConnectoin() || clientVirtualConnection.getRemoteConnectionID() != clientConnectionID)
				{
					clientVirtualConnection = new Connection(ConnectionManager.getInstance().generateConnectionID(), remoteNode, clientIP);
					clientVirtualConnection.setConnectionType(Connection.CONNECTION_TYPE_INTERFACE_TRANSFER_CLIENT);
					clientVirtualConnection.setRemoteConnectionID(clientConnectionID);
					clientVirtualConnection.setRemotePeerID(clientID);
					clientVirtualConnection.setRemotePeerIP(clientIP);
					clientVirtualConnection.setTransferConnectoin(connection);
					ConnectionManager.getInstance().addConnection(clientVirtualConnection);
					logger.debug("active: create virtual client connection, connectionID:{}, clientID:{}", clientVirtualConnection.getConnectionID(), clientID);
				}
				corgiMsg.setConnection(clientVirtualConnection);
				corgiMsg.setClientConnectionID(clientVirtualConnection.getConnectionID());

			}

			buf.markReaderIndex();
			int protocolID = buf.readInt();
			//判断是否为加密key更新协议
//			if (protocolID == ProtocolsConfig.P_CG_USE_NEW_KEY_RES)
//			{
//				//收到客户单加密key的应答消息，标识该消息后续消息将启用新key解密
//				connection.getEncryptCoder().decryptUseNewKey();
//				//其实后续都不需要走协议更新流程
//			}
			
			int callback = buf.readInt();
			corgiMsg.setProtocolID(protocolID);
			corgiMsg.setCallback(callback);

			
			if ((remoteNode != null))
				remoteNode.incRecvAmount();

			BaseMessageHandler<?> handler = messageInitializer.getMessageHandler(protocolID);
			if (handler == null)
			{
				throw new Exception("Illegal protocolID:" + protocolID + ", Can't find corresponding Protol Handler. clientRemoteID: " + connection.getRemotePeerID());
			}

			if (handler instanceof AbstractCustomizeMessageHandler)
			{
				Class<?> msgClass = messageInitializer.getMessageClass(protocolID);
				AbstractCustomizeMessage message =
					msgClass == null ? null : ((AbstractCustomizeMessage)msgClass.newInstance());

				if (message != null)
				{
					message.decode(buf);
				}
				else
				{
					logger.error("channelRead found message null protocolID {}:{}",
						protocolID,
						Integer.toHexString(protocolID));
				}
				corgiMsg.setMessage(message);
			}
			else if (handler instanceof AbstractProtobufMessageHandler)
			{
				Class<?> msgClass = messageInitializer.getMessageClass(protocolID);
				if (msgClass != null)
				{
					Method method = msgClass.getDeclaredMethod("parseFrom", byte[].class);
					if (method != null)
					{
						Object obj = method.invoke(null, ByteBuf2ByteArray(buf));
						corgiMsg.setMessage(obj);
					}
					else
					{
						throw new Exception("Illegal protocolID:" + Integer.toHexString(protocolID) + ", found class but Can't find parseFrom method. clientRemoteID: " + connection.getRemotePeerID());
					}
				}
				else
				{
					throw new Exception("Illegal protocolID:" + Integer.toHexString(protocolID) + ", Can't find corresponding Protobuf class. clientRemoteID: " + connection.getRemotePeerID());
				}
			}
			else if (handler instanceof AbstractByteArrayHandler)
			{
				//buf.resetReaderIndex();
				corgiMsg.setMessage(ByteBuf2ByteArray(buf));
			}
			else
			{
				throw new Exception("Illegal MsgHandlerType, Please set correct msg type in MessageInitializer.");
			}

			logger.debug("read: protocol id is {}:{}, ip address {}, connectionid {} len {} recvAmount {}",
					protocolID,
					Integer.toHexString(protocolID),
					remoteNode.getAddress(),
					corgiMsg.getConnection().getConnectionID(),
					len,
					remoteNode.getRecvAmount());

			MessageDispatcher.getInstance().dispatchMessage(messageInitializer, corgiMsg);
		}
		catch (Throwable e)
		{
			logger.error("channel " + ctx.channel().remoteAddress() + " read error:", e);
			throw e;
		}
		finally
		{
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
	{
		if (msg instanceof InternalMessage)
		{
			byte[] buf = null;
			InternalMessage internalMsg = (InternalMessage) msg;
			int protocolID = internalMsg.getProtocolID();			
			int callback = internalMsg.getCallback();
			if (internalMsg.getMessage() instanceof AbstractCustomizeMessage)
			{
				AbstractCustomizeMessage message = (AbstractCustomizeMessage) internalMsg.getMessage();
				buf = message.encode();
			}
			else if (internalMsg.getMessage() instanceof GeneratedMessage)
			{
				GeneratedMessage protobufMsg = (GeneratedMessage) internalMsg.getMessage();
				buf = protobufMsg.toByteArray();
			}
			else if (internalMsg.getMessage() instanceof byte[])
			{
				buf = (byte[]) internalMsg.getMessage();
			}

			int length = buf == null ? 9 : buf.length + 9;   //messageType(1)+protocolID(4)+callback(4)
			ByteBuf outBuffer = null;// = ctx.alloc().buffer(length);


			try
			{
				byte messageType = internalMsg.getMessageType();

				if ((messageType & 0x0f) == MESSAGE_TYPE_S2I)
				{
					length = length + 4;
					outBuffer = ctx.alloc().buffer(length);
					outBuffer.writeByte(messageType);
					outBuffer.writeInt(internalMsg.getClientConnectionID());
				}
				else if ((messageType & 0x0f) == MESSAGE_TYPE_I2S)
				{
					length = length + 4 + 4 + 4;
					outBuffer = ctx.alloc().buffer(length);
					outBuffer.writeByte(messageType);
					outBuffer.writeInt(internalMsg.getClientID());
					outBuffer.writeInt(internalMsg.getClientConnectionID());
					outBuffer.writeInt(internalMsg.getClientIP());
				}
				else
				{
					outBuffer = ctx.alloc().buffer(length);
					outBuffer.writeByte(messageType);
				}

				Connection connection = internalMsg.getConnection();
				NettyNode node = connection.getNettyNode();
				
				node.incSendAmount();

				outBuffer.writeInt(protocolID);
				outBuffer.writeInt(callback);
				if (buf != null)
				{
					outBuffer.writeBytes(buf);
				}
				ctx.write(outBuffer, promise);
				
				logger.debug("write: protocol id is {}:{}, ip address {}, connectionid {} connection {} len {} sendAmount {}",
					protocolID,
					Integer.toHexString(protocolID),
					node.getAddress(),
					connection.getConnectionID(),
					connection.hashCode(),
					buf.length,
					node.getSendAmount());
				
				//协议发送完毕后，启用新的加密key，
//				if (protocolID == ProtocolsConfig.P_GC_USE_NEW_KEY_RES)
//				{
//					//后续启用新的加密key
//					connection.getEncryptCoder().encryptUseNewKey();
//				}				
			}
			catch (Exception e)
			{
				if (outBuffer != null)
					ReferenceCountUtil.release(outBuffer);
				logger.error("channel write error: " + e.toString());
				throw e;
			}
			catch (Throwable t)
			{
				if (outBuffer != null)
					ReferenceCountUtil.release(outBuffer);
				logger.error("channel write error: " + t.toString());
				throw t;
			}
		}
		else
		{
			ctx.write(msg, promise);
		}
	}

	/**
	 * Interface上，Client连接成功后，可以Active
	 * Game和Battle上由Interface通知Client的Active和Inactive
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception
	{
		InetSocketAddress remoteAddress = (InetSocketAddress)ctx.channel().remoteAddress();
		NettyNode remoteNode = new NettyNode(ctx.channel());//messageInitializer.getChannelHandler().attachChannel(ctx.channel(), IPUtils.ipToInt(remoteAddress.getHostString()));
		Connection connection = new Connection(ConnectionManager.getInstance().generateConnectionID(), remoteNode, IPUtils.ipToInt(remoteAddress.getHostString()));
		ConnectionManager.getInstance().addConnection(connection);
		ctx.channel().attr(Connection.CONNECTIONINFO).set(connection);
		logger.debug("Active  connectionID:{}", connection.getConnectionID());
		MessageDispatcher.getInstance().connectionStatusDispatch(messageInitializer, connection, true);
	}

	/**
	 * Calls {@link ChannelHandlerContext#fireChannelInactive()} to forward to the next
	 * {@link ChannelInboundHandler} in the {@link ChannelPipeline}. Sub-classes may override this
	 * method to change behavior.
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception
	{
		//此处没有利用channelStatusDispatch，因为可以直接关联到connection
		//需要断开所有Client连接
		Connection connection = ctx.channel().attr(Connection.CONNECTIONINFO).get();
		ConnectionManager.getInstance().removeConnection(connection);
		logger.debug("Inactive connetionID:{}", connection.getConnectionID());
		MessageDispatcher.getInstance().connectionStatusDispatch(messageInitializer, connection, false);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		if (IOException.class.isAssignableFrom(cause.getClass()))
		{
			// 猜想是远程连接关
			logger.debug("exceptionCaught--Remote Peer:{} {}", ctx.channel().remoteAddress(), cause.toString());
		}
		else
		{
			logger.error("exceptionCaught {} --Exeption: {}", ctx.channel().remoteAddress(), cause.toString());
		}
	}
}
