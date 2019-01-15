package uk.ac.tees.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import uk.ac.tees.net.message.Message;
import uk.ac.tees.node.portal.Portal;

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
	 * {@link Consumer}s for incoming messages.
	 */
	private final List<Consumer<Message>> messageConsumers = new ArrayList<>();

	/**
	 * Constructs a new {@link UserAgent}.
	 * 
	 * @param uid
	 */
	protected UserAgent(String uid) {
		super(uid);
	}
	
	/**
	 * Adds a {@link Consumer} to {@link #messageConsumers}.
	 */
	public void addMessageConsumer(Consumer<Message> messageConsumer) {
		messageConsumers.add(messageConsumer);
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
		messageConsumers.forEach(h -> h.accept(message));
	}

}