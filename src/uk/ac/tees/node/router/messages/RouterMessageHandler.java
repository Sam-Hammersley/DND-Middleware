package uk.ac.tees.node.router.messages;

import uk.ac.tees.net.message.Message;

public interface RouterMessageHandler<T> {

	/**
	 * How a message should be handled.
	 * 
	 * @param message the message received.
	 */
	public void handleMessage(T t, Message message);
	
}