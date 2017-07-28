package com.kodgames.interfaces.action.transfer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.AbstractByteArrayHandler;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.interfaces.service.server.ServerService;
import com.kodgames.corgi.core.util.IPUtils;

@ActionAnnotation(actionClass = TransferAction.class, serviceClass = ServerService.class)
public class TransferAction extends AbstractByteArrayHandler
{
	private Logger logger = LoggerFactory.getLogger(TransferAction.class);
	@Override
	public void handleMessage(Connection connection, int protocolID, int callback, byte[] buffer)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	//C-->I
	public void handleMessage(Connection connection, int protocolID, int callback, int dstPeerID, byte[] message)
	{
		Connection dstConnection = ConnectionManager.getInstance().getServerConnnection(dstPeerID);
		if (dstConnection != null)
		{
			if (dstConnection.getRemotePeerID() > 0)
			{
				int realIP = IPUtils.ipToInt(connection.getNettyNode().getAddress().getHostString());
				logger.debug("C2S  protocolID:{} clientID:{} clientConnectionID:{}  dstServerID:{}, dstConnectionID:{}", Integer.toHexString(protocolID), connection.getRemotePeerID(), connection.getConnectionID(), dstPeerID, dstConnection.getConnectionID());
				dstConnection.write(callback, protocolID, message,
						connection.getRemotePeerID(), connection.getConnectionID(), realIP);
			}
			else
			{
				logger.error("C2S illegal connection. protocolID:{} clientID:{} clientConnectionID:{}  dstServerID:{}, dstConnectionID:{}", Integer.toHexString(protocolID), connection.getRemotePeerIP(), connection.getConnectionID(), dstPeerID, dstConnection.getConnectionID());
				dstConnection.close();
			}
		}
		else
		{
			logger.error("Can't find corresponding server, dstPeerID:{} protocolID:{}", dstPeerID, Integer.toHexString(protocolID));
		}
	}

	@Override
	//S-->I
	public void handleMessage(Connection connection, int protocolID, int callback, int clientID, int clientConnectionID, byte[] message)
	{
		Connection clientConnection = ConnectionManager.getInstance().getConnection(clientConnectionID);
		if (clientConnection != null) {
			logger.debug("S2C prococolID:{} clientID {}  clientConnection:{}", Integer.toHexString(protocolID), clientConnection.getRemotePeerID(), clientConnectionID);
			clientConnection.write(callback, protocolID,  message);
		}
		else
		{
			logger.debug("Can't find corresponding client, clientID:{}, clientConnectionID:{}", clientID,clientConnectionID);
		}

	}
}