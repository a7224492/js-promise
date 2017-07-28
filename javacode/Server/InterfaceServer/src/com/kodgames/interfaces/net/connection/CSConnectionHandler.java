package com.kodgames.interfaces.net.connection;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.constant.ServerType;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.handler.connection.AbstractConnectionHandler;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.message.proto.server.ServerProtoBuf.ClientDisconnectSYN;

public class CSConnectionHandler extends AbstractConnectionHandler 
{
	static private Logger logger = LoggerFactory.getLogger(CSConnectionHandler.class);
	@Override
    public void handleConnectionActive(Connection connection)
    {
		connection.setConnectionType(Connection.CONNECTION_TYPE_CLIENT);
	    logger.info("Connection active. connection id : {}", connection.getConnectionID());
    }

	@Override
    public void handleConnectionInactive(Connection connection)
    {
		//没有验证的client断开连接不需要通知其它server
		if (connection.getRemotePeerID() == 0)
		{
			return;
		}

		ClientDisconnectSYN.Builder builder = ClientDisconnectSYN.newBuilder();
		long mixID = (( 0xffffffffffffffffL & ConnectionManager.getInstance().getLocalPeerID()) << 32) + connection.getConnectionID();
		logger.info("Connection inactive. Remote Client Address:{}, mixID:{}", connection.getNettyNode().getAddress(), Long.toHexString(mixID));

		builder.setMixId(mixID);
		builder.setRoleId(connection.getRemotePeerID());
		builder.setFounder(0);		//Interface用0表示
		
		//通知所有在线的Battle和Game，客户端断开
		Collection<Connection> servers = ConnectionManager.getInstance().getAllServerConnections();
		for (Connection server : servers)
		{
			if ( ServerType.getType(server.getRemotePeerID()) == ServerType.BATTLE_SERVER ||
					ServerType.getType(server.getRemotePeerID()) == ServerType.GAME_SERVER)
			{
				server.write(GlobalConstants.DEFAULT_CALLBACK, builder.build());
			}
		}
    }
}
