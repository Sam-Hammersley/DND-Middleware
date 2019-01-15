package uk.ac.tees.node.router.message;

import uk.ac.tees.net.message.Message;
import uk.ac.tees.node.router.Router;

public class AddPortalMessageHandler implements RouterMessageHandler {

	@Override
	public void handleMessage(Router router, Message message) {
		String uid = message.getSource();
		

	}

}