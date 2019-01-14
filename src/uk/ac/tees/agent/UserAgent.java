package uk.ac.tees.agent;

import java.util.ArrayList;
import java.util.List;

import uk.ac.tees.net.message.Message;
import uk.ac.tees.net.message.handler.MessageHandler;
import uk.ac.tees.portal.Portal;

/**
 * Represents a user-defined agent.
 * 
 * @author q5315908
 */
public class UserAgent extends MetaAgent {

	/**
	 * A portal that accommodates communication with other agents.
	 */
	private Portal portal;

	/**
	 * {@link MessageHandler}s for incoming messages.
	 */
	private final List<MessageHandler<UserAgent>> messageHandlers = new ArrayList<>();

	/**
	 * Constructs a new {@link UserAgent}.
	 * 
	 * @param uid
	 * @param handlers
	 */
	protected UserAgent(String uid) {
		super(uid);
	}
	
	/**
	 * Adds a {@link MessageHandler} to {@link #messageHandlers}.
	 */
	public void addMessageHandler(MessageHandler<UserAgent> messageHandler) {
		messageHandlers.add(messageHandler);
	}
	
	/**
	 * Sets the portal to the specified.
	 * 
	 * @param portal the portal to set this UserAgent's to.
	 */
	public void setPortal(Portal portal) {
		this.portal = portal;
	}

	@Override
	public void send(Message message) {
		portal.handle(message);
	}

	@Override
	public void handle(Message message) {
		messageHandlers.forEach(h -> h.handleMessage(this, message));
	}

	@Override
	protected void receiveMessages() {
		// do nothing user agents are passed messages from the portal.
	}

}