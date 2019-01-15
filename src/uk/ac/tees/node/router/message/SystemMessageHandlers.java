package uk.ac.tees.node.router.message;

import java.util.HashMap;
import java.util.Map;

import uk.ac.tees.net.message.MessageType;

public class SystemMessageHandlers {

	private static final Map<MessageType, SystemMessageHandler> HANDLERS = new HashMap<>();
	
	static {
		HANDLERS.put(MessageType.ADD_AGENT, new AddAgentMessageHandler());
		HANDLERS.put(MessageType.ADD_PORTAL, new AddPortalMessageHandler());
	}
	
	public static SystemMessageHandler get(MessageType messageType) {
		return HANDLERS.get(messageType);
	}
	
}