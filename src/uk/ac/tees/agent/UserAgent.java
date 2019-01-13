package uk.ac.tees.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;

import uk.ac.tees.net.message.Message;
import uk.ac.tees.net.message.handler.MessageHandler;
import uk.ac.tees.portal.Portal;

public class UserAgent extends MetaAgent {

	/**
	 * A portal that facilitates communication with other agents.
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
	protected UserAgent(String uid, ThreadFactory threadFactory) {
		super(uid, threadFactory);
	}
	
	/**
	 * Adds a {@link MessageHandler} to {@link #messageHandlers}.
	 */
	public void addMessageHandler(MessageHandler<UserAgent> messageHandler) {
		messageHandlers.add(messageHandler);
	}
	
	public void setPortal(Portal portal) {
		this.portal = portal;
	}

	@Override
	public void send(Message message) {
		portal.handle(message);
	}

	@Override
	public void handle(Message message) {
		System.out.println(message);
	}
	
}