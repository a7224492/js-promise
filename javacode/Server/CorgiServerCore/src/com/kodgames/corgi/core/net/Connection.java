package com.kodgames.corgi.core.net;

import com.google.protobuf.GeneratedMessage;
import com.kodgames.corgi.core.net.common.NettyNode;
import com.kodgames.corgi.core.net.handler.netty.MessageProcessor;
import com.kodgames.corgi.core.net.message.InternalMessage;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.corgi.core.session.Session;

import io.netty.util.AttributeKey;

/**
 * 逻辑意义上的连接
 * 比如Client与Battle的连接，实际上，Client并不直接与Battle交互，是通过Interface进行
 * 
 * 会作为Map的key使用的，但是没有重载hashcode和equal，不同的对象即不同连接
 */
public class Connection implements Comparable<Connection>
{

	public static final int CONNECTION_TYPE_CLIENT = 1;
	public static final int CONNECTION_TYPE_SERVER = 2;        //不区分Game和Battle
	public static final int CONNECTION_TYPE_INTERFACE_TRANSFER_CLIENT = 3;

	public static final AttributeKey<Connection> CONNECTIONINFO = AttributeKey.valueOf("Connection");

	private int connectionID;
	private int remotePeerID;        		//RoleID or ServerID
	private int remoteConnectionID;
	private int remotePeerIP;
	private int connectionType;
	private NettyNode remoteNode = null;
	private Connection transferConnectoin;  //used for connection with CONNECTION_TYPE_INTERFACE_TRANSFER_CLIENT

	private int roomID = 0;

	public Connection(int connectionId, NettyNode remote, int remotePeerIP)
	{
		this.setRemoteNode(remote);
		this.setConnectionID(connectionId);
		this.setRemotePeerIP(remotePeerIP);
	}


	public void write(int callback, int protocolID, byte[] buffer)
	{
		InternalMessage p = new InternalMessage();
		p.setMessage(buffer);
		p.setProtocolID(protocolID);
		p.setCallback(callback);
		p.setConnection(this);
		switch (connectionType)
		{
			case CONNECTION_TYPE_CLIENT:
				p.setMessageType(MessageProcessor.MESSAGE_TYPE_CLIENT);
				getRemoteNode().writeAndFlush(p);
				break;
			case CONNECTION_TYPE_SERVER:
				p.setMessageType(MessageProcessor.MESSAGE_TYPE_SERVER);
				getRemoteNode().writeAndFlush(p);
				break;
			case CONNECTION_TYPE_INTERFACE_TRANSFER_CLIENT:
				p.setMessageType(MessageProcessor.MESSAGE_TYPE_S2I);
				p.setClientIP(remotePeerID);
				p.setClientConnectionID(remoteConnectionID);
				transferConnectoin.getRemoteNode().writeAndFlush(p);
				break;
		}


	}

	public void write(int callback, int protocolID, byte[] buffer, int clientID, int clientConnectionID, int clientIP)
	{
		InternalMessage p = new InternalMessage();
		switch (connectionType)
		{
			case CONNECTION_TYPE_SERVER:
				p.setMessageType(MessageProcessor.MESSAGE_TYPE_I2S);
				break;
		}
		p.setMessage(buffer);
		p.setProtocolID(protocolID);
		p.setCallback(callback);
		p.setConnection(this);
		p.setClientID(clientID);
		p.setClientConnectionID(clientConnectionID);
		p.setClientIP(clientIP);

		getRemoteNode().writeAndFlush(p);
	}

	public void write(int callback, GeneratedMessage msg)
	{
		int protocolID = ConnectionManager.getInstance().getMsgInitializer().getProtocolID(msg.getClass());
		
		InternalMessage p = new InternalMessage();
		p.setMessage(msg);
		p.setProtocolID(protocolID);
		p.setCallback(callback);
		p.setConnection(this);

		switch (connectionType)
		{
			case CONNECTION_TYPE_CLIENT:
				p.setMessageType(MessageProcessor.MESSAGE_TYPE_CLIENT);
				remoteNode.writeAndFlush(p);
				break;
			case CONNECTION_TYPE_SERVER:
				p.setMessageType(MessageProcessor.MESSAGE_TYPE_SERVER);
				remoteNode.writeAndFlush(p);
				break;
			case CONNECTION_TYPE_INTERFACE_TRANSFER_CLIENT:
				p.setMessageType(MessageProcessor.MESSAGE_TYPE_S2I);
				p.setClientIP(remotePeerID);
				p.setClientConnectionID(remoteConnectionID);
				transferConnectoin.getRemoteNode().writeAndFlush(p);
				break;
		}
	}

	public void writeAndClose(int callback, GeneratedMessage msg)
	{
		int protocolID = ConnectionManager.getInstance().getMsgInitializer().getProtocolID(msg.getClass());
		
		InternalMessage p = new InternalMessage();
		p.setMessage(msg);
		p.setProtocolID(protocolID);
		p.setCallback(callback);
		p.setConnection(this);

		switch (connectionType)
		{
			case CONNECTION_TYPE_CLIENT:
				p.setMessageType(MessageProcessor.MESSAGE_TYPE_CLIENT);
				remoteNode.writeAndClose(p);
				break;
			case CONNECTION_TYPE_SERVER:
				p.setMessageType(MessageProcessor.MESSAGE_TYPE_SERVER);
				remoteNode.writeAndClose(p);
				break;
			case CONNECTION_TYPE_INTERFACE_TRANSFER_CLIENT:
				p.setMessageType(MessageProcessor.MESSAGE_TYPE_S2I);
				p.setClientIP(remotePeerID);
				p.setClientConnectionID(remoteConnectionID);
				transferConnectoin.getRemoteNode().writeAndClose(p);
				break;
		}
	}

	public void close()
	{
		remoteNode.close();
	}


	public NettyNode getNettyNode()
	{
		return remoteNode;
	}

	@Override
	public int compareTo(Connection other)
	{
		return this.getConnectionID() - other.getConnectionID();
	}

	@Override
	public String toString()
	{
		return String.format("ConnectionID:%d connectyonType:%d RemotePeerID:%d remoteIP:%d",
				getConnectionID(), getConnectionType(), getRemotePeerID(), getRemotePeerIP());
	}

	public int getConnectionID() {
		return connectionID;
	}

	public void setConnectionID(int connectionID) {
		this.connectionID = connectionID;
	}

	public int getRemotePeerID() {
		return remotePeerID;
	}

	public void setRemotePeerID(int remotePeerID) {
		this.remotePeerID = remotePeerID;
	}

	public int getRemotePeerIP() {
		// 注意:
        // 当使用透明代理时, InterfaceServer使用该方法返回的地址为代理服务器地址
        // 如果需要正确的客户端地址, 使用getRemoteNode().getAddress().getHostString()
		// 在其他Server上使用该方法还是正确的客户端地址, InterfaceServer会传递正确的客户端地址给其他服务器
		return remotePeerIP;
	}

	public void setRemotePeerIP(int remotePeerIP) {
		this.remotePeerIP = remotePeerIP;
	}

	public int getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(int connectionType){ this.connectionType = connectionType;}

	public NettyNode getRemoteNode() {
		return remoteNode;
	}

	public void setRemoteNode(NettyNode remoteNode) {
		this.remoteNode = remoteNode;
	}

	public Connection getTransferConnectoin() {
		return transferConnectoin;
	}

	public void setTransferConnectoin(Connection transferConnectoin) {
		this.transferConnectoin = transferConnectoin;
	}

	public void setSession(Session session)
	{

	}

	public Session getSession()
	{
		return null;
	}

	public int getRemoteConnectionID() {
		return remoteConnectionID;
	}

	public void setRemoteConnectionID(int remoteConnectionID) {
		this.remoteConnectionID = remoteConnectionID;
	}

	public int getRoomID() {
		return roomID;
	}

	public void setRoomID(int roomID) {
		this.roomID = roomID;
	}

	public long getMixID()
	{
		long mixID = (( 0xffffffffffffffffL & transferConnectoin.getRemotePeerID()) << 32) + remoteConnectionID;
		return mixID;
	}
}
