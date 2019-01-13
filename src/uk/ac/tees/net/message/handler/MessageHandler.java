package uk.ac.tees.net.message.handler;

import uk.ac.tees.net.message.Message;

public interface MessageHandler<T> {

	/**
	 * How a message should be handled.
	 * 
	 * @param message the message received.
	 */
	public void handleMessage(T t, Message message);
	
}