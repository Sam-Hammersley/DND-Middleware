package uk.ac.tees.node.router.messages;

import java.util.HashSet;

import uk.ac.tees.net.message.Message;
import uk.ac.tees.node.router.Router;

public class AddAgentMessageHandler implements RouterMessageHandler<Router> {

	@Override
	public void handleMessage(Router router, Message message) {
		HashSet<String> portalAgents = router.getConnectionKey(message.getSource()).get();
		
		String uid = new String(message.getContents());
		
		portalAgents.add(uid);
	}
	
}