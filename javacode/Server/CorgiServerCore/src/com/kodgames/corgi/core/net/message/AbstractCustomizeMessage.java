package com.kodgames.corgi.core.net.message;

import io.netty.buffer.ByteBuf;

public abstract class AbstractCustomizeMessage {
	protected int protocolID;
	public int getProtocolID(){
		return protocolID;
	}
	public void setProtocolID(int protocolID){
		this.protocolID = protocolID;
	}
	protected int callback;

	public int getCallback() {
		return callback;
	}
	public void setCallback(int callback) {
		this.callback = callback;
	}
	public abstract byte[] encode();
	public abstract void decode(ByteBuf buf);
	
}