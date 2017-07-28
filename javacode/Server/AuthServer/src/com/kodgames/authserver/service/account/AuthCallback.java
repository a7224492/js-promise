package com.kodgames.authserver.service.account;

import com.kodgames.corgi.core.net.Connection;

public class AuthCallback
{
	private String platform;
	private int appCode;
	private String proVersion;
	private String libVersion;
	private Connection interfaceConnection;
	private int clientConectionId;
	private int clientcallback;
	
	/**
	 * 设备id
	 */
	private String deviceId;

	public AuthCallback(String platform, int appCode, String proVersion, String libVersion, Connection interfaceConnection,
		int clientConectionId, int clientCallback, String deviceId)
	{
		this.platform = platform;
		this.appCode = appCode;
		this.proVersion = proVersion;
		this.libVersion = libVersion;
		this.interfaceConnection = interfaceConnection;
		this.clientConectionId = clientConectionId;
		this.clientcallback = clientCallback;
		this.deviceId = deviceId;
	}

	public String getPlatform()
	{
		return platform;
	}

	public String getProVersion()
	{
		return proVersion;
	}

	public String getLibVersion()
	{
		return libVersion;
	}

	public Connection getInterfaceConnection()
	{
		return interfaceConnection;
	}

	public int getClientConnectionId()
	{
		return clientConectionId;
	}

	public int getClientCallback()
	{
		return clientcallback;
	}
	
	public int getAppCode()
	{
		return appCode;
	}
	
	public String getDeviceId()
	{
		return deviceId;
	}
}
