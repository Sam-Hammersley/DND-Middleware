package uk.ac.tees.net.message.handler;

import java.net.Socket;

import javax.net.SocketFactory;

import uk.ac.tees.Router;
import uk.ac.tees.net.Connection;
import uk.ac.tees.net.NetworkConstants;
import uk.ac.tees.net.message.Message;
import uk.ac.tees.net.util.SocketUtility;

public class AddPortalMessageHandler implements MessageHandler<Router> {

	@Override
	public void handleMessage(Message message, Router router) {
		
		router.addPortalConnection(message.getSource(), createConnection());
	}
	
	public Connection createConnection() {
		Socket socket = SocketUtility.createSocket(SocketFactory.getDefault(), "localhost", NetworkConstants.SERVER_PORT);
		Connection connection = new Connection(socket);
		
		return connection;
	}

}