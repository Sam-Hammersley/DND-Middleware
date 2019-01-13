package uk.ac.tees.net.message.handler;

import java.util.HashMap;
import java.util.Map;

import uk.ac.tees.net.message.MessageType;

public class MessageHandlers {

	private static final Map<MessageType, MessageHandler<?>> HANDLERS = new HashMap<>();
	
	static {
		HANDLERS.put(MessageType.ADD_AGENT, new AddAgentMessageHandler());
	}
	
	@SuppressWarnings("unchecked")
	public static <T> MessageHandler<T> get(MessageType messageType) {
		return (MessageHandler<T>) HANDLERS.get(messageType);
	}
	
}