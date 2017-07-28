package com.kodgames.corgi.core.session;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.GeneratedMessage;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.handler.AbstractMessageInitializer;

/**
 * 有InterfaceServer和没有InterfaceServer时，SessionService的作用不同
 * 有InterfaceServer时，Interface上需要有SessionService，Game和Battle上也需要SessionService Interface上维护Client、Battle、Game三者的session
 * Battle和Game维护Client的Session 服务器之间不需要断线重连，服务器重启后，注册到Manager，Manager推送给相关服务器
 */
public class ConnectionManager
{
	private Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
	private static ConnectionManager instance = new ConnectionManager();

	private int localPeerID = 0;
	private InetSocketAddress managerServerAddress = null;

	private AtomicInteger connectionIDSeed = new AtomicInteger(10001);

	private AbstractMessageInitializer msgInitializer;

	private Map<Integer, Connection> connections = new ConcurrentHashMap<Integer, Connection>();
	//key is remotePeerID for connection with CONNECTION_TYPE_SERVER
	private Map<Integer, Connection> serverConnections = new ConcurrentHashMap<Integer, Connection>();
	//key is remotePeerID for connection with CONNECTION_TYPE_CLIENT
	private Map<Integer, Connection> clientConnections = new ConcurrentHashMap<Integer, Connection>();
	//key is remotePeerID for connection with CONNECTION_TYPE_INTERFACE_TRANSFER_CLIENT
	private Map<Integer, Connection> clientVirtualConnections = new ConcurrentHashMap<Integer, Connection>();

	private Map<Long, Connection> clientISMixIDConnections = new ConcurrentHashMap<Long, Connection>();

	public static ConnectionManager getInstance()
	{
		return instance;
	}

	private ConnectionManager()
	{
	}

	public boolean init(AbstractMessageInitializer msgInitializer)
	{
		this.msgInitializer = msgInitializer;
		return true;
	}

	public int generateConnectionID()
	{
		int connectionID = connectionIDSeed.getAndIncrement();

		while(getConnection(connectionID) != null)
		{
			connectionID = connectionIDSeed.getAndIncrement();
			logger.warn("connection id {} already used, try another one", connectionID);
		}

		return connectionID;
	}

	public void  addConnection(Connection connection)
	{
		connections.put(connection.getConnectionID(), connection);
	}

	public void addToServerConnections(Connection connection)
	{
		serverConnections.putIfAbsent(connection.getRemotePeerID(), connection);
	}

	public void addToClientConnections(Connection connection)
	{
		clientConnections.putIfAbsent( connection.getRemotePeerID(), connection);
	}

	public void addToClientVirtualConnections(Connection connection)
	{
		clientVirtualConnections.putIfAbsent(connection.getRemotePeerID(), connection);
		//long mixID = (( 0xffffffffffffffffL & connection.getTransferConnectoin().getRemotePeerID()) << 32) + connection.getRemoteConnectionID();
		clientISMixIDConnections.putIfAbsent(connection.getMixID(), connection);
	}

	public void removeConnection(Connection connection)
	{
		connections.remove(connection.getConnectionID());

		switch (connection.getConnectionType())
		{
			case Connection.CONNECTION_TYPE_SERVER:
				Connection serverConnection = serverConnections.get(connection.getRemotePeerID());
				if (serverConnection != null && connection.getConnectionID() == serverConnection.getConnectionID())
				{
					serverConnections.remove(connection.getRemotePeerID());
				}
				break;
			case Connection.CONNECTION_TYPE_CLIENT:
				Connection clientConnection = clientConnections.get(connection.getRemotePeerID());
				if (clientConnection != null && connection.getConnectionID() == clientConnection.getConnectionID())
				{
					clientConnections.remove(connection.getRemotePeerID());
				}
				break;
			case Connection.CONNECTION_TYPE_INTERFACE_TRANSFER_CLIENT:
				Connection clientVirtualConnection = clientVirtualConnections.get(connection.getRemotePeerID());
				if (clientVirtualConnection != null && clientVirtualConnection.getConnectionID() == connection.getConnectionID())
				{
					clientVirtualConnections.remove(connection.getRemotePeerID());
					//long mixID = (( 0xffffffffffffffffL & connection.getTransferConnectoin().getRemotePeerID()) << 32) + connection.getRemoteConnectionID();
					clientISMixIDConnections.remove(connection.getMixID());
				}
				break;
		}
		logger.debug("Remove connectionid:{} ", connection.getConnectionID());
	}

	public Connection getConnection(int connectionID)
	{
		return connections.get(connectionID);
	}

	public Connection getConnectionByMixID( long mixID )
	{
		//long mixID = (( 0xffffffffffffffffL & interfaceServerConnectionID) << 32) + clientConnectionIDInIS;
		return clientISMixIDConnections.get(mixID);
	}

	public Connection getServerConnnection(int serverID)
	{
		return serverConnections.get(serverID);
	}

	public Connection getClientConnection(int roleID)
	{
		return clientConnections.get(roleID);
	}

	public Connection getClientVirtualConnection(int roleID)
	{
		return clientVirtualConnections.get(roleID);
	}

	public int getClientVirutalConnectionNumber()
	{
		return clientVirtualConnections.size();
	}

	public AbstractMessageInitializer getMsgInitializer()
	{
		return msgInitializer;
	}

	public int getLocalPeerID() {
		return localPeerID;
	}

	public void setLocalPeerID(int localPeerID) {
		this.localPeerID = localPeerID;
	}

	public InetSocketAddress getManagerServerAddress() {
		return managerServerAddress;
	}

	public void setManagerServerAddress(InetSocketAddress managerServerAddress) {
		this.managerServerAddress = managerServerAddress;
	}

	public Collection<Connection> getAllServerConnections()
	{
		return serverConnections.values();
	}

	public void broadcastToAllServers(int callback, GeneratedMessage msg)
	{
		for(Connection connection : getAllServerConnections())
		{
			connection.write(callback, msg);
		}
	}

	public void broadcastToAllVirtualClients(int callback, GeneratedMessage msg)
	{
		DelaySendRunnable delaySendRunnable = new DelaySendRunnable();
		delaySendRunnable.sendToClient(clientVirtualConnections, msg, callback);
	}

	public void broadcastToAllClients(int callback, GeneratedMessage msg)
	{
		for(Connection connection : connections.values())
		{
			connection.write(callback, msg);
		}
	}

}
