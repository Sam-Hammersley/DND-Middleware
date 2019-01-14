package uk.ac.tees.node.router.messages;

import java.util.HashMap;
import java.util.Map;

import uk.ac.tees.net.message.MessageType;

public class RouterMessageHandlers {

	private static final Map<MessageType, RouterMessageHandler<?>> HANDLERS = new HashMap<>();
	
	static {
		HANDLERS.put(MessageType.ADD_AGENT, new AddAgentMessageHandler());
	}
	
	@SuppressWarnings("unchecked")
	public static <T> RouterMessageHandler<T> get(MessageType messageType) {
		return (RouterMessageHandler<T>) HANDLERS.get(messageType);
	}
	
}