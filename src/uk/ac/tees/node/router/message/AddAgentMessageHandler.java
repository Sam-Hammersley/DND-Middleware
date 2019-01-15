package uk.ac.tees.node.router.message;

import java.util.Optional;

import uk.ac.tees.net.Connection;
import uk.ac.tees.net.ConnectionKey;
import uk.ac.tees.net.message.Message;
import uk.ac.tees.node.router.Router;

public class AddAgentMessageHandler implements SystemMessageHandler {

	@Override
	public void handleMessage(Router router, Message message, Connection connection) {
		Optional<ConnectionKey> key = router.getConnections().getConnectionKey(message.getSource());
		
		key.ifPresent(k -> {
			String agentUid = new String(message.getContents());
			
			k.add(agentUid);
		});
	}
	
}