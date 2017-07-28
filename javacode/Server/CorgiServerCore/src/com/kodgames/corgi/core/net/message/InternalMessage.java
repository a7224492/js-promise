package com.kodgames.corgi.core.net.message;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.handler.netty.MessageProcessor;

/**
 * @author mrui
 */
public class InternalMessage
{
	private int protocolID;
	private int callback;
	private Connection connection;
	private int clientID;  //used for servers-->IS--->client  or  client-->IS---->servers
	private int clientConnectionID;  //used for servers-->IS--->client or  client-->IS---->servers
	private int clientIP; //used for servers-->IS--->client  or  client-->IS---->servers
	private byte messageType = MessageProcessor.CODEC_TYPE_NONE;  //used for message is buffer
	private int dstPeerID; //used for client-->IS---->servers
	private int channelId; // udp使用的channelId

	private Object message;
	boolean urgency = false;

	public int getProtocolID()
	{
		return protocolID;
	}

	public void setProtocolID(int protocolID)
	{
		this.protocolID = protocolID;
	}

	public int getCallback()
	{
		return callback;
	}

	public void setCallback(int callback)
	{
		this.callback = callback;
	}

	public Connection getConnection()
	{
		return connection;
	}

	public void setConnection(Connection connection)
	{
		this.connection = connection;
	}

	public int getChannelId()
	{
		return channelId;
	}

	public void setChannelId(int channelId)
	{
		this.channelId = channelId;
	}

	public Object getMessage()
	{
		return message;
	}

	public void setMessage(Object message)
	{
		this.message = message;
	}

	public boolean isUrgency()
	{
		return urgency;
	}

	public void setUrgency(boolean urgency)
	{
		this.urgency = urgency;
	}

	public int getClientID() {
		return clientID;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}

	public int getClientConnectionID() {
		return clientConnectionID;
	}

	public void setClientConnectionID(int clientConnectionID) {
		this.clientConnectionID = clientConnectionID;
	}

	public int getClientIP() {
		return clientIP;
	}

	public void setClientIP(int clientIP) {
		this.clientIP = clientIP;
	}

	public int getDstPeerID() {
		return dstPeerID;
	}

	public void setDstPeerID(int dstPeerID) {
		this.dstPeerID = dstPeerID;
	}

	public byte getMessageType() {
		return messageType;
	}

	public void setMessageType(byte messageType) {
		this.messageType = messageType;
	}
}
