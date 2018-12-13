package uk.ac.tees.agent;

import uk.ac.tees.net.message.Message;

public interface MessageHandler {

	/**
	 * How a message should be handled.
	 * 
	 * @param message the message received.
	 */
	public void handleMessage(Message message);
	
}