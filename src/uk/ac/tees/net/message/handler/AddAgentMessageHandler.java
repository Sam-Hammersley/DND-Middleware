package uk.ac.tees.net.message.handler;

import java.util.HashSet;

import uk.ac.tees.Router;
import uk.ac.tees.net.message.Message;

public class AddAgentMessageHandler implements MessageHandler<Router> {

	@Override
	public void handleMessage(Router router, Message message) {
		HashSet<String> portalAgents = router.getConnectionKey(message.getSource()).get();
		
		String uid = new String(message.getContents());
		
		portalAgents.add(uid);
		
		System.out.println(router.getConnectionKey(message.getSource()).get());
	}
	
}