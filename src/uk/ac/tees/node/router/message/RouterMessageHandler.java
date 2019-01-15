package uk.ac.tees.node.router.message;

import uk.ac.tees.net.message.Message;
import uk.ac.tees.node.router.Router;

public interface RouterMessageHandler {

	/**
	 * How a message should be handled.
	 * 
	 * @param message the message received.
	 */
	public void handleMessage(Router router, Message message);
	
}