package uk.ac.tees.node.router.message;

import uk.ac.tees.net.Connection;
import uk.ac.tees.net.message.Message;
import uk.ac.tees.net.message.impl.StringMessage;
import uk.ac.tees.node.router.Router;

public class AddPortalMessageHandler implements SystemMessageHandler {

	@Override
	public void handleMessage(Router router, Message message, Connection connection) {
		
		if (!router.getConnections().storeConnection(message.getSource(), connection)) {

			System.out.println("Failed to establish connection " + message.getSource() + " already exists");
			connection.write(new StringMessage(router.getUid(), message.getSource(),
					"Already contains connection for " + router.getUid()));
			connection.close();
		} else {
			
			System.out.println("Connection established with " + connection.getAddress());
		}
	}
}