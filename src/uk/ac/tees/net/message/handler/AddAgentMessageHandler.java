package uk.ac.tees.net.message.handler;

import java.util.HashSet;

import uk.ac.tees.net.message.Message;
import uk.ac.tees.node.router.Router;

public class AddAgentMessageHandler implements RouterMessageHandler {

	@Override
	public void handleMessage(Router router, Message message) {
		HashSet<String> portalAgents = router.getConnectionKey(message.getSource()).get();

		String uid = new String(message.getContents());
		
		portalAgents.add(uid);
	}
	
}