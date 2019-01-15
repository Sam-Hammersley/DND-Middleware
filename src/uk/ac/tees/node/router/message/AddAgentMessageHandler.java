package uk.ac.tees.node.router.message;

import java.util.Optional;

import uk.ac.tees.net.ConnectionKey;
import uk.ac.tees.net.message.Message;
import uk.ac.tees.node.router.Router;

public class AddAgentMessageHandler implements RouterMessageHandler {

	@Override
	public void handleMessage(Router router, Message message) {
		Optional<ConnectionKey> key = router.getConnectionManager().getConnectionKey(message.getSource());

		key.ifPresent(k -> {
			String agentUid = new String(message.getContents());
			
			k.add(agentUid);
		});
	}
	
}