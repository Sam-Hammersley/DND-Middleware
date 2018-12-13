package uk.ac.tees.agent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.ac.tees.net.message.Message;
import uk.ac.tees.portal.Portal;

public class UserAgent extends MetaAgent {

	/**
	 * A portal that facilitates communication with other agents.
	 */
	private Portal portal;
	
	/**
	 * {@link MessageHandler}s for incoming messages.
	 */
	private final List<MessageHandler> messageHandlers = new ArrayList<>();
	
	/**
	 * Constructs a new {@link UserAgent}.
	 * 
	 * @param uid
	 * @param handlers
	 */
	protected UserAgent(String uid, MessageHandler...handlers) {
		super(uid);
		Arrays.stream(handlers).forEach(messageHandlers::add);
	}
	
	/**
	 * Adds a {@link MessageHandler} to {@link #messageHandlers}.
	 */
	public void addMessageHandler(MessageHandler messageHandler) {
		messageHandlers.add(messageHandler);
	}
	
	/**
	 * Assigns this agent's portal reference to the given instance.
	 * 
	 * @param portal the portal to send messages through.
	 */
	public void attach(Portal portal) {
		this.portal = portal;
	}

	@Override
	public void send(Message message) {
		portal.receive(message);
	}

	@Override
	public void receive(Message message) {
		messageHandlers.forEach(mh -> mh.handleMessage(message));
	}
	
}