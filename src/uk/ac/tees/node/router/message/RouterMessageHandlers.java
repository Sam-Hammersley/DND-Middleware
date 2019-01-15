package uk.ac.tees.node.router.message;

import java.util.HashMap;
import java.util.Map;

import uk.ac.tees.net.message.MessageType;

public class RouterMessageHandlers {

	private static final Map<MessageType, RouterMessageHandler> HANDLERS = new HashMap<>();
	
	static {
		HANDLERS.put(MessageType.ADD_AGENT, new AddAgentMessageHandler());
		//HANDLERS.put(MessageType.ADD_PORTAL, new AddPortalMessageHandler());
	}
	
	public static RouterMessageHandler get(MessageType messageType) {
		return (RouterMessageHandler) HANDLERS.get(messageType);
	}
	
}